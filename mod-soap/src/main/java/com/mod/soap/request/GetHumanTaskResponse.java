package com.mod.soap.request;

import com.mod.soap.model.SecurityAccess;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "taskResponse"
})
@XmlRootElement(name = "GetHumanTaskResponse")
public class GetHumanTaskResponse {
    @XmlElement(name = "taskResponse", required = true)
    protected List<TaskResponse> taskResponse;

    public List<TaskResponse> getTaskResponse() {
        return taskResponse;
    }

    public void setTaskResponse(TaskResponse taskResponse) {
        if (this.taskResponse == null ) this.taskResponse = new ArrayList<>();
        this.taskResponse.add(taskResponse);
    }
}
