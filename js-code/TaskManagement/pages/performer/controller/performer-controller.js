let performerControllerViews = {
    "task-performer" : "/cordys/html5/demo/TaskManagement/pages/performer/views/performer.html",
    "performer-reasign" : "/cordys/html5/demo/TaskManagement/pages/performer/views/performer-reasign.html"
}; 
let performerControllerStyles = [
    '/cordys/html5/demo/commons/styles/selectize.css'
]; 
let performerControllerScripts = [
    '/cordys/html5/demo/commons/javascripts/selectize-index.js',
    '/cordys/html5/demo/commons/javascripts/selectize.min.js',
    "/cordys/html5/demo/TaskManagement/pages/performer/model/performer-model.js",
    "/cordys/html5/demo/TaskManagement/pages/task/model/task-model.js"
]; 
let performerControllerLang = [
    '/cordys/html5/demo/TaskManagement/pages/performer/resources/language-pack/performer-' + window.lang + '.json'
]; 

var dependency = Dependency.getInstance();
dependency.execute("performerController");
let performerObject = {
    performerViewModel: null,
    model : null
}

function performerControllerOnload() {
    dependency.initializeSystemObject(['translatePage', 'showLoader',
        'translateWord', 'extendSelectize', 'showMessage', 'removeNullAndUndefined',
        'handleError']);

    dependency.initializeDomObject([ 'subscribe', 'publish']);

    window.system.translatePage('task-performer', 'task-performer')
    window.system.translatePage('performer-reasign', 'performer-reasign')


    performerObject.model = PerformerModel(['readPerformers', 'updatePerformer', 'returnTask', 'deletePerformers',
        'getSubUsers', 'getTargetPerformerDetailsByPerformerId'])
    performerObject.performerViewModel = new PerformerViewModel();
    ko.applyBindings(performerObject.performerViewModel,$("#task-performer-table")[0]);

    targetViewModel = new TargetViewModel();
    window.system.extendSelectize();
    KOValidation().initializeValidation(targetViewModel);
    ko.applyBindings(targetViewModel,$("#performer-reasign")[0]);

    window.dom.subscribe("task-performers", function(obj){
        performerObject.performerViewModel.taskId(obj.taskId);
        performerObject.performerViewModel.isOwner(obj.isOwner);
        performerObject.performerViewModel.taskStatus(obj.taskStatus);
        performerObject.performerViewModel.ownerId(obj.taskOwnerId)
        performerObject.performerViewModel.createdById(obj.taskCreatedById)
    });

    window.dom.subscribe("onclick-task-performers", function(){
        drawPerformers();
    })
}




function drawPerformers(){
    performerObject.model.readPerformers(performerObject.performerViewModel.taskId(), 1, 100).done(function(response){
        if(!response.tuple[0])  return;
        response = response.tuple[0].old.rowset.row;
        if (response instanceof Array) {
            for (let i=0; i<response.length; i++){
                response[i].progress = (response[i].progress)? response[i].progress : 0;
                response[i] = addAdditionalAttributesToPerformerResponse(response[i]);
                response[i].position = i;
            }
        }
        else {
            response.progress = (response.progress)? response.progress : 0;
            response = addAdditionalAttributesToPerformerResponse(response)
            response.position = 0;
        }
        performerObject.performerViewModel.performers(response);
        console.log(response);

    }).fail(function (error){
        window.system.handleError(error);
    });
}


