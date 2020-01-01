const taskControllerViews = {
    "inprogress-task": "/cordys/html5/demo/TaskManagement/pages/task/views/task-table.html",
    "task-created-by": "/cordys/html5/demo/TaskManagement/pages/task/views/task-table.html",
    "completed-task": "/cordys/html5/demo/TaskManagement/pages/task/views/task-table.html",
    "owned-task": "/cordys/html5/demo/TaskManagement/pages/task/views/task-table.html",
    // "task-draft": "/cordys/html5/demo/TaskManagement/pages/task/views/task-table.html",
    "task-archive" : "/cordys/html5/demo/TaskManagement/pages/task/views/task-table.html"
};
const taskControllerStyles = [];
const taskControllerScripts = [
    '/cordys/html5/demo/TaskManagement/pages/task/services/task-service.js',
    '/cordys/html5/demo/TaskManagement/pages/task/model/task-model.js',
    "/cordys/html5/demo/commons/javascripts/jquery.twbsPagination.js"
];

var dependency = Dependency.getInstance();
dependency.execute("taskController");

let taskControllerObject = {
    taskInProgressViewModel: null,
    taskCreatedBy: null,
    taskCompleted: null,
    taskArchived: null,
    taskOwned: null,
    // taskDraft: null,
    model: null,
    service: null,
    currentTab: 1
}

let filterObject = {
    sortBy: "endDate",
    sortDir: "sortAsc",
    freeText: "",
    progress: -1,
    startDate: null,
    endDate: null,
    filterOwner: -1
}

function taskControllerOnload() {
    dependency.initializeSystemObject(['getLanguage', 'translatePage', 'showLoader', 'translateWord',
        'showMessage', 'removeNullAndUndefined', 'encrypt']);
        
    dependency.initializeDomObject(['sortable', 'subscribe', 'publish', 'drawPagination']);

    let language = window.system.getLanguage() /*"ar"*/ ;
    window.system.translatePage(language, "task-controller", "TaskManagement", "task-container");

    var inprogressTable = document.getElementById("inprogress-task").getElementsByClassName("main-table")[0];
    window.dom.sortable(inprogressTable, "fillMyInProgressTaskTable")
    var createdByTable = document.getElementById("task-created-by").getElementsByClassName("main-table")[0];
    window.dom.sortable(createdByTable, "fillCreatedByTable")
    var taskOwnedTable = document.getElementById("owned-task").getElementsByClassName("main-table")[0];
    window.dom.sortable(taskOwnedTable, "fillMyOwnedTable")
    var completedTable = document.getElementById("completed-task").getElementsByClassName("main-table")[0];
    window.dom.sortable(completedTable, "fillCompletedTask");
    /*var draftTable = document.getElementById("task-draft").getElementsByClassName("main-table")[0];
    window.dom.sortable(draftTable, "fillDraftTable");*/
    
    var archiveTask = document.getElementById("task-archive").getElementsByClassName("main-table")[0];
    window.dom.sortable(archiveTask, "fillArchivedTask");


    taskControllerObject.taskInProgressViewModel = new TaskViewModel();
    ko.applyBindings(taskControllerObject.taskInProgressViewModel, inprogressTable);

    taskControllerObject.taskCreatedBy = new TaskViewModel();
    ko.applyBindings(taskControllerObject.taskCreatedBy, createdByTable);

    taskControllerObject.taskOwned = new TaskViewModel();
    ko.applyBindings(taskControllerObject.taskOwned, taskOwnedTable);

    taskControllerObject.taskCompleted = new TaskViewModel();
    ko.applyBindings(taskControllerObject.taskCompleted, completedTable);

    /*taskControllerObject.taskDraft = new TaskViewModel();
    ko.applyBindings(taskControllerObject.taskDraft, draftTable);*/

    taskControllerObject.taskArchived = new TaskViewModel();
    ko.applyBindings(taskControllerObject.taskArchived, archiveTask);

    taskControllerObject.model = TaskModel(['getCountTask', 'closeTask', 'startTask', 'endTask',
        'restartTask', 'deleteTask', 'getArchivedTask', 'archiveTask', 'forceCloseTask'
    ]);
    /*taskControllerObject.service = TaskService(['getCreatedByTask', 'getMyOwnedTask',
        'getInProgressTask', 'getCompletedTask', 'getDraftTask', 'addAttributesToTaskResponse',
        'viewTask', 'showEdit', 'openTaskCollapse'
    ]);*/
    taskControllerObject.service = TaskService(['getCreatedByTask', 'getMyOwnedTask',
        'getInProgressTask', 'getCompletedTask', 'addAttributesToTaskResponse',
        'viewTask', 'showEdit', 'openTaskCollapse'
    ]);

    window.dom.subscribe("in-progress-task", function (obj) {
        drawInProgressTask(obj.taskName(), obj.progress(), obj.startDate(), obj.endDate(), obj.owner());
    });
    window.dom.subscribe("completed-task", function (obj) {
        drawCompletedTask(obj.taskName(), obj.progress(), obj.startDate(), obj.endDate(), obj.owner());
    });
    window.dom.subscribe("created-by-task", function (obj) {
        drawCreatedByTask(obj.taskName(), obj.progress(), obj.startDate(), obj.endDate(), obj.owner());
    });
    window.dom.subscribe("my-owned-task", function (obj) {
        drawMyOwnedTask(obj.taskName(), obj.progress(), obj.startDate(), obj.endDate());
    });
    /*window.dom.subscribe("draft-task", function (obj) {
        drawDraftTask(obj.taskName(), obj.progress(), obj.startDate(), obj.endDate(), obj.owner());
    });*/

    window.dom.subscribe("task-archive", function (obj) {
        drawArchivedTask(obj.taskName(), obj.progress(), obj.startDate(), obj.endDate(), obj.owner());
    });

    window.dom.subscribe('start-task', function (obj) {
        window.system.showLoader(true, document.getElementById("inprogress-task"));
        taskControllerObject.model.startTask(obj).done(function () {
            window.system.showLoader(false);
            drawInProgressTask();
            window.system.showMessage("success", window.system.translateWord("task-start-success"));
        }).fail(function (error) {
            window.system.handleError(error);
        });
    })

}

