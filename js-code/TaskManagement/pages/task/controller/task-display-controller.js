const taskDisplayControllerViews = {};
const taskDisplayControllerStyles = [
    "/cordys/html5/demo/TaskManagement/pages/task/resources/styles/style.css"
];
const taskDisplayControllerScripts = [
    "/cordys/html5/demo/TaskManagement/pages/task/controller/task-data-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/shared/controller/dynamic-progress-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/shared/controller/dynamic-title-bar-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/performer/controller/performer-controller.js",
    // "/cordys/html5/demo/TaskManagement/pages/shared/controller/content-progress-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/task/model/task-model.js",
    "/cordys/html5/demo/TaskManagement/pages/note/model/note-model.js",
    "/cordys/html5/demo/TaskManagement/pages/task/services/task-service.js",
    "/cordys/html5/demo/TaskManagement/pages/note/controller/note-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/note/controller/note-create-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/task/controller/task-create-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/task/controller/task-history-controller.js",
    '/cordys/html5/demo/TaskManagement/pages/project/controller/project-selector-controller.js'
];

var dependency = Dependency.getInstance();
// dependency.addToTrigger("taskDisplayControllerOnReady");
dependency.execute("taskDisplayController");
let taskDisplay = {
    system: null,
    model: null,
    dom: null,
    taskData: null,
    taskType: false, // task single or multiple
    taskId: null,
    performerStatus: 0,
    isOwner: false
}

function taskDisplayControllerOnload() {
    dependency.initializeSystemObject([ 'getUrlVariable', 'removeNullAndUndefined',
        'translatePage', 'showLoader', 'handleUnAuthorizedEntry', 'showMessage', 'decrypt',
        'convertIntPirotityToString', 'translateWord', 'handleError', 'encrypt', 'attachBWS'
    ]);
    dependency.initializeDomObject(['publish', 'subscribe']);

    taskDisplay.model = TaskModel(['startTask', 'closeTask', 'restartTask', 'getTargetTaskPerformerDetails', 'endTask', 'updateProgress', 'deleteTask', 'getTaskDataById', 'getTaskById']);

    let taskId = window.system.getUrlVariable("taskId");
    taskId = window.system.decrypt(taskId);
    taskDisplay.taskId = taskId;
    // publish taskId to subscribers
    window.dom.publish('task-id', taskId);
    // load task data
    getTaskDataById(taskId);

    window.system.translatePage(window.lang, "task", "TaskManagement", "task-display");

    window.system.showLoader(false);

    window.dom.subscribe("progress-update", function (obj) {
        updateProgress(obj);
    });

    window.dom.subscribe("start-task", function (obj) {
        startTask(obj);
    });

    window.dom.subscribe("acquire", function (entityId) {
        
        startTask(entityId);
    });
    window.dom.subscribe("finish", function (entityId) { // user end task
        finishTask(entityId)
    });
    window.dom.subscribe("end", function (entityId) { // owner close task
        closeTask(entityId);
    });
    window.dom.subscribe("edit", function () {
        sendEditTask();

    });
    window.dom.subscribe("delete", function (entityId) {
        deleteTask(entityId);
    });
    window.dom.subscribe("restart", function (entityId) {
        restartTask(entityId);
    });
}

function restartTask(entityId) {
    if (!window.targetTaskPerformerId) window.targetTaskPerformerId = window.user.details.UserEntityId;

    taskDisplay.model.restartTask(entityId, window.targetTaskPerformerId).done(function () {
        window.system.showLoader(false);
        window.system.showMessage( "success",window.system.translateWord("restart-task-success"));
        window.dom.publish("restart-task-success", taskDisplay.taskType)
    }).fail(function (error) {
        system.handleError(error);
    })
}

function sendEditTask() {
    let taskId = window.system.getUrlVariable("taskId");
    taskId = window.system.decrypt(taskId);
    taskDisplay.model.getTaskDataById(taskId).done(function (response) {
        if (!response.tuple[0]) { // response is empty
            window.system.handleUnAuthorizedEntry();
            return;
        }
        response = response.tuple[0].old.rowset.row;
        // if (response instanceof Array) response = response[0];

        window.dom.publish("edit-task", response);
    });
}


