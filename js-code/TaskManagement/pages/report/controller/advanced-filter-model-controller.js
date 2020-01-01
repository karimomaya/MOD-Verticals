let advancedFilterModelControllerViews = {
    "advanced-filter-model" : "/cordys/html5/demo/TaskManagement/pages/report/views/advanced-filter-model.html"
};
let advancedFilterModelControllerStyles = [
    '/cordys/html5/demo/commons/styles/selectize.css'
];
let advancedFilterModelControllerScripts = [
    '/cordys/html5/demo/commons/javascripts/ui/selectize.js',
    '/cordys/html5/demo/commons/javascripts/selectize-index.js',
    '/cordys/html5/demo/TaskManagement/pages/task/model/task-model.js',
    '/cordys/html5/demo/TaskManagement/pages/project/model/project-model.js',
    '/cordys/html5/demo/RiskManagement/pages/risk/model/risk-model.js'
];

let advancedFilterModelControllerLang = [
    "/cordys/html5/demo/TaskManagement/pages/report/resources/language-pack/report-"+window.lang+".json"
]

var dependency = Dependency.getInstance();
dependency.addToTrigger("advancedFilterModelControllerOnReady");
dependency.execute("advancedFilterModelController");

let advancedFilterModelViewModel = null;

function advancedFilterModelControllerOnload() {
    dependency.initializeSystemObject(['getLanguage', 'translatePage', 'showLoader', 'handleError',
        'extendSelectize', 'translateWord'])

    window.system.extendSelectize();

    dependency.initializeDomObject(['publish', 'subscribe'])
    
    window.system.translatePage("advanced-filter-model", "advanced-filter-model");

    advancedFilterModelViewModel = new AdvancedFilterModelViewModel();
    ko.applyBindings(advancedFilterModelViewModel,document.getElementById("advanced-filter-model"));

    window.dom.subscribe('filter-button-clicked', function(){
        $("advanced-filter-model").modal();
    });

    window.dom.subscribe("unbind-report-modal", function(){
        resetAdvancedFilterModelViewModel()
    })
}


function advancedFilterModelControllerOnReady(){
    autocompleteUserOptions("", true);
    autocompleteProjectOptions("");

    $('#user-options input').on('input', function () {
        autocompleteUserOptions(this.value, true);
    });
    $('#project-selector input').on('input', function () {
        autocompleteProjectOptions(this.value);
    });
    $('#risk-selector input').on('input', function () {
        autocompleteRiskNameOptions(this.value);
    });
}

function autocompleteRiskNameOptions(input){
    RiskModel().GetCreatedRiskByName(input, window.user.details.UserEntityId, 1, assetConfig.autocompleteSize).done(function (response){
        let names = [];
        advancedFilterModelViewModel.riskNameOptions(names);
        if(!response.tuple[0]) return;

        response = response.tuple[0].old.rowset.row;

        if (response instanceof Array) {
            for (let i = 0; i < response.length; i++) {
                names.push({
                    id: response[i].Id+":"+response[i].riskName,
                    name: response[i].riskName
                });
            }
        } else {
            names.push({
                id: response.Id+":"+response.riskName,
                name: response.riskName
            });
        }
        advancedFilterModelViewModel.riskNameOptions(names);
    }).fail(function (error){
        window.system.handleError(error);
    });
}

