USE [awdb]
GO
/****** Object:  StoredProcedure [dbo].[MOD_TM_SP_task_GetProgramProgressReport]    Script Date: 1/5/2020 6:27:24 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[MOD_TM_SP_task_GetProgramProgressReport]
	-- Add the parameters for the stored procedure here
	@Owner int
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	

select distinct(select count(*) from O2MyCompanyTaskManagementMOD_TM_entity_Program where status = 2 and (owner = @Owner or createdBy = @Owner )) as ended,  
(select count(*) from O2MyCompanyTaskManagementMOD_TM_entity_Program where status <> 2 and isDeleted <> 1 and (owner = @Owner or createdBy = @Owner )) as inProgress
from O2MyCompanyTaskManagementMOD_TM_entity_Program where owner = @Owner or createdBy = @Owner and isDeleted <> 1

END





