const programControllerViews = {
    "inprogress-program" : "/cordys/html5/demo/TaskManagement/pages/program/view/program-table.html",
    "stopped-program" : "/cordys/html5/demo/TaskManagement/pages/program/view/program-table.html",
    "ended-program" : "/cordys/html5/demo/TaskManagement/pages/program/view/program-table.html"
    
}; 
const programControllerStyles = [
]; 
const programControllerScripts = [
    "/cordys/html5/demo/TaskManagement/pages/program/service/program-service.js",
    "/cordys/html5/demo/TaskManagement/pages/program/model/program-model.js",
    "/cordys/html5/demo/TaskManagement/pages/shared/controller/program-confirmation-controller.js"
]; 

var dependency = Dependency.getInstance();
dependency.execute("programController");
let programObjects = {
    programInProgressViewModel: null,
    programStopViewModel: null,
    programEndViewModel: null, 
    model : null,
    currentTab: 1
}

let programFilterObject = {
    sortBy: "endDate",
    sortDir: "sortAsc",
    freeText: "",
    progress: -1,
    startDate: null,
    endDate: null,
    filterOwner: ""
}

function programControllerOnload() {
    dependency.initializeSystemObject(['getLanguage', 'translatePage', 'showLoader','translateWord', 
        'getPageCount', 'showMessage', 'encrypt', 'removeNullAndUndefined']);
    dependency.initializeDomObject(['sortable', 'subscribe', 'publish', 'drawPagination']);

    let language = window.system.getLanguage()  /*"ar"*/;
    window.system.translatePage(language, "program", "TaskManagement", "program-container");

    var inprogressTable = document.getElementById("inprogress-program").getElementsByClassName("main-table")[0];
    window.dom.sortable(inprogressTable, "fillInprogressProgram")
    var endedTable = document.getElementById("ended-program").getElementsByClassName("main-table")[0];
    window.dom.sortable(endedTable, "fillEndedProgram")
    var stoppedTable = document.getElementById("stopped-program").getElementsByClassName("main-table")[0];
    window.dom.sortable(stoppedTable, "fillStoppedProgram");

    programObjects.programInProgressViewModel = new ProgramViewModel();
    ko.applyBindings(programObjects.programInProgressViewModel,inprogressTable);
    programObjects.programEndViewModel = new ProgramViewModel();
    ko.applyBindings(programObjects.programEndViewModel,endedTable);
    programObjects.programStopViewModel = new ProgramViewModel();
    ko.applyBindings(programObjects.programStopViewModel,stoppedTable);

    programObjects.model = ProgramModel(['changeProgramStatus', 'readProgram', 'countProgram', 'deleteProgram']);

    window.dom.subscribe("inprogress-program", function (obj) {
        drawInProgressProgram(obj.taskName(), obj.progress(), obj.startDate(), obj.endDate(), obj.owner());
    });

    window.dom.subscribe("ended-program", function (obj) {
        drawEndedProgram(obj.taskName(), obj.progress(), obj.startDate(), obj.endDate(), obj.owner());
    });

    window.dom.subscribe("stopped-program", function (obj) {
        drawStoppedProgram(obj.taskName(), obj.progress(), obj.startDate(), obj.endDate(), obj.owner());
    });
}


function setProgramFilterObject(programName, progress, startDate, endDate, filterOwner) {
    programFilterObject.sortBy = "programName";
    programFilterObject.sortDir = "sortAsc";
    programFilterObject.progress = progress || -1;
    
    programFilterObject.filterOwner = filterOwner || -1;
    if (programName) {
        programFilterObject.freeText = programName;
    } else {
        programFilterObject.freeText = $("#search-input").val() || "";
    }

    programFilterObject.startDate = startDate || new Date('October 14, 1920 05:35:32').toISOString().split("T")[0];
    programFilterObject.endDate = endDate || new Date('October 14, 2120 05:35:32').toISOString().split("T")[0];
}

