const taskCreateControllerViews = {
    "task-create": "/cordys/html5/demo/TaskManagement/pages/task/views/task-create.html"
};
const taskCreateControllerStyles = [
    '/cordys/html5/demo/TaskManagement/pages/task/resources/styles/style.css'
    // '/cordys/html5/demo/commons/styles/selectize.css'

];
const taskCreateControllerScripts = [
    '/cordys/html5/demo/TaskManagement/pages/task/services/task-service.js',
    '/cordys/html5/demo/TaskManagement/pages/project/service/project-service.js',
    '/cordys/html5/demo/TaskManagement/pages/task/model/task-model.js',
    '/cordys/html5/demo/TaskManagement/pages/project/model/project-model.js',
    '/cordys/html5/demo/commons/javascripts/ui/selectize.js',
    // '/cordys/html5/demo/commons/javascripts/selectize-index.js',
    '/cordys/html5/demo/commons/services/lookup-service.js'
];

var dependency = Dependency.getInstance();
dependency.addToTrigger("taskCreateControllerOnReady");
dependency.execute("taskCreateController");

let taskModelView = null;

function taskCreateControllerOnload() {
    dependency.initializeSystemObject(['handleError', 'translatePage', 'extendSelectize',
        'showLoader', 'translateWord', 'showMessage', 'removeNullAndUndefined', 'decrypt',
        'getMinDate', 'getUrlVariable', 'encrypt', 'generateNumber'
    ]);
    dependency.initializeDomObject(['subscribe', 'publish']);

    window.system.translatePage(window.lang, "task", "TaskManagement", "task-create");

    window.system.extendSelectize();

    taskModelView = new taskObject();

    KOValidation().initializeValidation(taskModelView);
    ko.applyBindings(taskModelView, $("#task-create")[0]);

    window.dom.subscribe("edit-task", function (taskData) {
        bindEditTask(taskData);
    });

    window.dom.subscribe("set-max-date", function (maxDate) {
        taskModelView.maxDate(maxDate);
    });

    window.dom.subscribe("have-point-of-discussion", function (havePoint) {
        taskModelView.havePointOfDiscussion(havePoint)
    })
}

function taskCreateControllerOnReady() {
    // if model show
    $('#task-create').on('shown.bs.modal', function () {
        // window.system.showLoader(true);
        restTask()
    });

    $('#task-owner-selectize input').on('input', function () {
        autocompleteTaskOwnerOptions(this.value);
    });

    // if there's autocomplete to team members from outside 
    taskModelView.autoCompleteTeamMembersFunc = TaskService().autocompleteTaskTeamOptions;
    // let autcompleteTeamMember = true;
    if ($('#task-create').attr("task-performers")) {
        // autcompleteTeamMember = false;
        taskModelView.autoCompleteTeamMembersFunc = window[$('#task-create').attr("task-performers")];
    }
    $('#task-team input').on('input', function () {
        taskModelView.autoCompleteTeamMembersFunc(taskModelView, this.value);
    });

    $('#task-project input').on('input', function () {

        TaskService().autocompleteProjectOptions(taskModelView.taskProjectOptions, this.value);
    });

    //autocompleteTaskOwnerOptions("");
    addTaskCurrentUserToBeOwner([]);
    taskModelView.autoCompleteTeamMembersFunc(taskModelView, "");
    TaskService().autocompleteProjectOptions(taskModelView.taskProjectOptions, "");
    autocompletePirorityOptions()
}

function autocompleteTaskOwnerOptions(input) {
    TaskModel().getOwners(input).done(function (response) {
        let ownerObject = [];
        if (!response.tuple[0]) {
            //addTaskCurrentUserToBeOwner(ownerObject)
            return;
        }

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

        taskModelView.taskOwnerOptions(ownerObject);

        if (input == "") {
            addTaskCurrentUserToBeOwner(ownerObject)
        }

    });
}

