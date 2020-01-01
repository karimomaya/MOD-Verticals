const taskCreateProgramControllerViews = {
    "program-create" : "/cordys/html5/demo/TaskManagement/pages/program/view/program-create.html"
}; 
const taskCreateProgramControllerStyles = [];

const taskCreateProgramControllerScripts = [
    "/cordys/html5/demo/TaskManagement/pages/program/service/program-service.js",
    "/cordys/html5/demo/TaskManagement/pages/program/model/program-model.js"
]; 
window.selectedOption = [];
// dimmed
var dependency = Dependency.getInstance();
dependency.addToTrigger("taskCreateProgramControllerOnReady");
dependency.execute("taskCreateProgramController");

let programModel = null;
function taskCreateProgramControllerOnload() {

    dependency.initializeSystemObject(['translatePage', 'showMessage',
        'translateWord', 'extendSelectize', 'showLoader', 'getMinDate', 'handleError'
    ]);

    programModel = new programObject();

    window.system.translatePage(window.lang, "program", "TaskManagement", "program-create");

    dependency.initializeValidation();
    window.kovalidation.initializeValidation(programModel);
    ko.applyBindings(programModel, document.getElementById("program-create"));
}

function taskCreateProgramControllerOnReady(){

    $('#program-create').on('shown.bs.modal', function () {
        // window.system.showLoader(true);
        restProgram()
    });

    autocompleteProgramOwnerOptions( "", true );

    $('#program-create #program-owner input').on('input', function() {
        autocompleteProgramOwnerOptions( this.value );
    });

}


function autocompleteProgramOwnerOptions(input, setCurrentUser) {
    
    if(['MSM','USM', 'AAM', 'EXM'].indexOf(window.user.details.UnitPathById) != -1) {
        SharedModel().getUserUnderUnitsByUnitCodes('MSM,USM,AAM,EXM', window.user.details.UnitPathById, input).done(function(response){

            if(!response.tuple[0]) { 
                return;
            }
            programModel.ownerDimmed("")
            var results = response.tuple[0].old.rowset.row;
            let ownerObject = [];
            if (results instanceof Array) {
                for (let i=0; i<results.length; i++){
                    ownerObject.push({id: results[i].UserEntityId, name : results[i].DisplayName})
                }
            }
            else {
                ownerObject.push({id: results.UserEntityId, name : results.DisplayName})
            }
            programModel.programOwnerOptions(ownerObject);
            /*if (setCurrentUser) {
                addProgramCurrentUserToBeOwner(ownerObject)
            }
            else {
                programModel.programOwnerOptions(ownerObject);
            }*/
        });
    }
    else {
        programModel.ownerDimmed("dimmed")
        addProgramCurrentUserToBeOwner([])
    }

    
    /*
    TaskModel().getOwners(input, true).done(function(response){
        if(!response.tuple[0]) { 
            return;
        }
        var results = response.tuple[0].old.rowset.row;
        let ownerObject = [];
        if (results instanceof Array) {
            for (let i=0; i<results.length; i++){
                ownerObject.push({id: results[i].UserEntityId, name : results[i].DisplayName})
            }
        }
        else {
            ownerObject.push({id: results.UserEntityId, name : results.DisplayName})
        }
        if (setCurrentUser) {
            addProgramCurrentUserToBeOwner(ownerObject)
        }
        else {
            programModel.programOwnerOptions(ownerObject);
        }
        
    });
    */
}

function addProgramCurrentUserToBeOwner(ownerObject) {
    ownerObject.push({id: window.user.details.UserEntityId, name : window.user.details.DisplayName})
    programModel.programOwnerOptions(ownerObject);
    programModel.programOwner([window.user.details.UserEntityId])
}

function programObject(){
    var self = this;
    // let system = System();
    // system.deleteMethods(['translateWord', 'showLoader', 'getMinDate', 'handleError'], system)
    self.ownerDimmed = ko.observable("dimmed")
    self.isEdit = ko.observable("");
    self.hasError =  ko.observable(true);
    self.minDate = ko.observable(window.system.getMinDate());
	self.programName = ko.observable().extend({ 
        required: {params: true, message:window.system.translateWord("requiredField")},
        maxLength: {params: 200, message: window.system.translateWord("maxLength200")}
    });
    self.programEndDate = ko.observable().extend({
        required: {params: true, message:window.system.translateWord("requiredField")},
        date: {params: true, message:window.system.translateWord("requiredDate")},
        biggerThanDate:  {params: self.programStartDate, message: window.system.translateWord("biggerThanDate")},
        "min" :self.minDate() 
    });
    self.programStartDate = ko.observable().extend({ 
        required: {params: true, message:window.system.translateWord("requiredField")},
        date: {params: true, message:window.system.translateWord("requiredDate")},
        smallerThanDate: {params: self.programEndDate, message: window.system.translateWord("smallerThanDate")} 
    });
    self.programStartDate.subscribe(function(newValue){
        if(newValue.length < 2) return;
        self.minDate(newValue);
    });
    self.programOwnerOptions = ko.observableArray();
    self.programOwner = ko.observable({
        required: {params: true, message:window.system.translateWord("requiredField")}
    });
    self.programCreatedBy = ko.observable();
	self.status = ko.observable();
    self.programNotes = ko.observable().extend({
        maxLength: {params: 4000, message: window.system.translateWord("maxLength4000")}
    });
    self.programDescription = ko.observable().extend({ 
        required: {
            params: true, 
            message:window.system.translateWord("requiredField")
        },
        maxLength: {params: 4000, message: window.system.translateWord("maxLength4000")}
    });
    self.programStatus = ko.observable();
    self.programProgress = ko.observable();
    self.buttonText = ko.observable(window.system.translateWord("add"));
    
    self.programSubmit = function() {
        
        if(!self.programOwner() || 0 === self.programOwner().length)  self.programOwner(window.user.details.UserEntityId);
        self.programCreatedBy(window.user.details.UserEntityId);
        self.programProgress(0);
        self.programStatus(1);
        ProgramModel().createProgram(self).done(function(){
            $('#mod-btn-program-close').click();
            window.system.showLoader(false);
            var fntocall = $("#program-create").attr("program-success");
            if(fntocall && fntocall.length > 3) window[fntocall]();
        }).fail(function(error){
            window.system.handleError(error);
            window.system.showLoader(false);
        });
    }
}

function restProgram(){
    programModel.hasError(true);
    programModel.programOwner("")
    autocompleteProgramOwnerOptions( "", true );
    programModel.programEndDate("")
    programModel.programStartDate("")
    programModel.programName("")
    programModel.programDescription("");
    programModel.programNotes("");
    window.system.showLoader(false);

    if (programModel && programModel.errors) programModel.errors.showAllMessages(false);
}