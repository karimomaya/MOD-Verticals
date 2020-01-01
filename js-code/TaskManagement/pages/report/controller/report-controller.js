let taskReportControllerViews = {
    "reports" : "/cordys/html5/demo/TaskManagement/pages/report/views/report.html"
};

let taskReportControllerStyles = [
    '/cordys/html5/demo/TaskManagement/pages/report/resources/styles/style.css',
    '/cordys/html5/demo/commons/styles/Chart.css'
];


let taskReportControllerScripts = [
    '/cordys/html5/demo/commons/javascripts/ui/apexcharts.js',
    '/cordys/html5/demo/TaskManagement/pages/report/model/report-model.js'
];

let taskReportControllerLang = [
    '/cordys/html5/demo/TaskManagement/pages/report/resources/language-pack/report-'+window.lang+'.json'
]


var dependency = Dependency.getInstance();
dependency.addToTrigger("taskReportControllerOnReady");
dependency.execute("taskReportController");

let reportViewModel = null;

function taskReportControllerOnload() {
    dependency.initializeSystemObject(['translatePage', 'translateWord', 'handleError']);
    dependency.initializeDomObject(['drawRadialBarChart', 'subscribe']);
    
    window.system.translatePage("reports", "reports");

    reportViewModel = new ReportViewModel();
    ko.applyBindings(reportViewModel,document.getElementById("reports"));

    window.dom.subscribe("reload-report", function () {
        reloadReportProgress();
    });

    window.dom.subscribe("change-report-label", function (obj) {
        reportViewModel.firstLabel(window.system.transleWord(obj.firstLabel))
        reportViewModel.secondLabel(window.system.transleWord(obj.secondLabel))
        reportViewModel.thirdLabel(window.system.transleWord(obj.thirdLabel))
    });
    
}


function taskReportControllerOnReady(){
    reloadReportProgress();
    
    
    // createChart(65, document.getElementById("program-chart"), "#c9a869")
}

function reloadReportProgress(){
    let reprtModel = ReportModel();

    reprtModel.getProgramProgressReport().done(function(response){
        
        if(!response.tuple[0]) {
            window.dom.drawRadialBarChart(0, document.getElementById("program-chart"), "#c9a869");
            return;
        }

        response = response.tuple[0].old.rowset.row;
        
        let ended = parseInt(response.ended);
        let inProgress = parseInt(response.inProgress);
        
        let total = ended + inProgress;
        let percentage = parseInt((ended * 100) /total);
        if(isNaN(percentage)){
            percentage = 0;
        }

        reportViewModel.endedFirstValue(ended);
        reportViewModel.totalFirstValue(total);

        window.dom.drawRadialBarChart(percentage, document.getElementById("program-chart"), "#c9a869");

    }).fail(function(error){
        window.dom.drawRadialBarChart(0, document.getElementById("program-chart"), "#c9a869");
        window.system.handleError(error);
    })

    reprtModel.getProjectProgressReport().done(function(response){
        if(!response.tuple[0]) {
            window.dom.drawRadialBarChart(0, document.getElementById("project-chart"), "#c9a869");
            return;
        }

        response = response.tuple[0].old.rowset.row;
        let ended = parseInt(response.ended);
        let inProgress = parseInt(response.inProgress);
        
        let total = ended + inProgress;
        let percentage = parseInt((ended * 100) /total);
        if(isNaN(percentage)){
            percentage = 0;
        }
        
        reportViewModel.endedSecondValue(ended);
        reportViewModel.totalSecondValue(total);

        window.dom.drawRadialBarChart(percentage, document.getElementById("project-chart"), "#c9a869");
    }).fail(function(error){
        window.dom.drawRadialBarChart(0, document.getElementById("project-chart"), "#c9a869");
        window.system.handleError(error);
    })

    reprtModel.getTaskProgressReport().done(function(response){
        if(!response.tuple[0]) {
            window.dom.drawRadialBarChart(0, document.getElementById("task-chart"), "#c9a869");
            return;
        }
        response = response.tuple[0].old.rowset.row;
        let ended = parseInt(response.total);
        let inProgress = parseInt(response.inProgress);

        let total = ended + inProgress;
        let percentage = parseInt((ended * 100) /total);
        if(isNaN(percentage)){
            percentage = 0;
        }

        reportViewModel.endedThirdValue(ended);
        reportViewModel.totalThirdValue(total);

        window.dom.drawRadialBarChart(percentage, document.getElementById("task-chart"), "#c9a869");
    }).fail(function(error){
        window.dom.drawRadialBarChart(0, document.getElementById("task-chart"), "#c9a869");
        window.system.handleError(error);
    })
}


function ReportViewModel(){
    this.firstLabel = ko.observable(window.system.translateWord("programs"))
    this.secondLabel = ko.observable(window.system.translateWord("projects"))
    this.thirdLabel = ko.observable(window.system.translateWord("tasks"))
    this.endedFirstValue = ko.observable(0);
    this.totalFirstValue = ko.observable(0);
    this.endedSecondValue = ko.observable(0);
    this.totalSecondValue = ko.observable(0);
    this.endedThirdValue = ko.observable(0);
    this.totalThirdValue = ko.observable(0);
    this.reportUrl =assetConfig.processExprience + "/" + _ConfigURL('TaskReport')
}