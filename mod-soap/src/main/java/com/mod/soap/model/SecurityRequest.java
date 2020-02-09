package com.mod.soap.model;

/**
 * Created by karim on 2/8/20.
 */
public class SecurityRequest {
    String samlart;
    int type;
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
