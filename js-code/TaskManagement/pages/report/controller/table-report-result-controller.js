const tableReportResultControllerViews = {"table-report-result" : "/cordys/html5/demo/TaskManagement/pages/report/views/table-report-result.html"};
const tableReportResultControllerStyles = [];
const tableReportResultControllerScripts = ["/cordys/html5/demo/TaskManagement/pages/report/service/report-service.js"];

var dependency = Dependency.getInstance();
dependency.execute("tableReportResultController");

let tableReportResultViewModel = null;

function tableReportResultControllerOnload() {
    dependency.initializeSystemObject([ 'drawPagination', 'convertJavaDate', 'translatePage', 'showLoader', 'translateWord', 'encrypt', 'removeNullAndUndefined', 'convertClassPriority'])
    dependency.initializeDomObject(['publish', 'subscribe'])
    
    window.system.translatePage("table-report-result", "report");

    tableReportResultViewModel = new TableReportResultViewModel();
    ko.applyBindings(tableReportResultViewModel,document.getElementById("table-report-result"));

    window.dom.subscribe("reset-data", function () {
        restReportTable();
    })

    window.dom.subscribe('clear-report', function (){
        restReportTable();
    });

    window.dom.subscribe("table-report-result",function(r){
        restReportTable();

        if(!r.searchData.reportType) {
            r.searchData.reportType = r.reportType;
        }

        let taskData = JSON.parse(r.result.data.tasks);

        if(taskData.length == 0){ 
            window.system.showLoader(false);
            return;
        }

        let config = {};

        tableReportResultViewModel.searchData(r.searchData);

        if(r.reportType != 5){
            config = addExtranalDataToTaskReport(taskData);
        }
        else if(r.reportType == 5){
            config = addExtranalDataToProjectReport(taskData);
        }

        var totalPages = r.result.pagination.lastPage;

        let element = document.getElementById("table-report-result");
        let e = element.getElementsByClassName('pagination')[0]
        
        if (totalPages > 0) {
            window.dom.drawPagination(e, totalPages, config.funcToCall)
        }

        let reportThNameHelper = "task-name";
        if(r.reportType == 5){
            reportThNameHelper = "project-name";
        }
        
        
        tableReportResultViewModel.results(config.result);
        tableReportResultViewModel.reportName(ReportService().getReportTypeName(r.reportType));
        tableReportResultViewModel.thName(window.system.translateWord(reportThNameHelper));
        window.system.showLoader(false);
    });
}

function restReportTable(){
    tableReportResultViewModel.results([])
    let element = document.getElementById("table-report-result");
    let e = element.getElementsByClassName('pagination')[0]
    if($(e).twbsPagination) $(e).removeData("twbs-pagination");
    $(e).attr("pagination-function", "");
}

function fillProjectsReport(pageNumber){
    window.system.showLoader(true, document.getElementById("table-report-result").getElementsByClassName("mytask-card")[0])
    let project = tableReportResultViewModel.searchData().project;
    let status = tableReportResultViewModel.searchData().status;
    project = (obj.project) ? parseInt(obj.project) : -1;
    status = (obj.status) ? obj.status : -1;
  
    let url = ReportService().getUrlToCall(status, project, 'table');
    
    _CordysRequest().serverGetRequest("task/get/"+url+"/" + userId + "/" + project + 
      "/"+pageNumber+"/" + assetConfig.pageSize, function (response) {
        handleTaskReportTableResponse(response)
    });
}

function fillTaskReport(pageNumber){
    window.system.showLoader(true, document.getElementById("table-report-result").getElementsByClassName("mytask-card")[0])
    tableReportResultViewModel.results([]);

    let searchData = ReportService().buildSearchData(tableReportResultViewModel.searchData());
    searchData.pageNumber = pageNumber;

    let reportObject = encodeURIComponent(JSON.stringify(searchData));
    _CordysRequest().serverGetRequest("api/report/get/" +reportObject, function (response) {
        handleTaskReportTableResponse(response)
    });

}

