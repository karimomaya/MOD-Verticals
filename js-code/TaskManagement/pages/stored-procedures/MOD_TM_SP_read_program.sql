SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_read_program]
	-- Add the parameters for the stored procedure here
    @PageNumber int,
    @PageSize int,
	@status int,
	@userId int,
	@sortBy	  varchar(20),
	@sortDir  varchar(20),
	@ProgramName NVARCHAR(MAX),
	@Progress int,
	@StartDate Date,
	@EndDate Date,
	@FilterOwner int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	
    -- Insert statements for procedure here
	select program.*, count(project.Id) as projectCount, person.DisplayName,
	program.progress as programProgress
	from O2MyCompanyTaskManagementMOD_TM_entity_Program as program 
		inner join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id = program.owner
		inner join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id
		left join O2MyCompanyTaskManagementMOD_TM_entity_TaskProject as project on project.programId = program.Id 
        left join  O2MyCompanyTaskManagementMOD_TM_entity_kpi as kpi on kpi.entityId = project.Id and kpi.[type]=1
where program.status = @status and (program.owner = @userId or program.createdBy = @userId or kpi.[owner] = @userId) 
	and (program.progress = @Progress or @Progress =-1)  
	and (program.startDate >=  @StartDate and  program.endDate <=  @EndDate) 
	and (program.owner = @FilterOwner or @FilterOwner = -1) and program.name like '%'+@ProgramName+'%' and program.isDeleted != 1

group by program.createdBy, program.createdBy, program.description, program.endDate, program.Id, program.name, program.notes,
		program.owner, program.startDate, program.status, person.DisplayName , program.S_ITEM_STATUS, program.progress,
		program.createdByUnitId, program.isDeleted
order by 
		case when @sortBy = 'programName' and @sortDir = 'sortAsc' 
		then program.name end asc, 
		case when @sortBy = 'programName' and @sortDir = 'sortDesc' 
		then program.name end desc,
		case when @sortBy = 'programOwner' and @sortDir = 'sortAsc' 
		then person.DisplayName end asc, 
		case when @sortBy = 'programOwner' and @sortDir = 'sortDesc' 
		then person.DisplayName end desc,
		case when @sortBy = 'startDate' and @sortDir = 'sortAsc' 
		then program.startDate end asc, 
		case when @sortBy = 'startDate' and @sortDir = 'sortDesc' 
		then program.startDate end desc,
		case when @sortBy = 'endDate' and @sortDir = 'sortAsc' 
		then program.endDate end asc, 
		case when @sortBy = 'endDate' and @sortDir = 'sortDesc' 
		then program.endDate end desc
OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE);

	
END

GO
