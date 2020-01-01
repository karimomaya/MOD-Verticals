const projectControllerViews = {
    "inprogress-project": "/cordys/html5/demo/TaskManagement/pages/project/view/project-table.html",
    "stopped-project": "/cordys/html5/demo/TaskManagement/pages/project/view/project-table.html",
    "ended-project": "/cordys/html5/demo/TaskManagement/pages/project/view/project-table.html"
};
const projectControllerStyles = [];
const projectControllerScripts = [
    "/cordys/html5/demo/TaskManagement/pages/project/service/project-service.js",
    "/cordys/html5/demo/TaskManagement/pages/shared/controller/project-confirmation-controller.js",
    '/cordys/html5/demo/TaskManagement/pages/project/model/project-model.js',
    "/cordys/html5/demo/commons/javascripts/jquery.twbsPagination.js"
];


let projectService = null;
let projectObjectController = {
    projectInProgressViewModel: null,
    projectEndViewModel: null,
    projectStopViewModel: null,
    currentTab: 1
}

let projectFilterObject = {
    sortBy: "projectName",
    sortDir: "sortAsc",
    freeText: "",
    progress: -1,
    startDate: null,
    endDate: null,
    filterOwner: ""
}

var dependency = Dependency.getInstance();
dependency.execute("projectController");

function projectControllerOnload() {
    dependency.initializeSystemObject(['getLanguage', 'translatePage', 'showLoader', 'translateWord',
        'getPageCount', 'showMessage', 'getAutoCompleteCount'
    ]);
    dependency.initializeDomObject(['sortable', 'subscribe', 'publish', 'drawPagination']);

    let language = window.system.getLanguage() /*"ar"*/ ;
    window.system.translatePage(language, "project", "TaskManagement", "project-container");
    projectService = ProjectService();

    var inprogressTable = document.getElementById("inprogress-project").getElementsByClassName("main-table")[0];
    window.dom.sortable(inprogressTable, "fillInProgressProjectTable")
    var endedTable = document.getElementById("ended-project").getElementsByClassName("main-table")[0];
    window.dom.sortable(endedTable, "fillEndedProjectTable")
    var stoppedTable = document.getElementById("stopped-project").getElementsByClassName("main-table")[0];
    window.dom.sortable(stoppedTable, "fillStoppedProjectTable")


    projectObjectController.projectInProgressViewModel = new ProjectViewModel();
    ko.applyBindings(projectObjectController.projectInProgressViewModel, inprogressTable);
    projectObjectController.projectEndViewModel = new ProjectViewModel();
    ko.applyBindings(projectObjectController.projectEndViewModel, endedTable);
    projectObjectController.projectStopViewModel = new ProjectViewModel();
    ko.applyBindings(projectObjectController.projectStopViewModel, stoppedTable);


    window.dom.subscribe("inprogress-project", function (obj) {
        drawInProgressProjects(obj.taskName(), obj.progress(), obj.startDate(), obj.endDate(), obj.owner());
    });

    window.dom.subscribe("ended-project", function (obj) {
        drawEndedProjects(obj.taskName(), obj.progress(), obj.startDate(), obj.endDate(), obj.owner());
    });

    window.dom.subscribe("stopped-project", function (obj) {
        drawStoppedProjects(obj.taskName(), obj.progress(), obj.startDate(), obj.endDate(), obj.owner());
    });
}


function setProjectFilterObject(projectName, progress, startDate, endDate, filterOwner) {
    projectFilterObject.sortBy = "endDate";
    projectFilterObject.sortDir = "sortAsc";
    projectFilterObject.progress = progress || -1;

    projectFilterObject.filterOwner = filterOwner || -1;
    if (projectName) {
        projectFilterObject.freeText = projectName;
    } else {
        projectFilterObject.freeText = $("#search-input").val() || "";
    }

    projectFilterObject.startDate = startDate || new Date('October 14, 1920 05:35:32').toISOString().split("T")[0];
    projectFilterObject.endDate = endDate || new Date('October 14, 2120 05:35:32').toISOString().split("T")[0];
}

