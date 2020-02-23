package com.mod.soap.model;

import com.mod.soap.request.SecurityRequest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karim on 2/17/20.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecurityLayer", propOrder = {
        "securityRequest", "username"
})
public class SecurityLayer {
    @XmlElement(name = "securityRequest", required = true)
    protected List<SecurityRequest> securityRequest;

    @XmlElement(name = "username", required = true)
    protected String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<SecurityRequest> getSecurityRequest() {
        return securityRequest;
    }

    public void setSecurityRequest(SecurityRequest securityRequest) {
        if (this.securityRequest == null ) this.securityRequest = new ArrayList<>();
        this.securityRequest.add(securityRequest);
    }
}
