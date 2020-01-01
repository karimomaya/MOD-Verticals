const programEditControllerViews = {
    "program-edit" : "/cordys/html5/demo/TaskManagement/pages/program/view/program-create.html"
}; 
const programEditControllerStyles = [
    '/cordys/html5/demo/commons/styles/selectize.css'
]; 
const programEditControllerScripts = [
    "/cordys/html5/demo/TaskManagement/pages/program/service/program-service.js",
    "/cordys/html5/demo/TaskManagement/pages/program/model/program-model.js",
    '/cordys/html5/demo/commons/javascripts/selectize-index.js',
    '/cordys/html5/demo/commons/javascripts/selectize.min.js'
]; 

window.selectedOption = [];

var dependency = Dependency.getInstance();
dependency.addToTrigger("programEditControllerOnReady");
dependency.execute("programEditController");

let programEditViewModel = null;
function programEditControllerOnload() {

    programEditViewModel = new ProgramEditViewModel();

    var system = System(['getLanguage', 'handleUnAuthorizedEntry', 'removeNullAndUndefined', 
    'translatePage',  'deleteMethods', 'extendSelectize']);

    let language = system.getLanguage()  /*"ar"*/;
    system.translatePage(language, "program", "TaskManagement", "program-edit");
    
    dependency.initializeValidation();
    system.extendSelectize();
    
    window.kovalidation.initializeValidation(programEditViewModel);
    ko.applyBindings(programEditViewModel, document.getElementById("program-edit"));

    Dom().subscribe("edit-program-by-id", function(programEntityId){
        restEditProgram(programEditViewModel);
        ProgramModel().getProgramByIdAndOwner(programEntityId).done(function(response) {
            if(!response.tuple[0]) {
                system.handleUnAuthorizedEntry()
                return;
            }
            response = system.removeNullAndUndefined(response.tuple[0].old.rowset.row);
            response.startDate = response.startDate.split("T")[0];
            response.endDate = response.endDate.split("T")[0];
            $('#program-edit').modal();
            bindEditProgram(response);
        }).fail(function(error){
            system.handleError(error);
        });
    });

    Dom().subscribe("edit-program", function(programData){
        console.log("edit-program")
        console.log(programData)
        bindEditProgram(programData)
    })
}
function bindEditProgram(programData){
    programEditViewModel.programName(programData.name);
    programEditViewModel.programEndDate(programData.endDate);
    programEditViewModel.programStartDate(programData.startDate);
    programEditViewModel.programOwnerOptions([{id: programData.owner+":"+programData.DisplayName, name : programData.DisplayName}]);
    programEditViewModel.programOwner([programData.owner+":"+programData.DisplayName]);
    programEditViewModel.programCreatedBy(programData.createdBy);
    programEditViewModel.status(programData.status);
    programEditViewModel.programNotes(programData.notes);
    programEditViewModel.programDescription(programData.description);
    programEditViewModel.Id(programData.Id);
}

function programEditControllerOnReady(){
    $('#program-edit #program-owner input').on('input', function() {
        drawEditProgramOwnerOptions( this.value );
    });
}

function drawEditProgramOwnerOptions(input) {
    TaskModel().getOwners(input, true).done(function(response){
        if(!response.tuple[0])  return;

        var results = response.tuple[0].old.rowset.row;
        let ownerObject = [];
        if (results instanceof Array) {
            for (let i=0; i<results.length; i++){
                console.log(results[i])
                ownerObject.push({id: results[i].UserEntityId+":"+ results[i].DisplayName, name : results[i].DisplayName})
            }
        }
        else {
            ownerObject.push({id: results.UserEntityId+":"+ results[i].DisplayName, name : results.DisplayName})
        }
        programEditViewModel.programOwnerOptions(ownerObject);
    });
}

function ProgramEditViewModel(){
    var self = this;
    self.ownerDimmed= ko.observable("dimmed")
    self.isEdit = ko.observable("dimmed")
    let system = System();
    system.deleteMethods(['translateWord', 'handleError', 'showMessage','showLoader', 'getMinDate'], system);

    self.minDate = ko.observable(system.getMinDate());

    self.hasError =  ko.observable(true);
    self.Id = ko.observable().extend({required: true})
	self.programName = ko.observable().extend({ 
        required: {params: true, message: system.translateWord("requiredField")},
        maxLength: {params: 200, message: system.translateWord("maxLength200")}
    });
    self.programEndDate = ko.observable().extend({
        required: {params: true, message:system.translateWord("requiredField")},
        date: {params: true, message:system.translateWord("requiredDate")},
        biggerThanDate:  {params: self.programStartDate, message: system.translateWord("biggerThanDate")}
    });
    self.programStartDate = ko.observable().extend({ 
        required: {params: true, message:system.translateWord("requiredField")},
        date: {params: true, message:system.translateWord("requiredDate")},
        smallerThanDate: {params: self.programEndDate, message: system.translateWord("smallerThanDate")} 
    });
    self.programStartDate.subscribe(function(newValue){
        if(newValue.length < 2) return;
        self.minDate(newValue);
    });
    self.programOwnerOptions = ko.observableArray();
    self.programOwner = ko.observable().extend({
        required: {params: true, message:system.translateWord("requiredField")}
    });
    self.programOwner.subscribe(function(newValue){
        if(newValue.length < 1 || newValue[0] == "") return;
        self.ownerId(newValue[0].split(":")[0]);
    });
    self.ownerId = ko.observable();
    self.programCreatedBy = ko.observable();
	self.status = ko.observable();
    self.programNotes = ko.observable().extend({
        maxLength: {params: 4000, message: system.translateWord("maxLength4000")}
    });
    self.programDescription = ko.observable().extend({
        maxLength: {params: 4000, message: system.translateWord("maxLength4000")}
    });
    self.programStatus = ko.observable();
    self.programProgress = ko.observable();
    self.buttonText = ko.observable(system.translateWord("edit"));
    
    self.programSubmit = function() {
        
        system.showLoader(true);
        if(!self.programOwner() || 0 === self.programOwner().length)  self.programOwner(window.user.details.UserEntityId);
        programEditViewModel.hasError(true);
        ProgramModel().updateProgram(programEditViewModel).done(function(){

            system.showLoader(false);
            $("#program-edit #mod-btn-program-close").click();

            system.showMessage("success", system.translateWord("updateSuccess"));
            let successFunction = $("#program-edit").attr("program-success");
            if (successFunction) {
                window[successFunction]();
            }
            else {
                /*let dom = Dom(['publish'])
                let obj = {
                    description: programEditViewModel.programDescription(),
                    notes: programEditViewModel.programNotes(),
                    owner: programEditViewModel.programOwner()[0].split(":")[1],
                    name: programEditViewModel.programName(),
                    endDate: programEditViewModel.programEndDate(),
                    startDate: programEditViewModel.programStartDate()
                }
    
                dom.publish("program-data", obj);
                dom.publish("dynamic-title", programEditViewModel.programName())*/

                location.reload(true);
            }
            
            
            programEditViewModel.hasError(false);

        }).fail(function(error){
            system.handleError(error);
            programEditViewModel.hasError(false);
        })

    }
    
    
}

function restEditProgram(model){
    // model.hasError =  ko.observable(true);
    model.programOwner("")
    model.programEndDate("")
    model.programStartDate("")
    model.programName("")
    model.programDescription("");
    model.programNotes("");
}