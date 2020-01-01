
const titleBarControllerViews = {
    "title-bar" : "/cordys/html5/demo/TaskManagement/pages/shared/views/title-bar.html"
}; 
const titleBarControllerStyles = []; 
const titleBarControllerScripts = []; 

var dependency = Dependency.getInstance();
dependency.addToTrigger("titleBarControllerOnReady");
dependency.execute("titleBarController");

let titleBarModel = null;


function titleBarControllerOnload(){
    var system = System();
    let language = system.getLanguage()  /*"ar"*/;
    system.translatePage(language, "shared", "TaskManagement", "title-bar");

    titleBarModel = new titleBarObject();
    ko.applyBindings(titleBarModel, document.getElementById("title-bar")); 
}

function titleBarControllerOnReady(){
}

function updateTitleBar(taskData){
    titleBarModel.title(taskData.taskName);
    titleBarModel.owner(taskData.isOwner)
    if(taskData.isOwner) getOwnerAction(taskData);
}

function updatePerformersButton(taskData){
    console.log(taskData);
    taskData = taskData.tuple[0];

    if(!taskData) System().handleUnAuthorizedEntry(); 

    taskData = taskData.old.rowset.row;
    
    let performerActionBtn =  getPerformerActionButtons(taskData);
    document.getElementById("task-action-buttons").innerHTML = performerActionBtn;
    if(performerActionBtn.length < 3) document.getElementById("action-title-menu").innerHTML = System().translateWord("ended") ;
}

function titleBarObject(){
    this.title =  ko.observable();
    this.owner =  ko.observable(false);   
}

function goBack(){
    window.history.back();
}

function getOwnerAction(taskData){
    let ownerActionBtn =  getOwnerActionButtons(taskData);
    document.getElementById("task-action-buttons").innerHTML = ownerActionBtn;
    if(ownerActionBtn.length < 3) document.getElementById("action-title-menu").innerHTML = System().translateWord("ended") ;

}


function getOwnerActionButtons(taskData){
    let taskStatus = parseInt(taskData.status);
    let system = System();
    let encryptedTaskId = system.encrypt(taskData.Id);
    let isFinished = parseInt(taskData.finished);
    let actionBtn = '<a class="dropdown-item" onclick="(function(){TaskService().showEdit(\''+encryptedTaskId+'\');})()" href="#"><span class="icon-Edit"></span> <span translate-text="edit">'+system.translateWord('edit')+'</span></a>';
    switch(taskStatus) { // task status
        case 1: // task not start
        case 10: // task draft
            actionBtn += '<a class="dropdown-item" onclick="(function(){TaskService().deleteTask(\''+encryptedTaskId+'\');})()" href="#"><span class="icon-Delete"></span> <span translate-text="delete">'+system.translateWord('delete')+'</span></a>';
            break;
        case 2: // task start
            if(isFinished)  actionBtn = '<a class="dropdown-item" onclick="(function(){TaskService().closeTask(\''+encryptedTaskId+'\');})()" href="#"><span class="icon-OnOff"></span> <span translate-text="task-finish">'+system.translateWord('task-finish')+'</span></a>';
            break;
        default: 
            actionBtn = ""
    }
    return actionBtn;
}

function getPerformerActionButtons(taskData) {
    let targetTaskPerformerStatus = parseInt(taskData.targetTaskPerformerStatus);
    let system = System();
    let taskId = system.encrypt(taskData.taskId);
    let performerId = system.encrypt(taskData.targetTaskPerformerId);
    let o = (taskData.singleOrMultiple == "false") ? {"name":"acquire", "icon": "icon-Approved"} : {"name": "start", "icon" : "icon-Clock"};
    
    switch(targetTaskPerformerStatus){
        case 0: // task not started
            return '<a onclick="(function(){TaskService().startTask( \''+performerId+'\', \'taskDisplay\');})()" class="dropdown-item" href="#"><span class="'+o.icon+'"></span> <span translate-text="'+o.name+'">'+system.translateWord(o.name)+'</span></a>';
        case 1: // task started or aquired
            return '<a class="dropdown-item" onclick="endTask('+taskData.targetTaskPerformerId+')" href="#"><span class="icon-OnOff"></span> <span translate-text="task-end">'+system.translateWord('task-end')+'</span></a>'+
                '<a class="dropdown-item" onclick="(function(){TaskService().restartTask( \''+performerId+'\', \''+taskId+'\', \'taskDisplay\');})()" href="#"><span class="icon-Update"></span> <span translate-text="task-restart">'+system.translateWord('task-restart')+'</span></a>';
        default : // task finished
            return '';

    }
}