function addTaskCurrentUserToBeOwner(ownerObject) {
    ownerObject.push({
        id: window.user.details.UserEntityId,
        name: window.user.details.DisplayName
    })
    taskModelView.taskOwnerOptions(ownerObject);
    taskModelView.taskOwner([window.user.details.UserEntityId])
}

function isSelectedBefore(selected, name) {
    let workingUserLi = document.getElementById("suppliers-name-list").getElementsByTagName('li');
    for (var i = 0; i < workingUserLi.length; i++) {
        let selectedBefore = window.system.decrypt(workingUserLi[i].id) + ":" + workingUserLi[i].getAttribute("type")
        if (selected == selectedBefore) {
            window.system.showMessage("warn", window.system.translateWord("isSelectedBefore") + " " + name)
            return true;
        }
    }
    return false;
}

function initializeEmptyData() {
    if (!taskModelView.taskOwner()) taskModelView.taskOwner(window.user.details.UserEntityId);
    if (!taskModelView.taskSource()) taskModelView.taskSource($("#task-create").attr("task-source"));
    if (!taskModelView.taskData()) taskModelView.taskData($("#task-create").attr("task-data"));
    if (!taskModelView.taskEntityItemId()) taskModelView.taskEntityItemId($("#task-create").attr("task-entity-itemId"))
    if (!taskModelView.controllerName()) taskModelView.controllerName($("#task-create").attr("controller-name"));
    if (!taskModelView.applicationName()) taskModelView.applicationName($("#task-create").attr("application-name"));
    if (!taskModelView.entityName()) taskModelView.entityName($("#task-create").attr("entity-name"));

    if (taskModelView.havePointOfDiscussion()) {
        taskModelView.taskData("post-" + taskModelView.pointOfDiscussion());
    }
}

function createSuccess() {

    window.dom.publish("task-create-success", true);

    $("#mod-btn-task-close").click();
    window.system.showLoader(false);
    var fntocall = $("#task-create").attr("task-success");
    if (fntocall && fntocall.length > 3) window[fntocall]();

    window.system.showMessage("success", window.system.translateWord(taskModelView.successMessage()))
    window.dom.publish("reload-report", true);

}

/*function drawGetCreatedTaskByEntityItemId(entityItemId) {
    window.system.translatePage(window.lang, "task", "TaskManagement", "mod-team-task");
    var pageNumber = 1;
    var pageSize = assetConfig.pageSize;
    var sortBy = "taskName";
    var sortDir = "sortAsc";
    var userId = window.user.details.UserEntityId
    entityItemId = entityItemId || taskModelView.taskEntityItemId();
    TaskModel().getCreatedTaskByEntityItemId(userId, entityItemId, pageNumber, pageSize, sortBy, sortDir).done(function (response) {
        TaskService().drawTaskTable(document.getElementById("mod-team-task").getElementsByClassName("task-data-table")[0], response)
    });
}*/

