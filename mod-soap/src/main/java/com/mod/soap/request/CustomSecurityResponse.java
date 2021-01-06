package com.mod.soap.request;

import com.mod.soap.model.SecurityAccess;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karim.omaya on 1/12/2020.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "securityAccess", "canViewTask"
})
@XmlRootElement(name = "CustomSecurityResponse")
public class CustomSecurityResponse {
    @XmlElement(name = "CustomSecurity", required = true)
    protected List<SecurityAccess> securityAccess;

    @XmlElement(name = "canViewTask", required = true)
    protected Boolean canViewTask = true;

    public List<SecurityAccess> getSecurityAccess() {
        return securityAccess;
    }

    public void setSecurityAccess(SecurityAccess securityAccess) {
        if (this.securityAccess == null ) this.securityAccess = new ArrayList<>();
        this.securityAccess.add(securityAccess);
    }

    public Boolean getCanViewTask() {
        return canViewTask;
    }

    public void setCanviewTask(Boolean canViewTask) {
        this.canViewTask = canViewTask;
    }
}
