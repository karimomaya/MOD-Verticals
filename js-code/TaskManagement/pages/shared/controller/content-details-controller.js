const contentDetailsControllerViews = {
    "content-details" : "/cordys/html5/demo/TaskManagement/pages/shared/views/content-details.html"
}; 
const contentDetailsControllerStyles = []; 
const contentDetailsControllerScripts = [
    
]; 

var dependency = Dependency.getInstance();
dependency.addToTrigger("contentDetailsControllerOnReady");
dependency.execute("contentDetailsController");

function contentDetailsControllerOnload(){
    var system = System();
    let language = system.getLanguage()  /*"ar"*/;
    system.translatePage(language, "shared", "TaskManagement", "title-bar");
}

function contentDetailsControllerOnReady(){

}


function removeBiniding(id){
    ko.cleanNode(document.getElementById(id));
}

function updateContentDetails(controllerPath){
    if(controllerPath) dependency.addScriptToHeader(controllerPath);
}
