USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_EvaluateTaskProgress]    Script Date: 1/5/2020 6:54:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_EvaluateTaskProgress]
	-- Add the parameters for the stored procedure here
	@TaskId int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	select CASE 
    WHEN task.singleOrMultiple = 0  
	THEN (CAST(max(taskPerformer.progress) as float) / (CAST(1 * 100 as float) ) * 100 )
	WHEN task.singleOrMultiple = 1  
	THEN (CAST(sum(taskPerformer.progress) as float) / (CAST(count(*)* 100 as float) ) * 100 )
	end  as progress

from O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as taskPerformer 
	inner join [awdb].[dbo].[O2MyCompanyTaskManagementMOD_TM_entity_Task] as task
		on task.Id = taskPerformer.taskPerformerToTask_Id
where  task.Id = @TaskId  and taskPerformer.isDeleted <> 1
group by task.singleOrMultiple
END
