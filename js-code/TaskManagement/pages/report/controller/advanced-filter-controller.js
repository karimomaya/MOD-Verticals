let advancedFilterControllerViews = {
    "advanced-filter" : "/cordys/html5/demo/TaskManagement/pages/report/views/advanced-filter.html"
};
let advancedFilterControllerStyles = [];
let advancedFilterControllerScripts = [
    "/cordys/html5/demo/commons/javascripts/jquery.twbsPagination.js",
    "/cordys/html5/demo/TaskManagement/pages/report/service/report-service.js"
];

let advancedFilterControllerLang = [
    "/cordys/html5/demo/TaskManagement/pages/report/resources/language-pack/report-"+window.lang+".json"
];

var dependency = Dependency.getInstance();
dependency.execute("advancedFilterController");

let filterViewModel = null;

function advancedFilterControllerOnload() {
    dependency.initializeSystemObject(['getLanguage', 'translatePage', 'showLoader', 'translateWord'])
    dependency.initializeDomObject(['publish', 'subscribe'])
    
    let language = window.system.getLanguage()  /*"ar"*/;
    window.system.translatePage(language, "report", "TaskManagement", "title-bar");

    filterViewModel = new FilterViewModel();
    ko.applyBindings(filterViewModel,document.getElementById("advanced-filter"));
    
    window.dom.subscribe('advanced-filter', function(model){
        let obj = [];

        obj.push(addReportType(parseInt(model.reportType()[0])));

        if(model.reportType() == 2 || model.reportType() == 3 || model.reportType() == 5 || model.reportType() == 10){
            obj = addStartAndEndDate(model, obj)
        } 
        if(model.reportType() == 4 || model.reportType() == 5 ){
            obj = addProjectToFilter(model.project(), obj)
        }

        if(model.reportType() == 1 ||model.reportType() == 2 ||  model.reportType() == 3 || 
                model.reportType() == 4 || model.reportType() == 6 || model.reportType() == 10){
            obj = addUsersToFilter(model, obj)
        }
        
        if(model.reportType() == 4 ||  model.reportType() == 10){
            obj = addStatusToFilter(model.statusType(), obj)
        }

        if(model.reportType() == 6  ||  model.reportType() == 10){
            obj = addRiskToFilter(model.riskName(), obj);
        }
        
        console.log(obj)
        filterViewModel.filters(obj);
        
        window.dom.publish("report-graph", filterViewModel.selectedOptions());

    });
   
}

function addRiskToFilter(risk, obj){
    if(!risk ) return obj;
    if( risk.length == 0) return obj;

    let tempRisksObject = [];

    for (var i= 0; i<risk.length; i++){
        tempRisksObject.push(risk[i].split(":")[0]);
        obj.push( {
            name:risk[i].split(":")[1], 
            index:  obj.length,
            riskPosition: i,
            type: "risks",
            remove: function(obj, event){
                removeOptionsFromFilters(obj, event.target)
            }
        });
    }

    addOptionToSelectedOptions('risk', tempRisksObject)

    return obj;
}

function addStatusToFilter(status, obj){
    if(!status) return obj;
    if(status instanceof Array){
        if(status.length == 0) return obj;
    }else {
        status = [status]
    }
    if(status[0] == "") return obj;
    addOptionToSelectedOptions('status', parseInt(status[0]))
    obj.push({
        name: getStatusTypeName(parseInt(status[0])),
        index:  obj.length,
        type: "status",
        remove: function(obj, event){
            removeOptionsFromFilters(obj, event.target);
            
        }
    })
    return obj;
}

function addProjectToFilter(project, obj){
    if(!project || project.length == 0) return obj;
    if(project == "") return obj;
    addOptionToSelectedOptions('project', project[0].split(":")[0])
    obj.push({
        name: project[0].split(":")[1],
        index:  obj.length,
        type: "projects",
        remove: function(obj, event){
            removeOptionsFromFilters(obj, event.target)
        },
    })
    return obj;
}

function addUsersToFilter(model, obj){
    if(!model.user()) return obj;
    if( model.user().length == 0) return obj;

    let tempUsersObject = [];
    for (var i= 0; i< model.user().length; i++){
        tempUsersObject.push(model.user()[i].split(":")[0]);
        obj.push( {
            name: model.user()[i].split(":")[1], 
            index:  obj.length,
            userPosition: i,
            type: "users",
            remove: function(obj, event){
                removeOptionsFromFilters(obj, event.target)
            }
        });
    }
    addOptionToSelectedOptions('users', tempUsersObject)
    return obj;
}


function addStartAndEndDate(model, obj){
    if(model.startDate()){
        addOptionToSelectedOptions('startDate', model.startDate())
        obj.push( {
            name: model.startDate(),
            index:  obj.length,
            type: "startDate",
            remove: function(obj, event){
                removeOptionsFromFilters(obj, event.target)
            }
        });
    } 
    if(model.endDate()){
        addOptionToSelectedOptions('endDate', model.endDate())
        obj.push( {
            name: model.endDate(), 
            index:  obj.length ,
            type: "endDate",
            remove: function(obj, event){
                removeOptionsFromFilters(obj, event.target)
            }
        });
    }
    return obj;
}



function addReportType(type){
    filterViewModel.selectedOptions({})
    addOptionToSelectedOptions('reportType', type)
    return {
        name: ReportService().getReportTypeName(type), 
        index: 0,
        type: "reportType",
        remove: function(obj, event){
            filterViewModel.filters([])
            window.dom.publish("reset-data", true);
        }
    };
    
}

function removeOptionsFromFilters(obj, element){
    
    filterViewModel.filters().splice(obj.index, 1);
    if(obj.type == "users"){
        let users = filterViewModel.selectedOptions()['users'];
        if(users.length > 1){
            filterViewModel.selectedOptions()['users'].splice(obj.userPosition, 1);
        } else {
            delete filterViewModel.selectedOptions()[obj.type];
        }
        
    } else if(obj.type == "risk"){
        let risk = filterViewModel.selectedOptions()['risk'];
        if(risk.length > 1){
            filterViewModel.selectedOptions()['risk'].splice(obj.riskPosition, 1);
        } else {
            delete filterViewModel.selectedOptions()[obj.type];
        }
    }
    else {
        delete filterViewModel.selectedOptions()[obj.type];
    }

    $(element).closest('div').remove();

    window.dom.publish("report-graph", filterViewModel.selectedOptions());
    window.dom.publish("unbind-report-modal", true)
}

function addOptionToSelectedOptions(key, value){
    let tempObj = filterViewModel.selectedOptions()
    tempObj[key] = value;
    filterViewModel.selectedOptions(tempObj)
}


function getStatusTypeName(type){
    switch(type){
        case 1:
            return window.system.translateWord('delayed');
        case 2:
            return window.system.translateWord('finished');
        case 10:
            return window.system.translateWord('late');
        case 11:
            return window.system.translateWord('closed');
        case 11:
            return window.system.translateWord('opened');
        default:
            return '';
    }
}


function FilterViewModel(){

    this.filterClick = function(){
        window.dom.publish('filter-button-clicked', true);
    }
    this.filters = ko.observableArray();
    this.selectedOptions = ko.observable({});
}