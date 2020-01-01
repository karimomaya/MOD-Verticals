
const dynamicProgressControllerViews = {
    "progress-bar" : "/cordys/html5/demo/TaskManagement/pages/shared/views/dynamic-progress.html"
}; 
const dynamicProgressControllerStyles = []; 
const dynamicProgressControllerScripts = []; 

var dependency = Dependency.getInstance();
dependency.execute("dynamicProgressController");

let dynamicProgressViewModel = null;

function dynamicProgressControllerOnload(){
    dependency.initializeSystemObject(['getLanguage', 'translatePage', 'translateWord', 'showMessage', 'convertIntPirotityToString']);
    let language = window.system.getLanguage()  /*"ar"*/;
    window.system.translatePage(language, "shared", "TaskManagement", "progress-bar");

    dynamicProgressViewModel = new DynamicProgressViewModel();
    ko.applyBindings(dynamicProgressViewModel, document.getElementById("progress-bar"));


    let dom = Dom(['subscribe']);

    dom.subscribe("dynamic-progress-update-progress", function(progress){
        progress = parseInt(progress);
        if(isNaN(progress)) progress = 0;
        dynamicProgressViewModel.progress(progress);
    })

    dom.subscribe("dynamic-progress", function(obj){
        obj.progress = parseInt(obj.progress);
        if(isNaN(obj.progress)) obj.progress = 0;
        dynamicProgressViewModel.progress(obj.progress);
        dynamicProgressViewModel.haveAction(obj.haveAction); 
        dynamicProgressViewModel.haveStart(obj.haveStart); 
        dynamicProgressViewModel.haveAcquire(obj.haveAcquire);
        dynamicProgressViewModel.haveFinish(obj.haveFinish);
        dynamicProgressViewModel.haveEdit(obj.haveEdit);
        dynamicProgressViewModel.priority(obj.priority);
        dynamicProgressViewModel.entityId(obj.entityId);
        dynamicProgressViewModel.havePriority(obj.havePriority)
    })

    dom.subscribe('restart-task-success', function(singleOrMultiple){
        dynamicProgressViewModel.haveStart(false);
        dynamicProgressViewModel.haveAcquire(false);
        if(singleOrMultiple){
            dynamicProgressViewModel.haveStart(true);
        }else {
            dynamicProgressViewModel.haveAcquire(true);
        }
        
        dynamicProgressViewModel.haveEdit(false);
        dynamicProgressViewModel.progress(0);
        dynamicProgressViewModel.haveFinish(false);
    });

    dom.subscribe('start-task-success', function(){
        dynamicProgressViewModel.haveStart(false);
        dynamicProgressViewModel.haveAcquire(false);
        dynamicProgressViewModel.haveEdit(true);
    });

    dom.subscribe('acquire-task-success', function(){
        dynamicProgressViewModel.haveAcquire(false);
        dynamicProgressViewModel.haveEdit(true);
    });

    dom.subscribe('finish-task-success', function(){
        location.reload(true);
    });
}

function updateProgressInput(event) {
    var key = event.which || event.keyCode;
    let progress = $(event.target).val();
    if (key == 13) { // enter pressed 
        console.log(dynamicProgressViewModel.progress());
        if(progress > 100 || progress < 0) {
            window.system.showMessage("error" , window.system.translateWord("invalidProgress"))
            return;
        }
    }
}

function DynamicProgressViewModel(){
    let self = this;
    let dom = Dom(['publish']);
    this.progress =  ko.observable(0).extend({ 
        required: {params: true, message: window.system.translateWord("requiredField")}
    });
    this.progressPercentage = ko.observable("0%");
    this.progress.subscribe(function(newValue) {
        if(newValue < 0 || newValue > 100) {
            // self.progress(0);
            window.system.showMessage("error", window.system.translateWord("valuenotaccept"))
            return;
        }
        else if(newValue == 100) {
            self.canEdit(false);
            self.haveEdit(false);
            self.haveFinish(true);
        }
        else {
            self.canEdit(false);
            self.haveEdit(true);
            
        }
        let obj = {
            entityId: self.entityId(),
            progress: self.progress()
        }
        if(self.entityId()){
            dom.publish("progress-update", obj)
        }
        

        self.progressPercentage(newValue+"%");
    });
    this.haveAction =  ko.observable(false); 
    this.haveStart =  ko.observable(false); 
    this.haveAcquire =  ko.observable(false);
    this.haveFinish = ko.observable(false);
    this.havePriority = ko.observable(false);
    this.haveEdit = ko.observable(false);
    this.priority = ko.observable();
    this.priority.subscribe(function(newValue) {
        if(!isNaN(parseInt(newValue))) self.priority(window.system.translateWord(window.system.convertIntPirotityToString(newValue)));
    });
    this.entityId = ko.observable();

    this.canEdit = ko.observable(false);

    this.edit = function() {
        self.canEdit(true);
        self.haveEdit(false);
    }

    this.submitEdit = function(){
        self.canEdit(false);
        self.haveEdit(true);
    }

    this.start = function() {
        dom.publish("start", self.entityId());
    } 
    this.acquire = function() {
        window.dom.publish('start', self.entityId());
        // dom.publish("acquire", self.entityId())
    } 

    this.finish = function() {
        dom.publish("finish", self.entityId())
    }
}

function goBack(){
    window.history.back();
}
