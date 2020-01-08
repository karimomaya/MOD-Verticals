USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_EvaluateProjectProgress]    Script Date: 1/5/2020 6:55:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_EvaluateProjectProgress]
	-- Add the parameters for the stored procedure here
	@ProjectId int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
select (CAST(sum(task.progress) as float) / (CAST(count(*)* 100 as float) ) * 100 ) as progress 
from O2MyCompanyTaskManagementMOD_TM_entity_TaskProject as project
	inner join O2MyCompanyTaskManagementMOD_TM_entity_Task as task on task.taskProjectId = project.Id 
where project.Id = @ProjectId and (task.status <> 11 or task.status <> 12) and task.isDeleted <> 1

END
