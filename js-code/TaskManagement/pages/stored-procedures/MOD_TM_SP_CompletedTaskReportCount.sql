USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_CompletedTaskReportCount]    Script Date: 1/6/2020 11:45:00 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_CompletedTaskReportCount]
	-- Add the parameters for the stored procedure here
	@userIds varchar(max),
	@userId int,
	@StartDate date,
	@EndDate date
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

SELECT count(task.Id) FROM O2MyCompanyTaskManagementMOD_TM_entity_Task  as task 
INNER JOIN O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as performer on performer.taskPerformerToTask_Id = task.Id
INNER JOIN O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer as targetTask on targetTask.targetTaskPerformerToTaskPerformer_Id = performer.Id
WHERE @userIds like  CASE WHEN LEN(@userIds) > 2  THEN '%;'+cast(targetTask.performerId as varchar(20))+';%' ELSE ';' end
AND (task.status = 3 or task.status= 12) AND (task.owner = @userId or task.createdBy = @userId)
AND task.dueDate >= @StartDate
	AND task.dueDate <= @EndDate and task.isDeleted <> 1


END
