let programConfirmationViews = {
    "program-confirmation" : "/cordys/html5/demo/TaskManagement/pages/shared/views/program-confirmation-message.html"
}; 
let programConfirmationStyles = []; 
let programConfirmationScripts = []; 

var dependency = Dependency.getInstance();
dependency.execute("programConfirmation");
let programConfirmationViewModel = null;

function programConfirmationOnload(){
    dependency.initializeSystemObject(['translateWord', 'getLanguage', 'translatePage']);
    dependency.initializeDomObject(['subscribe', 'publish'])

    let language = window.system.getLanguage()  /*"ar"*/;
    window.system.translatePage(language, "shared", "TaskManagement", "program-confirmation");
    
    programConfirmationViewModel = new ProgramConfirmationViewModel();
    ko.applyBindings(programConfirmationViewModel,document.getElementById("program-confirmation"));

    window.dom.subscribe("stop-program", function(o){
        programConfirmationViewModel.objectHolder(o) ;
        programConfirmationViewModel.title(window.system.translateWord("stop-program-title"))
        programConfirmationViewModel.question(window.system.translateWord("stop-program-message"))
        programConfirmationViewModel.warning(window.system.translateWord("stop-program-confirm"))
        $('#program-confirmation-model').modal('show'); 
    }) 

    window.dom.subscribe("end-program", function(o){
        programConfirmationViewModel.objectHolder(o) ;
        programConfirmationViewModel.title(window.system.translateWord("end-program-title"))
        programConfirmationViewModel.question(window.system.translateWord("end-program-message"))
        programConfirmationViewModel.warning(window.system.translateWord("end-program-confirm"))
        $('#program-confirmation-model').modal('show'); 
    }) 

    window.dom.subscribe("complete-program", function(o){
        programConfirmationViewModel.objectHolder(o) ;
        programConfirmationViewModel.title(window.system.translateWord("complete-program-title"))
        programConfirmationViewModel.question(window.system.translateWord("complete-program-message"))
        programConfirmationViewModel.warning(window.system.translateWord("complete-program-confirm"))
        $('#program-confirmation-model').modal('show'); 
    }) 

}

function ProgramConfirmationViewModel(){
    let self = this;
    this.title = ko.observable();
    this.question = ko.observable();
    this.warning = ko.observable();
    this.objectHolder = ko.observable();
    this.confirm = function(obj, event){
        self.objectHolder().callBack(self.objectHolder().id, self.objectHolder().status, self.objectHolder().element)
    }
}