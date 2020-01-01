function ProgramModel(value) {
    if(value){
      let obj = {};
      let model = ProgramModel();
      for(var i=0; i< value.length; i++){
        obj[value[i]] = (model[value[i]]);
      }
      return obj;
    }
    return {
        createProgram             : createProgram,
        changeProgramStatus       : changeProgramStatus,
        readProgram               : readProgram,
        getProgramByPathAndStatus : getProgramByPathAndStatus,
        countProgram              : countProgram,
        getProgramByIdAndOwner    : getProgramByIdAndOwner,
        getProgramProgress        : getProgramProgress,
        updateProgram             : updateProgram,
        deleteProgram             : deleteProgram
    }

    function deleteProgram(entityId){
      let obj={
        method: "DeleteMOD_TM_entity_Program",
        namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_Program/operations",
        param : {
          "MOD_TM_entity_Program-id": {Id : entityId},
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }

    function updateProgram(o){
      let obj={
        method: "UpdateMOD_TM_entity_Program",
        namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_Program/operations",
        param : {
          "MOD_TM_entity_Program-id": {Id : o.Id},
          "MOD_TM_entity_Program-update" : {
            name: o.programName(),
            description: o.programDescription(),
            startDate: new Date(o.programStartDate()).toISOString().replace(".000Z", ""),
            endDate: new Date(o.programEndDate()).toISOString().replace(".000Z", ""),
            notes: o.programNotes(),
            owner: o.ownerId()
          }
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }

    function getProgramProgress(programId){
      let obj={
        method: "MOD_TM_SP_task_GetProgramProgress",
        namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetProgramProgress",
        param : {
          "RETURN_VALUE" : "PARAMETER",
          "ProgramId" : programId
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }

    function getProgramByIdAndOwner(programEntityId) {
      let obj={
        method: "MOD_TM_SP_task_GetProgramByIdAndOwner",
        namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetProgramByIdAndOwner",
        param : {
          RETURN_VALUE : "PARAMETER",
          "ProgramEntityId" : programEntityId,
          "Owner" : window.user.details.UserEntityId
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }

    function countProgram(type, status, programFilterObject) {
      let progressL = (!programFilterObject.progress) ? 100 : (programFilterObject.progress == -1) ? 100 : programFilterObject.progress;
      let progressS = (!programFilterObject.progress) ? 0 : (programFilterObject.progress == -1) ? 0 : programFilterObject.progress;
      let filterOwnerS = (!programFilterObject.filterOwner) ? 0 : (programFilterObject.filterOwner == -1) ? 0 : programFilterObject.filterOwner;
      let filterOwnerL = (!programFilterObject.filterOwner) ? 2147483646 : (programFilterObject.filterOwner == -1) ? 2147483646 : programFilterObject.filterOwner;
      programFilterObject.freeText = programFilterObject.freeText || "";
      programFilterObject.filterOwner = programFilterObject.filterOwner || "";
      programFilterObject.startDate = programFilterObject.startDate || new Date('October 15, 1920 05:35:32').toISOString().split("T")[0];
      programFilterObject.endDate = programFilterObject.endDate || new Date('October 15, 2120 05:35:32').toISOString().split("T")[0];
      let obj={
        method: "MOD_TM_BP_count_task",
        namespace: "http://schemas.cordys.com/default",
        param : {
          "owner" : window.user.details.UserEntityId,
          "type" : type,
          "status": status,
          "taskName": programFilterObject.freeText,
          "progressS": progressS,
          "progressL": progressL,
          "startDate": programFilterObject.startDate,
          "endDate": programFilterObject.endDate, 
          "filterOwnerS" : filterOwnerS,
          "filterOwnerL" : filterOwnerL
        }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }


    function getProgramByPathAndStatus(path, input, status, pageNumber, pageSize){
      let obj={
        method: "MOD_TM_SP_task_GetProgramByPath",
        namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetProgramByPath",
        param : {
            input : input,
            status : status,
            path : path,
            PageNumber : pageNumber,
            PageSize: pageSize,
            RETURN_VALUE: "PARAMETER"
          
        }
  
      }
      
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }

    
  function readProgram(status, pageNumber, pageSize, sortBy, sortDir, filterObject) {
    let obj={
      method: "MOD_TM_SP_read_program",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_read_program",
      param : {
        PageNumber: pageNumber,
        PageSize: pageSize,
        userId: window.user.details.UserEntityId,
        status: status,
        sortBy: sortBy,
        sortDir: sortDir,
        Progress: filterObject.progress,
        StartDate: filterObject.startDate,
        EndDate: filterObject.endDate,
        ProgramName: filterObject.freeText,
        FilterOwner: filterObject.filterOwner,
        RETURN_VALUE : "PARAMETER"
      }
    };
    return Cordys().cordysAjax(obj.method,obj.namespace,obj.param, sortBy, sortDir);
    
  }

  function createProgram(o) {
    let obj={
        method: "CreateMOD_TM_entity_Program",
        namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_Program/operations",
        param : {
          "MOD_TM_entity_Program-create" : {
            name: o.programName(),
            description : o.programDescription(),
            startDate : new Date(o.programStartDate()).toISOString().replace(".000Z", ""),
            endDate : new Date(o.programEndDate()).toISOString().replace(".000Z", ""),
            owner : o.programOwner(),
            progress : o.programProgress(),
            notes : o.programNotes(),
            createdBy : o.programCreatedBy(),
            owner : o.programOwner(),
            progress : 0,
            status: o.programStatus(),
            createdByUnitId: window.user.details.UnitId
          }
      }

    }
    return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
  } 

  function changeProgramStatus(programId, status) {
    let obj={
      method: "MOD_TM_BP_update_program_status",
      namespace: "http://schemas.cordys.com/default",
      param : {
        programId:programId,
        ownerId:window.user.details.UserEntityId,
        status:status
      }
    };
    return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
  }

}