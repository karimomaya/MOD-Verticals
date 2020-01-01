USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_ReadNote]    Script Date: 11/6/2019 10:08:04 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_ReadNote]
	-- Add the parameters for the stored procedure here
    @taskId int,
	@owner int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	
    -- Insert statements for procedure here
	select note.*, person.DisplayName, task.owner as taskOwner, task.createdBy as taskCreatedBy from O2MyCompanyTaskManagementMOD_TM_entity_Note as note
	inner join O2MyCompanyTaskManagementMOD_TM_entity_Task as task on task.Id = note.note_to_task_Id
	inner join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id = note.createdBy
	inner join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id
	
	where  task.Id = @taskId
	--(task.createdBy =@owner or task.owner = @owner or note.createdBy = @owner ) and

	
END
