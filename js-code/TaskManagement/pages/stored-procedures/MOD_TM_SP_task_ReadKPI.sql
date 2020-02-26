SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_ReadKPI]
  @PageNumber INT = 1,
  @PageSize   INT = 10,
  @entityId	  INT,
  @type       INT

AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
select kpi.Id, kpi.name, kpi.startDate, kpi.endDate, kpi.[description], kpi.[type], person.DisplayName, kpi.owner
	from awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_kpi as kpi 
INNER JOIN O2MyCompanyTaskManagementMOD_TM_entity_Program as program on program.Id = kpi.entityId
inner join O2OpenTextEntityIdentityComponentsIdentity as iden on iden.Id = kpi.owner
inner join O2OpenTextEntityIdentityComponentsPerson as person on iden.toPerson_Id = person.Id

WHERE program.Id = @entityId AND kpi.[type] = @type
	ORDER by kpi.name ASC
	
	
	
OFFSET @PageSize * (@PageNumber - 1) ROWS
    FETCH NEXT @PageSize ROWS ONLY OPTION (RECOMPILE);

END

GO
