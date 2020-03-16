SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetProjectById] 
	-- Add the parameters for the stored procedure here
	@ProjectId int,
	@Owner int

AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	select  program.name as programName, project.*, unit.UnitName, unit.UnitPathById, person.DisplayName as projectOwner, 
	(
		select count(*) from O2MyCompanyTaskManagementMOD_TM_entity_Task as t where t.taskProjectId = project.Id
	) as taskCount,
	(select userV.DisplayName from MOD_SYS_OC_DB_Role_User_V as userV where  userV.UserEntityId = project.createdBy) as projectCreator
	from O2MyCompanyTaskManagementMOD_TM_entity_TaskProject as project 
	left join O2MyCompanyTaskManagementMOD_TM_entity_Program as program on project.programId = program.Id
    left join MOD_SYS_OC_DB_Units_ParentUnits_V as unit on project.assignToUnitId = unit.UnitId
    left join  O2MyCompanyTaskManagementMOD_TM_entity_kpi as kpi on kpi.entityId = project.Id and kpi.[type]=2
	inner join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id =project.owner
	inner join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id
	where project.Id = @ProjectId  and (project.createdBy = @Owner or project.owner = @Owner or kpi.[owner] = @Owner) and project.isDeleted != 1
END

GO
