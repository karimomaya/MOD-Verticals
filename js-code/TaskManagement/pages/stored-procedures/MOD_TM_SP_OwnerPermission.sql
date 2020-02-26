SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
Create PROCEDURE [dbo].[MOD_TM_SP_OwnerPermission]
	-- Add the parameters for the stored procedure here
    @taskId INT
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	
    -- Insert statements for procedure here
	select DISTINCT task.Id, task.owner, task.createdBy, task.[status]
	, ISNULL(([dbo].[GetTotalFinishedWorkingUsers](task.Id)/ NULLIF([dbo].[GetTotalWorkingUsers](task.Id),0)),0) as finished  
	
from  awdb.dbo.O2MyCompanyTaskManagementMOD_TM_entity_Task as task 
	
	where task.Id =  @taskId
		

	
END

GO
