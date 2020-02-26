SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetListOfWorkingUser] 
	-- Add the parameters for the stored procedure here
	@PageNumber INT = 1,
	@PageSize   INT = 10,
	@taskId	  INT
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
	

select distinct taskPerformer.displayName, taskPerformer.performerId, targetTaskPerformer.Id as targetTaskPerformerEntityId,
    task.singleOrMultiple, targetTaskPerformer.targetTaskPerformerToTaskPerformer_Id as taskPerformerEntityId, task.status as taskStatus,
    taskPerformer.taskPerformerToTask_Id, taskPerformer.type, taskPerformer.progress, task.owner, task.createdBy,
    taskPerformer.status as isAcquired,
    case when taskPerformer.type = 0 then targetTaskPerformer.status else taskPerformer.status end as status,
    case when taskPerformer.type = 0 then targetTaskPerformer.Id else taskPerformer.Id end as Id
from O2MyCompanyTaskManagementMOD_TM_entity_TaskPerformer as taskPerformer 
    left join O2MyCompanyTaskManagementMOD_TM_entity_TargetTaskPerformer as targetTaskPerformer on targetTaskPerformer.targetTaskPerformerToTaskPerformer_Id = taskPerformer.Id
    inner join O2MyCompanyTaskManagementMOD_TM_entity_Task as task on taskPerformer.taskPerformerToTask_Id = task.Id
where task.Id = @taskId
	order by taskPerformer.progress desc
OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE);



END

GO
