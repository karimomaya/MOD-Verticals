package com.mod.soap.request;

import com.mod.soap.model.SecurityRequest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karim.omaya on 1/12/2020.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "securityRequest"
})
public class CustomSecurityRequest {
    @XmlElement(name = "SecurityRequest", required = true)
    protected List<SecurityRequest> securityRequest;

    public List<SecurityRequest> getSecurityRequest() {
        return securityRequest;
    }

    public void setSecurityAccess(SecurityRequest securityRequest) {
        if (this.securityRequest == null ) this.securityRequest = new ArrayList<>();
        this.securityRequest.add(securityRequest);
    }
}



