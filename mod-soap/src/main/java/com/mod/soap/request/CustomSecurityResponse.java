package com.mod.soap.request;

import com.mod.soap.model.SecurityAccess;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karim.omaya on 1/12/2020.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "securityAccess"
})
@XmlRootElement(name = "CustomSecurityResponse")
public class CustomSecurityResponse {
    @XmlElement(name = "CustomSecurity", required = true)
    protected List<SecurityAccess> securityAccess;

    public List<SecurityAccess> getSecurityAccess() {
        return securityAccess;
    }

    public void setSecurityAccess(SecurityAccess securityAccess) {
        if (this.securityAccess == null ) this.securityAccess = new ArrayList<>();
        this.securityAccess.add(securityAccess);
    }
}
