USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_EvaluateProgramProgress]    Script Date: 1/6/2020 11:44:14 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_EvaluateProgramProgress]
	-- Add the parameters for the stored procedure here
	@ProgramId int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
select (CAST(sum(project.progress) as float) / (CAST(count(*)* 100 as float) ) * 100 ) as progress 
from O2MyCompanyTaskManagementMOD_TM_entity_Program as program
	inner join O2MyCompanyTaskManagementMOD_TM_entity_TaskProject as project on project.programId= program.Id 
where program.Id = @ProgramId and project.isDeleted <> 1 and project.status <> 3

END
