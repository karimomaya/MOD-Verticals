let taskHistoryControllerViews = {
    "task-history" : "/cordys/html5/demo/TaskManagement/pages/task/views/task-history.html",
};
let taskHistoryControllerStyles = [];
let taskHistoryControllerScripts = [];

var dependency = Dependency.getInstance();
dependency.execute("taskHistoryController");

function taskHistoryControllerOnload() {
    dependency.initializeSystemObject(['translatePage', 'showLoader', 'translateParag','showMessage' ]);
        
    dependency.initializeDomObject(['subscribe', 'publish']);

    taskHistoryViewModel = new TaskHistoryViewModel();
    ko.applyBindings(taskHistoryViewModel, $("#task-history")[0]);

    window.dom.subscribe('task-id', function(taskId){
        getTaskHistory(taskId);
    });
    

}

function getTaskHistory(taskId){
    TaskModel().getTaskHistoryByTaskId(taskId).done(function(response){
        if(!response.tuple[0]) return
        response = response.tuple[0].old.rowset.row;
        let obj = [];

        if (response instanceof Array) {
            for (let i = 0; i < response.length; i++) {
                response[i].actionClass = "athurize-box-status-approv";
                response[i].actionDate = response[i].actionDate.replace("T", " ").replace(".0", "");
                response[i].actionName = window.system.translateWord(response[i].actionName);
                response[i].haveDescription = false;
                response[i].description = "";
                console.log(response[i].fromUser)
                if(response[i].fromUser) {
                    console.log(response[i].fromUser)

                    response[i].actionClass = "athurize-box-status-reject";
                    response[i].haveDescription = true; 
                    response[i].description = window.system.translateParag(response[i].actionName + " from " +response[i].fromUser + " to " + response[i].toUser)
                   
                }
                response[i].openCollapse = function(obj, event){
                    $(event.target).parent().toggleClass("open").next(".fold").toggleClass("open");
                }
                // "تم تحويل المهمة من إلي "
                obj.push(response[i])
            }
        } else {
            response.actionClass = "athurize-box-status-approv";
            response.actionName = window.system.translateWord(response.actionName);
            response.description = "";
            response.haveDescription = false;
            if(response.fromUser) {
                response.actionClass = "athurize-box-status-reject";
                response.haveDescription = true;
                response.description = window.system.translateParag(response.actionName + " from " +response.fromUser + " to " + response.toUser)
                console.log(response.description)
            }
            response.openCollapse = function(obj, event){
                $(event.target).parent().toggleClass("open").next(".fold").toggleClass("open");
            }
            obj.push(response)
            
        }
        taskHistoryViewModel.taskHistories(obj)

        console.log(response);
    }).fail(function(error){
        window.system.handleError(error);
    })
}

function TaskHistoryViewModel(){
    this.taskId = 0;
    this.taskHistories = ko.observableArray();
}
