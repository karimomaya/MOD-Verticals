let reportGraphControllerViews = {
  "report-graph": "/cordys/html5/demo/TaskManagement/pages/report/views/report-graph.html"
};
let reportGraphControllerStyles = [];
let reportGraphControllerScripts = [
  '/cordys/html5/demo/commons/javascripts/ui/apexcharts.js'
];

var dependency = Dependency.getInstance();
dependency.execute("reportGraphController");
let reportChartGraph = null;

function reportGraphControllerOnload() {
  dependency.initializeDomObject(['publish', 'subscribe', 'drawPagination'])
  dependency.initializeSystemObject(['showLoader', 'getDifferenceBetweenTwoDates', 'encrypt '])


  window.dom.subscribe("report-graph", function (obj) {
    let userId = window.user.details.UserEntityId;

    if (obj.reportType != 4 || obj.reportType != 5) {
      handleReportGraph(obj);
    }else if(obj.reportType == 4){
      // handleTaskInProject(obj, userId);
      handleReportGraph(obj);
    } else if(obj.reportType == 5){
      handleProjectsReport(obj, userId);
    } 
  })

  window.dom.subscribe("reset-data", function (obj) {
    emptyGraphChart(getYearlyAxis())
  })

  window.dom.subscribe('clear-report', function (){
    emptyGraphChart(getYearlyAxis())
});

  renderDefaultGraphValue();
}



function handleProjectsReport(obj, userId){
  let project = (obj.project) ? parseInt(obj.project) : -1;
  let url = ReportService().getProjectUrlToCall(project, 'graph');

  let startDate = obj.startDate; // yyyy-MM-dd
  if (!startDate) startDate = "1920-01-01"
  let endDate = obj.endDate; //yyyy-MM-dd
  if (!endDate) endDate = "2120-12-12";

  _CordysRequest().serverGetRequest("project/get/"+url+"/" + userId + "/" + project + 
    "/"+startDate+ "/"+endDate+"/0/" + assetConfig.pageSize, function (response) {
      let xaxis =  getAxisBasedOnOutputValue(startDate, endDate);

      if (!response.response || response.status != 200) {
        window.system.showLoader(false);
        emptyGraphChart(xaxis)
        return;
      }
      let json = JSON.parse(response.response);
      let data = JSON.parse(json.data)
      let series = [];
      for (var key in data) {
        if(!data[key].projects) continue;
        let name = "";
        for(var i=0; i< data[key].projects.length; i++){
          name+= data[key].projects[i].name+", ";
        }
        name = name.substring(0, name.length - 2);

        series.push({
          name:name,
          data: data[key].data
        })
      }

      updateChartMultipleValue(series, xaxis);

      let result = {
        result: JSON.parse(json.projects),
        reportType: 5,
        searchData: {
          startDate: startDate,
          endDate: endDate,
          project: project
        }
      }
      window.dom.publish("table-report-result", result)

  });
  
}

function handleTaskInProject(obj, userId){
  let users = buildMultipleValueSearch(obj.users);

  let project = (obj.project) ? parseInt(obj.project) : "0";
  let status = (obj.status) ? obj.status : -1;

  let url = ReportService().getUrlToCall(status, project, 'graph');
  
  _CordysRequest().serverGetRequest("api/task/get/task-project/"+users+"/"+project+"/"+status+"/" + 
    assetConfig.pageSize, function (response) {

      window.system.showLoader(false);

      let xaxis = getYearlyAxis();
      
      let reportData = handleReportGraphResponse(response);

      if(!reportData) return;
      updateChartMultipleValue(reportData.graphData, xaxis, ReportService().getReportTypeName(obj.reportType))

      let result = {
        result: reportData.response,
        reportType: 4,
        searchData: {
          project: obj.project,
          status: obj.status,
          users: users
        }
      }
      window.dom.publish("table-report-result", result)

  });
}

function handleRiskTaskReport(obj){
  obj.pageNumber = 0;
  obj.pageSize = assetConfig.pageSize;
  let reportObject = encodeURIComponent(JSON.stringify(obj));
  _CordysRequest().serverGetRequest("api/report/get/" +reportObject, function (response) {

      let xaxis =  getYearlyAxis();
      
      let reportData = handleReportGraphResponse(response);
      if(!reportData) return;
      updateChartMultipleValue(reportData.graphData, xaxis, ReportService().getReportTypeName(obj.reportType))

      let result = {
        result:reportData.response,
        reportType: 6,
        searchData: {
          risks: obj.risks,
          users: obj.users
        }
      }

      window.dom.publish("table-report-result", result)
    });

}


