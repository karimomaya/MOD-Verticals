USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_GetTasksByPerformerId]    Script Date: 1/8/2020 11:07:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_GetTasksByPerformerId]
	-- Add the parameters for the stored procedure here
	@performerId int,
	@StartDate date,
	@EndDate date
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

SELECT task.Id, task.startDate, task.dueDate, task.status as taskStatus, task.taskName, targetTask.status as targetPerformerStatus, person.DisplayName  FROM O2MyCompanyTaskManagementMOD_TM_entity_Task  as task 
INNER JOIN O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as performer on performer.taskPerformerToTask_Id = task.Id
INNER JOIN O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer as targetTask on targetTask.targetTaskPerformerToTaskPerformer_Id = performer.Id
inner join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id =targetTask.performerId
	inner join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id
where 
targetTask.performerId = @performerId
and  (task.status = 1 or task.status = 2 or task.status = 3 ) and task.isDeleted <> 1 and task.startDate >= @StartDate and task.dueDate <=@EndDate and (targetTask.status <> 4 or targetTask.status <> 3) 

END
