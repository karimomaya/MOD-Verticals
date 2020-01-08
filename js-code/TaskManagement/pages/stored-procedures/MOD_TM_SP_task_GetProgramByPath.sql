USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_task_GetProgramByPath]    Script Date: 1/5/2020 5:41:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetProgramByPath] 
	-- Add the parameters for the stored procedure here
	@path varchar(MAX),
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
	/****** Script for SelectTopNRows command from SSMS  ******/
SELECT *
  /*FROM [awdb].[dbo].[O2MyCompanyTaskManagementMOD_TM_entity_Program] as program
  where CHARINDEX('/' + cast(createdByUnitId as varchar) + '/' ,@path) <> 0 and program.name like '%'+@input+'%' and program.status = @status*/
	FROM [awdb].[dbo].[O2MyCompanyTaskManagementMOD_TM_entity_Program] as program
  where  program.name like '%'+@input+'%' and program.status = @status and program.endDate >=  CAST(GETDATE() AS DATE)
  and program.isDeleted <> 1
  order by program.name
  OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE);

END
