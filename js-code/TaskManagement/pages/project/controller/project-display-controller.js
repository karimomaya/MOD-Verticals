const projectDisplayControllerViews = {}; 
const projectDisplayControllerStyles = []; 
const projectDisplayControllerScripts = [
    "/cordys/html5/demo/TaskManagement/pages/shared/controller/dynamic-title-bar-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/shared/controller/project-confirmation-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/project/controller/project-data-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/shared/controller/dynamic-progress-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/project/model/project-model.js",
    "/cordys/html5/demo/TaskManagement/pages/project/controller/task-related-project-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/project/controller/project-edit-controller.js"
]; 

var dependency = Dependency.getInstance();
dependency.execute("projectDisplayController");

let projectModel = null;
function projectDisplayControllerOnload(){

    dependency.initializeSystemObject(['getLanguage', 'handleUnAuthorizedEntry', 'decrypt',
    'removeNullAndUndefined', 'showMessage','translatePage', 'getUrlVariable', 'showLoader', 
    'deleteMethods', 'handleError']);

    dependency.initializeDomObject(['subscribe', 'publish']);
    projectModel = ProjectModel(['getProjectById', 'changeProjectStatus', 'deleteProgram']);
    
    let language = window.system.getLanguage()  /*"ar"*/;
    window.system.translatePage(language, "project", "TaskManagement", "project-display")
    
    let projectEntityId = window.system.decrypt(window.system.getUrlVariable("projectId"));

    window.dom.publish("task-related-project", projectEntityId)
    getProjectById(projectEntityId);
    // getProjectProgress(projectEntityId)
    
    window.system.showLoader(false);

    window.dom.subscribe("edit", function(entityId){
        window.dom.publish("edit-project-by-id", entityId);
    })
    window.dom.subscribe("stop", function(entityId){
        let o = {
            callBack : confirmStopProject,
            id: entityId,
            status: 3
        }
        window.dom.publish("stop-project", o)
        // changeProjectStatus(entityId, 3)
    })
    window.dom.subscribe("complete", function(entityId){
        changeProjectStatus(entityId, 1)
    })

    window.dom.subscribe("delete", function(entityId){
        window.system.showLoader(true);
        programModel.deleteProgram(entityId).done(function(){
            
            window.system.showMessage("success", window.system.translateWord("deleteSuccess"));
            window.dom.publish("go-back-dynamic-title", true)
        }).fail(function(error){
            window.system.handleError(error);
        })
    })

    window.dom.subscribe("end", function(entityId){
        let o = {
            callBack : confirmStopProject,
            id: entityId,
            status: 2
        }
        window.dom.publish("end-project", o)
        // changeProjectStatus(entityId, 2)
    })
}


function confirmStopProject(entityId, status) {
    changeProjectStatus(entityId, status)
}

function changeProjectStatus(entityId, status){
    ProjectModel().changeProjectStatus(entityId, status).done(function(){
        
        window.system.showLoader(false);
        window.system.showMessage("success", window.system.translateWord("updateProjectStatusSuccess"));
        if (status == 2) { // if project end go back
            // window.dom.publish("go-back-dynamic-title", true) 
            window.dom.publish("end-success", true);
        } else if(status == 1){
            window.dom.publish("complete-success", true);
        } else if(status == 3){
            window.dom.publish("stop-success", true);
        }
        var url = assetConfig.processExprience + "/" + _ConfigURL("taskIndex");
        window.top.location = url;
        // window.history.back();
    }).fail(function(error){
        window.system.handleError(error);
    })
}

function setProjectProgress(progress){
    let obj = {
        progress : progress
    }
    window.dom.publish("dynamic-progress", obj)
}


function getProjectById(projectEntityId){
    projectModel.getProjectById(projectEntityId).done(function(response) {
        if(!response.tuple[0]) {
            window.system.handleUnAuthorizedEntry()
            return;
        }
        response = window.system.removeNullAndUndefined(response.tuple[0].old.rowset.row);
        
        // Pass value to project-data-controller
        window.dom.publish("project-data", response)

        response.isOwner = false;

        let userId = window.user.details.UserEntityId;
        if (userId == response.createdBy || userId == response.owner ) response.isOwner = true;
        response.progress = response.progress || 0;
        setProjectProgress(parseInt(response.progress)); 
        updateDynamicTitle(response);
        
        
    }).fail(function(error){
        window.system.handleError(error);
    });
}

function updateDynamicTitle(response){
    let haveAction = false;
    let haveDelete = false;
    let haveComplete = false;
    let haveStop = false;
    let projectStatus = parseInt(response.status);
    //1: inprogress, 2: done, 3: stopped
    if (response.isOwner && (projectStatus == 1 || projectStatus == 3)) { // inprogressProgram or stopped Program
        haveAction = true;
        haveDelete = true;
        if (parseInt(response.taskCount) > 0) {
            haveDelete = false
        }
        if (projectStatus == 3) {
            haveComplete = true;
        }
        if (projectStatus == 1) {
            haveStop = true;
        }
    }
    
    obj = {
        title : response.name,
        haveAction : haveAction, 
        haveEdit : haveAction, 
        haveDelete : haveDelete,
        haveEnd : haveAction,
        haveComplete: haveComplete,
        haveStop: haveStop,
        entityId : response.Id
    }

    // Pass value to dynamic-title-controller
    window.dom.publish("dynamic-title", obj);
}