function drawInProgressProjects(projectName, progress, startDate, endDate, owner) {
    setProjectFilterObject(projectName, progress, startDate, endDate, owner)
    $("#project-create").attr("project-success", "drawInProgressProjects");
    $("#project-edit").attr("project-success", "drawInProgressProjects");
    projectObjectController.currentTab = 1;
    var element = document.getElementById("inprogress-project");
    window.system.showLoader(true, element);
    countProject(1, element, projectFilterObject); // status 1 inporgress
}

function fillInProgressProjectTable(pageNumber, sortBy, sortDir) {
    var element = document.getElementById("inprogress-project");
    fillProjectTable(element, 1, pageNumber, sortBy, sortDir);
}

function drawEndedProjects(projectName, progress, startDate, endDate, owner) {
    setProjectFilterObject(projectName, progress, startDate, endDate, owner)
    $("#project-create").attr("project-success", "drawEndedProjects");
    $("#project-edit").attr("project-success", "drawEndedProjects");
    projectObjectController.currentTab = 2;
    var element = document.getElementById("ended-project");
    window.system.showLoader(true, element);
    countProject(2, element, projectFilterObject); // status 2 Ended
}

function fillEndedProjectTable(pageNumber, sortBy, sortDir) {
    var element = document.getElementById("ended-project");
    fillProjectTable(element, 2, pageNumber, sortBy, sortDir);
}


function fillStoppedProjectTable(pageNumber, sortBy, sortDir) {
    var element = document.getElementById("stopped-project");
    fillProjectTable(element, 3, pageNumber, sortBy, sortDir);
}

function drawStoppedProjects(projectName, progress, startDate, endDate, owner) {
    setProjectFilterObject(projectName, progress, startDate, endDate, owner)
    $("#project-create").attr("project-success", "drawStoppedProjects");
    $("#project-edit").attr("project-success", "drawStoppedProjects");
    projectObjectController.currentTab = 3;
    var element = document.getElementById("stopped-project");
    window.system.showLoader(true, element);
    countProject(3, element, projectFilterObject); // status 3 stopped
}

function fillProjectTable(element, status, pageNumber, sortBy, sortDir) {
    status = status || 1; // 1 inprogress
    pageNumber = pageNumber || 1;
    var pageSize = window.system.getPageCount();
    sortBy = sortBy || "endDate";
    sortDir = sortDir || "sortAsc";
    window.system.showLoader(true, element);

    ProjectModel().getProjectByStatus(status, pageNumber, pageSize, sortBy, sortDir, projectFilterObject).done(function (response) {

        projectObjectController.projectInProgressViewModel.projects([])
        projectObjectController.projectEndViewModel.projects([])
        projectObjectController.projectStopViewModel.projects([])

        if (!response.tuple[0]) {
            window.system.showLoader(false);
            return;
        }
        response = response.tuple[0].old.rowset.row;

        

        if (response instanceof Array) {
            for (let i = 0; i < response.length; i++) {
                response[i] = window.system.removeNullAndUndefined(response[i])
                response[i] = addAdditionalAttributesToProjectResponse(response[i])
            }
        } else {
            response = window.system.removeNullAndUndefined(response)
            response = addAdditionalAttributesToProjectResponse(response)
        }

        if (status == 1) { // 1 inprogress
            projectObjectController.projectInProgressViewModel.projects(response);
        } else if (status == 2) { // 2 ended
            projectObjectController.projectEndViewModel.projects(response);
        } else if (status == 3) { // 3 stopped
            projectObjectController.projectStopViewModel.projects(response)
        }

        window.system.showLoader(false);
    }).fail(function (error) {
        window.system.handleError(error);
        window.system.showLoader(false);
    });
}

function countProject(status, element, projectFilterObject) {
    var type = "countProject"
    status = status || 1

    ProjectModel().countProject(type, status, projectFilterObject).done(function (response) {

        window.system.showLoader(false);
        var totalPages = Math.ceil(response.count.text / window.system.getPageCount());
        if (totalPages == 0) return;

        window.dom.drawPagination(element.getElementsByClassName('pagination')[0], totalPages, fillMyInProgressTaskTable)
        fillProjectTable(element, status);
    }).fail(function (error) {
        window.system.handleError(error);
        window.system.showLoader(false);
    });
}

function confirmProjectAction(entityId, status, element) {
    changeProjectStatus(entityId, status, element)
}


