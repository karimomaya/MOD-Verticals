const projectCreateControllerViews = {
    "project-create" : "/cordys/html5/demo/TaskManagement/pages/project/view/project-create.html"
}; 
const projectCreateControllerStyles = [
    '/cordys/html5/demo/commons/styles/selectize.css'
]; 
const projectCreateControllerScripts = [
    // '/cordys/html5/demo/TaskManagement/pages/task/resources/javascripts/task-helper.js',
    '/cordys/html5/demo/TaskManagement/pages/project/model/project-model.js',
    '/cordys/html5/demo/TaskManagement/pages/project/service/project-service.js',
    '/cordys/html5/demo/TaskManagement/pages/program/model/program-model.js',
    '/cordys/html5/demo/TaskManagement/pages/task/model/task-model.js',
    '/cordys/html5/demo/TaskManagement/pages/shared/model/shared-model.js',
    '/cordys/html5/demo/commons/javascripts/selectize-index.js',
    '/cordys/html5/demo/commons/javascripts/selectize.min.js'
]; 

var dependency = Dependency.getInstance();
dependency.addToTrigger("projectCreateControllerOnReady");
dependency.execute("projectCreateController");

let projectCreateViewModel = null;


function projectCreateControllerOnload() {

    var system = System();
    let language = system.getLanguage()  /*"ar"*/;
    
    system.extendSelectize();
    system.translatePage(language, "project", "TaskManagement", "project-create");
    
    projectCreateViewModel = new ProjectCreateViewModel();
    KOValidation().initializeValidation(projectCreateViewModel);
    ko.applyBindings(projectCreateViewModel, document.getElementById("project-create"));
    
}

function projectCreateControllerOnReady(){
    let projectService = ProjectService();
    projectService.autoCompleteProjectOwner("", projectCreateViewModel);
    projectService.autoCompleteProjectRelateProg( "", projectCreateViewModel);
    projectService.autoCompleteAssignToUnit( "", projectCreateViewModel);

    $('#project-manager-id input').on('input', function() {
        projectService.autoCompleteProjectOwner(this.value, projectCreateViewModel)
    });

    $('#project-relate-prog-id input').on('input', function() {
        projectService.autoCompleteProjectRelateProg( this.value, projectCreateViewModel);
    });

    $('#assign-to-unit-id .selectize-input input').on('input', function() {
        projectService.autoCompleteAssignToUnit( this.value, projectCreateViewModel);
    });

    /*$('#project-create').on('shown.bs.modal', function () { // when the model is show
        // window.system.showLoader(true);
        restProject(projectCreateViewModel);
    });*/
}

function restProject(){
    // model.hasError =  ko.observable(true);
    projectService.autoCompleteProjectOwner("", projectCreateViewModel);
    projectService.autoCompleteProjectRelateProg( "", projectCreateViewModel);
    projectService.autoCompleteAssignToUnit( "", projectCreateViewModel);

    projectCreateViewModel.assignToUnitNotSelected('dimmed');
    projectCreateViewModel.projectName("")

    projectCreateViewModel.institutionalPlan([""])
    projectCreateViewModel.assignToUnit([""]);
    projectCreateViewModel.projectManager([""])
    projectCreateViewModel.projectRelateProg([""])

    let inst = $("#institutional-plan").parent().find(".item:first");
    inst.attr("data-value", "");
    inst.html("");

    let assigntounit = $("#assign-to-unit").parent().find(".item:first");
    assigntounit.attr("data-value", "");
    assigntounit.html("");

    let projectmanager = $("#project-manager").parent().find(".item:first");
    projectmanager.attr("data-value", "");
    projectmanager.html("");

    let projectrelateprog= $("#project-relate-prog").parent().find(".item:first");
    projectrelateprog.attr("data-value", "");
    projectrelateprog.html("");

    
    projectCreateViewModel.projectEndDate("")
    projectCreateViewModel.projectStartDate("")
    projectCreateViewModel.selectedStartDate("");
    projectCreateViewModel.minDate("");
    projectCreateViewModel.maxDate("");
    projectCreateViewModel.projectDescription("");
    projectCreateViewModel.projectNotes("");
    projectCreateViewModel.userUnitId("");
    projectCreateViewModel.createdBy("");
    System().showLoader(false);
    
    if (projectCreateViewModel && projectCreateViewModel.errors) projectCreateViewModel.errors.showAllMessages(false);
}

