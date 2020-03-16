SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetProjectByStatus]
	-- Add the parameters for the stored procedure here
	@userId int,
	@status int,
	@PageNumber INT = 1,
	@PageSize   INT = 10,
	@sortBy varchar(max),
	@sortDir varchar(max),
    @ProjectName NVARCHAR(MAX),
	@Progress int,
	@StartDate Date,
	@EndDate Date,
	@FilterOwner int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	/****** Script for SelectTopNRows command from SSMS  ******/
SELECT project.*,count(task.taskName) as taskCount,  program.name as programName,
		person.DisplayName 
FROM [awdb].[dbo].[O2MyCompanyTaskManagementMOD_TM_entity_TaskProject] as project 
	left join [awdb].[dbo].[O2MyCompanyTaskManagementMOD_TM_entity_Program] as program on program.Id = project.programId
	inner join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id =project.owner
	inner join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id
	left join O2MyCompanyTaskManagementMOD_TM_entity_Task as task on task.taskProjectId = project.Id
    left join  O2MyCompanyTaskManagementMOD_TM_entity_kpi as kpi on kpi.entityId = project.Id and kpi.[type]=2
where (project.createdBy = @userId or project.owner = @userId or kpi.[owner] = @userId) and project.status = @status 
    -- and (@Progress = (CAST(sum(task.progress) as float) / (CAST(count(task.taskName)* 100 as float) ) * 100 ) or @Progress = -1) 
    and (project.startDate >=  @StartDate and  project.endDate <=  @EndDate) 
	and (project.owner = @FilterOwner or @FilterOwner = -1) and project.name like '%'+@ProjectName+'%'
	and project.isDeleted != 1

group by project.assignToUnitId, project.createdBy, project.createdByUnitId, project.description, project.endDate,
	project.Id, project.institutionalPlan, project.name, project.notes, project.owner, project.programId, 
	project.S_ITEM_STATUS, project.startDate, project.status, person.DisplayName, program.name, project.progress
	,project.isDeleted
order by
	case when @sortBy = 'projectName' and @sortDir = 'sortAsc' 
		then project.name end asc, 
	case when @sortBy = 'projectName' and @sortDir = 'sortDesc' 
		then project.name end desc,
	case when @sortBy = 'projectOwner' and @sortDir = 'sortAsc' 
		then person.DisplayName end asc, 
	case when @sortBy = 'projectOwner' and @sortDir = 'sortDesc' 
		then person.DisplayName end desc,
	case when @sortBy = 'startDate' and @sortDir = 'sortAsc' 
		then project.startDate end asc, 
	case when @sortBy = 'startDate' and @sortDir = 'sortDesc' 
		then project.startDate end desc,
	case when @sortBy = 'endDate' and @sortDir = 'sortAsc' 
		then project.endDate end asc, 
	case when @sortBy = 'endDate' and @sortDir = 'sortDesc' 
		then project.endDate end desc
	OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE);
END

GO