function drawArchivedTask(taskName, progress, startDate, endDate, filterOwner) {
    setFilterObject(taskName, progress, startDate, endDate, filterOwner)

    var element = document.getElementById("task-archive")
    getCount("archive", element, fillArchivedTask, filterObject.freeText, filterObject.progress, filterObject.startDate,
        filterObject.endDate, filterObject.filterOwner);

}

function fillArchivedTask(pageNumber, sortBy, sortDir) {
    if (sortBy) filterObject.sortBy = sortBy;
    if (sortDir) filterObject.sortDir = sortDir;

    var element = document.getElementById("task-archive")
    window.system.showLoader(true, element);
    // pageNumber, pageSize, sortBy, sortDir, taskName, progress, startDate, endDate, filterOwner

    taskControllerObject.model.getArchivedTask(pageNumber, assetConfig.pageSize,
                filterObject.sortBy, filterObject.sortDir, filterObject.freeText, 
                filterObject.progress,filterObject.startDate, filterObject.endDate, 
                filterObject.filterOwner).done(function (response) {
        addResponseToModel(response, taskControllerObject.taskArchived);
    }).fail(function (error) {
        window.system.handleError(error);
    });
}

function setFilterObject(taskName, progress, startDate, endDate, filterOwner) {
    filterObject.sortBy = "endDate";
    filterObject.sortDir = "sortAsc";
    filterObject.progress = progress || -1;
    filterObject.filterOwner = filterOwner || -1;

    if (taskName) {
        filterObject.freeText = taskName;
    } else {
        filterObject.freeText = $("#search-input").val() || "";
    }

    if (startDate) {
        filterObject.startDate = startDate;
    } else {
        filterObject.startDate = new Date('October 15, 1920').toISOString().split("T")[0];
    }

    if (endDate) {
        filterObject.endDate = endDate;
    } else {
        filterObject.endDate = new Date('October 15, 2120').toISOString().split("T")[0];
    }

}

function fillCompletedTask(pageNumber, sortBy, sortDir) {
    if (sortBy) filterObject.sortBy = sortBy;
    if (sortDir) filterObject.sortDir = sortDir;

    var element = document.getElementById("completed-task")
    window.system.showLoader(true, element);
    taskControllerObject.service.getCompletedTask(filterObject.sortBy,
        filterObject.sortDir, pageNumber, null, filterObject.freeText, filterObject.progress,
        filterObject.startDate, filterObject.endDate, filterObject.filterOwner).done(function (response) {

        addResponseToModel(response, taskControllerObject.taskCompleted, 'completedTask');
    }).fail(function (error) {
        window.system.handleError(error);
    });
}


function drawCompletedTask(taskName, progress, startDate, endDate, filterOwner) {
    setFilterObject(taskName, progress, startDate, endDate, filterOwner)

    var element = document.getElementById("completed-task")
    getCount("completed", element, fillCompletedTask, filterObject.freeText, filterObject.progress, filterObject.startDate,
        filterObject.endDate, filterObject.filterOwner);
}

