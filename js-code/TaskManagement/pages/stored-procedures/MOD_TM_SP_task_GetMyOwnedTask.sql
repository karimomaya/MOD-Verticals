USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_task_GetMyOwnedTask]    Script Date: 1/5/2020 8:22:40 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================


ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetMyOwnedTask]
  @PageNumber INT = 1,
  @PageSize   INT = 10,
  @sortBy	  varchar(20),
  @sortDir  varchar(20),
  @Owner	  NVARCHAR(MAX),
  @TaskName NVARCHAR(MAX),
  @Progress int,
  @startDate DATE,
  @endDate	Date
  

AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;


    -- Insert statements for procedure here
select task.Id, task.taskName, person.DisplayName as owner, task.source, task.status as taskStatus, task.dueDate, task.startDate, task.priority, task.description, taskProject.name as taskProjectName, 
	task.progress as progress, task.owner as ownerId, task.createdBy as createdById,
	ISNULL(([dbo].[GetTotalFinishedWorkingUsers](task.Id)/ NULLIF([dbo].[GetTotalWorkingUsers](task.Id),0)),0) as finished 
from awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_Task as task 
	left join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TaskProjectMOD_TM_entity_Task as taskProjectRelation on task.Id = taskProjectRelation.MOD_TM_entity_Task_Id 
	left join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TaskProject as taskProject on taskProject.Id = task.taskProjectId 
    inner join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id =task.owner
	inner join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id		
where task.owner = @Owner and  task.status <> 3 and task.status <> 10 and task.status <> 12 and task.taskName like '%'+@TaskName+'%' and
	(task.progress = @Progress or @Progress =-1) and (task.startDate >= @startDate and task.dueDate <= @endDate) and task.isDeleted <> 1

	order by 
	case when @sortBy = 'taskName' and @sortDir = 'sortAsc' 
    then task.taskName end asc, 
	case when @sortBy = 'taskName' and @sortDir = 'sortDesc' 
    then task.taskName end desc,
	case when @sortBy = 'progress' and @sortDir = 'sortAsc' 
    then task.progress end asc, 
	case when @sortBy = 'progress' and @sortDir = 'sortDesc' 
    then task.progress end desc,
	case when @sortBy = 'startDate' and @sortDir = 'sortAsc' 
    then task.startDate end asc, 
	case when @sortBy = 'startDate' and @sortDir = 'sortDesc' 
    then task.startDate end desc,
	case when @sortBy = 'endDate' and @sortDir = 'sortAsc' 
    then task.dueDate end asc, 
	case when @sortBy = 'endDate' and @sortDir = 'sortDesc' 
    then task.dueDate end desc,
	case when @sortBy = 'taskStatus' and @sortDir = 'sortAsc' 
    then task.status end asc, 
	case when @sortBy = 'taskStatus' and @sortDir = 'sortDesc' 
    then task.status end desc,
	case when @sortBy = 'pirority' and @sortDir = 'sortAsc' 
    then task.priority end asc, 
	case when @sortBy = 'pirority' and @sortDir = 'sortDesc' 
    then task.priority end desc
	
	 OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE);

END
