package com.mod.soap.request;

import javax.xml.bind.annotation.*;

/**
 * Created by karim on 5/6/20.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "status"
})
@XmlRootElement(name = "SendEmailResponse")
public class SendEmailResponse {
    @XmlElement(name = "status", required = true)
    protected String status;

    public String getStatus() {
        return status;
    }

    public SendEmailResponse setStatus(String status) {
        this.status = status;
        return this;
    }
}
