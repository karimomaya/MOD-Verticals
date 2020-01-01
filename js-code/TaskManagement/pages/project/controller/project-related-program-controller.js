const projectRelatedProgramControllerViews = {
    "project-related-program" : "/cordys/html5/demo/TaskManagement/pages/project/view/project-related-program-table.html"
}; 
const projectRelatedProgramControllerStyles = [
]; 
const projectRelatedProgramControllerScripts = [
    "/cordys/html5/demo/TaskManagement/pages/project/model/project-model.js"
]; 


var dependency = Dependency.getInstance();
dependency.execute("projectRelatedProgramController");

let programRelatedProjectViewModel = null;
function projectRelatedProgramControllerOnload(){
   
    var system = System();
    let language = system.getLanguage()  /*"ar"*/;
    system.translatePage(language, "project", "TaskManagement", "project-related-program");
    Dom().subscribe("program-related-controller-programId", function(newValue){
        programRelatedProjectViewModel.programId(newValue);
        
        readProjectsRealtedToProgram("sortAsc", "projectName", 1, 10 );
    });
    programRelatedProjectViewModel = new ProgramRelatedProjectViewModel();
    ko.applyBindings(programRelatedProjectViewModel, document.getElementById("project-related-program-tbody")); 
}

function readProjectsRealtedToProgram(sortDir, sortBy, pageNumber, pageSize) {
    let programId = programRelatedProjectViewModel.programId()
    ProjectModel().readProjectRelatedToProgram(programId, sortDir, sortBy, pageNumber, pageSize)
    .done(function(response){
        programRelatedProjectViewModel.projects([]);
        if(!response.tuple[0]) return;

        let result = response.tuple[0].old.rowset.row;
        result = cleanBeforeBinding(result);

        programRelatedProjectViewModel.projects(result);

    }).fail(function(error){
        System().handleError(error)
    });
}

function cleanBeforeBinding(obj) {
    if (obj instanceof Array) {
        for(var i=0; i< obj.length; i++){
            obj[i] =  cleanObjectBeforeBinding(obj[i])
            obj[i].displayProject = function(e) {
                console.log(e)
                openProject(e.Id);
            }
        }
    }
    else {
        obj =  cleanObjectBeforeBinding(obj)
        obj.displayProject = function(e) {
            console.log(e)
            openProject(e.Id);
        }
    }
    return obj;
}

function openProject(projectId) {
    projectId = System().encrypt(projectId);
    var url = 'http://appworks-dev:81/home/mod/html5/demo/TaskManagement/pages/project/project-display.html?projectId='+projectId
    // assetConfig.processExprience+"/"+_ConfigURL("projectDisplay") + "?projectId="+ projectId;
    window.location.href = url;
}

function cleanObjectBeforeBinding(obj) {

    if(!obj['progress']) obj['progress'] = "0" ;
    obj['progress'] = obj['progress']  | 0;

    

    obj['progress'] = obj['progress']+"%"

    obj["startDate"] = obj["startDate"].split("T")[0];
    obj["endDate"] = obj["endDate"].split("T")[0];
    
    return obj;
}

function ProgramRelatedProjectViewModel() {
    this.programId =  ko.observable();
    this.projects = ko.observableArray([]);
}