function bindEditTask(response) {

    taskModelView.isEdit(true) // flag that is task to edit
    var taskData = null; // task data will hold all the basic info of the task
    taskData = response;

    // $("#task-create-button").click(); // click the add button to show model and reset binding
    $('#task-create').modal('toggle'); // open create model

    taskModelView.headerTitle(window.system.translateWord('edit-task'));
    let users = [];
    if (response instanceof Array) { //if multiple records
        taskData = response[0]; // assign taskData to the first record
        for (let i = 0; i < response.length; i++) {
            users.push(response[i].performerId + ":" + response[i].type + ":" + response[i].performerName);
        }
    } else if (response.performerId) { //if single record 
        users = [response.performerId + ":" + response.type + ":" + response.performerName];
    }
    taskModelView.oldUsers(users);
    taskModelView.taskTeamOptions(users)
    taskModelView.taskTeam(users);

    taskData = window.system.removeNullAndUndefined(taskData);

    taskModelView.taskId(taskData.Id) // assign taskId to taskModelView after reset biding 
    // taskModelView.taskPage(taskData.Id) // assign taskId to remove save button
    taskModelView.taskPage(true)
    taskModelView.taskName(taskData.taskName)
    taskModelView.taskEndDate(taskData.dueDate.split("T")[0]);
    taskModelView.taskStartDate(taskData.startDate.split("T")[0]);
    taskModelView.taskDescription(taskData.description);
    taskModelView.taskSource(taskData.source);
    taskModelView.controllerName(taskData.controllerName);
    taskModelView.taskEntityItemId(taskData.entityItemId);
    taskModelView.entityName(taskData.entityName);
    taskModelView.applicationName(taskData.applicationName);
    if (taskData.taskData.indexOf("post-") == 0) {
        taskModelView.havePointOfDiscussion(true);
        let point = taskData.taskData.substring(5, taskData.taskData.length);
        taskModelView.pointOfDiscussion(point);
    } else {
        taskModelView.havePointOfDiscussion(false);
    }
    taskModelView.taskData(taskData.taskData);
    taskModelView.status(taskData.status);

    let singleOrMultiple = (taskData.singleOrMultiple == "false") ? 0 : 1;
    taskModelView.singleOrMultiple([singleOrMultiple])

    if (taskData.taskProjectId) { // if project is assigned
        taskModelView.taskProjectOptions([{
            id: taskData.taskProjectId,
            name: taskData.taskProjectName
        }]);
        taskModelView.taskProject([taskData.taskProjectId]);
    }
    taskModelView.taskOwnerOptions([{
        id: taskData.ownerId,
        name: taskData.owner
    }]);
    taskModelView.taskOwner([taskData.ownerId]);
    autocompletePirorityOptions(taskData.priority);
    taskModelView.autoCompleteTeamMembersFunc(taskModelView, "");
    // TaskService().autocompleteTaskTeamOptions(taskModelView, "");

}

function restTask() {
    if (taskModelView.isEdit()) {
        taskModelView.isEdit(false);
        $("#mod-btn-task-submit").html(window.system.translateWord("edit"));
        $("#mod-btn-task-submit").attr("translate-text", "edit");
        taskModelView.successMessage("editTaskSuccess");
    } else {
        let taskService = TaskService();
        taskModelView.taskPriority([]);
        autocompletePirorityOptions();
        autocompleteTaskOwnerOptions("");
        taskModelView.autoCompleteTeamMembersFunc(taskModelView, "");
        // taskService.autocompleteTaskTeamOptions(taskModelView, "");
        taskService.autocompleteProjectOptions(taskModelView.taskProjectOptions, "");
        taskModelView.taskId(null)
        $("#mod-btn-task-submit").html(window.system.translateWord("add"));
        $("#mod-btn-task-submit").attr("translate-text", "add");
    }
    taskModelView.deletedUser(null);
    // taskModelView.taskPage(window.system.getUrlVariable("taskId"));
    taskModelView.taskPage(true);
    taskModelView.taskName(null)
    taskModelView.taskEndDate("");
    taskModelView.taskStartDate("");
    taskModelView.taskDescription(null);

    // taskModelView.havePointOfDiscussion(false)
    taskModelView.pointOfDiscussion(null)

    taskModelView.taskSource(null);
    taskModelView.taskData(null);
    taskModelView.taskEntityItemId(null);
    taskModelView.status(null);
    taskModelView.singleOrMultiple();
    taskModelView.taskTeamOptions([]);
    taskModelView.taskTeam([]);
    taskModelView.taskUsers(null);

    taskModelView.minDate(window.system.getMinDate());

    if (taskModelView && taskModelView.errors) taskModelView.errors.showAllMessages(false);

    window.system.showLoader(false);

}

