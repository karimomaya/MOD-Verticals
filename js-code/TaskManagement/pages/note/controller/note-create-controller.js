const noteCreateControllerViews = {
    "note-create": "/cordys/html5/demo/TaskManagement/pages/note/views/note-create.html"
};
const noteCreateControllerStyles = [];
const noteCreateControllerScripts = [
    "/cordys/html5/demo/TaskManagement/pages/note/model/note-model.js"
];

var dependency = Dependency.getInstance();
dependency.addToTrigger("noteCreateControllerOnReady");
dependency.execute("noteCreateController");


let noteModelView = null;


function noteCreateControllerOnReady() {
    dependency.initializeSystemObject(['getLanguage', 'translatePage', 'showLoader',
        'showMessage', 'translateWord', 'handleError'
    ]);
    dependency.initializeDomObject(['subscribe']);
    
    noteModelView = new NoteViewModel();

    window.dom.subscribe('task-id', function (taskId) {
        noteModelView.taskId(taskId);
    })
    window.dom.subscribe('meeting-id', function (meetingId) {
        noteModelView.isMeeting = true;
        noteModelView.taskId(meetingId);
    })


    KOValidation().initializeValidation(noteModelView);
    ko.applyBindings(noteModelView, $("#note-create")[0]);
}

function noteCreateControllerOnload() {
    let language = window.system.getLanguage() /*"ar"*/ ;
    window.system.translatePage(language, "note", "TaskManagement", "note-create");

    $('#note-create').on('shown.bs.modal', function () {
        restNote()
    });
}

function bindEdit(response) {
    $("#add-note-btn").click();
    $("#note-modal-title").html(window.system.translateWord("edit-note"));
    $("#mod-btn-note-submit").html(window.system.translateWord("edit"));
    $("#mod-btn-note-submit").attr("translate-text", "edit");
    noteModelView.isEdit(true);
    noteModelView.Id(response['wstxns2:MOD_TM_entity_Note']['MOD_TM_entity_Note-id'].Id)
    noteModelView.note(response['wstxns2:MOD_TM_entity_Note'].note);
    noteModelView.parentNoteId(response['wstxns2:MOD_TM_entity_Note'].parentNoteId);
    noteModelView.createdBy(response['wstxns2:MOD_TM_entity_Note'].createdBy);
    noteModelView.replyTo(response['wstxns2:MOD_TM_entity_Note'].replyTo);
    noteModelView.taskId(response['wstxns2:MOD_TM_entity_Note'].note_to_task['wstxns3:MOD_TM_entity_Task-id'].Id);
}


function restNote() {
    $("#note-modal-title").html(window.system.translateWord("add-note"));
    $("#mod-btn-note-submit").html(window.system.translateWord("add"));
    $("#mod-btn-note-submit").attr("translate-text", "add");
    noteModelView.Id(0);

    noteModelView.parentNoteId(0);
    noteModelView.createdBy(window.user.details.UserEntityId);
    noteModelView.replyTo(0);
    noteModelView.taskId();
    noteModelView.isEdit(false);
    noteModelView.note("");

    if (noteModelView && noteModelView.errors) noteModelView.errors.showAllMessages(false);

    window.system.showLoader(false);
}

function NoteViewModel() {
    var self = this;
    self.isMeeting = false;
    self.hasError = ko.observable(true);
    self.Id = ko.observable();
    self.note = ko.observable().extend({
        required: {
            params: true,
            message: window.system.translateWord("requiredField")
        },
        
        
    });
    self.parentNoteId = ko.observable(0);
    self.createdBy = ko.observable(window.user.details.UserEntityId);
    self.replyTo = ko.observable(0);
    self.taskId = ko.observable().extend({
        required: true
    });
    self.isEdit = ko.observable(false);

    self.submit = function () {
        if (self.errors().length > 0) return;
        window.system.showLoader(true);

        if (self.isEdit()) {
            // handle edit
            if(!self.isMeeting){
                NoteModel().updateNote(self).done(function (response) {
                    $("#mod-btn-note-close").click();
                    window.system.showLoader(false);
                    window[$("#note-create").attr('success')]();
    
                }).fail(function (error) {
                    window.system.handleError(error);
                    window.system.showLoader(false)
                });
            }else {
                window.dom.publish("edit-note", self)
            }
           
        } else {
            // handle create
            if(!self.isMeeting){
                NoteModel().createNote(self).done(function (response) {
                    $("#mod-btn-note-close").click();
                    window.system.showLoader(false)
                    window[$("#note-create").attr('success')]();
                }).fail(function (error) {
                    window.system.handleError(error);
                    window.system.showLoader(false)
                });
            }else {
                window.dom.publish("add-note", self)
            }
            
        }
    }
    self.errors = ko.validation.group(self);

}