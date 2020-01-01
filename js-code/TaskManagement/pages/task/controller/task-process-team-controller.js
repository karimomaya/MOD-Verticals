const taskProcessTeamControllerViews = {
    "mod-team-task": "/cordys/html5/demo/TaskManagement/pages/task/views/task-table.html"
};
const taskProcessTeamControllerStyles = [];
const taskProcessTeamControllerScripts = [
    '/cordys/html5/demo/TaskManagement/pages/task/model/task-model.js',
    '/cordys/html5/demo/TaskManagement/pages/task/services/task-service.js'
];

var dependency = Dependency.getInstance();
dependency.execute("taskProcessTeamController");

let taskProcessTeamModelView = null;
let taskProcessTeamObject = {
    taskCount: 0,
    finishedTaskCount: 0
}

function taskProcessTeamControllerOnload() {

    dependency.initializeSystemObject(['getLanguage', 'encrypt', 'translatePage', 'translateWord', 'showLoader', 'removeNullAndUndefined']);
    dependency.initializeDomObject(['subscribe', 'publish']);

    let language = window.system.getLanguage() /*"ar"*/ ;
    window.system.translatePage(language, "task", "TaskManagement", "mod-team-task");

    taskProcessTeamModelView = new TaskProcessTeamModelView();

    ko.applyBindings(taskProcessTeamModelView, $("#mod-team-task")[0]);

    window.dom.subscribe("task-process-team", function (entityItemId) {
        taskProcessTeamObject.taskCount = 0;
        taskProcessTeamObject.finishedTaskCount = 0;
        taskProcessTeamModelView.entityItemId(entityItemId);
        drawGetCreatedTaskByEntityItemId();
    });
    window.dom.subscribe("task-create-success", function () {
        taskProcessTeamObject.taskCount++;
        window.dom.publish("task-finished", false);
    });
}


function drawGetCreatedTaskByEntityItemId() {
    var pageNumber = 1;
    var pageSize = 10;
    var sortBy = "taskName";
    var sortDir = "sortAsc";
    var userId = window.user.details.UserEntityId
    entityItemId = taskProcessTeamModelView.entityItemId()
    TaskModel().getCreatedTaskByEntityItemId(userId, entityItemId, pageNumber, pageSize, sortBy, sortDir).done(function (response) {

        console.log(response);
        taskProcessTeamObject.taskCount = 0;
        taskProcessTeamObject.finishedTaskCount = 0
        addResponseToModel(response, taskProcessTeamModelView)
    });
}


function addResponseToModel(response, model) {
    // taskStatus
    taskProcessTeamModelView.tasks([]);
    if (!response.tuple[0]) {
        return;
    }
    response = response.tuple[0].old.rowset.row;
    if (response instanceof Array) {
        for (let i = 0; i < response.length; i++) {
            // count number of tasks
            taskProcessTeamObject.taskCount++;
            let taskStatus = (response[i].taskStatus) ? parseInt(response[i].taskStatus) : 0;
            if (taskStatus == 3 || taskStatus == 11 || taskStatus == 0) taskProcessTeamObject.finishedTaskCount++;

            if (!response[i].progress) response[i].progress = 0;
            response[i] = window.system.removeNullAndUndefined(response[i])
            response[i] = TaskService().addAttributesToTaskResponse(response[i])
            response[i] = addActionToResponse(response[i])
            response[i].taskData = ko.observable();
        }
    } else {
        taskProcessTeamObject.taskCount++;
        let taskStatus = (response.taskStatus) ? parseInt(response.taskStatus) : 0;
        if (taskStatus == 3 || taskStatus == 11 || taskStatus == 0) taskProcessTeamObject.finishedTaskCount++;

        if (!response.progress) response.progress = 0;
        response = window.system.removeNullAndUndefined(response)
        response = TaskService().addAttributesToTaskResponse(response)
        response = addActionToResponse(response)
        response.taskData = ko.observable();
    }
    model.tasks(response);

    checkIsFinishedTasks();
}


function addActionToResponse(response) {
    response.archiveTask = function (obj, e) {

    }
    response.displayTask = function (obj, e) {
        TaskService().viewTask(window.system.encrypt(obj.Id))
    }
    response.closeTask = function (obj, e) {
        TaskModel().closeTask(obj.Id).done(function () {
            taskProcessTeamObject.finishedTaskCount++;

            checkIsFinishedTasks();
            window.system.showMessage("success", window.system.translateWord("close-task-success"));
        }).fail(function (error) {
            window.system.handleError(error)
        })
    }
    response.editTask = function (obj) {
        TaskService().showEdit(obj.Id, function (data) {
            window.dom.publish("edit-task", data);
        })

    }
    response.deleteTask = function (obj) {
        TaskModel().deleteTask(obj.Id).done(function (response) {
            taskProcessTeamObject.taskCount--;
            checkIsFinishedTasks();
            drawGetCreatedTaskByEntityItemId();
        }).fail(function (error) {
            window.system.handleError(error);
        });

    }
    response.openTaskCollapse = function (obj, e) {
        TaskService().openTaskCollapse(obj.Id, e.target, function (users) {
            obj.taskPerformers(users);
        })
    }

    response.forceCloseTask = function (obj, e) {
        TaskModel().forceCloseTask(obj.Id).done(function () {
            window.system.showMessage("success", window.system.translateWord("close-task-success"));
            drawGetCreatedTaskByEntityItemId();

        }).fail(function (error) {
            window.system.handleError(error)
        })
    }
    response.startTask = function (obj, e) {}
    response.endTask = function (obj, e) {} // performer end task
    response.restartTask = function (obj, e) {}
    return response;
}


function checkIsFinishedTasks() {
    if (taskProcessTeamObject.taskCount == taskProcessTeamObject.finishedTaskCount &&
        taskProcessTeamObject.taskCount != 0) window.dom.publish("task-finished", true);

}

function TaskProcessTeamModelView() {
    this.tasks = ko.observableArray([]);
    this.entityItemId = ko.observable();
}