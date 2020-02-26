SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_OwnerActionOnPerformerPermissions] 
	-- Add the parameters for the stored procedure here
	@taskId	  INT,
    @targetTaskPerformerEntityId INT
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
	

select distinct task.status as taskStatus, task.owner, task.createdBy, taskPerformer.type,
    targetTaskPerformer.status,  targetTaskPerformer.performerId
from O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as taskPerformer 
    left join O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer as targetTaskPerformer on targetTaskPerformer.targetTaskPerformerToTaskPerformer_Id = taskPerformer.Id
    inner join O2MyCompanyTaskManagementMOD_TM_entity_Task as task on taskPerformer.taskPerformerToTask_Id = task.Id
where task.Id = @taskId and  targetTaskPerformer.Id = @targetTaskPerformerEntityId
	
END

GO
