package com.mod.soap.request;

import com.mod.soap.model.RequestNumber;

import javax.xml.bind.annotation.*;

/**
 * Created by karim.omaya on 12/7/2019.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "requestNumber"
})
@XmlRootElement(name = "RequestNumberResponse")
public class RequestNumberResponse {
    @XmlElement(name = "RequestNumber", required = true)
    protected RequestNumber requestNumber;

    public RequestNumber getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(RequestNumber value) {
        this.requestNumber = value;
    }
}
