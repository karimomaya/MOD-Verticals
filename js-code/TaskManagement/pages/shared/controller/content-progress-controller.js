const contentProgressControllerViews = {
    "content-progress" : "/cordys/html5/demo/TaskManagement/pages/shared/views/content-progress.html"
}; 
const contentProgressControllerStyles = [
]; 
const contentProgressControllerScripts = [
]; 

let contentProgressModel = null;

var dependency = Dependency.getInstance();
dependency.execute("contentProgressController");

function contentProgressControllerOnload() {
    var system = System();
    let language = system.getLanguage()  /*"ar"*/;
    system.translatePage(language, "shared", "TaskManagement", "content-progress");
    contentProgressModel = new contentProgressObject();
    ko.applyBindings(contentProgressModel, document.getElementById("content-progress")); 
}

function updateContentProgress(obj , pirority, taskStatus){
    contentProgressModel.pirority(System().translateWord(pirority));
    
    obj.progress = obj.progress || 0; // if progress is null initialize it to 0

    let progress = parseInt(obj.progress);

    contentProgressModel.progress(progress);
    contentProgressModel.progressPercentage(progress+"%")
    contentProgressModel.editablePerson(obj.isEditable);
    contentProgressModel.targetTaskId(obj.targetTaskId);
    contentProgressModel.singleOrMultiple(obj.singleOrMultiple);
    contentProgressModel.status(parseInt(obj.targetTaskstatus));

    if(!obj.isEditable) {// owner priveliage
        $("#btnProgressEdit").remove(); 
        System().deleteMethods([handleEditProgress, handleStartTask, handleFinishTask, handleInputTextStart]) ;
        return;
    }

    if(parseInt(taskStatus) == 3) document.getElementById("btnProgressEdit").parentElement.removeChild(document.getElementById("btnProgressEdit"))
    else if(obj.singleOrMultiple == false && contentProgressModel.status() == 0)  handleInputTextStart("acquire")
    else if(contentProgressModel.status() == 0)  handleInputTextStart("start")
    else if ( (contentProgressModel.progress() == 100 || contentProgressModel.progress() == "100") && contentProgressModel.status() == 1) handleInputTextStart("finish")
    
}

function handleStartTask(){
    TaskModel().startTask(contentProgressModel.targetTaskId()).done(function(response){
        contentProgressModel.status(1)
        handleInputTextStart("edit")
        // let system =  System();
        // system.showMessage("success" , system.translateWord("taskStart"))
        location.reload(true);
    })
}

function handleInputTextStart(name){
    $("#inputTextStart").html(System().translateWord(name))
    $("#inputTextStart").attr("translate-text", name)
    let icon = "";
    switch(name){
        case 'edit': 
            icon = "icon-Edit";
            break;
        case 'start' :
            icon = "icon-Clock";
            break;
        case "acquire": 
            icon = "icon-Approved";
            break;
        case "finish" :
            icon = "icon-OnOff";
            break;
        default:
            icon = "";
    }
    $('span:first', $("#inputTextStart").parent()).prop('class', icon);
}

function handleEditProgress(e){ // one button with one name handle multiple behavior 
    if(contentProgressModel.status() == 0) { // button name is start because status is 0
        handleStartTask();
    }
    else if(contentProgressModel.progress() == 100  ){ // button name is finished because status is 100
        handleFinishTask();
    }
    else if(contentProgressModel.status() < 2) { // no button name because status is less than 2
        $("#btnProgressEdit").addClass("force-hide");
        $("#txtProgress").removeClass("force-hide");
    }
    
}

function handleFinishTask(){
    TaskModel().endTask(contentProgressModel.targetTaskId()).done(function(response){
        $("#btnProgressEdit").remove();
        let system = System();
        system.showMessage("success" , system.translateWord("taskFinish"))
        window.history.back();
    })
}

function updateProgress(event){
    var key = event.which || event.keyCode;
    if (key == 13) { // enter pressed 
        var progress = $("#progressTxt").val();
        if(progress > 100 || progress < 0) {
            let system = System();
            system.showMessage("error" , system.translateWord("invalidProgress"))
            return;
        }

        contentProgressModel.progress($("#progressTxt").val())
        let system = System();
        TaskModel().updateProgress(contentProgressModel.targetTaskId(), contentProgressModel.progress()).done(function(response){
            $("#btnProgressEdit").removeClass("force-hide");
            $("#txtProgress").addClass("force-hide")
            contentProgressModel.progressPercentage(contentProgressModel.progress() + "%")
            
            system.showMessage("success" , system.translateWord("updateProgress"))
            if(contentProgressModel.progress() == 100 )
                handleInputTextStart("finish")
        }).fail(function(error){
            system.handleError(error);
        });
       
    }
}

function contentProgressObject(){
    self = this;
    this.pirority =  ko.observable();
    this.progress =  ko.observable();
    this.targetTaskId = ko.observable();
    this.editablePerson = ko.observable();   
    this.progressPercentage = ko.observable();
    this.status = ko.observable();
    this.singleOrMultiple = ko.observable();
	this.taskNotFinish = ko.observable(true);
}

