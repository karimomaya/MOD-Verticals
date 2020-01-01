const advancedReportControllerViews = {
    // "reports" : "/cordys/html5/demo/TaskManagement/pages/report/views/report.html"
};
const advancedReportControllerStyles = ['/cordys/html5/demo/TaskManagement/pages/report/resources/styles/style.css',];
const advancedReportControllerScripts = [
    '/cordys/html5/demo/TaskManagement/pages/report/model/report-model.js',
    '/cordys/html5/demo/TaskManagement/pages/report/controller/advanced-filter-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/report/controller/advanced-filter-model-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/report/controller/report-graph-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/report/controller/table-report-result-controller.js',
    '/cordys/html5/demo/TaskManagement/pages/shared/controller/dynamic-title-bar-controller.js'
];

var dependency = Dependency.getInstance();
dependency.execute("advancedReportController");

let reportViewModel = null;

function advancedReportControllerOnload() {
    dependency.initializeSystemObject(['getLanguage', 'translatePage', 'showLoader', 'translateWord'])
    dependency.initializeDomObject(['publish', 'subscribe'])
    
    let language = window.system.getLanguage()  /*"ar"*/;
    window.system.translatePage(language, "report", "TaskManagement", "title-bar");

    // reportViewModel = new ReportViewModel();
    // ko.applyBindings(reportViewModel,document.getElementById("reports"));
    

    let title  = window.system.translateWord('reports');
    let obj = {
        title: title,
        haveAction: false, 
        haveEdit: false, 
        haveDelete: false,
        haveEnd: false,
        entityId: null,
        haveStop: false, 
        haveComplete: false, 
        canRestart: false,
        haveReport: false
    }
    window.dom.publish("dynamic-title", obj)

    // window.system.showLoader(false);
}


function ReportViewModel(){
}