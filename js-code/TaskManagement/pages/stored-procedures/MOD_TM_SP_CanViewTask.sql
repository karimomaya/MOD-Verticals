SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_CanViewTask] 
	-- Add the parameters for the stored procedure here
	@userId int,
    @taskId int,
    @userPath VARCHAR(MAX)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here

SELECT task.Id from O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer targettask 
INNER JOIN O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer taskperformer on taskperformer.Id = targettask.targetTaskPerformerToTaskPerformer_Id
INNER JOIN O2MyCompanyTaskManagementMOD_TM_entity_Task task on task.Id = taskperformer.taskPerformerToTask_Id
where 
(targettask.performerId in (SELECT UserEntityId FROM MOD_SYS_OC_DB_Role_User_V WHERE UnitPathById like '%'+ @userPath +'/%')
OR 
(task.createdBy = @userId or task.[owner] = @userId) 
OR targettask.performerId = @userId)
and task.Id = @taskId
	
END


GO
