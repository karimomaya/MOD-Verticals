let dynamicFilterControllerViews = {
    "task-filter": '/cordys/html5/demo/TaskManagement/pages/task/views/task-filter.html'
};
let dynamicFilterControllerStyles = [
];
let dynamicFilterControllerScripts = [
];
var dependency = Dependency.getInstance();
dependency.addToTrigger("dynamicFilterControllerOnReady");
dependency.execute("dynamicFilterController");
let dynamicFilterViewModel = null;

function dynamicFilterControllerOnload() {

    dependency.initializeSystemObject(['getLanguage', 'translatePage', 'showMessage',
        'translateWord', 'extendSelectize'
    ]);
    dependency.initializeDomObject(['subscribe']);

    let language = window.system.getLanguage(); //
    window.system.translatePage(language, "task", "TaskManagement", "task-filter");

    window.system.extendSelectize();

    dynamicFilterViewModel = new DynamicFilterViewModel();
    ko.applyBindings(dynamicFilterViewModel, $('#task-filter')[0]);


    window.dom.subscribe("filter-task", function (tab) {
        if (tab.indexOf("task") != -1) {
            dynamicFilterViewModel.headerLabel(window.system.translateWord("filter-task"));
            dynamicFilterViewModel.filterName(window.system.translateWord("task-name"));
            dynamicFilterViewModel.filterOwnerName(window.system.translateWord("task-owner"));
        } else if (tab.indexOf("program") != -1) {
            dynamicFilterViewModel.headerLabel(window.system.translateWord("filter-program"));
            dynamicFilterViewModel.filterName(window.system.translateWord("program-name"));
            dynamicFilterViewModel.filterOwnerName(window.system.translateWord("program-owner"));
        } else if (tab.indexOf("project") != -1) {
            dynamicFilterViewModel.headerLabel(window.system.translateWord("filter-project"));
            dynamicFilterViewModel.filterName(window.system.translateWord("project-name"));
            dynamicFilterViewModel.filterOwnerName(window.system.translateWord("project-owner"));
        }

        dynamicFilterViewModel.targetTab(tab);
        if (tab == "my-owned-task") {
            dynamicFilterViewModel.disableOwner(false);
        } else {
            dynamicFilterViewModel.disableOwner(true);
        }
    });

}

function autoCompleteOwner(input) {
    TaskModel().getSubUsers(window.user.details.UserEntityId, input, 1, 3).done(function (response) {
        let ownerObject = [];
        if (!response.tuple[0]) return;

        var results = response.tuple[0].old.rowset.row;

        if (results instanceof Array) {
            for (let i = 0; i < results.length; i++) {
                ownerObject.push({
                    id: results[i].UserEntityId,
                    name: results[i].DisplayName
                })
            }
        } else {
            ownerObject.push({
                id: results.UserEntityId,
                name: results.DisplayName
            })
        }

        dynamicFilterViewModel.ownerOptions(ownerObject);

    });
}

function dynamicFilterControllerOnReady() {
    autoCompleteOwner("");

    $('#task-filter').on('shown.bs.modal', function () {
        restDynamicFilter()
    });

    $("#filter-owner input").on('input', function () {
        autoCompleteOwner(this.value);
    });
}

function restDynamicFilter() {
    dynamicFilterViewModel.taskName("")
    dynamicFilterViewModel.startDate("")
    dynamicFilterViewModel.endDate("")
    dynamicFilterViewModel.owner("")
    // dynamicFilterViewModel.disableOwner(false);
    dynamicFilterViewModel.progress("")
}

function DynamicFilterViewModel() {
    let self = this;
    this.targetTab = ko.observable();
    this.taskName = ko.observable()
    this.startDate = ko.observable()
    this.endDate = ko.observable()
    this.owner = ko.observable()
    this.ownerOptions = ko.observableArray([]);
    this.progress = ko.observable()
    this.disableOwner = ko.observable(true);
    this.headerLabel = ko.observable();
    this.filterName = ko.observable();
    this.filterOwnerName = ko.observable();

    this.submit = function () {

        if (!this.taskName() && !this.startDate() && !this.endDate() && !this.owner() &&
            !this.progress()) {
            window.system.showMessage("warn", window.system.translateWord("emptyFieldsdynamicFilter"));
            return;
        }
        if (!self.startDate()) {
            let d = new Date('October 15, 1920 05:35:32').toISOString().split("T")[0];
            self.startDate(d)
        }
        if (!self.endDate()) {
            let d = new Date('October 15, 2120 05:35:32').toISOString().split("T")[0];
            self.endDate(d);
        }
        if (!self.progress()) {
            self.progress(-1);
        } else if (self.progress() < 0 || self.progress() > 100) {
            window.system.showMessage("error", window.system.translateWord("wrongProgressFieldDynamicFilter"));
            return;
        }
        if (!self.owner()) {
            self.owner(-1);
        }

        window.dom.publish(self.targetTab(), self);
        $("#cancel-filter").click();

    }
}