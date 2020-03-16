SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
-- to get task data for owner privellige 
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetTaskDataById]
	-- Add the parameters for the stored procedure here
	@taskId int
 
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements

	SET NOCOUNT ON;

select task.Id, task.progress, task.taskName, task.owner as ownerId, person.DisplayName as owner, task.controllerName,task.status,
	(select userV.DisplayName from MOD_SYS_OC_DB_Role_User_V as userV where  userV.UserEntityId = task.createdBy) as taskCreator, 
	task.entityItemId, task.entityName,task.priority, task.description, taskProject.name as taskProjectName, 
	task.taskData, task.dueDate, task.startDate, task.applicationName , task.source, task.createdBy as createdById,
	taskProject.Id as taskProjectId,  taskProject.endDate as projectEndDate, taskProject.startDate as projectStartDate,
	lookups.ar_value as lookup_ar, lookups.eng_value as lookup_eng,
	task.singleOrMultiple, taskPerformer.type, taskPerformer.performerId,
	taskPerformer.displayName as performerName
	, ISNULL(([dbo].[GetTotalFinishedWorkingUsers](task.Id)/ NULLIF([dbo].[GetTotalWorkingUsers](task.Id),0)),0) as finished  
	
from  awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_Task as task 
	left join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TaskProjectMOD_TM_entity_Task as taskProjectRelation on task.Id = taskProjectRelation.MOD_TM_entity_Task_Id 
	left join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TaskProject as taskProject on taskProject.Id = task.taskProjectId 
    left join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id =task.owner
	left join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id
	left join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as taskPerformer on taskPerformer.taskPerformerToTask_Id = task.Id
	left join O2MyCompanyGeneralSYS_GENERALMOD_SYS_entity_lookup as lookups on lookups.[key] = task.priority AND lookups.category = 'priority' 
	where task.Id =  @taskId
		

END

GO