function reloadDisplayPage(){
    window.location.reload(true);
}

function updateProgress(obj) {
    taskDisplay.model.updateProgress(obj.entityId, obj.progress).done(function (response) {
        window.system.showMessage("success", window.system.translateWord("updateProgress"))
    }).fail(function (error) {
        // window.system.handleError(error);
    });
}

function closeTask(entityId) {
    taskDisplay.model.closeTask(entityId).done(function () {
        window.dom.publish("end-success", true);
        window.system.showMessage("success", window.system.translateWord("close-task-success"));
        window.history.back();
    }).fail(function (error) {
        window.system.handleError(error)
    })
}

function finishTask(entityId) {
    taskDisplay.model.endTask(entityId).done(function () {
        window.system.showMessage("success", window.system.translateWord("taskFinish"))
        window.history.back();
    }).fail(function (error) {
        window.system.handleError(error);
    });
}


function deleteTask(entityId) {
    taskDisplay.model.deleteTask(entityId).done(function () {
        window.system.showMessage("success", window.system.translateWord("deleteSuccess"))
        window.history.back();
    }).fail(function (error) {
        window.system.handleError(error);
    });
}

function startTask(entityId) {
    taskDisplay.performerStatus = 1;
    taskDisplay.model.startTask(entityId).done(function () {
        window.dom.publish("start-task-success", true);
        window.system.showMessage("success", window.system.translateWord("task-start-success"))
        location.reload();
    }).fail(function (error) {
        window.system.handleError(error);
    });
}

function getTaskDataById(taskId) {
    taskDisplay.model.getTaskDataById(taskId).done(function (response) {
        if (!response.tuple[0]) { // response is empty
            window.system.handleUnAuthorizedEntry();
            return;
        }
        response = response.tuple[0].old.rowset.row;
        if (response instanceof Array) response = response[0];

        // taskDisplay.taskData = response;

        response = window.system.removeNullAndUndefined(response);

        let taskCreatedById = response.createdById;
        let taskOwnerId = response.ownerId;
        let userId = window.user.details.UserEntityId;
        let isOwner = false;
        if (userId == taskOwnerId || userId == taskCreatedById){ 
            isOwner = true;
            taskDisplay.isOwner = true;
        }

        let priority = (window.lang == "ar") ? response['lookup_ar'] : response['lookup_eng']

        //response.status is task status
        publishToTaskPerformers(taskId, isOwner, response.status, taskOwnerId, taskCreatedById);

        let taskStatus = parseInt(response.status);

        if (taskStatus == 1 || taskStatus == 3 || taskStatus == 11 || taskStatus == 12){ // if task is not started or finished or obselate or archived
            window.system.addDisbaled("add-note-btn")
    
        }
        if (isOwner) {
            let obj = {
                taskId: taskId,
                taskStatus: response.status
            }
            publishToDynamicTitleAsOwner(response.taskName, taskStatus, response.progress, response.Id)
            window.dom.publish("task-note", obj);
            publishAsOwnerToDynamicProgress(response.progress, priority);
            window.system.showLoader(false);
        } else {
            taskDisplay.taskType = (response.singleOrMultiple == "false") ? false : true;
            getTargetPerformersDetails(taskId, priority, taskStatus, taskDisplay.taskType, response.taskName);

        }

        response = cleanObjectBeforeBinding(response);
        response.taskProjectName = ko.observable(response.taskProjectName);
        window.dom.publish("task-data", response);


        if (response.applicationName == "" || response.applicationName == "TaskManagement" ||
            response.controllerName == "" || response.entityName == "" || response.entityItemId == "") return;

        if (response.entityItemId != "undefined" || response.entityItemId != "") $("#task-data").attr("entity-item-id", window.system.encrypt(response.entityItemId))

        if (response.taskData != "undefined" || response.taskData != "") $("#task-data").attr("task-data", window.system.encrypt(response.taskData))

        let controllerPath = "/cordys/html5/demo/" + response.applicationName + "/pages/" + response.entityName + "/controller/" + response.controllerName;
        dependency.addScriptToHeader(controllerPath);


        document.getElementById("action-tabs").innerHTML += '<a class="right-nav-link nav-link" onclick="changeNavActive(this)" href="#process-details" data-toggle="tab" translate-text="linked-service-data">' + System().translateWord('linked-service-data') + '</a>';



    }).fail(function (error) {
        window.system.handleError(error);
    });
}


