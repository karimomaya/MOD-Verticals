function ProjectModel(value){
    if(value){
      let obj = {};
      let proj = ProjectModel();
      for(var i=0; i< value.length; i++){
        obj[value[i]] = (proj[value[i]]);
      }
      return obj;
    }
    return {
        getOrgChartUnits            : getOrgChartUnits,
        createProject               : createProject,
        countProject                : countProject,
        getProjectByStatus          : getProjectByStatus,
        readProjectRelatedToProgram : readProjectRelatedToProgram,
        getProjectById              : getProjectById,
        // getProjectProgress          : getProjectProgress,
        getTaskRelatedToProject     : getTaskRelatedToProject,
        editProject                 : editProject,
        deleteProject               : deleteProject,
        changeProjectStatus         : changeProjectStatus,
        getUnitsByUnitTypeCode      : getUnitsByUnitTypeCode,
        getProjectByHeadUnit        : getProjectByHeadUnit
    }

    function changeProjectStatus(projectEntityId, status) {
      let obj={
        method: "MOD_TM_BP_update_project_status",
        namespace: "http://schemas.cordys.com/default",
        param : {
          projectId:projectEntityId,
          ownerId: window.user.details.UserEntityId,
          projectStatus:status
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }

    function deleteProject(entityId){
      let obj={
        method: "DeleteMOD_TM_entity_TaskProject",
        namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_TaskProject/operations",
        param : {
          "MOD_TM_entity_TaskProject-id": {Id : entityId},
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }

    function editProject(o){
      let assignToUnit = (o.assignToUnit() instanceof Array)? o.assignToUnit()[0] : o.assignToUnit();
      if(assignToUnit.indexOf(":") != -1){
        assignToUnit = assignToUnit.split(":")[0]
      }
      let obj = {
        method: "UpdateMOD_TM_entity_TaskProject",
        namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_TaskProject/operations",
        param : {
          "MOD_TM_entity_TaskProject-id" : {Id : o.Id()},
          "MOD_TM_entity_TaskProject-update" : {
            name: o.projectName(),
            description : o.projectDescription(),
            startDate : new Date(o.projectStartDate()).toISOString().replace(".000Z", ""),
            endDate : new Date(o.projectEndDate()).toISOString().replace(".000Z", ""),
            owner : o.projectManager(),
            // status : o.projectStatus(),
            notes : o.projectNotes(),
            // createdBy : o.createdBy(),
            institutionalPlan : o.institutionalPlan(),
            programId: o.projectRelateProg()[0].split(":")[0],
            // createdByUnitId: o.userUnitId(),
            assignToUnitId: assignToUnit
          }

        }
      }
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }

    function getTaskRelatedToProject(projectId){
      let obj = {
        method: "MOD_TM_SP_task_GetTaskRelatedToProject",
        namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetTaskRelatedToProject",
        param : {
          RETURN_VALUE: "PARAMETER",
          ProjectId : projectId,
          Owner : window.user.details.UserEntityId
        }
      }
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }

    /*function getProjectProgress(projectId){
      let obj={
        method: "MOD_TM_SP_task_GetProjectProgress",
        namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetProjectProgress",
        param : {
          "RETURN_VALUE" : "PARAMETER",
          "ProjectId"   : projectId,
          "Owner"       : window.user.details.UserEntityId
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }*/

    function getProjectById(projectId) {
      let obj={
        method: "MOD_TM_SP_task_GetProjectById",
        namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetProjectById",
        param : {
          "RETURN_VALUE" : "PARAMETER",
          "ProjectId"   : projectId,
          "Owner"       : window.user.details.UserEntityId
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    } 
    
    function readProjectRelatedToProgram(programId, sortDir, sortBy, pageNumber, pageSize) {
      let obj={
        method: "MOD_TM_SP_ReadProjectRelatedToProgram",
        namespace: "http://schemas.cordys.com/MOD_TM_SP_ReadProjectRelatedToProgram",
        param : {
          "RETURN_VALUE" : "PARAMETER",
          "programId"   : programId,
          "owner"       : window.user.details.UserEntityId,
          "sortDir"     : sortDir,
          "sortBy"      : sortBy,
          "PageNumber"  : pageNumber,
          "PageSize"    : pageSize
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }

    function getProjectByHeadUnit(input, status, pageNumber, pageSize){
      let headunit = window.user.details.HeadUnit;
      let unitId = window.user.details.UnitId;
      headunit = (headunit)? headunit: "all";
      if(headunit == "MSM" ||headunit == "USM" ||headunit == "AAM" ) unitId = 0
      let obj={
        method: "MOD_TM_SP_task_GetProjectByHeadUnit",
        namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetProjectByHeadUnit",
        param : {
          "unitId" : unitId,
          "input" : input,
          "status" : status,
          "PageNumber" : pageNumber,
          "PageSize" : pageSize,
          "RETURN_VALUE": "PARAMETER"
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }
    // status, pageNumber, pageSize, sortBy, sortDir, projectFilterObject
    function getProjectByStatus(status, pageNumber, pageSize, sortBy, sortDir, filterObject){
      let obj={
        method: "MOD_TM_SP_task_GetProjectByStatus",
        namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetProjectByStatus",
        param : {
          "userId" : window.user.details.UserEntityId,
          "status" : status,
          "PageNumber" : pageNumber,
          "PageSize" : pageSize,
          "sortBy" : sortBy, 
          "sortDir" : sortDir,
          "Progress": filterObject.progress,
          "StartDate": filterObject.startDate,
          "EndDate": filterObject.endDate,
          "ProjectName": filterObject.freeText,
          "FilterOwner": filterObject.filterOwner,
          "RETURN_VALUE": "PARAMETER"
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }


    function countProject(type, status, projectFilterObject) {
      let progressL = (!projectFilterObject.progress) ? 100 : (projectFilterObject.progress == -1) ? 100 : projectFilterObject.progress;
      let progressS = (!projectFilterObject.progress) ? 0 : (projectFilterObject.progress == -1) ? 0 : projectFilterObject.progress;
      let filterOwnerS = (!projectFilterObject.filterOwner) ? 0 : (projectFilterObject.filterOwner == -1) ? 0 : projectFilterObject.filterOwner;
      let filterOwnerL = (!projectFilterObject.filterOwner) ? 2147483646 : (projectFilterObject.filterOwner == -1) ? 2147483646 : projectFilterObject.filterOwner;
      projectFilterObject.freeText = projectFilterObject.freeText || "";
      projectFilterObject.filterOwner = projectFilterObject.filterOwner || "";
      projectFilterObject.startDate = projectFilterObject.startDate || new Date('October 15, 1920 05:35:32').toISOString().split("T")[0];
      projectFilterObject.endDate = projectFilterObject.endDate || new Date('October 15, 2120 05:35:32').toISOString().split("T")[0];
      let obj={
        method: "MOD_TM_BP_count_task",
        namespace: "http://schemas.cordys.com/default",
        param : {
          "owner" : window.user.details.UserEntityId,
          "type" : type,
          "status": status,
          "taskName": projectFilterObject.freeText,
          "progressS": progressS,
          "progressL": progressL,
          "startDate": projectFilterObject.startDate,
          "endDate": projectFilterObject.endDate, 
          "filterOwnerS" : filterOwnerS,
          "filterOwnerL" : filterOwnerL
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }

    function createProject(o) {
      let programId = ( o.projectRelateProg())?  o.projectRelateProg()[0].split(":")[0] : "";
      let assignToUnit = (o.assignToUnit() instanceof Array)? o.assignToUnit()[0] : o.assignToUnit();
      if(assignToUnit.indexOf(":") != -1){
        assignToUnit = assignToUnit.split(":")[0]
      }
      let obj={
        method: "CreateMOD_TM_entity_TaskProject",
        namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_TaskProject/operations",
        param : {
          "MOD_TM_entity_TaskProject-create" : {
            name: o.projectName(),
            description : o.projectDescription(),
            startDate : new Date(o.projectStartDate().split(":")[0]).toISOString().replace(".000Z", ""),
            endDate : new Date(o.projectEndDate().split(":")[0]).toISOString().replace(".000Z", ""),
            owner : o.projectManager(),
            status : o.projectStatus(),
            notes : o.projectNotes(),
            createdBy : o.createdBy(),
            institutionalPlan : o.institutionalPlan(),
            programId: programId,
            createdByUnitId: o.userUnitId(),
            progress: 0,
            assignToUnitId: assignToUnit
          }
        }
      }
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }

    function getOrgChartUnits(input, pageNumber, pageSize){
      let obj={
        method: "MOD_TM_SP_task_get_subUnits_of_user",
        namespace: "http://schemas.cordys.com/MOD_TM_SP_task_get_subUnits_of_user",
        param : {
          "userId" : window.user.details.UserEntityId,
          "input"  : input,
          "PageSize" : pageSize,
          "PageNumber" : pageNumber,
          "RETURN_VALUE" : "PARAMETER"
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }

    function getUnitsByUnitTypeCode(unitTypeCode, input, pageNumber, pageSize){
      let obj={
        method: "MOD_TM_SP_task_GetUnitsByUnitTypeCode",
        namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetUnitsByUnitTypeCode",
        param : {
          "unitTypeCode"  : unitTypeCode,
          "input"  : input,
          "PageSize" : pageSize,
          "PageNumber" : pageNumber,
          "RETURN_VALUE" : "PARAMETER"
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }
}