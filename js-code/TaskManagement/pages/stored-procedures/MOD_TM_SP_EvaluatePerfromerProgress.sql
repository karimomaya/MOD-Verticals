USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_EvaluatePerfromerProgress]    Script Date: 1/5/2020 6:51:37 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_EvaluatePerfromerProgress]
	-- Add the parameters for the stored procedure here
	@taskPerformerEntityId int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	select (CAST(sum(targetTaskPerformer.progress) as float) / (CAST(count(*)* 100 as float) ) * 100 ) as progress
from O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as taskPerformer 
	inner join [awdb].[dbo].[O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer] as targetTaskPerformer
		on targetTaskPerformer.targetTaskPerformerToTaskPerformer_Id = taskPerformer.Id
where  taskPerformer.Id = @taskPerformerEntityId and  targetTaskPerformer.status <> 4 and targetTaskPerformer.isDeleted <> 1
END
