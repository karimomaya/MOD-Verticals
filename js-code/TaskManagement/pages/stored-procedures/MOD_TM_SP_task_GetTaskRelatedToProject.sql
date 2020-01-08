USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_task_GetTaskRelatedToProject]    Script Date: 1/5/2020 2:38:28 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetTaskRelatedToProject]
	-- Add the parameters for the stored procedure here
	@ProjectId int,
	@Owner int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	select  task.*, person.DisplayName 
	from O2MyCompanyTaskManagementMOD_TM_entity_Task as task 
	inner join O2MyCompanyTaskManagementMOD_TM_entity_TaskProject as project on task.taskProjectId = project.Id
	inner join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id =task.owner
	inner join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id

	where task.taskProjectId = @ProjectId  and task.isDeleted != 1
END
