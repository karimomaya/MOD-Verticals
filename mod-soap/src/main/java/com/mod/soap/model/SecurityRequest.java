package com.mod.soap.model;

import javax.xml.bind.annotation.*;

/**
 * Created by karim on 2/8/20.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "target", "input"
})
public class SecurityRequest {

    @XmlElement(name = "target", required = false)
    String target;
    @XmlElement(name = "input", required = false)
    String input;

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