function drawCreatedByTask(taskName, progress, startDate, endDate, filterOwner) {
    setFilterObject(taskName, progress, startDate, endDate, filterOwner)

    var element = document.getElementById("task-created-by")
    getCount("createdTask", element, fillCreatedByTable, filterObject.freeText, filterObject.progress, filterObject.startDate, filterObject.endDate, filterObject.filterOwner);
}

function fillCreatedByTable(pageNumber, sortBy, sortDir) {

    if (sortBy) filterObject.sortBy = sortBy;
    if (sortDir) filterObject.sortDir = sortDir;

    var element = document.getElementById("task-created-by")
    window.system.showLoader(true, element);
    taskControllerObject.service.getCreatedByTask(filterObject.sortBy,
            filterObject.sortDir, pageNumber, null, filterObject.freeText, filterObject.progress,
            filterObject.startDate, filterObject.endDate, filterObject.filterOwner)
        .done(function (response) {
            
            addResponseToModel(response, taskControllerObject.taskCreatedBy);
        }).fail(function (error) {
            window.system.handleError(error);
        });
}
/*
function drawDraftTask(taskName, progress, startDate, endDate, filterOwner) {
    setFilterObject(taskName, progress, startDate, endDate, filterOwner)

    var element = document.getElementById("task-draft")
    getCount("draft", element, fillDraftTable, filterObject.freeText, filterObject.progress, filterObject.startDate, filterObject.endDate, filterObject.filterOwner);
}
*/
/*
function fillDraftTable(pageNumber, sortBy, sortDir) {

    if (sortBy) filterObject.sortBy = sortBy;
    if (sortDir) filterObject.sortDir = sortDir;

    var element = document.getElementById("task-draft")
    window.system.showLoader(true, element);
    taskControllerObject.service.getDraftTask(filterObject.sortBy, filterObject.sortDir,
            pageNumber, null, filterObject.freeText, filterObject.progress,
            filterObject.startDate, filterObject.endDate, filterObject.filterOwner)
        .done(function (response) {

            addResponseToModel(response, taskControllerObject.taskDraft);
        }).fail(function (error) {
            window.system.handleError(error);
        });
}
*/

function drawMyOwnedTask(taskName, progress, startDate, endDate) {
    setFilterObject(taskName, progress, startDate, endDate);

    var element = document.getElementById("owned-task");
    getCount("ownedTask", element, fillMyOwnedTable, filterObject.freeText, filterObject.progress, filterObject.startDate, filterObject.endDate);
}

function fillMyOwnedTable(pageNumber, sortBy, sortDir) {
    if (sortBy) filterObject.sortBy = sortBy;
    if (sortDir) filterObject.sortDir = sortDir;

    var element = document.getElementById("owned-task")
    window.system.showLoader(true, element);
    taskControllerObject.service.getMyOwnedTask(filterObject.sortBy, filterObject.sortDir,
        pageNumber, null, filterObject.freeText, filterObject.progress,
        filterObject.startDate, filterObject.endDate).done(function (response) {

        addResponseToModel(response, taskControllerObject.taskOwned);
    }).fail(function (error) {
        window.system.handleError(error);
    });
}

function drawInProgressTask(taskName, progress, startDate, endDate, filterOwner) {
    setFilterObject(taskName, progress, startDate, endDate, filterOwner)

    var element = document.getElementById("inprogress-task");
    getCount("inProgress", element, fillMyInProgressTaskTable, filterObject.freeText, filterObject.progress, filterObject.startDate, filterObject.endDate, filterObject.filterOwner)
}

function fillMyInProgressTaskTable(pageNumber, sortBy, sortDir) {
    if (sortBy) filterObject.sortBy = sortBy;

    if (sortDir) filterObject.sortDir = sortDir;

    var element = document.getElementById("inprogress-task");
    window.system.showLoader(true, element);
    taskControllerObject.service.getInProgressTask(filterObject.sortBy,
            filterObject.sortDir, pageNumber, null, filterObject.freeText, filterObject.progress,
            filterObject.startDate, filterObject.endDate, filterObject.filterOwner)
        .done(function (response) {

            addResponseToModel(response, taskControllerObject.taskInProgressViewModel);

        }).fail(function (error) {
            window.system.handleError(error);
        });
}

function getCount(type, element, callBack, name, progress, startDate, endDate, filterOwner) {
    taskControllerObject.taskInProgressViewModel.tasks([]);
    taskControllerObject.taskCreatedBy.tasks([]);
    taskControllerObject.taskCreatedBy.tasks([]);
    taskControllerObject.taskCompleted.tasks([]);
    taskControllerObject.taskOwned.tasks([]);
    // taskControllerObject.taskDraft.tasks([]);
    taskControllerObject.taskArchived.tasks([]);

    taskControllerObject.model.getCountTask(type, name, progress, startDate, endDate, filterOwner).done(function (response) {
        window.system.showLoader(false);
        var totalPages = Math.ceil(response.count.text / assetConfig.pageSize);

        if (totalPages == 0) {
            let e = element.getElementsByClassName('pagination')[0]
            if ($(e).twbsPagination) $(e).removeData("twbs-pagination");
            return;
        }
        window.dom.drawPagination(element.getElementsByClassName('pagination')[0], totalPages, callBack)

        callBack();

    }).fail(function (error) {
        window.system.handleError(error);
    });
}

