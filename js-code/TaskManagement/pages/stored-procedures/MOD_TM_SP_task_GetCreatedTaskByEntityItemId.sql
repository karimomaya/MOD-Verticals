USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_task_GetCreatedTaskByEntityItemId]    Script Date: 1/9/2020 5:38:24 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetCreatedTaskByEntityItemId]
  @PageNumber INT = 1,
  @PageSize   INT = 10,
  @sortBy	  varchar(20),
  @sortDir  varchar(20),
  @Owner	  NVARCHAR(MAX),
  @entityItemId varchar(max)

AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
select task.Id, task.taskName, person.DisplayName as owner, task.source, 
	task.status as taskStatus, task.dueDate, task.startDate, task.priority, 
	ISNULL(([dbo].[GetTotalFinishedWorkingUsers](task.Id)/ NULLIF([dbo].[GetTotalWorkingUsers](task.Id),0)),0) as finished,
	task.description, taskProject.name as taskProjectName,  task.owner as ownerId, task.createdBy as createdById,
	task.progress from awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_Task as task 
left join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TaskProjectMOD_TM_entity_Task as taskProjectRelation on task.Id = taskProjectRelation.MOD_TM_entity_Task_Id 
left join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TaskProject as taskProject on taskProject.Id = task.taskProjectId 
inner join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id =task.owner
inner join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id
	
	
where task.createdBy = @Owner and task.entityItemId = @entityItemId and task.isDeleted<>1
order by 
	case when @sortBy = 'taskName' and @sortDir = 'sortAsc' 
    then task.taskName end asc, 
	case when @sortBy = 'taskName' and @sortDir = 'sortDesc' 
    then task.taskName end desc,
	case when @sortBy = 'taskOwner' and @sortDir = 'sortAsc' 
    then person.DisplayName end asc, 
	case when @sortBy = 'taskOwner' and @sortDir = 'sortDesc' 
    then person.DisplayName end desc,
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
	then task.status end desc
	
	
OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE);

END