function drawInProgressProgram(programName, progress, startDate, endDate, owner){
    setProgramFilterObject(programName, progress, startDate, endDate, owner)
    $("#program-create").attr("program-success", "drawInProgressProgram");
    $("#program-edit").attr("program-success", "drawInProgressProgram");
    var element = document.getElementById("inprogress-program");
    countProgram("countProgram", 1, element, fillInprogressProgram, programFilterObject);
    fillInprogressProgram();

}

function fillInprogressProgram(pageNumber, sortBy, sortDir) {
    readProgram(1, pageNumber, sortBy, sortDir, "inprogress-program");
}

function drawEndedProgram(programName, progress, startDate, endDate, owner) {
    setProgramFilterObject(programName, progress, startDate, endDate, owner)
    $("#program-create").attr("program-success", "drawEndedProgram");
    $("#program-edit").attr("program-success", "drawEndedProgram");
    var element = document.getElementById("ended-program");
    countProgram("countProgram", 2, element, fillEndedProgram, programFilterObject);
    fillEndedProgram()
}

function fillEndedProgram(pageNumber,  sortBy, sortDir){
    readProgram(2, pageNumber,  sortBy, sortDir, "ended-program");
}

function drawStoppedProgram(programName, progress, startDate, endDate, owner){
    setProgramFilterObject(programName, progress, startDate, endDate, owner)
    $("#program-create").attr("program-success", "drawStoppedProgram");
    $("#program-edit").attr("program-success", "drawStoppedProgram");
    var element = document.getElementById("stopped-program");
    countProgram("countProgram", 3, element, fillStoppedProgram, programFilterObject);
    fillStoppedProgram();
}

function fillStoppedProgram(pageNumber,  sortBy, sortDir){
    readProgram(3, pageNumber,  sortBy, sortDir, "stopped-program");
}


function showLoaderBasedOnStatus(status){
    let id = "inprogress-program";
    if (status == 1) {
        id = "inprogress-program";
    }
    else if (status == 2) {
        id = "ended-program";
    }
    else if (status == 3) {
        id = "stopped-program";
    }
    window.system.showLoader(true, document.getElementById(id))
}

function changeProgramStatus(progamId, status,  element){
    showLoaderBasedOnStatus(programObjects.currentTab);
    programObjects.model.changeProgramStatus(progamId, status).done(function(response){
        let tr = $(element).closest('tr')
        let fold = $(tr).next('tr');
        $(tr).remove();
        $(fold).remove();
        window.system.showMessage("success", window.system.translateWord('updateProgramStatusSuccess'))
        window.system.showLoader(false);
        window.dom.publish("reload-report", true);
        
    }).fail(function(error){
        window.system.handleError(error);
        window.system.showLoader(false);
    });
}