function addAdditionalAttributesToPerformerResponse(response) {
    response.singleOrMultiple = ( response.singleOrMultiple == "false")? false : true; // multiple: true, single: false
    
    let isAcquired = response.isAcquired;
    response.isAcquired = true;

    if(!response.singleOrMultiple && isAcquired == "0") response.isAcquired = false; 

    response.progress = response.progress + "%";
    
    response.isOwner = false;
    response.canReassign = false;
    response.canReturn = ko.observable(false);
    // task status: 0:stopped, 1: not started, 2: started, 3: finished 10: draft, 11: obselate
    let taskStatus = parseInt(performerObject.performerViewModel.taskStatus());
    console.log(taskStatus)
    response.status = parseInt(response.status);
    response.type = parseInt(response.type);

    if(taskStatus != 3 && taskStatus != 0 && taskStatus != 11 &&  performerObject.performerViewModel.isOwner()){
        response.isOwner = true;
        if(response.status == 2 ){
            response.canReturn(true);
        }
        if(response.type == 0){
            response.canReassign = true;
        }
    }
    
    response.reassign = function(e) {
       openReassignTask(e);
    }
    response.returnTask = function(e){
        performerObject.model.returnTask(e.Id).done(function(response){
            window.system.showMessage("success", window.system.translateWord("return-task-success"));
            e.canReturn(false)
        }).fail(function(error){
            window.system.handleError(error);
        })
    }
    response.isCollapse =  (parseInt(response.type) != 0 )? true: false; // if type user
    response.targets = ko.observableArray([]);
    response.openCollapse = function(e, event){
        getTargetPerformerDetailsByPerformerId(e, event.target);
    }
    response.deletePerformer = function(e){
        deletePerformer(e.performerId);
    }
    return response;
}

function deletePerformer(performerEntityId) {
    window.system.showLoader(true, document.getElementById("performers"));
    
    performerObject.model.deletePerformers(performerEntityId,performerObject.performerViewModel.taskId()).done(function(response){
        window.system.showLoader(false);
        window.system.showMessage("success", window.system.translateWord("deleteSuccess"))
        window.dom.publish('dynamic-progress-update-progress',response.success.text);
        drawPerformers();
        
    }).fail(function(error){
        window.system.handleError(error);
    })
}


function getTargetPerformerDetailsByPerformerId(obj, element) {
    performerObject.model.getTargetPerformerDetailsByPerformerId(obj.Id).done(function(response){
        $(element).parent().toggleClass("open").next(".fold").toggleClass("open");

        if(!response.tuple[0])  return;

        response = response.tuple[0].old.rowset.row;
        if (response instanceof Array) {
            for (let i=0; i<response.length; i++){
                response[i] = addAdditionalAttributesToTargetResponse(response[i]);
                response[i].position = i;
            }
        }
        else {
            response = addAdditionalAttributesToTargetResponse(response);
            response.position = 0;
        }
        obj.targets(response);

    }).fail(function(error){
        window.system.handleError(error);
    });

}

function addAdditionalAttributesToTargetResponse(response) {
    let isAcquired = response.isAcquired;
    response.isAcquired = true;

    if(!response.singleOrMultiple && isAcquired == "0") response.isAcquired = false; 
    
    let canReturn = false;
    response.status = parseInt(response.status)
    let taskStatus = parseInt(performerObject.performerViewModel.taskStatus)
    console.log(taskStatus);
    
    if(taskStatus != 3 && taskStatus != 0 && taskStatus != 11 &&  performerObject.performerViewModel.isOwner()){
        if(response.status == 2 ){
            canReturn = true;
        }
    }
    
    response.canReturnToTarget = ko.observable(canReturn);

    response.returnToTargetTask = function(e){
        performerObject.model.returnTask(e.Id).done(function(){
            window.system.showMessage("success", window.system.translateWord("return-task-success"));
            e.canReturnToTarget(false)
        }).fail(function(error){
            window.system.handleError(error);
        })
    }
    response.progress = response.progress + "%";
    return response;
}

function openReassignTask(e) {
    $('#performer-reasign').modal();
    let performerId = e.performerId;
    let targetPerformerEntityId = e.Id;
    autocompletePerformerReasign("", e.displayName, e.performerId);
    targetViewModel.performerEntityId(targetPerformerEntityId);
    targetViewModel.oldPerformerId(performerId);
}

