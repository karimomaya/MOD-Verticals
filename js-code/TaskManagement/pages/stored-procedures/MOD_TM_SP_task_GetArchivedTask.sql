USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_task_GetArchivedTask]    Script Date: 1/5/2020 8:26:27 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetArchivedTask]
  @PageNumber INT = 1,
  @PageSize   INT = 10,
  @UserId	  NVARCHAR(MAX),
  @sortBy	  varchar(20),
  @sortDir	  varchar(20),
  @TaskName   NVARCHAR(MAX),
  @Progress   INT,
  @startDate DATE,
  @endDate	Date,
  @filterOwner INT

AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	select task.Id, task.status as taskStatus, targetTaskPerformer.Id as targetTaskPerformerId, task.taskName, task.source, 
		person.DisplayName as owner, task.owner as ownerId, task.createdBy as createdById, task.dueDate, task.startDate, task.priority, task.description, 
		targetTaskPerformer.status as targetTaskPerformerStatus, 
		task.singleOrMultiple, targetTaskPerformer.progress as progress,
		taskProject.Id as taskProjectId,  
		taskProject.name as taskProjectName
	from awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_Task as task 
	inner join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as taskPerformer on taskPerformer.taskPerformerToTask_Id = task.Id
	inner join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer as targetTaskPerformer on targetTaskPerformer.targetTaskPerformerToTaskPerformer_Id = taskPerformer.Id
	left join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TaskProject as taskProject on taskProject.Id = task.taskProjectId 
	inner join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id =task.owner
	inner join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id
	
	where (targetTaskPerformer.performerId= @UserId  or task.owner = @UserId or task.createdBy = @UserId) and task.status = 12
	and task.taskName like '%'+@TaskName+'%' and (task.progress = @Progress or @Progress =-1) and (task.startDate >= @startDate and task.dueDate <= @endDate) and (task.owner = @filterOwner or @filterOwner = -1) and task.isDeleted <> 1
	
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
    then task.status end desc

	 OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE);

END