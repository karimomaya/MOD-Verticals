let dynamicFilterControllerViews = {
    "dynamic-filter": '/cordys/html5/demo/TaskManagement/pages/shared/views/dynamic-filter.html'
};
let dynamicFilterControllerStyles = [];
let dynamicFilterControllerScripts = [];
var dependency = Dependency.getInstance();
// dependency.addToTrigger("dynamicFilterControllerOnReady");
dependency.execute("dynamicFilterController");

let dynamicFilterViewModel = null;

function dynamicFilterControllerOnload() {
    dependency.initializeSystemObject(['getLanguage', 'translatePage', 'showMessage', 'translateWord']);
    dependency.initializeDomObject(['publish', 'subscribe']);
    let language = window.system.getLanguage();

    window.system.translatePage(language, "shared", "TaskManagement", "dynamic-filter");
    dynamicFilterViewModel = new DynamicFilterViewModel();

    ko.applyBindings(dynamicFilterViewModel, $("dynamic-filter")[0])

    window.dom.subscribe('dynamic-filter', function(tab){
        dynamicFilterViewModel.targetTab(tab)
    }) 


}

function DynamicFilterViewModel() {
    let self = this;
    this.targetTab = ko.observable();
    this.taskName = ko.observable();
    this.startDate = ko.observable();
    this.endDate = ko.observable();
    this.owner = ko.observable();
    this.progress = ko.observable();
    this.disableOwner  = ko.observable();
    this.submit = function(){  
        if (!this.taskName() && !this.startDate() && !this.endDate() && !this.owner() &&
            !this.progress()) {
            window.system.showMessage("warn", window.system.translateWord("emptyFieldsTaskFilter"));
            return;
        }

        window.dom.publish(self.targetTab(), self);

    }
}
