SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_ReadByKpiId] 
	-- Add the parameters for the stored procedure here
	@kpiId int,
    @lang NVARCHAR(10)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	SELECT  CASE WHEN @lang = 'eng'  THEN lookup.eng_value ELSE  lookup.ar_value END AS quarter,  subkpi.Id, subkpi.progress, subkpi.targetValue, subkpi.[year] from O2MyCompanyTaskManagementMOD_TM_entity_subkpi subkpi 
INNER JOIN O2MyCompanyGeneralSYS_GENERALMOD_SYS_entity_lookup lookup on lookup.[key] = subkpi.quarter and lookup.category = 'annualQuarter' 
WHERE subkpi.subKPI_to_KPI_Id = @kpiId


END

GO
