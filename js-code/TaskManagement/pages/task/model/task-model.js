function TaskModel(value) {

  if (value) {
    let obj = {};
    let taskModel = TaskModel();
    for (var i = 0; i < value.length; i++) {
      obj[value[i]] = (taskModel[value[i]]);
    }
    return obj;
  }

  return {
    createTask                        : createTask,
    editTask                          : editTask,
    getMyInProgressTask               : getMyInProgressTask,
    getCompletedTask                  : getCompletedTask,
    startTask                         : startTask,
    getSubUsers                       : getSubUsers,
    getSubUsersRolesAndUnits          : getSubUsersRolesAndUnits,
    getListOfWorkingUser              : getListOfWorkingUser,
    endTask                           : endTask,
    getMyOwnedTask                    : getMyOwnedTask,
    getCountTask                      : getCountTask,
    getCreatedByTask                  : getCreatedByTask,
    getDraftTask                      : getDraftTask,
    getTaskById                       : getTaskById,
    getTaskDataById                   : getTaskDataById,
    updateTargetTaskPerformerProgress : updateTargetTaskPerformerProgress,
    getTargetTaskPerformerDetails     : getTargetTaskPerformerDetails,
    getCreatedTaskByEntityItemId      : getCreatedTaskByEntityItemId,
    deletePerformers                  : deletePerformers,
    deleteTask                        : deleteTask,
    getPerformerDetailsByTaskId       : getPerformerDetailsByTaskId,
    restartTask                       : restartTask,
    getTotalTaskProgress              : getTotalTaskProgress,
    closeTask                         : closeTask,
    updateProgress                    : updateProgress,
    updateTaskWithProject             : updateTaskWithProject,
    getOwners                         : getOwners,
    getArchivedTask                   : getArchivedTask,
    archiveTask                       : archiveTask,
    forceCloseTask                    : forceCloseTask,
    getCreatedTaskMeeting             : getCreatedTaskMeeting,
    getTaskHistoryByTaskId            : getTaskHistoryByTaskId
  }

  function getTaskHistoryByTaskId(taskId){
    let obj = {
      method: "MOD_TM_SP_GetTaskHistoryByTaskId",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_GetTaskHistoryByTaskId",
      param: {
        "RETURN_VALUE": "PARAMETER",
        "TaskId": taskId
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }


  function forceCloseTask(taskId){
    let obj = {
      method: "MOD_TM_BP_ForceCloseTask",
      namespace: "http://schemas.cordys.com/default",
      param: {
        "ownerId": window.user.details.UserEntityId,
        "taskId": taskId
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getArchivedTask (pageNumber, pageSize, sortBy, sortDir, taskName, progress, startDate, endDate, filterOwner){
    pageNumber = pageNumber || 1;
    pageSize = pageSize || assetConfig.pageSize;
    sortBy = sortBy || "endDate";
    sortDir = sortDir || "sortAsc";
    taskName = taskName || "";
    progress = progress || -1;
    startDate = startDate.split("T")[0] || new Date('October 15, 1920 05:35:32').toISOString().split("T")[0];
    endDate = endDate.split("T")[0] || new Date('October 15, 2120 05:35:32').toISOString().split("T")[0];
    filterOwner = filterOwner || -1;
    let obj = {
      method: "MOD_TM_SP_task_GetArchivedTask",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetArchivedTask",
      param: {
        "UserId": window.user.details.UserEntityId,
        "PageNumber": pageNumber,
        "PageSize": pageSize,
        "sortBy": 'progress',
        "sortDir": sortDir,
        "TaskName": taskName,
        "Progress": progress,
        "startDate": startDate,
        "endDate": endDate,
        "filterOwner": filterOwner,
        "RETURN_VALUE": "PARAMETER"
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);

  }

  function updateProgress(id, progress) {
    let obj = {
      method: "MOD_TM_BP_UpdateUserProgress",
      namespace: "http://schemas.cordys.com/default",
      param: {
        targetPerformerEntityId: id,
        progress: progress
      }
    }
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function updateTaskWithProject(taskId, projectId) {
    let obj = {
      method: "UpdateMOD_TM_entity_Task",
      namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_Task/operations",
      param: {
        "MOD_TM_entity_Task-id": {
          "Id": taskId
        },
        "MOD_TM_entity_Task-update": {
          "taskProjectId": projectId
        }
      }
    }
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function archiveTask(taskId) {
    let obj = {
      method: "UpdateMOD_TM_entity_Task",
      namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_Task/operations",
      param: {
        "MOD_TM_entity_Task-id": {
          "Id": taskId
        },
        "MOD_TM_entity_Task-update": {
          "status": 12
        }
      }
    }
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function closeTask(taskId) {
    let obj = {
      method: "UpdateMOD_TM_entity_Task",
      namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_Task/operations",
      param: {
        "MOD_TM_entity_Task-id": {
          "Id": taskId
        },
        "MOD_TM_entity_Task-update": {
          "status": 3
        }
      }
    }
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getTotalTaskProgress(taskId) {
    let obj = {
      method: "MOD_TM_SP_task_GetTotalTaskProgress",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetTotalTaskProgress",
      param: {
        "taskId": taskId,
        "RETURN_VALUE": "PARAMETER"
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function restartTask(taskId, performerId) {
    let obj = {
      method: "MOD_TM_BP_restart_task",
      namespace: "http://schemas.cordys.com/default",
      param: {
        "taskId": taskId,
        "performerId": performerId
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getPerformerDetailsByTaskId(performerId, taskId) {
    let obj = {
      method: "MOD_TM_webservice_TargetTaskPerformer_GetPerformerDetailsByTaskId",
      namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_TargetTaskPerformer/operations",
      param: {
        "taskId": taskId,
        "performerId": performerId
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function deleteTask(taskId) {
    let obj = {
      method: "MOD_TM_BP_delete_task",
      namespace: "http://schemas.cordys.com/default",
      param: {
        "ownerId": window.user.details.UserEntityId,
        "taskId": taskId
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function deletePerformers(performerId, taskId) {
    let obj = {
      method: "MOD_TM_BP_delete_performers",
      namespace: "http://schemas.cordys.com/default",
      param: {
        "performerId": performerId,
        "taskId": taskId
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }
  
  function getCreatedTaskMeeting(userId, entityItemId, type, pageNumber, pageSize, sortBy, sortDir) {
    let obj = {
      method: "MOD_TM_SP_task_GetCreatedTaskMeeting",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetCreatedTaskMeeting",
      param: {
        "Owner": userId,
        "entityItemId": entityItemId,
        "type" : type,
        "PageNumber": pageNumber,
        "PageSize": pageSize,
        "sortBy": sortBy,
        "sortDir": sortDir,
        "RETURN_VALUE": "PARAMETER"
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getCreatedTaskByEntityItemId(userId, entityItemId, pageNumber, pageSize, sortBy, sortDir) {
    let obj = {
      method: "MOD_TM_SP_task_GetCreatedTaskByEntityItemId",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetCreatedTaskByEntityItemId",
      param: {
        "Owner": userId,
        "entityItemId": entityItemId,
        "PageNumber": pageNumber,
        "PageSize": pageSize,
        "sortBy": sortBy,
        "sortDir": sortDir,
        "RETURN_VALUE": "PARAMETER"
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getTargetTaskPerformerDetails(taskId, performerId) {
    let obj = {
      method: "MOD_TM_SP_task_GetTargetTaskPerformerDetails",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetTargetTaskPerformerDetails",
      param: {
        RETURN_VALUE: "PARAMETER",
        TaskId: taskId,
        UserId: performerId
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getTaskDataById(taskId) {
    let obj = {
      method: "MOD_TM_SP_task_GetTaskDataById",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetTaskDataById",
      param: {
        RETURN_VALUE: "PARAMETER",
        taskId: taskId
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getTaskById(taskId) {
    let obj = {
      method: "ReadMOD_TM_entity_Task",
      namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_Task/operations",
      param: {
        "MOD_TM_entity_Task-id": {
          Id: taskId
        }
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getDraftTask(pageNumber, pageSize, sortBy, sortDir, taskName, progress, startDate, endDate, filterOwner) {
    let obj = {
      method: "MOD_TM_SP_task_GetDraftTask",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetDraftTask",
      param: {
        "userId": window.user.details.UserEntityId,
        "PageNumber": pageNumber,
        "PageSize": pageSize,
        "sortBy": sortBy,
        "sortDir": sortDir,
        "TaskName": taskName,
        "Progress": progress,
        "startDate": startDate,
        "endDate": endDate,
        "filterOwner": filterOwner,
        "RETURN_VALUE": "PARAMETER"
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getCreatedByTask(pageNumber, pageSize, sortBy, sortDir, taskName, progress, startDate, endDate, filterOwner) {
    let obj = {
      method: "MOD_TM_SP_task_GetMyCreatedTask",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetMyCreatedTask",
      param: {
        "Owner": window.user.details.UserEntityId,
        "PageNumber": pageNumber,
        "PageSize": pageSize,
        "sortBy": sortBy,
        "sortDir": sortDir,
        "TaskName": taskName,
        "Progress": progress,
        "startDate": startDate,
        "endDate": endDate,
        "filterOwner": filterOwner,
        "RETURN_VALUE": "PARAMETER"
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getMyOwnedTask(pageNumber, pageSize, sortBy, sortDir, taskName, progress, startDate, endDate) {
    let obj = {
      method: "MOD_TM_SP_task_GetMyOwnedTask",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetMyOwnedTask",
      param: {
        "Owner": window.user.details.UserEntityId,
        "PageNumber": pageNumber,
        "PageSize": pageSize,
        "sortBy": sortBy,
        "sortDir": sortDir,
        "TaskName": taskName,
        "Progress": progress,
        "startDate": startDate,
        "endDate": endDate,
        "RETURN_VALUE": "PARAMETER"
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getCompletedTask(pageNumber, pageSize, sortBy, sortDir, taskName, progress, startDate, endDate, filterOwner) {
    let obj = {
      method: "MOD_TM_SP_task_GetMyCompletedTask",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetMyCompletedTask",
      param: {
        "PageNumber": pageNumber,
        "PageSize": pageSize,
        "userId": window.user.details.UserEntityId,
        "sortBy": sortBy,
        "sortDir": sortDir,
        "TaskName": taskName,
        "Progress": progress,
        "startDate": startDate,
        "endDate": endDate,
        "filterOwner": filterOwner,
        "RETURN_VALUE": "PARAMETER"
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);

  }


  function getMyInProgressTask(pageNumber, pageSize, sortBy, sortDir, taskName, progress, startDate, endDate, filterOwner) {
    startDate = (startDate)? startDate :  new Date('October 15, 1920 05:35:32').toISOString().split("T")[0];
    endDate = (endDate)? endDate: new Date('October 15, 2120 05:35:32').toISOString().split("T")[0];
    progress = (progress)? progress : -1;
    filterOwner = (filterOwner)? filterOwner : -1;
    taskName = (taskName) ? taskName : "";
    let obj = {
      method: "MOD_TM_SP_task_GetMyInProgressTask",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetMyInProgressTask",
      param: {
        "PageNumber": pageNumber,
        "PageSize": pageSize,
        "Owner": window.user.details.UserEntityId,
        "sortBy": sortBy,
        "sortDir": sortDir,
        "TaskName": taskName,
        "Progress": progress,
        "startDate": startDate,
        "endDate": endDate,
        "filterOwner": filterOwner,
        "RETURN_VALUE": "PARAMETER"
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);

  }

  function getCountTask(type, taskName, progress, startDate, endDate, filterOwner) {
    let progressL = (!progress) ? 100 : (progress == -1) ? 100 : progress;
    let progressS = (!progress) ? 0 : (progress == -1) ? 0 : progress;
    let filterOwnerL = (!filterOwner) ? 2147483646 : (filterOwner == -1) ? 2147483646 : filterOwner;
    let filterOwnerS = (!filterOwner) ? 0 : (filterOwner == -1) ? 0 : filterOwner;
    taskName = taskName || "";
    startDate = startDate || new Date('October 15, 1920 05:35:32').toISOString().split("T")[0];
    endDate = endDate || new Date('October 15, 2120 05:35:32').toISOString().split("T")[0];

    let obj = {
      method: "MOD_TM_BP_count_task",
      namespace: "http://schemas.cordys.com/default",
      param: {
        "owner": window.user.details.UserEntityId,
        "type": type,
        "taskName": taskName,
        "progressS": progressS,
        "progressL": progressL,
        "startDate": startDate,
        "endDate": endDate,
        "filterOwnerS": filterOwnerS,
        "filterOwnerL": filterOwnerL
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getListOfWorkingUser(taskId, pageNumber, pageSize) {

    let obj = {
      method: "MOD_TM_webservice_TaskPerformer_GetListOfWorkingUser",
      namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_TaskPerformer/operations",
      param: {
        taskId: taskId,
        Cursor: {
          "@offset": pageNumber - 1,
          "@limit": pageSize
        }
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getOwners(input, anyone, unitId,  pageNumber, pageSize) {
    pageNumber = pageNumber || 1;
    pageSize = pageSize || assetConfig.autocompleteSize;

    let headunit = window.user.details.HeadUnit;
    headunit = (headunit)? headunit: "all";

    if(headunit == "MSM" || headunit == "USM" || headunit == "AAM") headunit = "all"

    if(anyone)  headunit = "all";

    if(!unitId) {
      unitId = -1;
    } else if(unitId && unitId != -1) {
      headunit = 'all';
    }
    
    let obj = {
      method: "MOD_SYS_GENERAL_SP_GetUsersUnderHeadUnitCode",
      namespace: "http://schemas.cordys.com/MOD_SYS_GENERAL_SP_GetUsersUnderHeadUnitCode",
      param: {
        headunit: headunit,
        input: input,
        RETURN_VALUE: "PARAMETER",
        PageNumber: pageNumber,
        unitId: unitId,
        PageSize: pageSize
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getSubUsers(userId, input, pageNumber, pageSize) {

    let obj = {
      method: "MOD_TM_SP_task_get_subusers_of_user",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_get_subusers_of_user",
      param: {
        userId: userId,
        input: input,
        RETURN_VALUE: "PARAMETER",
        PageNumber: pageNumber,
        PageSize: pageSize
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function getSubUsersRolesAndUnits(userId, input, pageNumber, pageSize) {
    let obj = {
      method: "MOD_TM_SP_task_get_subUsersRolesUnits_of_user",
      namespace: "http://schemas.cordys.com/MOD_TM_SP_task_get_subUsersRolesUnits_of_user",
      param: {
        userId: userId,
        input: input,
        RETURN_VALUE: "PARAMETER",
        PageNumber: pageNumber,
        PageSize: pageSize
      }
    };
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function startTask(id) {
    let obj = {
      method: "MOD_TM_BP_start_task",
      namespace: "http://schemas.cordys.com/default",
      param: {
        "id": id
      }
    }
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function updateTargetTaskPerformerProgress(id, progress) {

    let obj = {
      method: "UpdateMOD_TM_entity_TargetTaskPerformer",
      namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_TargetTaskPerformer/operations",
      param: {
        "MOD_TM_entity_TargetTaskPerformer-id": {
          "Id": id
        },
        "MOD_TM_entity_TargetTaskPerformer-update": {
          "progress": progress
        }
      }
    }
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function endTask(id) {

    let obj = {
      method: "UpdateMOD_TM_entity_TargetTaskPerformer",
      namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_TargetTaskPerformer/operations",
      param: {
        "MOD_TM_entity_TargetTaskPerformer-id": {
          "Id": id
        },
        "MOD_TM_entity_TargetTaskPerformer-update": {
          "status": 2,
          "progress": 100
        }
      }
    }
    return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
  }

  function editTask(o) {
    return {
      "url": Cordys().getSoapURL(),
      "message": edtiTaskMessage(o)
    }
  }

  function createTask(o) {
    return {
      "url": Cordys().getSoapURL(),
      "message": createTaskMessage(o)
    }
  }


  function edtiTaskMessage(obj) {
    let singleOrMultiple = (obj.singleOrMultiple() instanceof Array) ? obj.singleOrMultiple()[0] : (obj.singleOrMultiple()) ? obj.singleOrMultiple() : 1;
    let taskProject = (obj.taskProject() instanceof Array) ? obj.taskProject()[0] : (obj.taskProject()) ? obj.taskProject() : 0;
    if(taskProject.indexOf(":") != -1){
      taskProject = taskProject.split(":")[0]
    }
    if(taskProject == ""){
      taskProject = 0;
    }
    
    
    return '<SOAP:Envelope xmlns:SOAP="http://schemas.xmlsoap.org/soap/envelope/">' +
      '<SOAP:Body>' +
      '<MOD_TM_BP_edit_task xmlns="http://schemas.cordys.com/default">' +
      '<name>' + obj.taskName() + '</name>' +
      '<description>' + obj.taskDescription() + '</description>' +
      '<startDate>' + new Date(obj.taskStartDate()).toISOString().replace(".000Z", "") + '</startDate>' +
      '<endDate>' + new Date(obj.taskEndDate()).toISOString().replace(".000Z", "") + '</endDate>' +
      '<owner>' + obj.taskOwner() + '</owner>' +
      '<users>' + obj.taskUsers() + '</users>' +
      '<entityItemId>' + obj.taskEntityItemId() + '</entityItemId>' +
      '<pirority>' + obj.taskPriority()[0] + '</pirority>' +
      '<source>' + obj.taskSource() + '</source>' +
      '<taskData>' + obj.taskData() + '</taskData>' +
      '<createdBy>' + window.user.details.UserEntityId + '</createdBy>' +
      '<status>' + obj.status() + '</status>' +
      '<taskProjectId>' + taskProject + '</taskProjectId>' +
      '<controllerName>' + obj.controllerName() + '</controllerName>' +
      '<applicationName>' + obj.applicationName() + '</applicationName>' +
      '<entityName>' + obj.entityName() + '</entityName>' +
      '<tId>' + obj.taskId() + '</tId>' +
      '<editUserId>' + window.user.details.UserEntityId + '</editUserId>' +
      '<singleOrMultiple>' + singleOrMultiple + '</singleOrMultiple>' +
      '<oldUsers>' + obj.deletedUser() + '</oldUsers>' +
      // '<userCN>'+configureCN(window.user.UserName,1)+'</userCN>'+
      '</MOD_TM_BP_edit_task>' +
      '</SOAP:Body>' +
      '</SOAP:Envelope>';
  }


  function createTaskMessage(obj) {
    let taskProject = "";
    if (obj.taskProject) taskProject = (obj.taskProject() instanceof Array) ? obj.taskProject()[0] : (obj.taskProject()) ? obj.taskProject() : "";
    if(taskProject.indexOf(":") != -1){
      taskProject = taskProject.split(":")[0]
    }
    let singleOrMultiple = (obj.singleOrMultiple() instanceof Array) ? obj.singleOrMultiple()[0] : obj.singleOrMultiple();
    let sdate = '';
    let edate = '';
    if (obj.taskStartDate())
      sdate = '<startDate>' + new Date(obj.taskStartDate()).toISOString().replace(".000Z", "") + '</startDate>';
    if (obj.taskEndDate())
      edate = '<endDate>' + new Date(obj.taskEndDate()).toISOString().replace(".000Z", "") + '</endDate>';
    return '<SOAP:Envelope xmlns:SOAP="http://schemas.xmlsoap.org/soap/envelope/">' +
      '<SOAP:Body>' +
      '<MOD_TM_BP_create_task xmlns="http://schemas.cordys.com/default">' +
      '<name>' + obj.taskName() + '</name>' +
      '<description>' + obj.taskDescription() + '</description>' +
      sdate +
      edate +
      '<owner>' + obj.taskOwner() + '</owner>' +
      '<users>' + obj.taskUsers() + '</users>' +
      '<pirority>' + obj.taskPriority()[0] + '</pirority>' +
      '<source>' + obj.taskSource() + '</source>' +
      '<taskData>' + obj.taskData() + '</taskData>' +
      '<entityItemId>' + obj.taskEntityItemId() + '</entityItemId>' +
      '<createdBy>' + window.user.details.UserEntityId + '</createdBy>' +
      '<controllerName>' + obj.controllerName() + '</controllerName>' +
      '<applicationName>' + obj.applicationName() + '</applicationName>' +
      '<entityName>' + obj.entityName() + '</entityName>' +
      '<status>' + obj.status() + '</status>' +
      '<projectId>' + taskProject + '</projectId>' +
      '<singleOrMultiple>' + singleOrMultiple + '</singleOrMultiple>' +
      '<entityBWSId>' + obj.entityBWSId() + '</entityBWSId>' +
      '</MOD_TM_BP_create_task>' +
      '</SOAP:Body>' +
      '</SOAP:Envelope>'
  }

}