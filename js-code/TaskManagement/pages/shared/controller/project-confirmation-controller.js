let projectConfirmationViews = {
    "project-confirmation" : "/cordys/html5/demo/TaskManagement/pages/shared/views/project-confirmation-message.html"
}; 
let projectConfirmationStyles = []; 
let projectConfirmationScripts = []; 

var dependency = Dependency.getInstance();
dependency.execute("projectConfirmation");
let projectConfirmationViewModel = null;

function projectConfirmationOnload(){
    dependency.initializeSystemObject(['translateWord', 'getLanguage', 'translatePage']);
    dependency.initializeDomObject(['subscribe', 'publish'])

    let language = window.system.getLanguage()  /*"ar"*/;
    window.system.translatePage(language, "shared", "TaskManagement", "project-confirmation");
    
    projectConfirmationViewModel = new ProjectConfirmationViewModel();
    ko.applyBindings(projectConfirmationViewModel,document.getElementById("project-confirmation"));

    window.dom.subscribe("stop-project", function(o){
        projectConfirmationViewModel.objectHolder(o) ;
        projectConfirmationViewModel.title(window.system.translateWord("stop-project-title"))
        projectConfirmationViewModel.question(window.system.translateWord("stop-project-message"))
        projectConfirmationViewModel.warning(window.system.translateWord("stop-project-confirm"))
        $('#project-confirmation-model').modal('show'); 
    }) 

    window.dom.subscribe("end-project", function(o){
        projectConfirmationViewModel.objectHolder(o) ;
        projectConfirmationViewModel.title(window.system.translateWord("end-project-title"))
        projectConfirmationViewModel.question(window.system.translateWord("end-project-message"))
        projectConfirmationViewModel.warning(window.system.translateWord("end-project-confirm"))
        $('#project-confirmation-model').modal('show'); 
    }) 

    window.dom.subscribe("complete-project", function(o){
        projectConfirmationViewModel.objectHolder(o) ;
        projectConfirmationViewModel.title(window.system.translateWord("complete-project-title"))
        projectConfirmationViewModel.question(window.system.translateWord("complete-project-message"))
        projectConfirmationViewModel.warning(window.system.translateWord("complete-project-confirm"))
        $('#project-confirmation-model').modal('show'); 
    })
}

function ProjectConfirmationViewModel(){
    let self = this;
    this.title = ko.observable();
    this.question = ko.observable();
    this.warning = ko.observable();
    this.objectHolder = ko.observable();
    this.confirm = function(obj, event){
        self.objectHolder().callBack(self.objectHolder().id, self.objectHolder().status, self.objectHolder().element)
    }
}