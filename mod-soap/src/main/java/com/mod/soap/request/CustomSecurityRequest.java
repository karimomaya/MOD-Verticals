package com.mod.soap.request;

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
        "securityRequest", "username", "url", "previousTargetTag"
})
public class CustomSecurityRequest {
    @XmlElement(name = "securityRequest", required = true)
    protected ArrayList<SecurityRequest> securityRequest;

    @XmlElement(name = "username", required = true)
    protected String username;

    @XmlElement(name = "url", required = false)
    protected String url;

    @XmlElement(name = "previousTargetTag", required = false)
    protected String previousTargetTag;

    public String getPreviousTargetTag() {
        return previousTargetTag;
    }

    public void setPreviousTargetTag(String previousTargetTag) {
        this.previousTargetTag = previousTargetTag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public List<SecurityRequest> getSecurityRequest() {
        return securityRequest;
    }

    public void setSecurityRequest(ArrayList<SecurityRequest> securityRequest) {
        this.securityRequest = securityRequest;
    }
}



