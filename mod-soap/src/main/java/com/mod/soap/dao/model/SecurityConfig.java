package com.mod.soap.dao.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mod.soap.model.SecurityRequest;
import lombok.Data;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by karim on 2/9/20.
 */
@Data
public class SecurityConfig {
    String unitTypeCode;
    String unitCode;
    String roleCode;
    JsonNode webservice;
    JsonNode output;
    SecurityQuery securityQuery;


    public SecurityConfig(){
        this.securityQuery =  new SecurityQuery();
    }


    public SecurityConfig setSecurityType(int securityType){
        this.securityQuery.setSecurityType(securityType);
        return this;
    }


    public SecurityQuery build(User user){
        securityQuery.setUser(user);
        if (securityQuery.getSecurityType() == SecurityType.WEBSERVICE){
            processOutputNode(output, 0);
        }else if (securityQuery.getSecurityType() == SecurityType.UNIT_TYPE_Code){
            String template = securityQuery.getSecurityType().getTemplate();
            securityQuery.setTemplate(template.replace("{code}", unitTypeCode));
        }else if(securityQuery.getSecurityType() == SecurityType.UNIT_CODE){
            String template = securityQuery.getSecurityType().getTemplate();
            securityQuery.setTemplate(template.replace("{code}", unitCode));
        }else if(securityQuery.getSecurityType() == SecurityType.ROLE_CODE){
            String template = securityQuery.getSecurityType().getTemplate();
            securityQuery.setTemplate(template.replace("{code}", unitCode));
        }

        return this.securityQuery;
    }

    public SecurityConfig build(int securityType){
        setSecurityType(securityType);
        processOutputNode(output, 0);
        return this;
    }



    private void processOutputNode(JsonNode jsonNode, int depth) {
        if (jsonNode.isValueNode()) {
            securityQuery.addValue(jsonNode.asText());
        } else if (jsonNode.isArray()) {
            for (JsonNode arrayItem : jsonNode) {
                discoverOutputNode(arrayItem, depth);
            }
        } else if (jsonNode.isObject()) {
            discoverOutputNode(jsonNode, depth);
        }
    }

    private void discoverOutputNode(JsonNode node, int depth) {
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> jsonField = fields.next();
            securityQuery.addKey(jsonField.getKey());
            processOutputNode(jsonField.getValue(), depth+1);
        }

    }
}
