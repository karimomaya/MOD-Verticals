let taskMeetingTeamControllerViews = {
    "mod-post-task" : "/cordys/html5/demo/TaskManagement/pages/task/views/task-table.html",
    "mod-pre-task" : "/cordys/html5/demo/TaskManagement/pages/task/views/task-table.html"
}; 
let taskMeetingTeamControllerStyles = [
]; 
let taskMeetingTeamControllerScripts = [
    '/cordys/html5/demo/TaskManagement/pages/task/model/task-model.js',
    '/cordys/html5/demo/TaskManagement/pages/task/services/task-service.js'
]; 
let taskMeetingTeamControllerLang = [
    '/cordys/html5/demo/TaskManagement/pages/task/resources/language-pack/task-'+window.lang+'.json'
]; 

var dependency = Dependency.getInstance();
dependency.execute("taskMeetingTeamController");

let taskMeetingPostViewModel = null;
let taskMeetingPreViewModel = null;

function taskMeetingTeamControllerOnload() {

    dependency.initializeSystemObject([ 'encrypt','translatePage', 'translateWord', 'showLoader', 'removeNullAndUndefined']);
    dependency.initializeDomObject(['subscribe', 'publish']);

    window.system.translatePage("mod-post-task", "mod-post-task");
    window.system.translatePage("mod-pre-task", "mod-pre-task");
    
    taskMeetingPostViewModel = new TaskMeetingViewModel();
    taskMeetingPreViewModel = new TaskMeetingViewModel();
        
    ko.applyBindings(taskMeetingPostViewModel,$("#mod-post-task")[0]);
    ko.applyBindings(taskMeetingPreViewModel,$("#mod-pre-task")[0]);

    window.dom.subscribe("mod-meeting-task", function(obj){
        taskMeetingPostViewModel.entityItemId(obj.entityItemId);
        drawGetCreatedTaskByEntityItemId(obj.type);
    });

    window.dom.subscribe("task-create-success", function(){
        window.dom.publish("task-finished", false);
    });
}



function drawGetCreatedTaskByEntityItemId(type) {
    if(!type) {
        let taskData = $("#task-create").attr("task-data").split("-")[0];
        type = taskData;
    }
	var pageNumber =  1;
    var pageSize = 10;
    var sortBy = "taskName";
    var sortDir = "sortAsc";
	var userId = window.user.details.UserEntityId
	entityItemId = taskMeetingPostViewModel.entityItemId()
	TaskModel().getCreatedTaskMeeting(userId, entityItemId ,type, pageNumber, pageSize, sortBy, sortDir).done(function(response){
        let model = taskMeetingPostViewModel;
        if(type == "pre") model = taskMeetingPreViewModel
        addResponseToModel(response, model)
	});
}


function addResponseToModel(response, model){
    // taskStatus
    taskMeetingPostViewModel.tasks([]);
    taskMeetingPreViewModel.tasks([]);
    if(!response.tuple[0]) {
        return;
    }
    response = response.tuple[0].old.rowset.row;
    if (response instanceof Array) {
        for (let i=0; i<response.length; i++){
            // count number of tasks
            if(!response[i].progress) response[i].progress = 0;
            response[i] = window.system.removeNullAndUndefined(response[i])
            response[i] = TaskService().addAttributesToTaskResponse(response[i])
            response[i] = addActionToResponse(response[i])
        }
    }
    else {
        if(!response.progress) response.progress = 0;
        response = window.system.removeNullAndUndefined(response)
        response = TaskService().addAttributesToTaskResponse(response)
        response = addActionToResponse(response)
    }
    
    model.tasks(response);
}


function addActionToResponse(response) {
    response.archiveTask = function(obj, e){
        
    }
    response.displayTask = function(obj, e){
        TaskService().viewTask(window.system.encrypt(obj.Id))
    }
    response.closeTask = function(obj, e){
        TaskModel().closeTask(obj.Id).done(function(){
            window.system.showMessage("success", window.system.translateWord("close-task-success"));
        }).fail(function(error){
            window.system.handleError(error)
        })
    }
    response.editTask = function(obj){
        TaskService().showEdit(obj.Id, function(data){
            window.dom.publish("edit-task", data);
        })
        
    }
    response.deleteTask = function(obj){
        TaskModel().deleteTask(obj.Id).done(function(response){
            drawGetCreatedTaskByEntityItemId();
        }).fail(function(error){
            window.system.handleError(error);
        });
        
    }
    response.openTaskCollapse = function(obj, e){
        TaskService().openTaskCollapse(obj.Id, e.target, function(users){
            obj.taskPerformers(users);
        })
    }

    response.forceCloseTask = function(obj, e){
        TaskModel().forceCloseTask(obj.Id).done(function () {
            window.system.showMessage("success", window.system.translateWord("close-task-success"));
            drawGetCreatedTaskByEntityItemId();
            
        }).fail(function (error) {
            window.system.handleError(error)
        })
    }
    response.startTask = function(obj, e){}
    response.endTask = function(obj, e){}// performer end task
    response.restartTask = function(obj, e){}
    return response;
}

function TaskMeetingViewModel(){
    this.tasks = ko.observableArray([]);
    this.entityItemId = ko.observable();
}