function ProjectCreateViewModel(){
    var self = this;
    self.isEdit = ko.observable("");
    self.hasError =  ko.observable(true);
    self.assignToUnitNotSelected =  ko.observable('dimmed');

    self.selectedStartDate = ko.observable(window.system.getMinDate());
    self.minDate = ko.observable(window.system.getMinDate());
    self.maxDate = ko.observable(new Date('October 15, 2120 05:35:32').toISOString().split("T")[0]);


    self.title = ko.observable(window.system.translateWord('add-project'));
    self.textBtn = ko.observable(window.system.translateWord('add'));
	self.projectName = ko.observable().extend({ 
        required: {
            params: true,
            message: window.system.translateWord("requiredField")
        },
        maxLength: {params: 200, message: window.system.translateWord("maxLength200")}
    });
    self.projectManagerOptions = ko.observableArray();
    self.projectManager = ko.observable().extend();
    self.institutionalPlanOptions = ko.observableArray([{name :"الخطة المؤساسية", id: 122}]);
    self.institutionalPlan = ko.observable().extend({
        required: {
            params: true,
            message: window.system.translateWord("requiredField")
        }
    });
    self.assignToUnitOptions = ko.observableArray();
    self.assignToUnit = ko.observable().extend({
        required: {
            params: true,
            message: window.system.translateWord("requiredField")
        }
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

    self.assignToUnit.subscribe(function(newValue){
        projectService.autoCompleteProjectOwner("", projectCreateViewModel);
    })
    self.projectRelateProgOptions = ko.observableArray();
    self.projectRelateProg = ko.observable();
    // .extend({
    //     required: {
    //         params: true,
    //         message: window.system.translateWord("requiredField")
    //     }
    // });
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
        required: {
            params: true,
            message: window.system.translateWord("requiredField")
        },
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
        required: {
            params: true,
            message: window.system.translateWord("requiredField")
        },
        date: {
            params: true,
            message: window.system.translateWord("requiredDate")
        },
        smallerThanDate: {
            params: self.projectEndDate,
            message: window.system.translateWord("smallerThanDate")   
        }
    });
    self.projectStartDate.subscribe(function(newValue){
        if (newValue.length < 2) return;
        self.selectedStartDate(newValue);
    })
	self.projectDescription = ko.observable().extend({
        required: {
            params: true,
            message: window.system.translateWord("requiredField")
        },
        maxLength: {params: 4000, message: window.system.translateWord("maxLength4000")}
    });
    self.projectNotes = ko.observable().extend({
        maxLength: {params: 4000, message: window.system.translateWord("maxLength4000")}
    });
    self.userUnitId = ko.observable();
    self.createdBy = ko.observable();
    self.projectStatus = ko.observable();
    
    self.projectSubmit = function(){
        let _system = System();
        _system.showLoader(true);
        self.userUnitId(window.user.details.UnitId);
        self.createdBy(window.user.details.UserEntityId);
        self.projectStatus(1); // 1 inprogress
        if(!self.projectManager()) self.projectManager(window.user.details.UserEntityId);
        
        ProjectService().createProject(self).done(function(response){
            $( "#mod-btn-project-close" ).click();
            var fntocall = $("#project-create").attr("project-success");
            if(fntocall && fntocall.length > 3) window[fntocall]();
            restProject(self);
            _system.showLoader(false);
        }).fail(function(error){
            _system.showLoader(false);
        });
        
    }
    
    self.projectClose = function() {
        restProject(self)
    
    }
    self.errors = ko.validation.group(self);
    
}
