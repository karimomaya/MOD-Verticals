package com.mod.soap.request;

import javax.xml.bind.annotation.*;

/**
 * Created by karim on 2/8/20.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "target", "input"
})
public class SecurityRequest {
    @XmlElement(name = "target", required = true)
    public String target;
    @XmlElement(name = "input", required = true)
    public String input;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
