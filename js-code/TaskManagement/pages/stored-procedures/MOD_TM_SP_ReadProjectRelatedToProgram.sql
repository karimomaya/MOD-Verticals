USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_ReadProjectRelatedToProgram]    Script Date: 1/5/2020 1:27:54 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_ReadProjectRelatedToProgram]
	-- Add the parameters for the stored procedure here
	@programId int,
	@owner int,
	@sortDir varchar(100),
	@sortBy varchar(50),
	@PageNumber int, 
	@PageSize int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
select distinct project.*,iden.Display_Name,
	project.progress as progress
	--[dbo].[GetTotalProjectProgress](project.Id) as progress 
from O2MyCompanyTaskManagementMOD_TM_entity_TaskProject as project 
	left join O2MyCompanyTaskManagementMOD_TM_entity_Program as program on project.programId = program.Id
	left join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id = project.createdBy
	left join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id 
	left join  O2MyCompanyTaskManagementMOD_TM_entity_Task as task on task.taskProjectId = project.Id

where project.programId = @programId  and project.isDeleted != 1
	 order by project.startDate asc
	/*order by 
	case when @sortBy = 'projectName' and @sortDir = 'sortAsc' 
    then project.name end asc, 
	case when @sortBy = 'projectName' and @sortDir = 'sortDesc' 
    then project.name end desc,
	case when @sortBy = 'startDate' and @sortDir = 'sortAsc' 
    then project.startDate end asc, 
	case when @sortBy = 'startDate' and @sortDir = 'sortDesc' 
    then project.startDate end desc,
	case when @sortBy = 'endDate' and @sortDir = 'sortAsc' 
    then project.endDate end asc, 
	case when @sortBy = 'endDate' and @sortDir = 'sortDesc' 
    then project.endDate end desc,

	case when @sortBy = 'progress' and @sortDir = 'sortAsc' 
    then project.progress end asc, 
	case when @sortBy = 'progress' and @sortDir = 'sortDesc' 
    then project.progress end desc*/
	
    OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE);

END
