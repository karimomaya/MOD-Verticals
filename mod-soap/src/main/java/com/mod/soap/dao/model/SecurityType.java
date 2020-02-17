package com.mod.soap.dao.model;

/**
 * Created by karim on 2/11/20.
 */
public enum SecurityType {
    UNIT_TYPE_Code("{code}"),
    UNIT_CODE("{code}"),
    ROLE_CODE("{code}"),
    WEBSERVICE("<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP:Header><OTAuthentication xmlns=\"urn:api.bpm.opentext.com\"><AuthenticationToken>{ticket}</AuthenticationToken></OTAuthentication></SOAP:Header><SOAP:Body><{method} xmlns=\"{namespace}\">{params}</{method}></SOAP:Body></SOAP:Envelope>");

    private String template;

    private SecurityType(String tpl) {
        this.template = tpl;
    }

    public String getTemplate() {
        return this.template;
    }

}