function addExtranalDataToProjectReport(data){
    for(var i = 0; i< data.length; i++){
        data[i].endDate = window.system.convertJavaDate(data[i].endDate);
        data[i].startDate = window.system.convertJavaDate(data[i].startDate);
        data[i].owner = (data[i].userOwner)? data[i].userOwner.displayName : "";
        data[i].ownerLabel = window.system.translateWord("project-owner");
        data[i].extraInfoLabel  = window.system.translateWord("program");
        
        data[i].extraInfo  = (data[i].program)? data[i].program.name: "";
        if(typeof data[i].extraInfo == "object") data[i].extraInfo = (data[i].extraInfo)? data[i].extraInfo.name :"";  
        
        data[i].openCollapse = function(data, event){
            $(event.target).parent().toggleClass("open").next(".fold").toggleClass("open");
        }

        data[i].view = function(obj, event){
            var url = assetConfig.processExprience + "/" + _ConfigURL("projectDisplay") + "?projectId=" + window.system.encrypt(obj.id);
            window.location.href = url;
        }
    }
    return {
        result: data,
        funcToCall: fillProjectsReport
    };
}

function handleTaskReportTableResponse(response){
    if(response.status != 200 ){
        window.system.showLoader(false)
        return;
    }
    let data = JSON.parse(response.response).data;
    if(!data.tasks){
        window.system.showLoader(false)
        return;
    }
    data = JSON.parse(data.tasks);
    if(data && data.length == 0){
        window.system.showLoader(false)
        return;
    }
    tableReportResultViewModel.results(addExtranalDataToTaskReport(data).result);
    window.system.showLoader(false)
}

function addExtranalDataToTaskReport(data){
    
    for(var i = 0; i< data.length; i++){

        data[i] = window.system.removeNullAndUndefined(data[i]);
        data[i].priorityClass = window.system.convertClassPriority(parseInt(data[i].priority))
        data[i].endDate = window.system.convertJavaDate(data[i].dueDate);
        data[i].startDate = window.system.convertJavaDate(data[i].startDate);

        if((data[i].status == 1 || data[i].status == 2) && tableReportResultViewModel.searchData().reportType == 4 && tableReportResultViewModel.searchData().status == -1){
            let todayDate = new Date();
            let dueDate =  new Date(data[i].dueDate);
            let diffDays = parseInt((dueDate - todayDate) / (1000 * 60 * 60 * 24), 10); 
            if(diffDays <= 1) {
                data[i].dueDateClass = "due-date-class";
            }
        }
        if(!data[i].dueDateClass){
            data[i].dueDateClass = "";
        }


        data[i].name = data[i].taskName
        data[i].owner = (data[i].userOwner)? data[i].userOwner.displayName : "";
        data[i].ownerLabel = window.system.translateWord("task-owner");
        data[i].extraInfoLabel  = window.system.translateWord("project");
        data[i].progress = (data[i].progress) ? data[i].progress+"%" : "0%";
        data[i].extraInfo  =data[i].project;
        if(typeof data[i].extraInfo == "object") data[i].extraInfo = (data[i].extraInfo)? data[i].extraInfo.name :"";  
        data[i].openCollapse = function(data, event){
            $(event.target).parent().toggleClass("open").next(".fold").toggleClass("open");
        }

        data[i].view = function(obj, event){
            var url = assetConfig.processExprience + "/" + _ConfigURL("taskDisplay") + "?taskId=" + window.system.encrypt(obj.id+"");
            window.location.href = url;
        }
    }
    return {
        result: data,
        funcToCall: fillTaskReport
    };
}

function TableReportResultViewModel(){
    let self = this;
    this.reportName = ko.observable(window.system.translateWord('report-name'));
    this.thName = ko.observable(window.system.translateWord('name'))
    this.results = ko.observableArray([])
    this.searchData = ko.observable();
    this.exportExcel = function(){

        if(!self.results()) {
            window.system.showMessage("warn", window.system.translateWord("no-data-to-export"))
            return;
        }

        if(!self.searchData() || self.results().length == 0) {
            window.system.showMessage("warn", window.system.translateWord("no-data-to-export"))
            return;
        }
        let obj = self.searchData();
        if(obj.users == ";" || obj.users == "%3B") obj.users = [];
        if(obj.risks == ";" || obj.risks == "%3B") obj.risks = [];
        obj.detectedReportType = 3;

        let reportObject = encodeURIComponent(JSON.stringify(tableReportResultViewModel.searchData()));


        let url = assetConfig.serverURL+"api/report/export/" +reportObject +"/"+ Cordys().getCookie("defaultinst_SAMLart")
        window.open(url, 'Download');
        // _CordysRequest().serverGetRequest("api/report/export/" +reportObject+"/"+ Cordys().getCookie("defaultinst_SAMLart"), function (response) {
        // });
    }
}