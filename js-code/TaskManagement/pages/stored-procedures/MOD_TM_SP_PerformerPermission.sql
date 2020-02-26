SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
create PROCEDURE [dbo].[MOD_TM_SP_PerformerPermission]
  @taskId INT ,
  @performerId   INT

AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	select task.Id, task.status as taskStatus, targetTaskPerformer.status as targetTaskPerformerStatus, 
		task.singleOrMultiple, targetTaskPerformer.progress as progress
	from awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_Task as task 
	inner join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as taskPerformer on taskPerformer.taskPerformerToTask_Id = task.Id
	inner join awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer as targetTaskPerformer on targetTaskPerformer.targetTaskPerformerToTaskPerformer_Id = taskPerformer.Id
	
	where targetTaskPerformer.performerId= @performerId  and task.Id = @taskId

END
GO
