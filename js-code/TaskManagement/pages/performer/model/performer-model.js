function PerformerModel(value) {
  if(value){
    let obj = {};
    let performerModel = PerformerModel();
		for(var i=0; i< value.length; i++){
			obj[value[i]] = (performerModel[value[i]]);
		}
		return obj;
	}
  return {
      readPerformers                          : readPerformers,
      deletePerformers                        : deletePerformers,
      updatePerformer                         : updatePerformer,
      getTargetPerformerDetailsByPerformerId  : getTargetPerformerDetailsByPerformerId,
      returnTask                              : returnTask,
      getSubUsers                             : getSubUsers
  }

  function getSubUsers(userId, input, pageNumber, pageSize){
    
    let obj={
      method: "MOD_TM_SP_task_get_subusers_of_user",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_get_subusers_of_user",
      param : {
        userId: userId,
        input: input,
        RETURN_VALUE : "PARAMETER",
        PageNumber : pageNumber,
        PageSize : pageSize
      }
    };
    return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
  }

  function returnTask(performerEntityId) {
    let obj={
      method: "UpdateMOD_TM_entity_TargetTaskPerformer",
      namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_TargetTaskPerformer/operations",
      param : {
        "MOD_TM_entity_TargetTaskPerformer-id" : {Id: performerEntityId},
        "MOD_TM_entity_TargetTaskPerformer-update": {"status" : 1}
      }
    };
    return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
  }

  function getTargetPerformerDetailsByPerformerId(performerId) {
    let obj={
      method: "MOD_TM_SP_task_GetTargetPerformerDetailsByPerformerId",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetTargetPerformerDetailsByPerformerId",
      param : {
        "performerId" : performerId,
        "RETURN_VALUE": "PARAMETER",
        "PageNumber" : 1, 
        "PageSize": 200,
        "sortBy": "sortBy",
        "sortDir": "sortDir"
      }
    };
    return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
  }

  function readPerformers(taskId, pageNumber, pageSize) {
      let obj={
          method: "MOD_TM_SP_task_GetListOfWorkingUser",
          namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetListOfWorkingUser",
          param : {
              "RETURN_VALUE" : "PARAMETER",
              PageNumber : pageNumber,
              PageSize : pageSize,
              taskId : taskId
          }
      };
      return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
  }

  function deletePerformers(performerId, taskId) {
    let obj={
      method: "MOD_TM_BP_delete_performers",
      namespace: "http://schemas.cordys.com/default",
      param : {
        "performerId" : performerId,
        "taskId" : taskId
      }
    };
    return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
  }

  function updatePerformer(o) {
    let obj={
      method: "MOD_TM_BP_change_task_performer",
      namespace: "http://schemas.cordys.com/default",
      param : {
        "performerId"           : o.performerId(),
        "dispalyName"           : o.displayName(),
        "taskPerformerEntityId" : o.performerEntityId(),
        "oldPerformerId"        : o.oldPerformerId(),
        "comment"               : o.comment()
      }
    };
    return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
  }
  
}