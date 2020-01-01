let taskIndexControllerViews = {};
let taskIndexControllerStyles = [
    //'/cordys/html5/demo/TaskManagement/pages/task/resources/styles/style.css'
];
let taskIndexControllerScripts = [
    '/cordys/html5/demo/TaskManagement/pages/task/model/task-model.js',
    '/cordys/html5/demo/TaskManagement/pages/task/controller/task-create-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/task/controller/task-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/task/controller/task-filter-controller.js',
    // '/cordys/html5/demo/TaskManagement/pages/task/controller/task-report-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/program/controller/program-create-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/program/controller/program-edit-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/program/controller/program-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/project/controller/project-create-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/project/controller/project-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/project/controller/project-edit-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/project/controller/project-selector-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/report/controller/report-controller.js'
    // '/cordys/html5/demo/TaskManagement/pages/shared/controller/dynamic-filter-controller.js'
];

var dependency = Dependency.getInstance();
dependency.execute("taskIndexController");

function taskIndexControllerOnload() {
    var system = System();
    let language = system.getLanguage() /*"ar"*/ ;
    system.translatePage(language, "task", "TaskManagement", "task-nav-header");
    system.showLoader(false);
    clickOnFirstLinkNav("task-nav-header");

    dependency.initializeDomObject(['publish']);
}

function freeTextSearch() {
    let searchFunc = $("#search-input").attr("search-in");
    window[searchFunc]();
}

function clickOnFirstLinkNav(target) {
    let taskContainer = document.getElementById(target);
    let secondNav = taskContainer.getElementsByTagName("nav")[0];
    $(secondNav).find("a:first").click();
}

function waitForActive(e, tag) {
    if (e.offsetTop > 0) {
        let drawFn = document.getElementsByTagName(tag)[0].getAttribute("output-draw-function");
        window[drawFn]();
    } else {
        window.requestAnimationFrame(function () {
            waitForActive(e, tag)
        });
    }
}

function reloadArchivedTask(){
    window.dom.publish("filter-task", "archive-task");
    $("#search-input").attr("search-in", "drawArchivedTask");
    //call function drawInProgressTask when creation function success
    $("#task-create").attr("task-success", "reloadArchivedTask")
    waitForActive(document.getElementById("task-archive").getElementsByClassName("task-data-table")[0], "task-archive")
}
/*
function reloadDraftTask() {
    window.dom.publish("filter-task", "draft-task");
    $("#search-input").attr("search-in", "drawDraftTask");
    //call function drawInProgressTask when creation function success
    $("#task-create").attr("task-success", "reloadDraftTask")
    waitForActive(document.getElementById("task-draft").getElementsByClassName("task-data-table")[0], "task-draft")
}
*/

function reloadInProgressTask() {
    window.dom.publish("filter-task", "in-progress-task");
    $("#search-input").attr("search-in", "drawInProgressTask");
    //call function drawInProgressTask when creation function success
    $("#task-create").attr("task-success", "reloadInProgressTask")
    waitForActive(document.getElementById("inprogress-task").getElementsByClassName("task-data-table")[0], "inprogress-task")
}

function reloadCompletedTask() {
    window.dom.publish("filter-task", "completed-task");
    $("#search-input").attr("search-in", "drawCompletedTask");
    //call function drawCompletedTask when creation function success
    $("#task-create").attr("task-success", "reloadCompletedTask")
    waitForActive(document.getElementById("completed-task").getElementsByClassName("task-data-table")[0], "completed-task");
}

function reloadCreatedByTask() {
    window.dom.publish("filter-task", "created-by-task");
    $("#search-input").attr("search-in", "drawCreatedByTask");
    //call function drawInProgressTask when creation function success
    $("#task-create").attr("task-success", "reloadCreatedByTask")
    waitForActive(document.getElementById("task-created-by").getElementsByClassName("task-data-table")[0], "task-created-by")
}

function reloadMyOwnedTask() {
    window.dom.publish("filter-task", "my-owned-task");
    $("#search-input").attr("search-in", "drawMyOwnedTask");
    //call function drawInProgressTask when creation function success
    $("#task-create").attr("task-success", "reloadMyOwnedTask")
    waitForActive(document.getElementById("owned-task").getElementsByClassName("task-data-table")[0], "owned-task")
}

function reloadStoppedProgram() {
    $("#search-input").attr("search-in", "drawStoppedProgram");
    window.dom.publish("filter-task", "stopped-program");
    $("#program-create").attr("program-success", "reloadStoppedProgram")
    waitForActive(document.getElementById("stopped-program").getElementsByClassName("program-data")[0], "stopped-program")
}

function reloadEndedProgram() {
    $("#search-input").attr("search-in", "drawEndedProgram");
    window.dom.publish("filter-task", "ended-program");
    $("#program-create").attr("program-success", "reloadEndedProgram")
    waitForActive(document.getElementById("ended-program").getElementsByClassName("program-data")[0], "ended-program")
}

function reloadInProgressProgram() {
    $("#search-input").attr("search-in", "drawInProgressProgram");
    window.dom.publish("filter-task", "inprogress-program");
    $("#program-create").attr("program-success", "reloadInProgressProgram")
    waitForActive(document.getElementById("inprogress-program").getElementsByClassName("program-data")[0], "inprogress-program")
}

function reloadInProgressProject() {
    $("#search-input").attr("search-in", "drawInProgressProjects");
    window.dom.publish("filter-task", "inprogress-project");
    $("#project-create").attr("project-success", "reloadInProgressProject")
    waitForActive(document.getElementById("inprogress-project").getElementsByClassName("project-data-table")[0], "inprogress-project")
}

function reloadStoppedProject() {
    $("#search-input").attr("search-in", "drawStoppedProjects");
    window.dom.publish("filter-task", "stopped-project");
    $("#project-create").attr("project-success", "reloadStoppedProject")
    waitForActive(document.getElementById("stopped-project").getElementsByClassName("project-data-table")[0], "stopped-project")
}

function reloadEndedProject() {
    $("#search-input").attr("search-in", "drawEndedProjects");
    window.dom.publish("filter-task", "ended-project");
    $("#project-create").attr("project-success", "reloadEndedProject")
    waitForActive(document.getElementById("ended-project").getElementsByClassName("project-data-table")[0], "ended-project")
}

function changeParentNavCurrent(e, model, text, canAccess) {
    canAccess = canAccess || 1;
    var ele = e.parentElement.getElementsByClassName("current-tab");
    let id = $(ele).attr("add-button");
    ele[0].classList.remove("current-tab");
    e.classList.add("current-tab");

    var addButton = document.getElementById(id);
    addButton.id = model + "-button";
    addButton.setAttribute("data-target", model);
    addButton.parentElement.getElementsByTagName("span")[1].innerHTML = System().translateWord(text);

    if(!System().canAccess(canAccess)){
        addButton.parentElement.parentElement.style.display = "none";
    }
    else {
        addButton.parentElement.parentElement.style.display = "inline-block";
    }

    

    // click on the first nav
    var target = e.getAttribute('href');
    target = target.replace("#", "");
    clickOnFirstLinkNav(target);
}

function changeChildNavCurrent(e) {
    var ele = e.parentElement.getElementsByClassName("current-tab");
    ele[0].classList.remove("current-tab");
    e.classList.add("current-tab");
}