function addAdditionalAttributesToProgramResponse(response){
    /* Splite start and end Date to only show date  */
    response.endDate = response.endDate.split("T")[0];
    response.startDate = response.startDate.split("T")[0];
    /* Update Progress to contain percentage sign  */
    let progress = response.programProgress;
    if (isNaN(progress)) progress = 0;
    progress = progress | 0;
    response.programProgress = progress+"%";
    /* Declare helpful variable to update menu  */
    response.isOwner = false;
    response.isEnded = false;
    response.isStopped = false;
    response.isInprogresss= false;
    let status = parseInt(response.status);
    if(status == 1){ // program in-progress
        response.isInprogresss= true;
    }else if(status == 2){ // program end
        response.isEnded= true;
    }else if(status == 3){ // program stopped
        response.isStopped= true;
    }
    /* if there's related project to this program it cannot be deleted  */
    response.haveDelete = true;
    if(parseInt(response.projectCount) > 0) response.haveDelete = false;
    /* Check if the current user is the owner  */
    let userId = window.user.details.UserEntityId;
    if (userId == response.createdBy || userId == response.owner)  response.isOwner = true;
    /* open collapse  */
    response.openProgramCollapse = function(obj, e ){
        $(e.target).parent().toggleClass("open").next(".fold").toggleClass("open");
    }
    response.displayProgram = function(obj, e){
        let id = window.system.encrypt(obj.Id)
        var url = 'http://appworks-dev:81/home/mod/html5/demo/TaskManagement/pages/program/program-display.html?programId='+id
		window.top.location =  url
    }
    response.editProgram = function(obj){
        window.dom.publish('edit-program-by-id', parseInt(obj.Id));
    }
    response.stopProgram = function(obj, e){
        let o = {
            callBack : confirmActionProgram,
            id: obj.Id,
            status: 3,
            element: e.target
        }
        window.dom.publish("stop-program", o)
        // changeProgramStatus(obj.Id, 3, e.target)
    }
    response.endProgram = function(obj, e){
        let o = {
            callBack : confirmActionProgram,
            id: obj.Id,
            status: 2,
            element: e.target
        }
        window.dom.publish("end-program", o)
        // changeProgramStatus(obj.Id, 2, e.target)
    }
    response.completeProgram = function(obj, e){
        let o = {
            callBack : confirmActionProgram,
            id: obj.Id,
            status: 1,
            element: e.target
        }
        window.dom.publish("complete-program", o)
        // changeProgramStatus(obj.Id, 1, e.target)
    }
    response.deleteProgram = function(obj, e){
        showLoaderBasedOnStatus(programObjects.currentTab);
        
        programObjects.model.deleteProgram(obj.Id).done(function(){
            let tr = $(e.target).closest('tr')
            let fold = $(tr).next('tr');
            $(tr).remove();
            $(fold).remove();
            window.system.showLoader(false);
            window.system.showMessage("success", window.system.translateWord("deleteSuccess"));
        }).fail(function(error){
            window.system.showLoader(false);
            window.system.handleError(error);
        })

    }
    return response;
}

function confirmActionProgram(entityId, status, element){
    changeProgramStatus(entityId, status, element)
}

function readProgram(status, pageNumber, sortBy, sortDir, id) {
    

    pageNumber = pageNumber || 1;
    pageSize = 10;
    sortBy = sortBy || "endDate";
    sortDir = sortDir || "sortAsc";
    
    window.system.showLoader(true, document.getElementById(id));
    programObjects.model.readProgram(status, pageNumber, pageSize, sortBy, sortDir, programFilterObject).done(function(response){
        /*ProgramService().drawProgramTable(id, response);*/
        
        if(!response.tuple[0]) {
            window.system.showLoader(false);
            return;
            
        }
        response = response.tuple[0].old.rowset.row;
        if (response instanceof Array) {
            for (let i=0; i<response.length; i++){
                response[i] = window.system.removeNullAndUndefined(response[i])
                response[i] = addAdditionalAttributesToProgramResponse(response[i])
            }
        }
        else {
            response = window.system.removeNullAndUndefined(response)
            response = addAdditionalAttributesToProgramResponse(response)
        }

        if(status == 1){
            programObjects.programInProgressViewModel.programs(response);
        } else if(status == 2){
            programObjects.programEndViewModel.programs(response);
        }else if(status == 3){
            programObjects.programStopViewModel.programs(response)
        }
        window.system.showLoader(false);
    }).fail(function(error){
        window.system.handleError(error);
        window.system.showLoader(false);
    });
}


function countProgram(type, status, element, func, programFilterObject){
    programObjects.programInProgressViewModel.programs([])
    programObjects.programStopViewModel.programs([])
    programObjects.programEndViewModel.programs([])
    programObjects.currentTab = status;
    window.system.showLoader(true, element);
    programObjects.model.countProgram(type, status, programFilterObject).done(function(response){
        window.system.showLoader(false);
        var totalPages = Math.ceil(response.count.text / window.system.getPageCount());
        if(totalPages == 0) return;
        window.dom.drawPagination(element.getElementsByClassName('pagination')[0], totalPages, func)
    }).fail(function(error){
        window.system.handleError(error);
        window.system.showLoader(false);
    });
}

function ProgramViewModel(){
    this.programs = ko.observableArray([]);
    this.tab = ko.observable();
}