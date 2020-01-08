USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_task_GetProgramByIdAndOwner]    Script Date: 1/5/2020 1:22:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetProgramByIdAndOwner] 
	-- Add the parameters for the stored procedure here
	@ProgramEntityId int, 
	@Owner int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	select program.*, 
	(select userV.DisplayName from MOD_SYS_OC_DB_Role_User_V as userV where  userV.UserEntityId = program.createdBy) as programCreator, 
	count(project.Id) as projectCount, person.DisplayName 
	from O2MyCompanyTaskManagementMOD_TM_entity_Program as program
		inner join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id =program.owner
		left join O2MyCompanyTaskManagementMOD_TM_entity_TaskProject as project on project.programId = program.Id 
		inner join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id
	where program.Id = @ProgramEntityId  and (program.createdBy = @Owner or program.owner = @Owner)
	and program.isDeleted != 1
	group by  program.createdBy, program.createdBy, program.description, program.endDate, program.Id, program.name, program.notes,
		program.owner, program.startDate, program.status, person.DisplayName , program.S_ITEM_STATUS, program.progress, program.createdByUnitId, program.isDeleted

END
