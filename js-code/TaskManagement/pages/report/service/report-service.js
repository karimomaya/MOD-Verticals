function ReportService(){
    return {
        getReportTypeName   : getReportTypeName,
        getUrlToCall        : getUrlToCall,
        getProjectUrlToCall : getProjectUrlToCall,
        buildSearchData     : buildSearchData
    }

    function buildSearchData(obj) {
        let startDate = obj.startDate; // yyyy-MM-dd
        if (!startDate) startDate = "1920-01-01"
        let endDate = obj.endDate; //yyyy-MM-dd
        if (!endDate) endDate = "2120-12-12";
        let projects = [];
        let status = -1;
        if(obj.reportType == 4) {
            if(obj.project) {
                if(obj.project instanceof Array){
                    projects = (obj.project.length == 0)? []: obj.project
                }else {
                    obj.project = parseInt(obj.project);
                    projects = (isNaN(project))? [] : [parseInt(project)];
                }
      
            }
            status = (obj.status) ? obj.status : -1;
        }
        
        return {
            startDate: startDate,
            endDate: endDate,
            pageSize: assetConfig.pageSize,
            pageNumber: obj.pageNumber,
            users: obj.users,
            reportType: obj.reportType,
            projects: projects,
            status: status
        }
    }

    function getProjectUrlToCall(project, type){
        let url = "";
         if(project == -1){ // if have status and not project
            url = "projects-report-"+type;
        } else {
            url = "project-report-"+type;
        }
        return url;
    }

    function getUrlToCall(status, projectId, type){
        
        let url = "";
        if(status== 1 && projectId  > 0){ // if have status: 1 and have project
            url = "delayed-task-in-project-"+type;
        } else if(status== 2 && projectId  > 0) { // if have status: 2 and have project
            url = "completed-task-in-project-"+type;
        } else if(status == 1 && projectId == -1){ // if have status and not project
            url = "delayed-task-"+type;
        } else if(status == 2 && projectId == -1){ // if have status and not project 
            url = "completed-task-"+type;
        } else if(status == -1 && projectId == -1){ // if not have status and not project 
            url = "task-"+type;
        } else if(status == -1 && projectId > 0) { // if not have status and have project
            url = "task-inproject-"+type;
        }
        return url;
    }

    

    function getReportTypeName(type){
        switch(type){
            case 1:
                return window.system.translateWord('delayedTaskReport');
            case 2:
                return window.system.translateWord('userProductivityReport');
            case 3:
                return window.system.translateWord('finishedTaskReport');
            case 4:
                return window.system.translateWord('projectFinishedDelayedReport');
            case 5:
                return window.system.translateWord('projectReportInDate');
            case 6:
                return window.system.translateWord('taskAssociatedWithSpecificChallend');
            case 10:
                return window.system.translateWord('risks-report');
            default:
                    return '';
        }
    }
}