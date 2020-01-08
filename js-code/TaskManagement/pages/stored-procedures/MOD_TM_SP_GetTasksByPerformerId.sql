USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_FinishedAndDelayedTaskReportProject]    Script Date: 1/6/2020 12:19:48 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[MOD_TM_SP_GetTasksByPerformerId]
	-- Add the parameters for the stored procedure here
	@performerId int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

SELECT task.* FROM O2MyCompanyTaskManagementMOD_TM_entity_Task  as task 
INNER JOIN O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as performer on performer.taskPerformerToTask_Id = task.Id
INNER JOIN O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer as targetTask on targetTask.targetTaskPerformerToTaskPerformer_Id = performer.Id
where 
targetTask.performerId = @performerId
and  (task.status = 1 or task.status = 2 ) and task.isDeleted <> 1

END