function autocompletePirorityOptions(d) {

    LookupService().getByCategory("priority").done(function (obj) {
        obj = (obj['MOD_SYS_entity_lookup']) ? obj['MOD_SYS_entity_lookup'] : obj;
        var priorityObject = [];
        // obj = (obj['MOD_SYS_entity_lookup'])?obj['MOD_SYS_entity_lookup'] : [];

        for (let key in obj) {
            if (typeof obj[key] != "object") continue;
            var lookupObj = obj[key];
            var value = (window.lang == 'ar') ? lookupObj.ar_value : lookupObj.eng_value;
            priorityObject.push({
                id: lookupObj['MOD_SYS_entity_lookup-id'].Id,
                name: value
            })
        }
        taskModelView.taskPriorityOptions(priorityObject);

        if (d) {
            taskModelView.taskPriority([d]);
        }
        /*else {
                   //taskModelView.taskPriority([0]);
               }*/
    }).fail(function (error) {
        window.system.handleError(error)
        console.log(error.responseJSON.faultstring.text);
    });
}

function taskObject() {

    var self = this;
    self.hasError = ko.observable(true);
    self.autoCompleteTeamMembersFunc = null;

    self.headerTitle = ko.observable(window.system.translateWord("add-task"));
    self.havePointOfDiscussion = ko.observable(false);
    self.pointOfDiscussion = ko.observable().extend({
        required: {
            message: window.system.translateWord("requiredField"),
            onlyIf: function () {
                return self.havePointOfDiscussion()
            }
        }
    });

    let tId = Cordys().getTaskId();
    self.showSave = ko.observable(true);
    if (tId) self.showSave(false);
    else self.showSave(true);

    self.successMessage = ko.observable("createTaskSuccess");
    self.taskName = ko.observable().extend({
        required: {
            params: true,
            message: window.system.translateWord("requiredField")
        },
        maxLength: {
            params: 200,
            message: window.system.translateWord("maxLength200")
        }
    });
    self.minDate = ko.observable(window.system.getMinDate());
    self.maxDate = ko.observable(new Date('October 15, 2120 05:35:32').toISOString().split("T")[0]);
    self.todayDate = ko.observable(window.system.getMinDate());
    self.taskEndDate = ko.observable().extend({
        required: {
            params: true,
            message: window.system.translateWord("requiredField")
        },
        date: {
            params: true,
            message: window.system.translateWord("requiredDate")
        },
        biggerThanDate: {
            params: self.taskStartDate,
            message: window.system.translateWord("biggerThanDate")
        }
    });
    self.taskStartDate = ko.observable().extend({
        required: {
            params: true,
            message: window.system.translateWord("requiredField")
        },
        date: {
            params: true,
            message: window.system.translateWord("requiredDate")
        },
        smallerThanDate: {
            params: self.taskEndDate,
            message: window.system.translateWord("smallerThanDate")
        }
    });
    self.taskStartDate.subscribe(function (newValue) {
        if (newValue.length < 2) return;
        self.minDate(newValue);
    });

    self.taskPriorityOptions = ko.observableArray();
    self.taskPriority = ko.observable().extend({
        required: {
            params: true,
            message: window.system.translateWord("requiredField")
        }
    });
    self.taskDescription = ko.observable().extend({
        maxLength: {
            params: 4000,
            message: window.system.translateWord("maxLength4000")
        }
    });
    self.taskProjectOptions = ko.observableArray();
    self.taskProject = ko.observable();
    self.taskProject.subscribe(function (newValue) {
        let endDate = (newValue instanceof Array) ? newValue[0] : (newValue) ? newValue : "";
        if (endDate.indexOf(":") != -1) {
            endDate = endDate.split(":")[1].split("T")[0];
            window.dom.publish("set-max-date", endDate)
        }
    })
    self.singleOrMultiple = ko.observable();
    self.singleOrMultipleOptions = ko.observableArray([{
        id: 0,
        name: "منفذ"
    }, {
        id: 1,
        name: "منفذين"
    }])
    self.taskSource = ko.observable();
    self.taskData = ko.observable();
    self.taskEntityItemId = ko.observable();
    self.status = ko.observable();

    self.taskOwnerOptions = ko.observableArray();
    self.taskOwner = ko.observable();
    self.controllerName = ko.observable("");
    self.applicationName = ko.observable("");
    self.entityName = ko.observable("");
    self.taskTeamOptions = ko.observableArray();
    self.taskTeam = ko.observableArray();
    self.entityBWSId = ko.observable(window.system.generateNumber());
    self.taskTeam.subscribe(function (newValue) {
        self.taskUsers([]);

        var userXML = "";
        for (var i = 0; i < newValue.length; i++) {
            let newValueArr = newValue[i].split(":");

            if (newValueArr[0] == taskModelView.taskOwner()) {
                window.system.showMessage("warn", newValueArr[2] + " " + window.system.translateWord("taskPerformerIsTaskOwner"))
                self.taskTeam().splice(self.taskTeam().length - 1, 1);
                self.taskTeam(self.taskTeam())
                continue;
            }

            if (newValueArr[1] == 'Role') {
                newValueArr[1] = 1;
            } else if (newValueArr[1] == 'User') {
                newValueArr[1] = 0;
            } else if (newValueArr[1] == 'Unit') {
                newValueArr[1] = 2;
            } else {
                newValueArr[1] = 0;
            }

            userXML += "<user>";
            userXML += "<type>" + newValueArr[1] + "</type>";
            userXML += "<id>" + newValueArr[0] + "</id>";
            userXML += "<displayName>" + newValueArr[2] + "</displayName>";
            userXML += "</user>"
        }
        self.taskUsers(userXML);

    })
    self.taskUsers = ko.observable().extend({
        required: true
    });

    self.oldUsers = ko.observable();
    self.isEdit = ko.observable(false);
    self.taskId = ko.observable();
    // self.taskPage = ko.observable(window.system.getUrlVariable("taskId"));
    self.taskPage = ko.observable(true);
    self.deletedUser = ko.observable();

    self.saveTask = function () {
        if (!self.taskName()) {
            KOValidation().showMessage("task-name", window.system.translateWord("requiredField"))
            return;
        }
        // task can be saved
        self.status(10); //draft

        initializeEmptyData();



        if (self.taskId()) { // handle edit task
            TaskService().editTask(self, createSuccess, window.system.handleError);
        } else { // handle save


            TaskService().createTask(self, createSuccess, window.system.handleError);
        }

    }

    self.submit = function () {
        self.entityBWSId(window.system.generateNumber());

        if (self.errors().length > 0) return;

        window.system.showLoader(true);
        initializeEmptyData();
        if (self.taskId()) { // handle edit task
            if (parseInt(self.status()) == 10) {
                self.status(1);
            }

            console.log(self.oldUsers());
            console.log(self.taskTeam());

            let oldUserArr = self.oldUsers();
            let selectedUserArr = self.taskTeam();
            var userXML = "";
            for (var i = 0; i < oldUserArr.length; i++) {
                let newValueArr = oldUserArr[i].split(":");
                if (selectedUserArr.indexOf(oldUserArr[i]) == -1) {
                    console.log(oldUserArr[i] + " is deleted");

                    if (newValueArr[1] == 'Role') {
                        newValueArr[1] = 1;
                    } else if (newValueArr[1] == 'User') {
                        newValueArr[1] = 0;
                    } else if (newValueArr[1] == 'Unit') {
                        newValueArr[1] = 2;
                    } else {
                        newValueArr[1] = 0;
                    }

                    userXML += "<user>";
                    userXML += "<type>" + newValueArr[1] + "</type>";
                    userXML += "<id>" + newValueArr[0] + "</id>";
                    userXML += "</user>"
                }
            }

            self.deletedUser(userXML);
            // [response.performerId+":"+response.type+":"+response.performerName];

            TaskService().editTask(self, createSuccess, window.system.handleError);
        } else { // handle create task
            self.status(1); //opened
            TaskService().createTask(self, createSuccess, window.system.handleError);
        }

    }
    self.close = function () {}
    self.errors = ko.validation.group(self);
}