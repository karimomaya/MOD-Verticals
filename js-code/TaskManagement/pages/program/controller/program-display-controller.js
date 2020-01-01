const programDisplayControllerViews = {}; 
const programDisplayControllerStyles = []; 
const programDisplayControllerScripts = [
    "/cordys/html5/demo/TaskManagement/pages/shared/controller/dynamic-title-bar-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/program/controller/program-data-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/program/controller/program-edit-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/shared/controller/dynamic-progress-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/program/model/program-model.js",
    "/cordys/html5/demo/TaskManagement/pages/project/controller/project-related-program-controller.js",
    "/cordys/html5/demo/TaskManagement/pages/shared/controller/program-confirmation-controller.js"
]; 

var dependency = Dependency.getInstance();
// dependency.addToTrigger("programDisplayControllerOnReady");
dependency.execute("programDisplayController");
let programModel = null;
function programDisplayControllerOnload(){

    dependency.initializeSystemObject(['getLanguage', 'handleUnAuthorizedEntry', 'decrypt',
    'removeNullAndUndefined', 'translatePage', 'getUrlVariable', 'showLoader', 'translateWord',
    'deleteMethods', 'handleError']);
    dependency.initializeDomObject(['subscribe', 'publish']);
    programModel = ProgramModel(['getProgramByIdAndOwner', 'changeProgramStatus', 'deleteProgram']);

    let language = window.system.getLanguage()  /*"ar"*/;
    window.system.translatePage(language, "program", "TaskManagement", "program-display")
    
    let programId = window.system.decrypt(window.system.getUrlVariable("programId"));

    window.dom.publish("program-related-controller-programId", programId)

    getProgramById(programId);
    //getProgramProgress(programId)
    
    window.dom.subscribe("edit", function(){
        $('#program-edit').modal();
    })
    window.dom.subscribe("stop", function(entityId){
        let o = {
            callBack : confirmActionProgram,
            id: entityId,
            status: 3
        }
        window.dom.publish("stop-program", o)
        
    })
    window.dom.subscribe("complete", function(entityId){
        
        changeProgramStatus(entityId, 1)
    })

    window.dom.subscribe("delete", function(entityId){
        window.system.showLoader(true);
        programModel.deleteProgram(entityId).done(function(){
            
            window.system.showMessage("success", window.system.translateWord("deleteSuccess"));
            window.dom.publish("go-back-dynamic-title", true)
        }).fail(function(error){
            window.system.showLoader(false);
            window.system.handleError(error);
        })
    })

    window.dom.subscribe("end", function(entityId){
        let o = {
            callBack : confirmActionProgram,
            id: entityId, 
            status: 2
        }
        window.dom.publish("end-program", o)
        
        // changeProgramStatus(entityId, 2)
    })

    window.system.showLoader(false);
}

function confirmActionProgram(entityId, status){
    changeProgramStatus(entityId, status)
}

function setProgramProgress(progress){
    let obj = {
        progress : progress
    }
    window.dom.publish("dynamic-progress", obj)
    /*programModel.getProgramProgress(programId).done(function(response) {
        let progress = calculateProgress(response);

        let obj = {
            progress : progress
        }
        window.dom.publish("dynamic-progress", obj)

    }).fail(function(error){
        window.system.handleError(error);
    });*/
}


/*function calculateProgress(response){
    let progress = 0;
    if(!response.tuple[0]) return progress

    response = response.tuple[0].old.rowset.row;
    let count = 0;
    
    if (response instanceof Array) {
        
        for(var i=0; i< response.length; i++){
            progress += response[i].alias | 0;
            count++;
        }
    }
    else {
        progress += response.alias | 0;
        count++;
    }

    return Math.round((progress*100) / (count * 100));
}*/

function getProgramById(programId){
    programModel.getProgramByIdAndOwner(programId).done(function(response) {
        
        if(!response.tuple[0]) {
            window.system.handleUnAuthorizedEntry()
            return;
        }
        console.log("getprogrammbyID");
        console.log(response);
        response = window.system.removeNullAndUndefined(response.tuple[0].old.rowset.row);
        
        response.startDate = response.startDate.split("T")[0];
        response.endDate = response.endDate.split("T")[0];

        // $("#title-bar").attr("title", response.name)

        response.isOwner = false;
        let userId = window.user.details.UserEntityId;
        if (userId == response.createdBy || userId == owner)  response.isOwner = true;
        response.progress = response.progress || 0;
        setProgramProgress(parseInt(response.progress));
        updateDynamicTitle(response);

        window.dom.publish("edit-program", response)

        let fillProgramDatafunc = $('#program-data').attr("fill-program-data");
        window[fillProgramDatafunc](response);
        
    }).fail(function(error){
        window.system.handleError(error);
    });
}


function updateDynamicTitle(response){
    let haveAction = false;
    let haveDelete = false;
    let haveComplete = false;
    let haveStop = false;
    let programStatus = parseInt(response.status);
    if (response.isOwner && (programStatus == 1 || programStatus == 3)) { // inprogressProgram or stopped Program
        haveAction = true;
        haveDelete = true;
        if (parseInt(response.projectCount) > 0) {
            haveDelete = false
        }
        if (programStatus == 3) {
            haveComplete = true;
        }
        if (programStatus == 1) {
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
    window.dom.publish("dynamic-title", obj)
}

function changeProgramStatus(entityId, status){
    
    programModel.changeProgramStatus(entityId, status).done(function(response){
        
        if(status == 1){
            window.dom.publish("complete-success", true)
        }
        else if(status == 2){
            window.dom.publish("end-success", true)
        }
        else if(status == 3){
            window.dom.publish("stop-success", true)
        }
        
        window.system.showMessage("success", window.system.translateWord('updateProgramStatusSuccess'))
        window.system.showLoader(false);

        var url = assetConfig.processExprience + "/" + _ConfigURL("taskIndex");
        window.top.location = url;
        // window.history.back();
    }).fail(function(error){
        window.system.handleError(error);
        window.system.showLoader(false);
    });
}