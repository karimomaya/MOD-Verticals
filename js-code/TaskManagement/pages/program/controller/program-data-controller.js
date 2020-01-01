const programDataControllerViews = {
    "program-data": "/cordys/html5/demo/TaskManagement/pages/program/view/program-data.html"
};
const programDataControllerStyles = [];
const programDataControllerScripts = [
    "/cordys/html5/demo/TaskManagement/pages/program/model/program-model.js"
];

var dependency = Dependency.getInstance();
dependency.addToTrigger("programDataControllerOnReady");
dependency.execute("programDataController");

let programDataViewModel = null;


function programDataControllerOnload() {
    let system = System();
    let language = system.getLanguage()  /*"ar"*/;
    system.translatePage(language, "program", "TaskManagement", "program-data");
    programDataViewModel = new ProgramDataViewModel();
    ko.applyBindings(programDataViewModel, document.getElementById("program-data"));

    Dom().subscribe("program-data", function (response) {
        fillProgramData(response)
    })
}

function programDataControllerOnReady() {
}

function fillProgramData(data) {
    programDataViewModel.description(data.description);
    programDataViewModel.name(data.name);
    programDataViewModel.endDate(data.endDate.replace("Z", ""));
    programDataViewModel.startDate(data.startDate.replace("Z", ""));
    programDataViewModel.notes(data.notes);
    programDataViewModel.status(handleProgramStatus(data.status));
    programDataViewModel.owner(data.DisplayName);
}

function ProgramDataViewModel() {
    this.name = ko.observable();
    this.description = ko.observable();
    this.endDate = ko.observable();
    this.startDate = ko.observable();
    this.notes = ko.observable();
    this.status = ko.observable();
    this.owner = ko.observable();
}

function handleProgramStatus(status) {
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

function goBack() {
    window.history.back();
}
