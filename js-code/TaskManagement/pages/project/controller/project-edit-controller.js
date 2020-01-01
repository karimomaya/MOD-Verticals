const projectEditControllerViews = {
    "project-edit" : "/cordys/html5/demo/TaskManagement/pages/project/view/project-create.html"
}; 
const projectEditControllerStyles = [
    '/cordys/html5/demo/commons/styles/selectize.css'
]; 
const projectEditControllerScripts = [
    '/cordys/html5/demo/TaskManagement/pages/project/model/project-model.js',
    '/cordys/html5/demo/TaskManagement/pages/project/service/project-service.js',
    '/cordys/html5/demo/TaskManagement/pages/program/model/program-model.js',
    '/cordys/html5/demo/TaskManagement/pages/task/model/task-model.js',
    '/cordys/html5/demo/commons/javascripts/selectize-index.js',
    '/cordys/html5/demo/commons/javascripts/selectize.min.js'
]; 

var dependency = Dependency.getInstance();
dependency.addToTrigger("projectEditControllerOnReady");
dependency.execute("projectEditController");

let projectEditViewModel = null;

function projectEditControllerOnload() {

    dependency.initializeSystemObject(['getLanguage', 'handleUnAuthorizedEntry', 
    'removeNullAndUndefined', 'translatePage', 'translateWord', 'showLoader', 
     'handleError', 'extendSelectize', 'getMinDate']);

    dependency.initializeDomObject(['subscribe', 'publish']);

    projectEditViewModel = new ProjectEditViewModel();

    
    let language = window.system.getLanguage()  /*"ar"*/;
    
    window.system.extendSelectize();
    window.system.translatePage(language, "project", "TaskManagement", "project-edit");
    
    
    KOValidation().initializeValidation(projectEditViewModel);
    ko.applyBindings(projectEditViewModel, document.getElementById("project-edit"));


    window.dom.subscribe("edit-project-by-id", function(entityId){
        restEditProject(projectEditViewModel);
        ProjectModel().getProjectById(entityId).done(function(response) {
            if(!response.tuple[0]) {
                window.system.handleUnAuthorizedEntry()
                return;
            }
            response = window.system.removeNullAndUndefined(response.tuple[0].old.rowset.row);
            response.startDate = response.startDate.split("T")[0];
            response.endDate = response.endDate.split("T")[0];

            projectEditViewModel.minDate(response.endDate);

            

            $('#project-edit').modal();
            bindEditProject(response);
        }).fail(function(error){
            window.system.handleError(error);
        });
    });
    
}


function bindEditProject(response){
    console.log(response);
    projectEditViewModel.Id(response.Id);
	projectEditViewModel.projectName(response.name);
    projectEditViewModel.projectManagerOptions([{name :response.projectOwner, id: response.owner}]);
    projectEditViewModel.projectManager([response.owner]);
    projectEditViewModel.institutionalPlanOptions([{name :"الخطة المؤساسية", id: 122}]);
    projectEditViewModel.institutionalPlan([122]);
    projectEditViewModel.assignToUnitOptions([{id: response.assignToUnitId+":"+response.UnitPathById, name: response.UnitName}]);
    projectEditViewModel.assignToUnit([response.assignToUnitId+":"+response.UnitPathById]);
    projectEditViewModel.projectRelateProgOptions([{id: response.programId, name : response.programName}]);
    projectEditViewModel.projectRelateProg([response.programId]);
    projectEditViewModel.projectEndDate(response.endDate);
    projectEditViewModel.projectStartDate(response.startDate);
	projectEditViewModel.projectDescription(response.description);
    projectEditViewModel.projectNotes(response.notes);
    projectEditViewModel.userUnitId(response.createdByUnitId);
    projectEditViewModel.createdBy(response.createdBy);
    projectEditViewModel.projectStatus(response.status);
    projectEditViewModel.assignToUnitNotSelected('');
}


function projectEditControllerOnReady(){
    // let projectService = ProjectService();
    /*projectService.autoCompleteProjectOwner("", projectEditViewModel);
    projectService.autoCompleteProjectRelateProg("", projectEditViewModel);
    projectService.autoCompleteAssignToUnit("", projectEditViewModel);*/

    $('#project-edit #project-manager-id input').on('input', function() {
        ProjectService().autoCompleteProjectOwner(this.value, projectEditViewModel, false)
    });

    $('#project-edit #project-relate-prog-id input').on('input', function() {
        ProjectService().autoCompleteProjectRelateProg( this.value, projectEditViewModel);
    });

    $('#project-edit #assign-to-unit-id .selectize-input input').on('input', function() {
        ProjectService().autoCompleteAssignToUnit( this.value, projectEditViewModel);
    });
}