function addAdditionalAttributesToProjectResponse(response) {
    /* Splite start and end Date to only show date  */
    response.endDate = response.endDate.split("T")[0];
    response.startDate = response.startDate.split("T")[0];
    /* Update Progress to contain percentage sign  */
    let progress = response.progress;
    if (isNaN(progress)) progress = 0;
    progress = progress | 0;
    response.progress = progress + "%";
    /* Declare helpful variable to update menu  */
    response.isOwner = false;
    response.isEnded = false;
    response.isStopped = false;
    response.isInprogresss = false;
    let status = parseInt(response.status);
    if (status == 1) { // project in-progress
        response.isInprogresss = true;
    } else if (status == 2) { // project end
        response.isEnded = true;
    } else if (status == 3) { // project stopped
        response.isStopped = true;
    }
    /* if there's related project to this program it cannot be deleted  */
    response.haveDelete = true;
    if (parseInt(response.taskCount) > 0) response.haveDelete = false;
    /* Check if the current user is the owner  */
    console.log(response);
    let userId = window.user.details.UserEntityId;
    if (userId == response.createdBy || userId == response.owner ) response.isOwner = true;
    /* open collapse  */
    response.openProjectCollapse = function (obj, e) {
        $(e.target).parent().toggleClass("open").next(".fold").toggleClass("open");
    }
    response.displayProject = function (obj, e) {
        let id = window.system.encrypt(obj.Id)
        var url = 'http://appworks-dev:81/home/mod/html5/demo/TaskManagement/pages/project/project-display.html?projectId=' + id
        window.top.location = url
    }
    response.editProject = function (obj) {
        window.dom.publish('edit-project-by-id', parseInt(obj.Id));
    }
    response.stopProject = function (obj, e) {
        let o = {
            callBack: confirmProjectAction,
            id: obj.Id,
            status: 3,
            element: e.target
        }
        
        window.dom.publish("stop-project", o)
        // changeProjectStatus(obj.Id, 3, e.target)
    }
    response.endProject = function (obj, e) {
        let o = {
            callBack: confirmProjectAction,
            id: obj.Id,
            status: 2,
            element: e.target
        }
        window.dom.publish("end-project", o)
        // changeProjectStatus(obj.Id, 2, e.target)
    }
    response.completeProject = function (obj, e) {
        let o = {
            callBack: confirmProjectAction,
            id: obj.Id,
            status: 1,
            element: e.target
        }
        window.dom.publish("complete-project", o)
        // changeProjectStatus(obj.Id, 1, e.target)
    }
    response.deleteProject = function (obj, e) {
        showProjectLoaderBasedOnStatus(projectObjectController.currentTab);
        ProjectModel().deleteProject(obj.Id).done(function () {
            let tr = $(e.target).closest('tr')
            let fold = $(tr).next('tr');
            $(tr).remove();
            $(fold).remove();
            window.system.showLoader(false);

            window.system.showMessage("success", window.system.translateWord("deleteSuccess"));
        }).fail(function (error) {
            window.system.showLoader(false);
            window.system.handleError(error);
        })

    }
    return response;
}

function changeProjectStatus(entityId, status, element) {
    showProjectLoaderBasedOnStatus(projectObjectController.currentTab);
    ProjectModel().changeProjectStatus(entityId, status).done(function () {
        let tr = $(element).closest('tr')
        let fold = $(tr).next('tr');
        $(tr).remove();
        $(fold).remove();
        window.system.showLoader(false);
        var successMessage = "";
        switch(status){
            case 1:
                successMessage = "project-complete-success";
                break;
            case 2:
                successMessage = "project-end-success";
                break;
            case 3:
                successMessage = "project-stop-success";
                break;
            default:
                successMessage = "updateProjectStatusSuccess";
        }
        window.system.showMessage("success", window.system.translateWord(successMessage));

        window.dom.publish("reload-report", true);

    }).fail(function (error) {
        window.system.handleError(error);
    })
}

function showProjectLoaderBasedOnStatus(currentTab) {

    let id = "inprogress-project";
    if (currentTab == 1) {
        id = "inprogress-project";
    } else if (currentTab == 2) {
        id = "ended-project";
    } else if (currentTab == 3) {
        id = "stopped-project";
    }
    window.system.showLoader(true, document.getElementById(id))
}

function ProjectViewModel() {
    this.projects = ko.observableArray([]);
    this.tab = ko.observable();
}