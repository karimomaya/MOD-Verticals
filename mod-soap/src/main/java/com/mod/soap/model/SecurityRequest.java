package com.mod.soap.model;

import javax.xml.bind.annotation.*;

/**
 * Created by karim on 2/8/20.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "samlart", "type", "target"
})
public class SecurityRequest {
    @XmlElement(name = "samlart", required = false)
    String samlart;
    @XmlElement(name = "type", required = false)
    int type;
    @XmlElement(name = "target", required = false)
    String target;


    public String getSamlart() {
        return samlart;
    }

    public void setSamlart(String samlart) {
        this.samlart = samlart;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
