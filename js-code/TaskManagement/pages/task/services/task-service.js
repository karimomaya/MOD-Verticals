function TaskService(value) {

    if (value) {
        let obj = {};
        let taskService = TaskService();
        for (var i = 0; i < value.length; i++) {
            obj[value[i]] = (taskService[value[i]]);
        }
        return obj;
    }

    return {
        fillLookupValue             : fillLookupValue,
        createTask                  : createTask,
        getInProgressTask           : getInProgressTask,
        getCompletedTask            : getCompletedTask,
        getMyOwnedTask              : getMyOwnedTask,
        getCreatedByTask            : getCreatedByTask,
        getDraftTask                : getDraftTask,
        autocompleteProjectOptions  : autocompleteProjectOptions,
        autocompleteTaskTeamOptions : autocompleteTaskTeamOptions,
        viewTask                    : viewTask,
        showEdit                    : showEdit,
        editTask                    : editTask,
        addAttributesToTaskResponse : addAttributesToTaskResponse,
        openTaskCollapse            : openTaskCollapse
    }

    function openTaskCollapse(taskId, element, callBack) {
        let pageNumber = 1;
        let pageSize = 3;
        TaskModel().getListOfWorkingUser(taskId, pageNumber, pageSize).done(function (response) {
            let users = [];
            let count = 0;
            for (var key in response) {
                if (typeof response[key] != "object" || key.indexOf(":Cursor") > -1) continue;
                if (count < pageSize) users.push({
                    performerName: response[key].displayName,
                    className: ""
                });
                count++;
            }
            if (count > pageSize) {
                count = count - pageSize;
                users.push({
                    performerName: count,
                    className: "user-record-gold"
                });
            }
            callBack(users);

            $(element).parent().toggleClass("open").next(".fold").toggleClass("open");
            let ele = $(element).parent().next(".fold")[0];
            let extraDataDiv = ele.getElementsByClassName('extra-task-data')[0];
            let extraData = $(extraDataDiv).attr("data-value");
            if (extraData) {
                if (extraData.indexOf("post-") == 0) {
                    let label = extraDataDiv.getElementsByTagName("label")[0]
                    label.innerHTML = System().translateWord("point-of-discussion")
                    let div = extraDataDiv.getElementsByTagName("div")[0]
                    div.innerHTML = extraData.substring(5, extraData.length);
                }
            }


        }).fail(function (error) {
            System().handleError(error);
        });
    }

    function addAttributesToTaskResponse(response, type) {
        console.log(response);
        if(!response.priority) {
            response.priority = 3;
        }

        response.priorityClass = System().convertClassPriority(parseInt(response.priority))
        response.finished = parseInt(response.finished);
        if (isNaN(response.finished)) {
            response.isFinished = false;
        } else if (response.finished) {
            response.isFinished = true;
        } else {
            response.isFinished = false;
        }
        // response.isFinished = (response.finished == "0")? response.isFinished : false; // check if task is finished
        response.taskStatus = parseInt(response.taskStatus);
        response.isOwner = false;
        let userId = window.user.details.UserEntityId;
        if (userId == response.ownerId || userId == response.createdById) response.isOwner = true;
        response.canDelete = ko.observable(false);
        response.canClose = ko.observable(false);
        response.canEdit = ko.observable(false);
        response.canAcquireTask = ko.observable(false);
        response.canStartTask = ko.observable(false);
        response.canEndTask = ko.observable(false);
        response.canRestartTask = ko.observable(false);
        response.canArchive = ko.observable(false);
        response.canForceClose = ko.observable(false);
        response.progressColor = ko.observable("progress-bar");
        let progress = parseInt(response.progress);
        if(progress < 35) {
            response.progressColor = "progress-bar-red"
        }else if(progress < 70){
            response.progressColor = "progress-bar-gold"
        }else {
            response.progressColor = "progress-bar"
        }

        if (response.isOwner) {
            // 0:stopped, 1: not started, 2: started, 3: finished 10: draft, 11: obselate, 12: Archived
            if (response.taskStatus == 1 || response.taskStatus == 2 || response.taskStatus == 10)
                response.canEdit(true);

            if (response.taskStatus == 1 || response.taskStatus == 2) {
                response.canForceClose(true);
            }

            if ((response.taskStatus == 1 || response.taskStatus == 10) && userId == response.createdById) { // task not start or is draft
                response.canDelete(true);
            } else if (response.taskStatus == 2 && response.isFinished) { // task start and is finished
                response.canClose(true);
                response.canForceClose(false);
            }
            else if (response.taskStatus == 3) { // task is finished
                response.canArchive(true);
            }


        } else {
            
            response.targetTaskPerformerStatus = parseInt(response.targetTaskPerformerStatus)
            response.singleOrMultiple = (response.singleOrMultiple == "false") ? false : true; // false single true multiple
            if (response.taskStatus == 1) { // task not started
                if (response.targetTaskPerformerStatus == 0 && response.singleOrMultiple) { //task performer not started and multiple user work on it
                    response.canStartTask(true);
                } else if (response.targetTaskPerformerStatus == 0 && !response.singleOrMultiple) { //task performer not started and single user work on it
                    response.canAcquireTask(true);
                }
            } else if (response.taskStatus == 2) {
                if (response.targetTaskPerformerStatus == 1) { // task started or acquired
                    response.canRestartTask(true);
                } else if (response.targetTaskPerformerStatus == 0 && response.singleOrMultiple) { //task not started and multiple user work on it
                    response.canStartTask(true);
                }
                if (parseInt(response.progress) == 100) {
                    response.canEndTask(true);
                }
            }
        }

        response.taskUrl = assetConfig.processExprience + "/" + _ConfigURL("taskDisplay") + "?taskId=" + window.system.encrypt(response.Id);

        response.startDate = response.startDate.split("T")[0];
        response.dueDate = response.dueDate.split("T")[0];


        // 0:stopped, 1: not started, 2: started, 3: finished 10: draft, 11: obselate, 12: Archived
        if(response.taskStatus == 1 || response.taskStatus == 2){
            let todayDate = new Date();
            let dueDate =  new Date(response.dueDate);
            let diffDays = parseInt((dueDate - todayDate) / (1000 * 60 * 60 * 24), 10); 
            if(diffDays <= 1) {
                response.dueDateClass = "due-date-class";
            }
        }
        if(!response.dueDateClass){
            response.dueDateClass = "";
        }
        
        
        response.taskStatus = translateTaskStatus(response.taskStatus, response.isFinished, type);
        response.taskPerformers = ko.observableArray([]);

        response.progress = response.progress + "%";
        
        

        

        return response;
    }

    function translateTaskStatus(taskStatus, isFinished, type) {
        switch (taskStatus) {
            case 0:
                return window.system.translateWord("stopped");
            case 1:
                return window.system.translateWord("not-start");
            case 2:
                if (type == "completedTask") return window.system.translateWord("ended")
                if (isFinished) return window.system.translateWord("task-can-closed")
                return window.system.translateWord("start")
            case 3:
                return window.system.translateWord("ended")
            case 10:
                return window.system.translateWord("draft");
            case 11:
                return system.translateWord("obselate");
            case 12:
                return system.translateWord("archived");
            default:
                return taskStatus;
        }
    }

    function showEdit(taskId, callBack) {
        TaskModel().getTaskDataById(taskId, window.user.details.UserEntityId).done(function (response) {
            if (!response.tuple[0]) return; // response is empty
            response = response.tuple[0].old.rowset.row;

            // if(response instanceof Array) data = response[0];
            callBack(response);

        }).fail(function (error) {
            system.handleError(error)
            console.log(error.responseJSON.faultstring.text);
        });
    }

    function viewTask(taskId) { // encrypted task Id
        var hasTaskId = Cordys().getTaskId();
        // var url = 'http://appworks-dev:81/home/mod/html5/demo/TaskManagement/pages/task/task-display.html?taskId='+taskId;
        var url = assetConfig.processExprience + "/" + _ConfigURL("taskDisplay") + "?taskId=" + taskId;

        /*if (hasTaskId && hasTaskId.indexOf("-") != -1) {
            window.top.location = url
        } else {
            window.location.href = url;
            
        }*/

        if (window.cntrlIsPressed) {
            var win = window.open(url, '_blank');
            win.focus();
        }
        else {
            window.top.location = url;
        }

    }

    function autocompleteTaskTeamOptions(model, input, pageNumber, pageSize) {
        pageNumber = pageNumber || 1;
        pageSize = pageSize || assetConfig.autocompleteSize;
        userId = window.user.details.UserEntityId
        TaskModel().getSubUsersRolesAndUnits(userId, input, pageNumber, pageSize).done(function (response) {

            if (!response.tuple[0]) return;

            let currentUserId = window.user.details.UserEntityId;

            var results = response.tuple[0].old.rowset.row;
            let taskTeamObject = [];
            if (results instanceof Array) {
                for (let i = 0; i < results.length; i++) {
                    let type = results[i].type;
                    type = (type == "User") ? 0 : (type == "Role") ? 1 : 2;

                    if (currentUserId == results[i].Id && type == "User") continue;

                    taskTeamObject.push({
                        id: results[i].Id + ":" + type + ":" + results[i].DisplayName,
                        name: results[i].DisplayName
                    })
                }
            } else {
                let type = results.type;
                type = (type == "User") ? 0 : (type == "Role") ? 1 : 2;
                // false  && true 
                if (currentUserId == results.Id && type == "User") { }
                else {
                    taskTeamObject.push({
                        id: results.Id + ":" + type + ":" + results.DisplayName,
                        name: results.DisplayName
                    })
                }


            }
            var selected = model.taskTeam();
            for (var i = 0; i < selected.length; i++) {
                var oldUserSelected = selected[i];
                var oldUserArr = oldUserSelected.split(":");
                taskTeamObject.push({
                    id: oldUserSelected,
                    name: oldUserArr[2]
                });
            }

            model.taskTeamOptions(taskTeamObject);
            model.taskTeam(selected);
        });
    }

    function autocompleteProjectOptions(model, input, pageNumber, pageSize) {
        model([]);
        //var path = window.user.details.UnitPathById;
        var status = 1;
        pageNumber = pageNumber || 1;
        pageSize = pageSize || assetConfig.autocompleteSize;
        ProjectModel().getProjectByHeadUnit(input, status, pageNumber, pageSize).done(function (response) {
            if (!response.tuple[0]) return;
            var results = response.tuple[0].old.rowset.row;
            let projectObject = [];
            if (results instanceof Array) {
                for (let i = 0; i < results.length; i++) {
                    projectObject.push({
                        id: results[i].Id + ":" + results[i].endDate + ";" + results[i].name,
                        name: results[i].name
                    })
                }
            } else {
                projectObject.push({
                    id: results.Id + ":" + results.endDate + ";" + results.name,
                    name: results.name
                })
            }
            model(projectObject);
        }).fail(function (error) {
            System().handleError(error)
            console.log(error.responseJSON.faultstring.text);
        });
    }

    function getDraftTask(sortBy, sortDir, pageNumber, pageSize, taskName, progress, startDate, endDate, filterOwner) {
        pageNumber = pageNumber || 1;
        pageSize = pageSize || window.system.getPageCount();
        sortBy = sortBy || "taskName";
        sortDir = sortDir || "sortAsc";
        taskName = taskName || "";
        progress = progress || -1;
        startDate = startDate.split("T")[0] || new Date('October 15, 1920 05:35:32').toISOString().split("T")[0];
        endDate = endDate.split("T")[0] || new Date('October 15, 2120 05:35:32').toISOString().split("T")[0];
        filterOwner = filterOwner || -1;
        return TaskModel().getDraftTask(pageNumber, pageSize, sortBy, sortDir, taskName, progress, startDate, endDate, filterOwner);
    }

    function getCreatedByTask(sortBy, sortDir, pageNumber, pageSize, taskName, progress, startDate, endDate, filterOwner) {
        pageNumber = pageNumber || 1;
        pageSize = pageSize || window.system.getPageCount();
        sortBy = sortBy || "taskName";
        sortDir = sortDir || "sortAsc";
        taskName = taskName || "";
        progress = progress || -1;
        startDate = startDate.split("T")[0] || new Date('October 15, 1920 05:35:32').toISOString().split("T")[0];
        endDate = endDate.split("T")[0] || new Date('October 15, 2120 05:35:32').toISOString().split("T")[0];
        filterOwner = filterOwner || -1;
        return TaskModel().getCreatedByTask(pageNumber, pageSize, sortBy, sortDir, taskName, progress, startDate, endDate, filterOwner);
    }

    function getInProgressTask(sortBy, sortDir, pageNumber, pageSize, taskName, progress, startDate, endDate, filterOwner) {
        pageNumber = pageNumber || 1;
        pageSize = pageSize || window.system.getPageCount();
        sortBy = sortBy || "taskName";
        sortDir = sortDir || "sortAsc";
        taskName = taskName || "";
        progress = progress || -1;
        startDate = startDate.split("T")[0] || new Date('October 15, 1920 05:35:32').toISOString().split("T")[0];
        endDate = endDate.split("T")[0] || new Date('October 15, 2120 05:35:32').toISOString().split("T")[0];
        filterOwner = filterOwner || -1;
        return TaskModel().getMyInProgressTask(pageNumber, pageSize, sortBy, sortDir, taskName, progress, startDate, endDate, filterOwner);
    }

    function getCompletedTask(sortBy, sortDir, pageNumber, pageSize, taskName, progress, startDate, endDate, filterOwner) {
        pageNumber = pageNumber || 1;
        pageSize = pageSize || window.system.getPageCount();
        sortBy = sortBy || "taskName";
        sortDir = sortDir || "sortAsc";
        taskName = taskName || "";
        progress = progress || -1;
        startDate = startDate.split("T")[0] || new Date('October 15, 1920 05:35:32').toISOString().split("T")[0];
        endDate = endDate.split("T")[0] || new Date('October 15, 2120 05:35:32').toISOString().split("T")[0];
        filterOwner = filterOwner || -1;
        return TaskModel().getCompletedTask(pageNumber, pageSize, sortBy, sortDir, taskName, progress, startDate, endDate, filterOwner);
    }

    function getMyOwnedTask(sortBy, sortDir, pageNumber, pageSize, taskName, progress, startDate, endDate) {
        pageNumber = pageNumber || 1;
        pageSize = pageSize || window.system.getPageCount();
        sortBy = sortBy || "taskName";
        sortDir = sortDir || "sortAsc";
        taskName = taskName || "";
        progress = progress || -1;
        startDate = startDate.split("T")[0] || new Date('October 15, 1920 05:35:32').toISOString().split("T")[0];
        endDate = endDate.split("T")[0] || new Date('October 15, 2120 05:35:32').toISOString().split("T")[0];
        return TaskModel().getMyOwnedTask(pageNumber, pageSize, sortBy, sortDir, taskName, progress, startDate, endDate);
    }

    function editTask(obj, success, fail) {
        let data = TaskModel().editTask(obj);

        _CordysRequest().postRequest(data.url, data.message, success, null, fail);
    }

    function createTask(object, success, fail) {

        let data = TaskModel().createTask(object);

        _CordysRequest().postRequest(data.url, data.message, success, null, fail);

        // return TaskModel().createTask(object);
    }


    function fillLookupValue(language, model) {
        var priorityObject = [];
        LookupService().getByCategory("priority").done(function (obj) {
            for (let key in obj) {
                if (typeof obj[key] != "object") continue;
                var lookupObj = obj[key];
                var value = (language == 'ar') ? lookupObj.ar_value : lookupObj.eng_value;
                priorityObject.push({
                    id: lookupObj['MOD_SYS_entity_lookup-id'].Id,
                    name: value
                })
            }
            model.taskPriorityOptions(priorityObject);

            /*$('#task-pirority').selectize({
                valueField: 'id',
                labelField: 'value',
                options: priorityObject
            });*/
        });


    }
}