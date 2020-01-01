const noteControllerViews = {
    "task-note": "/cordys/html5/demo/TaskManagement/pages/note/views/task-note.html"
};
const noteControllerStyles = [];
const noteControllerScripts = [
    "/cordys/html5/demo/TaskManagement/pages/note/model/note-model.js"
];

var dependency = Dependency.getInstance();
dependency.execute("noteController");

let noteTableViewModel = null;

function noteControllerOnload() {
    dependency.initializeSystemObject(['getLanguage', 'translatePage', 'showLoader', 'translateWord',
        'showMessage', 'removeNullAndUndefined'
    ]);

    let language = window.system.getLanguage() /*"ar"*/ ;
    window.system.translatePage(language, "note-controller", "TaskManagement", "task-note");

    noteTableViewModel = new NoteTableViewModel();
    dependency.initializeDomObject(['subscribe']);
    ko.applyBindings(noteTableViewModel, document.getElementById('task-note'));

    window.dom.subscribe("task-note", function (obj) {
        noteTableViewModel.taskStatus(obj.taskStatus);
        noteTableViewModel.performerStatus(obj.performerStatus);
        noteTableViewModel.taskId(obj.taskId);

        if (obj.taskStatus == 3 ||  obj.performerStatus != 1) {
            $("#add-note-btn").addClass("dimmedClick")
        }
        drawNotes();
    })
    window.dom.subscribe('reload-note', function () {
        drawNotes();
    })

    window.dom.subscribe('disable-note', function (response) {
        if(response){
            
        }
    })
}




function drawNotes() {
    if (!window.user.details.UserEntityId) {
        window.requestAnimationFrame(function () {
            drawNotes();
        });

        return;
    } else {
        NoteModel().readNotesByTaskId(noteTableViewModel.taskId()).done(function (response) {
            noteTableViewModel.notes([]);
            if (!response.tuple[0]) return;

            response = response.tuple[0].old.rowset.row;
            if (response instanceof Array) {
                for (let i = 0; i < response.length; i++) {
                    response[i] = window.system.removeNullAndUndefined(response[i])
                    response[i] = addAditionalAttributeToNoteResponse(response[i]);

                    // document.getElementById("task-note").innerHTML+= drawNote(results[i], window.system)
                }
            } else {
                response = window.system.removeNullAndUndefined(response)
                response = addAditionalAttributeToNoteResponse(response);
                // document.getElementById("task-note").innerHTML+= drawNote(results, window.system)
            }
            noteTableViewModel.notes(response);
        }).fail(function (error) {
            window.system.handleError(error);
        });

    }

}


function addAditionalAttributeToNoteResponse(response) {
    response.canEdit = false;
    response.canDelete = false;
    response.haveAction = false;

    let dateStrings = response.createdDate.split("T");
    dateStrings[1] = dateStrings[1].split(".")[0];
    response.createdDate = dateStrings[1] + " " + dateStrings[0];

    if (noteTableViewModel.taskStatus() != 3 && noteTableViewModel.performerStatus() != 2) {
        let userId = window.user.details.UserEntityId;
        if (userId == response.createdBy) // if is the creator of the note
            response.canEdit = true;
        if (userId == response.taskOwner || userId == response.taskCreatedBy || userId == response.createdBy)
            response.canDelete = true;
        response.haveAction = true;
    }

    response.editNote = function (obj, e) {
        window.system.showLoader(true, document.getElementById('task-note'));
        NoteModel().readNote(obj.Id).done(function (response) {
            let bindEditNoteFunc = $("#note-create").attr("bind-edit");
            window[bindEditNoteFunc](response);
        }).fail(function (error) {
            window.system.handleError(error)
        });
    }
    response.deleteNote = function (obj, e) {
        window.system.showLoader(true, document.getElementById('task-note'));
        NoteModel().deleteNote(obj.Id).done(function (response) {
            let card = $(e.target).closest('.card');
            $(card).remove();
            window.system.showLoader(false);
        }).fail(function (error) {
            window.system.handleError(error);
            window.system.showLoader(false);
        });
    }

    return response;
}

function NoteTableViewModel() {
    this.taskId = ko.observable();
    this.taskStatus = ko.observable();
    this.performerStatus = ko.observable();
    this.notes = ko.observableArray([]);

}