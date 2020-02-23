package com.mod.soap.request;

import javax.xml.bind.annotation.*;

/**
 * Created by karim.omaya on 12/7/2019.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "userId", "type"
})
@XmlRootElement(name = "RequestNumberRequest")
public class RequestNumberRequest {
    @XmlElement(required = true)
    protected String userId;

    @XmlElement(required = true)
    protected String type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