function publishAsPerformerToDynamicProgress(progress, taskStatus, singleOrMultiple, performerStatus, priority, perfomerEntityId) {
    taskDisplay.performerStatus = performerStatus;

    let haveAction = true;
    let haveStart = false;
    let haveAcquire = false;
    let haveFinish = false;
    let haveEdit = false;
    // task status: 0:stopped, 1: not started, 2: started, 3: finished 10: draft, 11: obselate
    if (taskStatus == 3 || taskStatus == 0 || taskStatus == 11) { // task is finished
        haveAction = false;
        haveStart = false;
        haveAcquire = false;
        haveFinish = false;
        haveEdit = false;
    } else {
        if (performerStatus == 0 && !singleOrMultiple) {
            haveAcquire = true;
        } else if (performerStatus == 0 && singleOrMultiple) {
            haveStart = true;
        } else if (parseInt(progress) == 100) {
            haveFinish = true;
        } else if (performerStatus == 1) {
            haveEdit = true;
        }
    }

    

    dynamicProgressHelperObj = {
        progress: progress,
        haveAction: haveAction,
        haveStart: haveStart,
        haveAcquire: haveAcquire,
        haveFinish: haveFinish,
        haveEdit: haveEdit,
        priority: priority,
        entityId: perfomerEntityId,
        havePriority: true
    }

    window.dom.publish("dynamic-progress", dynamicProgressHelperObj);
}

function getTargetPerformersDetails(taskId, priority, taskStatus, singleOrMultiple, taskName) {
    taskDisplay.model.getTargetTaskPerformerDetails(taskId, window.user.details.UserEntityId).done(function (response) {
        if (!response.tuple[0]) {
            window.system.handleUnAuthorizedEntry()
            throw "unauthorized";
        }
        response = response.tuple[0].old.rowset.row;
        let obj = {
            taskId: taskId,
            performerStatus: response.targetTaskPerformerStatus
        }
        window.dom.publish("task-note", obj);
        window.targetTaskPerformerId = response.targetTaskPerformerId;
        publishToDynamicTitleAsPerformer(taskName, taskStatus, response.targetTaskPerformerStatus, taskId)

        publishAsPerformerToDynamicProgress(response.progress, taskStatus, singleOrMultiple, response.targetTaskPerformerStatus, priority, response.targetTaskPerformerId)


    }).fail(function (error) {
        window.system.handleError(error);
    });
}

function publishAsOwnerToDynamicProgress(progress, priority, id) {
    let dynamicProgressHelperObj = {
        progress: parseInt(progress),
        haveAction: false,
        haveStart: false,
        haveAcquire: false,
        haveFinish: false,
        haveEdit: false,
        priority: priority,
        havePriority: true,
        entityId: id
    }
    window.dom.publish("dynamic-progress", dynamicProgressHelperObj);
}

function publishToDynamicTitleAsPerformer(taskName, taskStatus, performerStatus, taskId) {
    haveAction = false;
    haveEdit = false;
    haveDelete = false;
    haveEnd = false;
    canRestartTask = false;
    if (taskStatus != 3 || taskStatus != 0 || taskStatus != 11) { // if task is not finished  
        if (performerStatus == 1) {
            haveAction = true;
            canRestartTask = true;
        }
    }


    let dynamicTitleHelperObj = {
        title: taskName,
        haveAction: haveAction,
        haveEdit: haveEdit,
        haveDelete: haveDelete,
        haveEnd: haveEnd,
        entityId: taskId,
        canRestartTask: canRestartTask
    }
    window.dom.publish("dynamic-title", dynamicTitleHelperObj);
}

