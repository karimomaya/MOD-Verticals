package com.mod.soap.model;

/**
 * Created by karim.omaya on 1/12/2020.
 */
public class SecurityAccess {
    boolean access;
    String target;


    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    private int convertCodeToId(String code){
        switch(code){
            case "MSM": // وزير
                return 8;
            case "USM": // وكيل وزارة
                return 7;
            case "AAM": // وكيل مساعد
                return 6;
            case "EXM": // رئيس إدارة تنفيذية
                return 5;
            case "DIR": //مدير مديرية
                return 4;
            case "DIV": // رئيس شعبة
                return 3;
            case "SEC": // رئيس قسم
                return 2;
            case "STF":// ركن
                return 1;
            default:
                return 1;
        }
    }
}
