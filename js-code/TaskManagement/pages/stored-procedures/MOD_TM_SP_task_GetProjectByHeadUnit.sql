USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_task_GetProjectByHeadUnit]    Script Date: 1/5/2020 7:46:42 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetProjectByHeadUnit] 
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

		WITH w1( id, ptr, f1, level ) AS 
(		SELECT UnitId, ParentUnitId, DisplayName, 0 AS level
		FROM 
			MOD_SYS_OC_DB_Role_User_V t1
		WHERE UnitId = @unitId
			--( @unitId=0 and UnitId is null) or (@unitId <> 0 and UnitId = @unitId)
	UNION ALL 
		SELECT t1.UnitId, t1.ParentUnitId, t1.DisplayName,
			level + 1
		FROM 
			MOD_SYS_OC_DB_Role_User_V as t1 JOIN w1 ON w1.ptr = t1.UnitId
) 
select * from O2MyCompanyTaskManagementMOD_TM_entity_TaskProject where (assignToUnitId <> 0 and @unitId = 0) or (assignToUnitId in (SELECT id FROM w1) and @unitId <> 0) and isDeleted <> 1 and name like '%'+@input+'%' and endDate >=  CAST(GETDATE() AS DATE)
and status = @status
	order by name
	OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE);

END
