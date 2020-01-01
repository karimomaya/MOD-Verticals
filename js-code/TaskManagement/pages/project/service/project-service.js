function ProjectService() {

    return {
        createProject                   : createProject,
        getProjectRelateProgOptions     : getProjectRelateProgOptions,
        autoCompleteProjectRelateProg   : autoCompleteProjectRelateProg,
        assignToUnit                    : assignToUnit,
        autoCompleteAssignToUnit        : autoCompleteAssignToUnit,
        autoCompleteProjectOwner        : autoCompleteProjectOwner,
        drawProjectObj                  : drawProjectObj,
        fillProjectTable                : fillProjectTable
    }

    function drawProjectTable(element, response, type){
        element.innerHTML = "";
        if(!response.tuple[0]) {
            System().showLoader(false);
            return;
        }
        var results = response.tuple[0].old.rowset.row;
        if (results instanceof Array) {
            for (let i=0; i<results.length; i++){
                element.innerHTML += drawProject(results[i], i, type);
            }
        }
        else {
            element.innerHTML = drawProject(results, 0, type);
        }
    }


    function fillProjectTable(element, status, pageNumber, sortBy, sortDir){
        status = status || 1; // 1 inprogress
        pageNumber = pageNumber || 1;
        var pageSize = 10;
        sortBy = sortBy || "projectName";
        sortDir = sortDir || "sortAsc";
        var userId = window.user.details.UserEntityId
        var _system = System();
        ProjectModel().getProjectByStatus(userId, status, pageNumber, pageSize, sortBy, sortDir).done(function(response){
            element = element.getElementsByClassName("project-data-table")[0];
            element.innerHTML = "";
            drawProjectTable(element, response, status);
            _system.showLoader(false);
        }).fail(function(error){
            // TODO handle error message
            _system.handleError(error);
            console.log(error.responseJSON.faultstring.text);
            _system.showLoader(false);
        });
    }

    function drawProjectObj(status, element) {
        var owner = window.user.details.UserEntityId;
        let _system = System();
        var type = "countProject"
        status = status || 1

        ProjectModel().countProject(owner, type, status).done(function(response){
            
            _system.showLoader(false);
            var totalPages = Math.ceil(response.count.text / _system.getPageCount());
            if(totalPages == 0) return;
            
            Dom().drawPagination(element.getElementsByClassName('pagination')[0], totalPages, fillMyInProgressTaskTable)
            fillProjectTable(element, status);
        }).fail(function(error){
            _system.handleError(error);
            _system.showLoader(false);
            console.log(error.responseJSON.faultstring.text);
        });
    }

    function autoCompleteProjectOwner(input, model, setOwner, pageNumber, pageSize) {
        pageNumber = pageNumber || 1;
        pageSize = pageSize || window.system.getAutoCompleteCount();
        userId = window.user.details.UserEntityId;
        input = input || "";
        let unitPathById = -1;
        if(model.assignToUnit()&& model.assignToUnit().length > 0 ){
            unitPathById = model.assignToUnit()[0];
            if(unitPathById.indexOf(":") != -1){
                unitPathById = unitPathById.split(":")[1];
            }
        }

        SharedModel().getUserUnderUnitsByUnitCodes('MSM,USM,AAM,EXM', unitPathById, input).done(function(response){
            if(!response.tuple[0])  return;
            var results = response.tuple[0].old.rowset.row;
            let ownerObject = [];
            if (results instanceof Array) {
                for (let i=0; i<results.length; i++){
                    ownerObject.push({id: results[i].UserEntityId, name : results[i].DisplayName})
                }
            }
            else {
                ownerObject.push({id: results.UserEntityId, name : results.DisplayName})
            }
            if(setOwner){
                addCurrentUserTobeProjectOwner(ownerObject)
            }else {
                model.projectManagerOptions(ownerObject);
            }
            
        }).fail(function(error){
            System().handleError(error);
            console.log(error.responseJSON.faultstring.text);
        });
            
        
        /*TaskModel().getOwners(input, false, unitCode ).done(function(response){
            
        }).fail(function(error){
            System().handleError(error);
            console.log(error.responseJSON.faultstring.text);
        });*/
    }

    
    function addCurrentUserTobeProjectOwner(ownerObject){
        ownerObject.push({id: window.user.details.UserEntityId, name : window.user.details.DisplayName})
        projectCreateViewModel.projectManagerOptions(ownerObject);
        projectCreateViewModel.projectManager([window.user.details.UserEntityId])
    }

    function createProject(projectModel) {
        return ProjectModel().createProject(projectModel);
    }

    function autoCompleteAssignToUnit(input, projectModel){
        assignToUnit(projectModel, input)
    }

    function autoCompleteProjectRelateProg(input, projectModel){
        getProjectRelateProgOptions(projectModel, input)
    }

    function assignToUnit(model, input, pageNumber, pageSize){
        input = input || "";
        pageNumber = pageNumber || 1;
        pageSize = pageSize || window.system.getAutoCompleteCount();

        ProjectModel().getUnitsByUnitTypeCode("DIR,OFC,IGM", input, pageNumber, pageSize).done(function(response){
            if(!response.tuple[0])  return;
            var results = response.tuple[0].old.rowset.row;
            let assignToUnitObject = [];
            if (results instanceof Array) {
                for (let i=0; i<results.length; i++){
                    assignToUnitObject.push({id: results[i].UnitId+":"+results[i].UnitPathById, name : results[i].UnitName})
                }
            }
            else {
                assignToUnitObject.push({id: results.UnitId+":"+results[i].UnitPathById, name : results.UnitName})
            }
            model.assignToUnitOptions(assignToUnitObject);
        }).fail(function(error){
            // TODO handle error message
            System().handleError(error);
            console.log(error.responseJSON.faultstring.text);
        });
    
    }
    
    function getProjectRelateProgOptions(projectModel, input, status, pageNumber, pageSize){
        pageNumber = pageNumber || 1;
        pageSize = pageSize || 5;
        input = input || "";
        status = status || 1; // 1: inprogress, 2: done, 3:stopped 
        let path = window.user.details.UnitId +"/"; 
        ProgramModel().getProgramByPathAndStatus(path, input, status, pageNumber, pageSize).done(function(response){
            if(!response.tuple[0])  return;
            var results = response.tuple[0].old.rowset.row;
            let projectRelateProg = [];
            if (results instanceof Array) {
                for (let i=0; i<results.length; i++){
                    let startDate = (results[i].startDate)? results[i].startDate.split("T")[0]: "";
                    let endDate =(results[i].endDate)?  results[i].endDate.split("T")[0]: "";
                    projectRelateProg.push({id: results[i].Id+":"+startDate+":"+endDate, name : results[i].name})
                }
            }
            else {
                let startDate = (results.startDate)? results.startDate.split("T")[0]: "";
                let endDate =(results.endDate)?  results.endDate.split("T")[0]: "";
                projectRelateProg.push({id: results.Id+":"+startDate+":"+endDate, name : results.name})
            }

            projectModel.projectRelateProgOptions(projectRelateProg);
        }).fail(function(error){
            System().handleError(error);
            // TODO handle error message
            console.log(error.responseJSON.faultstring.text);
        });
    }
}