function autocompletePerformerReasign(input, displayName, performerId, pageNumber, pageSize) {
    pageNumber = pageNumber || 1;
    pageSize = pageSize || assetConfig.autocompleteSize

    TaskModel().getOwners(input, false, null, pageNumber, pageSize).done(function(response){
        let ownerObject = [];
        if(!response.tuple[0]) return;
        
        let selected = performerId;

        let arrayOfSelected = [selected];

        let selectedUsers = performerObject.performerViewModel.performers();
        for (var i= 0; i < selectedUsers.length; i++) {
            selectedUser = selectedUsers[i].performerId;
            arrayOfSelected.push(selectedUser)
        }

        var results = response.tuple[0].old.rowset.row;
        
        if (results instanceof Array) {
            for (let i=0; i<results.length; i++){
                let id = results[i].UserEntityId ;
                if(arrayOfSelected.indexOf(id) == -1 && id != performerObject.performerViewModel.ownerId() && id != performerObject.performerViewModel.createdById()) ownerObject.push({id: id+":"+results[i].DisplayName, name : results[i].DisplayName})
            }
        }
        else {
            let id = results.UserEntityId ;
            if(arrayOfSelected.indexOf(id) == -1&& id != performerObject.performerViewModel.ownerId() && id != performerObject.performerViewModel.createdById()) ownerObject.push({id: id +":"+results.DisplayName, name : results.DisplayName})
        }
        targetViewModel.taskUsersOptions(ownerObject);
        
        if(displayName) {
            targetViewModel.taskUsersOptions.push({id: performerId +":"+displayName, name : displayName})
            targetViewModel.taskUsers([performerId +":"+displayName]);
        }

    }).fail(function(error){
        window.system.handleError(error)
    });

    /*performerObject.model.getSubUsers(window.user.details.UserEntityId, input, start, end).done(function(response){
        let ownerObject = [];
        if(!response.tuple[0]) return;
        
        let selected = performerId;

        let arrayOfSelected = [selected];

        let selectedUsers = performerObject.performerViewModel.performers();
        for (var i= 0; i < selectedUsers.length; i++) {
            selectedUser = selectedUsers[i].performerId;
            arrayOfSelected.push(selectedUser)
        }

        var results = response.tuple[0].old.rowset.row;
        
        if (results instanceof Array) {
            for (let i=0; i<results.length; i++){
                let id = results[i].UserEntityId ;
                if(arrayOfSelected.indexOf(id) == -1) ownerObject.push({id: id+":"+results[i].DisplayName, name : results[i].DisplayName})
            }
        }
        else {
            let id = results.UserEntityId ;
            if(arrayOfSelected.indexOf(id) == -1) ownerObject.push({id: id +":"+results.DisplayName, name : results.DisplayName})
        }
        targetViewModel.taskUsersOptions(ownerObject);
        
        if(displayName) {
            targetViewModel.taskUsersOptions.push({id: performerId +":"+displayName, name : displayName})
            targetViewModel.taskUsers([performerId +":"+displayName]);
        }

    });*/
}



function TargetViewModel() {
    // let system = System(["translateWord", "showLoader",'handleError', 'showMessage']);
    let self = this;
    self.hasError = ko.observable(true);
    self.comment = ko.observable().extend({ 
        required: {params: true, message: window.system.translateWord("requiredField")}
    });

    self.performerId = ko.observable();
    self.oldPerformerId = ko.observable();
    self.performerEntityId = ko.observable();
    self.displayName = ko.observable();
    self.taskUsersOptions = ko.observableArray();
    self.taskUsers = ko.observable().extend({ 
        required: {params: true, message: window.system.translateWord("requiredField")}
    });
    self.update = function() {
        window.system.showLoader(true);
        self.hasError = ko.observable(true);
        
        let newPerformer = (targetViewModel.taskUsers() instanceof Array) ? targetViewModel.taskUsers()[0] : targetViewModel.taskUsers(); 
        targetViewModel.performerId(newPerformer.split(":")[0]);
        targetViewModel.displayName(newPerformer.split(":")[1]);
        
        performerObject.model.updatePerformer(targetViewModel).done(function(response) {
            $("#performer-close").click();
            drawPerformers();
            window.system.showLoader(false);
            $("#note-nav").click();
            window.system.showMessage("success", window.system.translateWord("updateSuccess"));
        }).fail(function(error) {
            window.system.handleError(error);
            window.system.showLoader(false);
        });
    }
    self.close = function() { }
}

function PerformerViewModel() {
    this.taskId = ko.observable();
    this.isOwner = ko.observable(false);
    this.performers = ko.observableArray([]);
    this.taskStatus = ko.observable();
    this.ownerId = ko.observable();
    this.createdById = ko.observable();
}