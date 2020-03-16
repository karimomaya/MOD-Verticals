SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetLowerProjectByHeadUnit] 
	-- Add the parameters for the stored procedure here
	@unitId int,
	@PageNumber INT = 1,
	@PageSize   INT = 10, 
	@input varchar(max),
	@status Int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	
with tree as (
   SELECT Id as UnitId, ParentUnitId, UnitName as DisplayName
		FROM 
			O2comassetegOrganizationChartMOD_SYS_OC_entity_unit  
  WHERE Id = @unitId
   union all
  SELECT child.Id as UnitId, child.ParentUnitId, child.UnitName as DisplayName
  FROM 
			O2comassetegOrganizationChartMOD_SYS_OC_entity_unit as child
     join tree as parent on parent.UnitId = child.ParentUnitId -- the self join to the CTE builds up the recursion
) 
select * from O2MyCompanyTaskManagementMOD_TM_entity_TaskProject where (assignToUnitId <> 0 and @unitId = 0) or (assignToUnitId in (SELECT UnitId FROM tree) 
and @unitId <> 0) and isDeleted <> 1 and name like '%'+@input+'%' and endDate >=  CAST(GETDATE() AS DATE)
and status = @status

	order by name
	OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE);


END

GO
