
const taskDataViewControllerViews = {
    "task-data" : "/cordys/html5/demo/TaskManagement/pages/task/views/task-data.html"
}; 
const taskDataViewControllerStyles = []; 
const taskDataViewControllerScripts = []; 
var system = System();

var dependency = Dependency.getInstance();
dependency.execute("taskDataViewController");

console.log("taskDataViewController");

let taskDataViewModel = null;

function taskDataViewControllerOnload(){
    let system = System();
    let dom = Dom();

    let language = system.getLanguage()  /*"ar"*/;
    system.translatePage(language, "task", "TaskManagement", "task-data");

    dom.subscribe("task-data", function(taskObject){
        taskDataViewModel.taskObject(taskObject);
    })
    
    dom.subscribe('set-project-name-to-task-data', function(projectName){
        taskDataViewModel.taskObject().taskProjectName(projectName);
    });
    
    taskDataViewModel = new TaskDataViewModel();
    ko.applyBindings(taskDataViewModel, document.getElementById("task-data")); 
}

function TaskDataViewModel(){
    this.taskObject = ko.observable();
}

function updateProcessDetails(controllerPath){
    if(controllerPath) dependency.addScriptToHeader(controllerPath);
}
