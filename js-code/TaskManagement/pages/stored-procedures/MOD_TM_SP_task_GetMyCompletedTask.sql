USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_task_GetMyCompletedTask]    Script Date: 1/5/2020 8:27:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetMyCompletedTask] 
	-- Add the parameters for the stored procedure here
	@PageNumber INT = 1,
	@PageSize   INT = 10,
	@userId	  NVARCHAR(MAX),
	@sortBy	  varchar(20),
	@sortDir  varchar(20),
	@TaskName NVARCHAR(MAX),
	@Progress INT,
	@startDate DATE,
	@endDate	Date,
	@filterOwner INT

AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

select task.Id, targetTaskPerformer.Id as targetTaskPerformerId, task.taskName, task.source, 
	person.DisplayName as owner, task.dueDate, task.status as taskStatus,
	task.owner as ownerId, task.createdBy as createdById,  taskProject.name as taskProjectName,
	task.startDate, task.priority, task.description, targetTaskPerformer.status as targetTaskPerformerStatus, 
	taskPerformer.singleOrMultiple, targetTaskPerformer.progress as progress
from awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_Task as task 
	left join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TaskProject as taskProject on taskProject.Id = task.taskProjectId
	inner join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as taskPerformer on taskPerformer.taskPerformerToTask_Id = task.Id
	inner join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer as targetTaskPerformer on targetTaskPerformer.targetTaskPerformerToTaskPerformer_Id = taskPerformer.Id
	
	inner join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id =task.owner
	inner join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id
	
where targetTaskPerformer.performerId= @userId  and targetTaskPerformer.status = 2 and task.taskName like '%'+@TaskName+'%' and
	task.status <> 12 and
	(task.progress = @Progress or @Progress =-1) and (task.startDate >= @startDate and task.dueDate <= @endDate) and (task.owner = @filterOwner or @filterOwner = -1) and task.isDeleted <> 1

	order by 
	case when @sortBy = 'taskName' and @sortDir = 'sortAsc' 
    then task.taskName end asc, 
	case when @sortBy = 'taskName' and @sortDir = 'sortDesc' 
    then task.taskName end desc,
	case when @sortBy = 'progress' and @sortDir = 'sortAsc' 
    then targetTaskPerformer.progress end asc, 
	case when @sortBy = 'progress' and @sortDir = 'sortDesc' 
    then targetTaskPerformer.progress end desc,
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