function handleReportGraph(obj) {
  window.system.showLoader(true);
  window.dom.publish("reset-data")
  obj.pageNumber = 0;
  let searchData = ReportService().buildSearchData(obj);
  let reportObject = encodeURIComponent(JSON.stringify(searchData));

  _CordysRequest().serverGetRequest("api/report/get/" + reportObject , function (response) {
    window.system.showLoader(false);
    let reportData = handleReportGraphResponse(response);
    if(!reportData) return;
    let xaxis = null;

    if(obj.reportType == 3){
      xaxis =  getAxisBasedOnOutputValue(obj.startDate, obj.endDate );
    }else {
      xaxis = getYearlyAxis()
    }

    updateChartMultipleValue(reportData.graphData,xaxis )
    
    let result = {
      result: reportData.response,
      reportType: obj.reportType,
      searchData: searchData
    }

    window.dom.publish("table-report-result", result)

  })
}

function getTaskStatusName(status){
  //0:stopped, 1: not started, 2: started, 3: finished 10: draft
  switch(parseInt(status)){
    case 0:
      return window.system.translateWord("stopped")
    case 1:
      return window.system.translateWord("not-started")
    case 2:
      return window.system.translateWord("started")
    case 3:
      return window.system.translateWord("finished")
    case 10:
        return window.system.translateWord("finished")
        
  }
}

function handleReportGraphResponse(response){
  let xaxis = getYearlyAxis()

  if (response.status != 200) {
    emptyGraphChart( xaxis)
    
    window.system.showMessage("error", window.system.translateWord("serverError"))
    return null;
  }

  response = JSON.parse(response.response);
  if(!response.data ){
    emptyGraphChart(xaxis)
    
    window.system.showMessage("warn", window.system.translateWord("no-result-found"))
    return null;
  }

  if(response.data.length == 0 || !response.data.graph){
    emptyGraphChart(xaxis)
    
    window.system.showMessage("warn", window.system.translateWord("no-result-found"))
    return null;
  }

  let data = JSON.parse(response.data.graph)
  if(data.length == 0) {
      emptyGraphChart(xaxis)
      window.system.showMessage("warn", window.system.translateWord("no-result-found"))
      
      return null;
  }
  return {
    response: response,
    graphData: data
  }

}


function buildMultipleValueSearch(users) {
  let u = ";";
  if (users) {
    for (var i = 0; i < users.length; i++) {
      u += users[i] + ";"
    }
  }

  return encodeURIComponent(u);
}

function emptyGraphChart(xaxis){
  updateChartMultipleValue([{
    name: '',
    data: []
  }], xaxis)
}

function getAxisBasedOnOutputValue(startDate, endDate) {

  let differenceBetweenDate = window.system.getDifferenceBetweenTwoDates(new Date(startDate), new Date(endDate));

  let xaxis = [];

  if (differenceBetweenDate >= 0 && differenceBetweenDate <= 8) { // weekly report

    xaxis = getWeeklyAxis();
  }
  else if (differenceBetweenDate > 8 && differenceBetweenDate <= 31) { // monthly report
    xaxis = getMonthlyAxis();
  }
  else if (differenceBetweenDate > 31) { // yearly report
    xaxis = getYearlyAxis();
  }else {
    xaxis = getYearlyAxis();
  }
  return xaxis
}

function updateChartMultipleValue(series, axis) {
  reportChartGraph.updateOptions({
    series: series,
    xaxis: {
      categories: axis,
      minWidth: 0,
      maxWidth: 60,
      minHeight: 0,
      maxHeight: 60,
    }
  })
}

function updateChart(data, axis, reportName) {
  reportChartGraph.updateOptions({
    series: [{
      name: reportName,
      data: data
    }],
    xaxis: {
      categories: axis,
      minWidth: 0,
      maxWidth: 60,
      minHeight: 0,
      maxHeight: 60,
    }
  })
}

function getYearlyAxis() {
  return ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
}

function getMonthlyAxis() {
  return ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"];
}

function getWeeklyAxis() {
  return ["Sun", "Mon", "Tues", "Wed", "Thu", "Fri", "Sat"];
}

function renderDefaultGraphValue() {
  let axis = getYearlyAxis();
  var options = {
    chart: {
      type: 'bar'
    },
    toolbar: {
      show: false,
    },
    series: [],
    xaxis: {
      categories: axis,
      minWidth: 0,
      maxWidth: 60,
      minHeight: 0,
      maxHeight: 60,
    }

  }

  reportChartGraph = new ApexCharts(document.querySelector("#apexchartsfalegwrr"), options);
  reportChartGraph.render();

  window.system.showLoader(false);
}