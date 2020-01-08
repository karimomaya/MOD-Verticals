USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_UserProductivityReport]    Script Date: 1/6/2020 11:40:48 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_UserProductivityReport]
	-- Add the parameters for the stored procedure here
	@StartDate date,
	@EndDate date,
	@List varchar(max),
	@PageNumber INT = 1,
	@PageSize   INT = 10
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
SELECT task.*, targetTask.performerId as performerId FROM O2MyCompanyTaskManagementMOD_TM_entity_Task as task 
	INNER JOIN O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as performer on performer.taskPerformerToTask_Id = task.Id
	INNER JOIN O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer as targetTask on targetTask.targetTaskPerformerToTaskPerformer_Id = performer.Id
-- ';123;434;365;' 
WHERE @List like  CASE WHEN LEN(@List) > 2  THEN '%;'+cast(targetTask.performerId as varchar(20))+';%' ELSE ';' end
	AND task.dueDate >= @StartDate
	AND task.dueDate <= @EndDate
	AND ((task.status = 3 or task.status= 12) or targetTask.status = 2 ) and task.isDeleted <> 1
order by task.dueDate asc
	OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE);

END
