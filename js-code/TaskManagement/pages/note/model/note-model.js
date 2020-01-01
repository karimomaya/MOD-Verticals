function NoteModel() {
    return {
        createNote: createNote,
        readNotesByTaskId: readNotesByTaskId,
        readNote: readNote,
        updateNote: updateNote,
        deleteNote: deleteNote
    }

    function deleteNote(noteId) {
        let obj = {
            method: "DeleteMOD_TM_entity_Note",
            namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_Note/operations",
            param: {
                "MOD_TM_entity_Note-id": {
                    Id: noteId
                }
            }
        };
        return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
    }

    function updateNote(o) {
        let obj = {
            method: "UpdateMOD_TM_entity_Note",
            namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_Note/operations",
            param: {
                "MOD_TM_entity_Note-id": {
                    Id: o.Id()
                },
                "MOD_TM_entity_Note-update": {
                    note: o.note,

                }
            }
        };
        return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
    }

    function readNote(noteId) {
        let obj = {
            method: "ReadMOD_TM_entity_Note",
            namespace: "http://schemas/MyCompanyTaskManagement/MOD_TM_entity_Note/operations",
            param: {
                "MOD_TM_entity_Note-id": {
                    Id: noteId,
                }
            }
        };
        return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
    }

    function readNotesByTaskId(taskId) {
        let obj = {
            method: "MOD_TM_SP_ReadNote",
            namespace: "http://schemas.cordys.com/MOD_TM_SP_ReadNote",
            param: {
                "RETURN_VALUE": "PARAMETER",
                "taskId": taskId,
                "owner": window.user.details.UserEntityId

            }
        };
        return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
    }


    function createNote(o) {
        let obj = {
            method: "MOD_TM_BP_create_note",
            namespace: "http://schemas.cordys.com/default",
            param: {
                "createdBy": o.createdBy,
                "note": o.note,
                "prentNoteId": o.parentNoteId,
                "replyTo": o.replyTo,
                "taskID": o.taskId
            }
        };

        return Cordys().cordysAjax(obj.method, obj.namespace, obj.param);
    }
}