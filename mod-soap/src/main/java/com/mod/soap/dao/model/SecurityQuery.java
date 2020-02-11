package com.mod.soap.dao.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by karim on 2/10/20.
 */
public class SecurityQuery {
    private SecurityType securityType; //
    private Map<String, Object> orOperator;
    private Map<String, Object> andOperator;
    private Map<String, Object> anonymousAssertion;
    private String key;

    public SecurityQuery() {
        orOperator = new HashMap<>();
        andOperator = new HashMap<>();
        anonymousAssertion = new HashMap<>();
    }

    public SecurityQuery(int securityType) {
        this();
        setSecurityType(securityType);
    }


    public void setSecurityType(int securityType){
        if (securityType == 4){
            this.securityType = SecurityType.WEBSERVICE;
        }
    }

    public SecurityType getSecurityType(){
        return this.securityType;
    }

    private void changeAssertion(String type){
        if (type.equals("or")){
            anonymousAssertion = orOperator;
        }else if (type.equals("and")){
            anonymousAssertion = andOperator;
        }
    }

    public SecurityQuery addKey(String key){
        this.key = key;
        changeAssertion(key);
        return this;
    }

    public SecurityQuery addValue(Object object){
        anonymousAssertion.put(key, object);
        return this;
    }

    public boolean execute(){

        return false;
    }

    public final boolean equals(Object other) { return this==other; }


}
