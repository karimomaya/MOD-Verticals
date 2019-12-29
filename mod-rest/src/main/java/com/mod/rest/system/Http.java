package com.mod.rest.system;
import com.mod.rest.system.Config;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;

import java.io.IOException;

/**
 * Created by karim.omaya on 12/7/2019.
 */
public class Http {
    HttpClient client;
    Header header;
    String organizationName;
    String domain;
//    String o = "MOD.COM";
//    String organzationCN = "o="+organizationName+",cn=cordys,cn=defaultInst,o="+o;
    String SAMLart;
    Config config;

    public Http(String SAMLart, Config config){
        this.SAMLart = SAMLart;
        this.client = new HttpClient();
        header = new Header();
        header.setName("SAMLart");
        header.setValue(SAMLart);
        this.config = config;
        this.domain = config.getProperty("domain");
        this.organizationName = config.getProperty("organizationName");
    }

    public String cordysRequest(String data){
        return this.post(this.getCordysURL(), data);
    }

    public String post(String url, String data) {
        PostMethod method = new PostMethod(url);
        method.setDoAuthentication(true);
        int statusCode;
        String response;
        try {
            method.setRequestEntity(new StringRequestEntity(data, "text/xml", "UTF-8"));
            method.setRequestHeader(header);
            statusCode = this.client.executeMethod(method);
            response = method.getResponseBodyAsString();
            java.lang.System.out.println(response);
        } catch (HttpException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (statusCode != HttpStatus.SC_OK) {
            throw new RuntimeException("Method failed: " + method.getStatusLine() + "\n" + response);
        }
        return response;
    }

    public String getCordysURL(){

        return domain+"/home/"+organizationName+"/com.eibus.web.soap.Gateway.wcp?SAMLart="+this.SAMLart;
    }
}