function publishToDynamicTitleAsOwner(taskName, taskStatus, progress, entityId) {
    taskStatus = parseInt(taskStatus);
    progress = parseInt(progress);
    // 0:stopped, 1: not started, 2: started, 3: finished 10: draft, 11: obselate
    let haveAction = false;
    let haveEdit = false;
    let haveDelete = false;
    let haveEnd = false;
    if (taskStatus == 3 || taskStatus == 0 || taskStatus == 11) { // if task finished or ended or obselate 
        haveAction = false;
        haveEdit = false;
        haveDelete = false;
        haveEnd = false;
    } else if (taskStatus == 1 || taskStatus == 10) {
        haveAction = true;
        haveEdit = true;
        haveDelete = true;
    } else if (taskStatus == 2 && progress == 100) {
        haveAction = true;
        haveEdit = true;
        haveEnd = true;
    } else if (taskStatus == 2) {
        haveAction = true;
        haveEdit = true;
    }
    let dynamicTitleHelperObj = {
        title: taskName,
        haveAction: haveAction,
        haveEdit: haveEdit,
        haveDelete: haveDelete,
        haveEnd: haveEnd,
        entityId: entityId,
        canRestartTask: false
    }
    window.dom.publish("dynamic-title", dynamicTitleHelperObj);
}

function publishToTaskPerformers(taskId, isOwner, taskStatus, taskOwnerId, taskCreatedById) {
    let taskPerformersHelperObj = {
        taskId: taskId,
        isOwner: isOwner,
        taskStatus: taskStatus,
        taskOwnerId: taskOwnerId,
        taskCreatedById: taskCreatedById
    }
    window.dom.publish("task-performers", taskPerformersHelperObj)
}

function cleanObjectBeforeBinding(obj) {
    obj['priority'] = window.system.translateWord(window.system.convertIntPirotityToString(parseInt(obj['priority'])));
    obj['singleOrMultiple'] = (obj['singleOrMultiple'] == "false") ? "task-single" : "task-multiple";
    obj['singleOrMultiple'] = window.system.translateWord(obj['singleOrMultiple']);
    obj['startDate'] = obj['startDate'] = obj['startDate'].split("T")[0];
    obj['dueDate'] = obj['dueDate'] = obj['dueDate'].split("T")[0];
    return obj;
}


function reloadPerformers() {
    waitForActive(document.getElementById("task-performer"), function () {
        window.dom.publish("onclick-task-performers", null)
    });
}

function waitForActive(e, callback) {
    if (e.offsetTop > 0) {
        callback();
    } else {
        window.requestAnimationFrame(function () {
            waitForActive(e, callback)
        });
    }
}

function reloadBWS() {
    if(taskDisplay.performerStatus != 1 && !taskDisplay.isOwner){
        window.system.showMessage("warn", window.system.translateWord("you-need-to-start-task"))
        return;
    }
    // start/web/perform/items/000C2934C078A1E999792678B8110454.<Id>/000C29C52049A1E9BCD163BC5CA78445
    waitForActive(document.getElementById("task-attachment"), function () {
        $("#add-note-btn-div").css("display", "none")
        if (!$('#task-attachment').attr("reloaded")) {

            taskDisplay.model.getTaskById(taskDisplay.taskId).done(function (response) {
                console.log(response['wstxns2:MOD_TM_entity_Task']);
                window.system.attachBWS(response['wstxns2:MOD_TM_entity_Task']['MOD_TM_entity_Task-id'].ItemId, _ConfigURL('taskBWS'));
            });

        }
    });
}

function changeNavActive(e) {
    var ele = e.parentElement.getElementsByClassName("current-tab");
    ele[0].classList.remove("current-tab");
    e.classList.add("current-tab");
}


function reloadTaskNote() {
    $("#add-note-btn-div").css("display", "inline")
    window.dom.publish('reload-note', true);
}