function autocompleteUserOptions(input, setCurrentUser) {
    TaskModel().getSubUsers(window.user.details.UserEntityId, input, 1, 3).done(function (response) {
        let ownerObject = [];
        if (!response.tuple[0]) return;

        let oldSelected = advancedFilterModelViewModel.user();
        if(oldSelected){
            for (var i=0; i< oldSelected.length; i++){
                let results = oldSelected[i].split(":")
                ownerObject.push({
                    id: results[0]+":"+results[1],
                    name: results[1]
                })
            }
        }

        var results = response.tuple[0].old.rowset.row;

        if (results instanceof Array) {
            for (let i = 0; i < results.length; i++) {
                if(oldSelected){
                    if(oldSelected.indexOf(results[i].UserEntityId+":"+results[i].DisplayName) == -1){
                        ownerObject.push({
                            id: results[i].UserEntityId+":"+results[i].DisplayName,
                            name: results[i].DisplayName
                        })
                    }
                }else {

                    ownerObject.push({
                        id: results[i].UserEntityId+":"+results[i].DisplayName,
                        name: results[i].DisplayName
                    })
                }
                
            }
        } else {
            if(oldSelected){
                if(oldSelected.indexOf(results.UserEntityId+":"+results.DisplayName) == -1){
                    ownerObject.push({
                        id: results.UserEntityId+":"+results.DisplayName,
                        name: results.DisplayName
                    })
                }
            }else {
                ownerObject.push({
                    id: results.UserEntityId+":"+results.DisplayName,
                    name: results.DisplayName
                })
            }
            
        }

        if(setCurrentUser){
            ownerObject.push({
                id: window.user.details.UserEntityId+":"+window.user.details.DisplayName,
                name: window.user.details.DisplayName
            })
        }

        advancedFilterModelViewModel.usersOptions(ownerObject);
        if(oldSelected){
            advancedFilterModelViewModel.user(oldSelected)
        }
    });
}


function autocompleteProjectOptions( input, pageNumber, pageSize) {
    var path = window.user.details.UnitPathById;
    pageNumber = pageNumber || 1;
    pageSize = pageSize || assetConfig.autocompleteSize;;
    ProjectModel().getProjectByHeadUnit(input, 1, pageNumber, pageSize).done(function (response) {
        if (!response.tuple[0]) return;
        var results = response.tuple[0].old.rowset.row;
        let projectObject = [];
        if (results instanceof Array) {
            for (let i = 0; i < results.length; i++) {
                projectObject.push({
                    id: results[i].Id+":"+results[i].name,
                    name: results[i].name
                })
            }
        } else {
            projectObject.push({
                id: results.Id+":"+results.name,
                name: results.name
            })
        }
        advancedFilterModelViewModel.taskProjectOptions(projectObject);
    }).fail(function (error) {
        window.system.handleError(error)
    });
}


function resetAdvancedFilterModelViewModel(){
    autocompleteUserOptions("", true);
    autocompleteProjectOptions("");
    // advancedFilterModelViewModel.headerLabel(window.system.translateWord('task-report'));
    advancedFilterModelViewModel.reportType([]);
    advancedFilterModelViewModel.user([]);
    advancedFilterModelViewModel.startDate(null);
    advancedFilterModelViewModel.endDate(null);
    advancedFilterModelViewModel.project([]);
    advancedFilterModelViewModel.statusType([]);

    if (advancedFilterModelViewModel && advancedFilterModelViewModel.errors) advancedFilterModelViewModel.errors.showAllMessages(false);
}

function AdvancedFilterModelViewModel(){
    let self = this;
    this.headerLabel = ko.observable(window.system.translateWord('task-report'))
    this.reportTypeOptions = ko.observableArray([
        { id: 1, name: window.system.translateWord("delayedTaskReport")},
        { id: 2, name: window.system.translateWord("userProductivityReport")},
        { id: 3, name: window.system.translateWord("finishedTaskReport")},
        { id: 4, name: window.system.translateWord("projectFinishedDelayedReport") },
        // { id: 5, name: "تقرير بالمشروعات في فترة محددة"},
        { id: 6, name: window.system.translateWord("taskAssociatedWithSpecificChallend") }
    ]);
    this.reportType = ko.observable();
    this.reportType.subscribe(function(newValue){
        self.user([]);
        self.startDate(null);
        self.endDate(null);
        self.project([]);
        self.statusType([]);
        self.riskName(null);
    })
    this.riskName = ko.observable();
    this.riskNameOptions = ko.observableArray([]);
    this.usersOptions = ko.observableArray([]);
    this.user = ko.observable();
    this.startDate = ko.observable();
    this.endDate = ko.observable();
    this.taskProjectOptions = ko.observableArray([]);
    this.project = ko.observable();
    this.statusType = ko.observable();
    this.statusTypeOptions = ko.observableArray([
        {id: 1, name: window.system.translateWord("delayed")},
        {id: 2, name: window.system.translateWord("finished")}
    ])
    this.submit = function(){
        // window.system.showLoader(true);
        window.dom.publish('advanced-filter', this);
        window.dom.publish('clear-report', true);
        $('#cancel-filter').click()
    }
}

