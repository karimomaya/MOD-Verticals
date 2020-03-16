SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[MOD_TM_SP_EvaluateKpiProgress]
	-- Add the parameters for the stored procedure here
	@KpiId int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	select 
(CAST(sum(subkpi.progress) as float) / (CAST(sum(subkpi.targetValue)* 100 as float) ) * 100 )

from O2MyCompanyTaskManagementMOD_TM_entity_kpi as kpi 
inner join O2MyCompanyTaskManagementMOD_TM_entity_subkpi as subkpi
		on kpi.Id = subkpi.subKPI_to_KPI_Id
where  kpi.Id = @KpiId 
END

GO
