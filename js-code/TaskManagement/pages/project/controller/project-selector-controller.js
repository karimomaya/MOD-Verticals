const projectSelectorControllerViews = {
    "project-selector": "/cordys/html5/demo/TaskManagement/pages/project/view/project-selector.html"
};
const projectSelectorControllerStyles = [];
const projectSelectorControllerScripts = [
    "/cordys/html5/demo/TaskManagement/pages/project/model/project-model.js",
    "/cordys/html5/demo/TaskManagement/pages/task/services/task-service.js",
    "/cordys/html5/demo/TaskManagement/pages/task/model/task-model.js"
];

var dependency = Dependency.getInstance();
dependency.execute("projectSelectorController");
dependency.addToTrigger("projectSelectorControllerOnReady");
let projectSelectorViewModel = null;

function projectSelectorControllerOnload() {

    dependency.initializeSystemObject(['getLanguage', 'translatePage', 'extendSelectize', 'handleError'])
    dependency.initializeDomObject(['subscribe', 'publish'])
    let language = window.system.getLanguage();
    window.system.translatePage(language, "project", 'TaskManagement', "project-selector");

    window.system.extendSelectize();

    projectSelectorViewModel = new ProjectSelectorViewModel();
    KOValidation().initializeValidation(projectSelectorViewModel);
    ko.applyBindings(projectSelectorViewModel, $('project-selector')[0]);

    window.dom.subscribe("start", function (obj) {
        let taskId = obj.Id;
        let targetTaskPerformerId = obj.targetTaskPerformerId;

        if (!taskId) { // case if obj is from task display
            taskId = window.system.getUrlVariable("taskId");
            taskId = window.system.decrypt(taskId);
            targetTaskPerformerId = obj;
        }

        console.log(targetTaskPerformerId);
        
        TaskModel().getTaskById(taskId).done(function (response) {
            response = response['wstxns2:MOD_TM_entity_Task'] || response['MOD_TM_entity_Task'];
            if (!response.taskProjectId) {
                $('#project-selector').modal({
                    show: true
                });
                projectSelectorViewModel.taskId = taskId;
                projectSelectorViewModel.targetTaskPerformerId = targetTaskPerformerId;
            } else {
                window.dom.publish("start-task",targetTaskPerformerId);
            }

        }).fail(function (error) {
            window.system.handleError(error);
        });
    });

}

function projectSelectorControllerOnReady() {
    TaskService().autocompleteProjectOptions(projectSelectorViewModel,"");

    $("#project-select input").on('input', function () {
        projectSelectorViewModel.projectInputCounter++;
        if(projectSelectorViewModel.projectInputCounter == 2){
            projectSelectorViewModel.projectInputCounter = 0 ;
            TaskService().autocompleteProjectOptions(projectSelectorViewModel, this.value);
            // autocompleteProject(this.value);
        }
    });
}

/*
function autocompleteProject(input) {

    input = input || "";
    ProjectModel().getProjectByPath(window.user.details.UnitPathById, input, 1, 1, 5).done(function (response) {
        if (!response.tuple[0]) return;
        var results = response.tuple[0].old.rowset.row;
        let projectObject = [];
        if (results instanceof Array) {
            for (let i = 0; i < results.length; i++) {
                projectObject.push({
                    id: results[i].Id,
                    name: results[i].name
                })
            }
        } else {
            projectObject.push({
                id: results.Id,
                name: results.name
            })
        }
        projectSelectorViewModel.projectOptions(projectObject);
    }).fail(function (error) {
        window.system.handleError(error);
    });

}
*/
function ProjectSelectorViewModel() {
    var self = this;
    this.hasError = ko.observable(true);
    this.taskProjectOptions = ko.observableArray([]);
    this.project = ko.observable().extend({
        required: true
    });
    this.projectInputCounter = 0;
    this.taskId = "";
    this.targetTaskPerformerId = "";

    this.submit = function () {
        // update task
        var projectId = self.project()[0].split(":")[0];
        var projectName = self.project()[0].split(";")[1];
        TaskModel().updateTaskWithProject(self.taskId,projectId).done(function(response){
            window.dom.publish('set-project-name-to-task-data',projectName);
            window.dom.publish("start-task", self.targetTaskPerformerId);
        }).fail(function(error){
            window.system.handleError(error);
        });

        $("#cancel-project-selector").click();
    }
}