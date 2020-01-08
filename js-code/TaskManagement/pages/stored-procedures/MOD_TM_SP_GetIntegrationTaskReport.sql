USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_GetIntegrationTaskReport]    Script Date: 1/6/2020 11:58:53 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_GetIntegrationTaskReport]
	-- Add the parameters for the stored procedure here
	@PageNumber INT = 1,
	@PageSize   INT = 10,
	@userIds varchar(max),
	@integrationIds varchar(max),
	@source varchar(max)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	
		SELECT task.* FROM O2MyCompanyTaskManagementMOD_TM_entity_Task  as task 
INNER JOIN O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as performer on performer.taskPerformerToTask_Id = task.Id
INNER JOIN O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer as targetTask on targetTask.targetTaskPerformerToTaskPerformer_Id = performer.Id
where 
@userIds like  CASE WHEN LEN(@userIds) > 2  THEN '%;'+cast(targetTask.performerId as varchar(20))+';%' ELSE ';' end
and  (task.status = 1 or task.status = 2 or task.status = 3 or task.status= 12)  and task.isDeleted <> 1
 AND task.integrationId in (select *
               FROM ufnDelimitedBigIntToTable(@integrationIds,',')Ids
               WHERE task.integrationId  = Ids.id ) and task.source = @source
 order by task.dueDate asc
	OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE);
END
