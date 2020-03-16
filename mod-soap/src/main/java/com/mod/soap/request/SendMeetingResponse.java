package com.mod.soap.request;

import javax.xml.bind.annotation.*;

/**
 * Created by karim on 3/9/20.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "status"
})
@XmlRootElement(name = "SendMeetingResponse")
public class SendMeetingResponse {
    @XmlElement(name = "status", required = true)
    protected String status;

    public String getStatus() {
        return status;
    }

    public SendMeetingResponse setStatus(String status) {
        this.status = status;
        return this;
    }
}

