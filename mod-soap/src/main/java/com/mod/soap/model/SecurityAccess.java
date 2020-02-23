package com.mod.soap.model;

/**
 * Created by karim.omaya on 1/12/2020.
 */
public class SecurityAccess {
    boolean access;
    String target;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean getAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

}
