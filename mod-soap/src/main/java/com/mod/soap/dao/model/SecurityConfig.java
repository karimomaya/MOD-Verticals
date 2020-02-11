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
    JsonNode webservice ;
    JsonNode output;
    SecurityQuery securityQuery;


    public SecurityConfig(){
        this.securityQuery =  new SecurityQuery();
    }


    public SecurityConfig setSecurityType(int securityType){
        this.securityQuery.setSecurityType(securityType);
        return this;
    }


    public SecurityConfig build(){
        processOutputNode(output, 0);
        return this;
    }

    public SecurityConfig build(int securityType){
        setSecurityType(securityType);
        processOutputNode(output, 0);
        return this;
    }

    public boolean execute(User user){
        if (SecurityType.ROLE_TYPE.WEBSERVICE == securityQuery.getSecurityType()){
            String template = securityQuery.getSecurityType().getTemplate();
            System.out.println(template);
            template = template.replace("{ticket}", user.getTicket());
            String methodName = webservice.findPath("method").asText();
            String namespace = webservice.findPath("namespace").asText();
            template = template.replaceAll("\\{method\\}", methodName);
            template = template.replace("{namespace}", namespace);

            ObjectMapper xmlMapper = new XmlMapper();

            try {
                String params = xmlMapper.writer().writeValueAsString(webservice.findPath("param"));
                params = params.replace("<ObjectNode>","");
                params = params.replace("</ObjectNode>","");

                template = template.replace("{params}", params);
                System.out.println(template);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
//            template = template.replace("{ticket}", user.getTicket());

//{ticket} {method}  {namespace} {params}
        }
        return false;
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
