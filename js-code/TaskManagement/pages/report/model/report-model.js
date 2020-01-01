function ReportModel(){
    return {
        getProgramProgressReport : getProgramProgressReport,
        getProjectProgressReport : getProjectProgressReport,
        getTaskProgressReport     : getTaskProgressReport
    }

    function getProgramProgressReport(){
        let obj={
            method: "MOD_TM_SP_task_GetProgramProgressReport",
            namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetProgramProgressReport",
            param : {
                RETURN_VALUE: "PARAMETER",
                Owner: window.user.details.UserEntityId
            }
        };
        return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }
    function getProjectProgressReport(){
        let obj={
            method: "MOD_TM_SP_task_GetProjectProgressReport",
            namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetProjectProgressReport",
            param : {
                RETURN_VALUE: "PARAMETER",
                Owner: window.user.details.UserEntityId
            }
        };
        return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }
    function getTaskProgressReport(){
        let obj={
            method: "MOD_TM_SP_task_GetTaskProgressReport",
            namespace: "http://schemas.cordys.com/MOD_TM_SP_task_GetTaskProgressReport",
            param : {
                RETURN_VALUE: "PARAMETER",
                Owner: window.user.details.UserEntityId
            }
        };
        return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }
}