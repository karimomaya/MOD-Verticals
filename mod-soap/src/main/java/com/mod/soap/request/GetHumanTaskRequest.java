package com.mod.soap.request;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "roleName","processName"
})
@XmlRootElement(name = "GetHumanTaskRequest")
public class GetHumanTaskRequest {
    @XmlElement(required = true)
    protected String roleName;
    protected String processName;
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}
