const taskRelatedProjectControllerViews = {
    "task-related-project" : "/cordys/html5/demo/TaskManagement/pages/project/view/task-related-project-table.html"
}; 
const taskRelatedProjectControllerStyles = [
]; 
const taskRelatedProjectControllerScripts = [
    "/cordys/html5/demo/TaskManagement/pages/project/model/project-model.js"
]; 


var dependency = Dependency.getInstance();
dependency.execute("taskRelatedProjectController");


let taskRelatedProjectViewModel = null;
let taskRelatedProject = {
    model : null,
    system : null
}
function taskRelatedProjectControllerOnload(){
   
    taskRelatedProject.system = System();

    taskRelatedProject.system.deleteMethods(['getLanguage', 'removeNullAndUndefined', 'translatePage',  
                    'deleteMethods', 'handleError', 'encrypt'], taskRelatedProject.system)

    let language = taskRelatedProject.system.getLanguage()  /*"ar"*/;
    taskRelatedProject.system.translatePage(language, "project", "TaskManagement", "task-related-project");
   
    Dom().subscribe("task-related-project", function(newValue){
        taskRelatedProjectViewModel.projectId(newValue);
        getTaskRelatedToProject( taskRelatedProjectViewModel.projectId())
    });


    taskRelatedProjectViewModel = new TaskRelatedProjectViewModel();
    ko.applyBindings(taskRelatedProjectViewModel, document.getElementById("task-related-project-tbody")); 
}


function getTaskRelatedToProject(projectEntityId) {
    ProjectModel().getTaskRelatedToProject(projectEntityId).done(function(response) {
        if(!response.tuple[0]) return;

        response = response.tuple[0].old.rowset.row;

        response = cleanBeforeBinding(response);
        
        taskRelatedProjectViewModel.tasks(response);
        
        
    }).fail(function(error){
        projectDisplay.system.handleError(error);
    });
}

function cleanBeforeBinding(obj) {
    if (obj instanceof Array) {
        for(var i=0; i< obj.length; i++){
            obj[i] =  cleanObjectBeforeBinding(obj[i]);
            obj[i].displayTask = function(e){
                openTask(e.Id);
            }
        }
    }
    else {
        obj =  cleanObjectBeforeBinding(obj);
        obj.displayTask = function(e){
            openTask(obj.Id);
        }
    }
    return obj;
}

function openTask(taskId){
    var url = 'http://appworks-dev:81/home/mod/html5/demo/TaskManagement/pages/task/task-display.html?taskId='+ taskRelatedProject.system.encrypt(taskId)
    // assetConfig.processExprience+"/"+_ConfigURL("taskDisplay") + "?taskId="+ taskRelatedProject.system.encrypt(taskId);
    window.location.href = url;
}

function cleanObjectBeforeBinding(obj) {
    for(var key in obj) {
        if(key == 'progress') {
            if(!obj[key]) obj[key] = "0" ;
            obj[key] = obj[key].substring(0, 3).replace(".", "");
            obj[key] = obj[key]+"%"
        }

        if(key=="startDate" || key =="dueDate") {
            obj[key] = obj[key].split("T")[0];
        }
    }
    return obj;
}

function TaskRelatedProjectViewModel() {
    this.projectId =  ko.observable();
    this.tasks = ko.observableArray([]);
    
}



