
const projectDataControllerViews = {
    "project-data" : "/cordys/html5/demo/TaskManagement/pages/project/view/project-data.html"
}; 
const projectDataControllerStyles = []; 
const projectDataControllerScripts = []; 

var dependency = Dependency.getInstance();
dependency.execute("projectDataController");

let projectDataViewModel = null;


function projectDataControllerOnload(){
    let system = System();
    let language = system.getLanguage()  /*"ar"*/;
    system.translatePage(language, "project", "TaskManagement", "project-data");
    projectDataViewModel = new ProjectDataViewModel();
    ko.applyBindings(projectDataViewModel, document.getElementById("project-data")); 

    Dom().subscribe("project-data", function(response){
        fillProjectData(response)
    })
}

function fillProjectData(data) {
    projectDataViewModel.description(data.description);
    projectDataViewModel.notes(data.notes);
    projectDataViewModel.owner(data.projectOwner);
    projectDataViewModel.name(data.name);
    projectDataViewModel.endDate(data.endDate.split("T")[0].replace("Z", "")); 
    projectDataViewModel.startDate(data.startDate.split("T")[0].replace("Z", "")); 
    projectDataViewModel.notes(data.notes); 
    projectDataViewModel.status(handleprojectStatus(data.status)); 
    projectDataViewModel.program(data.programName); 
    projectDataViewModel.projectCreator(data.projectCreator);
}

function ProjectDataViewModel(){
    this.name =  ko.observable();
    this.description =  ko.observable();
    this.endDate =  ko.observable(); 
    this.startDate =  ko.observable(); 
    this.notes =  ko.observable(); 
    this.status =  ko.observable(); 
    this.owner =  ko.observable(); 
    this.program =  ko.observable(); 
    this.projectCreator = ko.observable();
}

function handleprojectStatus(status) {
    let system = System();
    switch (parseInt(status)) {
        case 1: 
            return system.translateWord("inprogress");
        case 2:
            return system.translateWord("done");
        case 3:
            return system.translateWord("stopped");
        default: 
            return status;
    }
}

function goBack(){
    window.history.back();
}
