USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_GetDelayedTask]    Script Date: 1/6/2020 2:07:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_GetDelayedTask]
	-- Add the parameters for the stored procedure here
	@UserIds nvarchar(max),
	@PageNumber int,
	@PageSize int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here

select * from ( SELECT Distinct ROW_NUMBER() OVER (ORDER BY task.Id) AS RowNum, task.*  from O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer as targetPerformer
	inner join O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as taskPerformer on targetPerformer.targetTaskPerformerToTaskPerformer_Id = taskPerformer.Id
	inner join O2MyCompanyTaskManagementMOD_TM_entity_Task as task on task.Id = taskPerformer.taskPerformerToTask_Id
	where @UserIds like  CASE WHEN LEN(@UserIds) > 2  THEN '%;'+cast(targetPerformer.performerId as varchar(20))+';%' ELSE ';' end 
	AND task.dueDate < getdate()  AND (task.status = 1 or task.status = 2) and task.isDeleted <> 1
	 ) 
	as result where
	result.RowNum BETWEEN ((@PageNumber * @PageSize) - @PageSize) + 1 AND @PageSize * @PageNumber
	
	/*

SELECT task.*  from O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer as targetPerformer
	inner join O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as taskPerformer on targetPerformer.targetTaskPerformerToTaskPerformer_Id = taskPerformer.Id
	inner join O2MyCompanyTaskManagementMOD_TM_entity_Task as task on task.Id = taskPerformer.taskPerformerToTask_Id
	where  @UserIds like  CASE WHEN LEN(@UserIds) > 2  THEN '%;'+cast(targetPerformer.performerId as varchar(20))+';%' ELSE ';' end and task.dueDate < getdate()

	order by task.endDate asc
	OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE); */

END
