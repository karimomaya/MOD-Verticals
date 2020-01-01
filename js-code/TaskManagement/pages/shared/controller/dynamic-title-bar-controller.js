
const dynamicTitleBarControllerViews = {
    "title-bar" : "/cordys/html5/demo/TaskManagement/pages/shared/views/dynamic-title-bar.html"
}; 
const dynamicTitleBarControllerStyles = []; 
const dynamicTitleBarControllerScripts = []; 

var dependency = Dependency.getInstance();
dependency.execute("dynamicTitleBarController");

let dynamicTitleViewModel = null;

function dynamicTitleBarControllerOnload(){
    dependency.initializeSystemObject(["translatePage", "getLanguage", 'historyPop', 'showMessage'])
    let language = window.system.getLanguage()  /*"ar"*/;
    window.system.translatePage(language, "shared", "TaskManagement", "title-bar");

    dynamicTitleViewModel = new DynamicTitleViewModel();
    ko.applyBindings(dynamicTitleViewModel, document.getElementById("title-bar")); 
    let dom = Dom(['subscribe'])
    
    dom.subscribe("go-back-dynamic-title", function(){
        goBackDynamicTitle();
    });

    dom.subscribe("start-task-success", function(){
        dynamicTitleViewModel.haveAction(true); 
        dynamicTitleViewModel.canRestart(true)
    })

    dom.subscribe("restart-task-success", function(){
        
        dynamicTitleViewModel.haveEdit(false); 
        dynamicTitleViewModel.haveDelete(false);
        dynamicTitleViewModel.haveEnd(false);
        dynamicTitleViewModel.haveComplete(false); 
        dynamicTitleViewModel.haveStop(false)
        dynamicTitleViewModel.canRestart(false);
        dynamicTitleViewModel.haveAction(false); 
    })

    dom.subscribe("dynamic-title", function(obj){
        obj.haveReport = obj.haveReport || false;
        dynamicTitleViewModel.title(obj.title);
        dynamicTitleViewModel.haveAction(obj.haveAction); 
        dynamicTitleViewModel.haveEdit(obj.haveEdit); 
        dynamicTitleViewModel.haveDelete(obj.haveDelete);
        dynamicTitleViewModel.haveEnd(obj.haveEnd);
        dynamicTitleViewModel.entityId(obj.entityId);
        dynamicTitleViewModel.haveStop(obj.haveStop); 
        dynamicTitleViewModel.haveComplete(obj.haveComplete); 
        dynamicTitleViewModel.canRestart(obj.canRestartTask);
        dynamicTitleViewModel.haveReport(obj.haveReport);
    })

    dom.subscribe("complete-success", function(){
        dynamicTitleViewModel.haveComplete(false); 
        dynamicTitleViewModel.haveStop(true); 
    }) 

    dom.subscribe("stop-success", function(){
        dynamicTitleViewModel.haveComplete(true); 
        dynamicTitleViewModel.haveStop(false); 
    }) 

    dom.subscribe("end-success", function(){
        dynamicTitleViewModel.haveAction(false); 
        dynamicTitleViewModel.haveEdit(false); 
        dynamicTitleViewModel.haveDelete(false);
        dynamicTitleViewModel.haveEnd(false);
        dynamicTitleViewModel.haveComplete(false); 
        dynamicTitleViewModel.haveStop(false)
        dynamicTitleViewModel.canRestart(false)
        dynamicTitleViewModel.entityId("");
    });
}

function DynamicTitleViewModel(){
    let self = this;
    let dom = Dom(["publish"]);
    this.title =  ko.observable();
    this.haveAction =  ko.observable(false); 
    this.haveEdit =  ko.observable(false); 
    this.haveStop =  ko.observable(false); 
    this.haveComplete =  ko.observable(false); 
    this.haveDelete =  ko.observable(false);
    this.haveEnd = ko.observable(false);
    this.canRestart = ko.observable(false);
    this.haveReport = ko.observable(false);
    this.entityId = ko.observable();

    this.editBtn = function() {
        dom.publish("edit", self.entityId())
    } 
    this.stopBtn = function() {
        dom.publish("stop", self.entityId())
    } 
    this.completeBtn = function() {
        dom.publish("complete", self.entityId())
    } 
    this.deleteBtn = function() {
        dom.publish("delete", self.entityId())
    } 
    this.endBtn = function() {
        dom.publish("end", self.entityId())
    }
    this.reportBtn = function() {
        dom.publish("filter-button-clicked", self.entityId())
    }
    this.restart = function() {
        dom.publish("restart", self.entityId())
    }
    
}

function goBackDynamicTitle(){
    window.history.back();
    // var iframes = parent.parent.parent.parent.document.getElementsByTagName("iframe");
    // let url = window.system.historyPop()
    // if(url){
    //     iframes[1].src = url;
    // }
    // else {
    //     window.system.showMessage("warn", window.system.translateWord("noturlfound"))
    // }
    
}