function ProjectEditViewModel(){
    var self = this;
    self.isEdit = ko.observable("dimmed");
    self.hasError =  ko.observable(true);
    self.assignToUnitNotSelected =  ko.observable('dimmed');


    self.selectedStartDate = ko.observable(window.system.getMinDate());
    self.minDate = ko.observable();
    self.maxDate = ko.observable(new Date('October 15, 2120 05:35:32').toISOString().split("T")[0]);


    self.Id = ko.observable().extend({ 
        required:  {params: true, message:system.translateWord("requiredField")}
    });
    self.title = ko.observable(window.system.translateWord('edit-project'));
    self.textBtn = ko.observable(window.system.translateWord('edit'));

	self.projectName = ko.observable().extend({ 
        required:  {params: true, message:system.translateWord("requiredField")},
        maxLength: {params: 200, message: window.system.translateWord("maxLength200")}
        
    });
    self.projectManagerOptions = ko.observableArray();
    self.projectManager = ko.observable().extend();
    self.institutionalPlanOptions = ko.observableArray([{name :"الخطة المؤساسية", id: 122}]);
    self.institutionalPlan = ko.observable().extend({
        required:  {params: true, message:system.translateWord("requiredField")}
    });
    self.assignToUnitOptions = ko.observableArray();
    self.assignToUnit = ko.observable().extend({
        required:  {params: true, message:system.translateWord("requiredField")}
    })
    self.assignToUnit.subscribe(function(newValue){
        if(newValue.length ==0){
            self.assignToUnitNotSelected('dimmed')
        }
        else {
            if(newValue[0] == ""){
                self.assignToUnitNotSelected('dimmed')
            }
            else {
                self.assignToUnitNotSelected('')

            }
        }
    })
    self.projectRelateProgOptions = ko.observableArray();
    self.projectRelateProg = ko.observable().extend({
        required:  {params: true, message:system.translateWord("requiredField")}
    });
    self.projectRelateProg.subscribe(function(newValue){
        if(newValue == "") return; 
        // let startDate = newValue[0].split(":")[1]
        let endDate = newValue[0].split(":")[2]
        // if(startDate && endDate){
        if(endDate){
            // self.minDate(startDate);
            self.maxDate(endDate);
        }
    })
    self.projectEndDate = ko.observable().extend({
        required:  {params: true, message:system.translateWord("requiredField")},
        date: {
            params: true,
            message: window.system.translateWord("requiredDate")
        },
        smallerThanDate: {
            params: self.maxDate,
            message: window.system.translateWord("smallerThanDate")   
        }
    });
    self.projectStartDate = ko.observable().extend({ 
        required:  {params: true, message:system.translateWord("requiredField")},
        date: {
            params: true,
            message: window.system.translateWord("requiredDate")
        },
        smallerThanDate: {
            params: self.projectEndDate,
            message: window.system.translateWord("smallerThanDate")   
        }
    });
	self.projectDescription = ko.observable().extend({
        maxLength: {params: 5000, message: window.system.translateWord("maxLength5000")}
    });
    self.projectNotes = ko.observable().extend({
        maxLength: {params: 5000, message: window.system.translateWord("maxLength5000")}
    });
    self.userUnitId = ko.observable();
    self.createdBy = ko.observable();
    self.projectStatus = ko.observable();
    
    self.projectSubmit = function(){
        window.system.showLoader(true);
        self.userUnitId(window.user.details.UnitId);
        self.createdBy(window.user.details.UserEntityId);
        self.projectStatus(1); // 1 inprogress
        if(!self.projectManager()) self.projectManager(window.user.details.UserEntityId);
        
        ProjectModel().editProject(self).done(function(response){
            $("#project-edit #mod-btn-project-close" ).click();
            var fntocall = $("#project-edit").attr("project-success");

            if(fntocall && fntocall.length > 3) window[fntocall]();
            else location.reload(true);
            restProject(self);
            window.system.showLoader(false);
        }).fail(function(error){
            window.system.handleError(error);
            window.system.showLoader(false);
        });
    }
    self.errors = ko.validation.group(self);
    
}

function restEditProject(model){
    // model.hasError =  ko.observable(true);
    projectEditViewModel.assignToUnitNotSelected('dimmed');
    projectEditViewModel.projectName("")
    projectEditViewModel.projectManager("")
    projectEditViewModel.projectManagerOptions([])
    projectEditViewModel.institutionalPlan("")
    projectEditViewModel.institutionalPlanOptions([])
    projectEditViewModel.projectRelateProg("")
    projectEditViewModel.projectRelateProgOptions([])
    projectEditViewModel.projectEndDate("")
    projectEditViewModel.projectStartDate("")
    projectEditViewModel.projectDescription("");
    projectEditViewModel.projectNotes("");
    projectEditViewModel.userUnitId("");
    projectEditViewModel.createdBy("");
    System().showLoader(false);
    var hasErrors = document.getElementsByClassName('has-error')
    for(var i=0; i<hasErrors.length; i++){
        $(hasErrors[i]).removeClass('has-error');
    }
}
