package com.mod.soap.dao.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.Iterator;
import java.util.Map;

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
    // 1:unit type code, 2: unit code,3: role name, 4: webservice, stored procedure
    public SecurityConfig(String unitTypeCode, String unitCode, String roleCode){
        this.securityQuery =  new SecurityQuery();
        this.unitCode = unitCode;
        this.unitTypeCode = unitTypeCode;
        this.roleCode = roleCode;
    }


    public SecurityConfig setSecurityType(int securityType){
        this.securityQuery.setSecurityType(securityType);
        return this;
    }

    public SecurityQuery newBuilder(User user){
        securityQuery.setUser(user);

        if (securityQuery.getSecurityType() == SecurityType.WEBSERVICE){
            processOutputNodeNew(output, 0);
        }else if (securityQuery.getSecurityType() == SecurityType.UNIT_TYPE_Code){
            String template = securityQuery.getSecurityType().getTemplate();
            securityQuery.setTemplate(template.replace("{code}", unitTypeCode));
        }else if(securityQuery.getSecurityType() == SecurityType.UNIT_CODE){
            String template = securityQuery.getSecurityType().getTemplate();
            securityQuery.setTemplate(template.replace("{code}", unitCode));
        }else if(securityQuery.getSecurityType() == SecurityType.ROLE_CODE){
            String template = securityQuery.getSecurityType().getTemplate();
            securityQuery.setTemplate(template.replace("{code}", roleCode));
        }




        return this.securityQuery;
    }


    public SecurityConfig build(int securityType){
        setSecurityType(securityType);
        processOutputNode(output, 0);
        return this;
    }


    private void processOutputNodeNew(JsonNode jsonNode, int depth) {

        if (depth == 1){
            System.out.println(securityQuery.getLastElement());
        }
        if (jsonNode.isValueNode()) {
            securityQuery.addNewValue(jsonNode.asText(), depth);
        } else if (jsonNode.isArray()) {
            for (JsonNode arrayItem : jsonNode) {
                if (arrayItem.isValueNode()) {
                    securityQuery.addNewValue(arrayItem.asText(), depth);
                }
                discoverOutputNodeNew(arrayItem, depth);
            }
        } else if (jsonNode.isObject()) {
            discoverOutputNodeNew(jsonNode, depth);
        }
    }

    private void discoverOutputNodeNew(JsonNode node, int depth) {
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> jsonField = fields.next();
            securityQuery.addNewKey(jsonField.getKey(), depth);
            processOutputNodeNew(jsonField.getValue(), depth+1);
        }
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