function addResponseToModel(response, model, type) {
    if (!response.tuple[0]) {
        window.system.showLoader(false);
        return;
    }
    response = response.tuple[0].old.rowset.row;
    if (response instanceof Array) {
        for (let i = 0; i < response.length; i++) {
            if (!response[i].progress) response[i].progress = 0;
            response[i] = window.system.removeNullAndUndefined(response[i])
            response[i] = taskControllerObject.service.addAttributesToTaskResponse(response[i], type)
            response[i] = addActionToResponse(response[i])
            response[i].taskData = ko.observable();
        }
    } else {
        if (!response.progress) response.progress = 0;
        response = window.system.removeNullAndUndefined(response)
        response = taskControllerObject.service.addAttributesToTaskResponse(response, type)
        response = addActionToResponse(response)
        response.taskData = ko.observable();
    }
    
    model.tasks(response);
    window.system.showLoader(false);
}

function addActionToResponse(response) {
    response.archiveTask = function (obj, e) {
        taskControllerObject.model.archiveTask(obj.Id).done(function (response) {
            window.system.showMessage("success", system.translateWord("archive-task-success"));

            let fntocall = $("#task-create").attr("task-success");
            if(fntocall && fntocall.length > 2) window[fntocall]();
            window.dom.publish("reload-report", true);
            
        }).fail(function (error) {
            window.system.handleError(error)
        })
    }
    response.forceCloseTask = function(obj, e){
        taskControllerObject.model.forceCloseTask(obj.Id).done(function (response) {
            window.system.showMessage("success", system.translateWord("close-task-success"));

            let fntocall = $("#task-create").attr("task-success");
            if(fntocall && fntocall.length > 2) window[fntocall]();
            window.dom.publish("reload-report", true);
            
        }).fail(function (error) {
            window.system.handleError(error)
        })
    }
    response.displayTask = function (obj, e) {
        taskControllerObject.service.viewTask(window.system.encrypt(obj.Id))
    }
    response.closeTask = function (obj, e) {
        taskControllerObject.model.closeTask(obj.Id).done(function () {
            obj.canArchive(true);
            obj.canClose(false);
            obj.canEdit(false);
            window.system.showMessage("success", system.translateWord("close-task-success"));
            window.dom.publish("reload-report", true);
        }).fail(function (error) {
            window.system.handleError(error)
        })
    }
    response.editTask = function (obj) {
        taskControllerObject.service.showEdit(obj.Id, function (response) {
            window.dom.publish("edit-task", response);
        })

    }
    response.deleteTask = function (obj) {
        taskControllerObject.model.deleteTask(obj.Id).done(function (response) {
            var fntocall = $("#task-create").attr("task-success");
            if (fntocall && fntocall.length > 3) window[fntocall]();
            window.dom.publish("reload-report", true);
        }).fail(function (error) {
            window.system.handleError(error);
        });

    }
    response.openTaskCollapse = function (obj, e) {
        taskControllerObject.service.openTaskCollapse(obj.Id, e.target, function (users) {
            obj.taskPerformers(users);
        })
    }
    response.startTask = function (obj, e) {
        // obj.Id
        window.dom.publish('start', obj);
    }
    response.endTask = function (obj, e) { // performer end task
        window.system.showLoader(true, document.getElementById("inprogress-task"));
        taskControllerObject.model.endTask(obj.targetTaskPerformerId).done(function () {
            let tr = $(e.target).closest('tr')
            let fold = $(tr).next('tr');
            $(tr).remove();
            $(fold).remove();
            window.system.showLoader(false);
            window.system.showMessage("success", window.system.translateWord("task-end-success"));
            window.dom.publish("reload-report", true);
        }).fail(function (error) {
            window.system.handleError(error);
        });
    }
    response.restartTask = function (obj, e) {
        window.system.showLoader(true, document.getElementById("inprogress-task"));
        taskControllerObject.model.restartTask(obj.Id, obj.targetTaskPerformerId).done(function () {
            system.showLoader(false);
            drawInProgressTask();
            window.dom.publish("reload-report", true);
        }).fail(function (error) {
            system.handleError(error);
        })
    }
    return response;
}


function TaskViewModel() {
    this.tasks = ko.observableArray([]);
}