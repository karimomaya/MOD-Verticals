function ProgramService() {
    return {
        createProgram               : createProgram,
        readProgram                 : readProgram,
        getProgramByPathAndStatus   : getProgramByPathAndStatus
    }

    function getProgramByPathAndStatus(input, status, pageNumber, pageSize) {
        pageNumber = pageNumber || 1;
        pageSize = pageSize || 5;
        input = input || "";
        status = status || 1; // 1: inprogress, 2: done, 3:stopped 
        let path = window.user.details.UnitPathById +"/";
        return ProgramModel().getProgramByPathAndStatus(path, input, status, pageNumber, pageSize);
    }

    function readProgram(status, pageNumber, pageSize, sortBy, sortDir, id) {
        pageNumber = pageNumber || 1;
        pageSize = pageSize || 5;
        sortBy = sortBy || "programName";
        sortDir = sortDir || "sortAsc";
        var _system = System();
        _system.showLoader(true, document.getElementById(id));
        ProgramModel().readProgram(window.user.details.UserEntityId, status, pageNumber, pageSize, sortBy, sortDir).done(function(response){
            _system.showLoader(false);
        }).fail(function(error){
            // TODO handle error message
            console.log(error.responseJSON.faultstring.text);
            _system.showLoader(false);
        });
    }


    function createProgram(object) {
        return ProgramModel().createProgram(object);
    }
    
}