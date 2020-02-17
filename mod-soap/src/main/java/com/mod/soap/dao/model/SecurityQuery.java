package com.mod.soap.dao.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mod.soap.system.Utils;
import lombok.Data;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by karim on 2/10/20.
 */
@Data
public class SecurityQuery {
    private SecurityType securityType; //
    private Map<String, Object> orOperator;
    private Map<String, Object> andOperator;
    private Map<String, Object> anonymousAssertion;
    private String template;
    private User user;
    JsonNode webservice;
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

    public SecurityQuery setUser(User user){
        this.user = user;
        return this;
    }

    public boolean evaluateWebserviceResponse(String res){

        Document dom = Utils.convertStringToXMLDocument(res);
        NodeList nl=dom.getDocumentElement().getChildNodes();

        boolean evaluator = evaluateOrOperatorFromXML(nl);
        evaluator &= evaluateAndOperatorFromXML(nl);
        return evaluator;
    }

    public boolean evaluateRoleCode(){
        String[] targets = template.split(",");
        boolean evaluate = false;
        for (int i= 0; i< targets.length; i++){
            if (user.getRoleCode().equals(targets[i])) evaluate = true;
        }
        return evaluate;
    }

    public boolean evaluateUnitCode(){
        String[] targets = template.split(",");
        boolean evaluate = false;
        for (int i= 0; i< targets.length; i++){
            if (user.getUserUnitCode().equals(targets[i])) evaluate = true;
        }
        return evaluate;
    }

    public boolean evaluateUnitTypeCode(){
        int target = convertCodeToId(template);
        int current = convertCodeToId(user.UserUnitTypeCode);
        if(current >= target) return true;
        return false;
    }

    private int convertCodeToId(String code){
        switch(code){
            case "MSM": // وزير
                return 9;
            case "USM": // وكيل وزارة
                return 8;
            case "AAM": // وكيل مساعد
                return 7;
            case "EXM": // رئيس إدارة تنفيذية
                return 6;
            case "OFC": //مكتب
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


    private boolean evaluateAndOperatorFromXML(NodeList nl){
        boolean evaluate = true;
        Iterator andIterator = andOperator.entrySet().iterator();

        while (andIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)andIterator.next();
            String output = "";
            for(int k=0;k<nl.getLength();k++){
                output =  getTagByName((Node)nl.item(k), (String) mapElement.getKey(), "");
                if (!output.equals("")) break;
            }
            if (output.equals("")){
                evaluate = evaluate && false;
            }else {
                String predictedOutput = getRealValue((String)mapElement.getValue());
                if (output.equals(predictedOutput)) {
                    evaluate = evaluate && true;
                }
                else  {
                    evaluate = evaluate && false;
                }
            }

        }
        return evaluate;
    }

    private boolean evaluateOrOperatorFromXML(NodeList nl){
        boolean evaluate = false;
        Iterator orIterator = orOperator.entrySet().iterator();

        if(orOperator.size() == 0) return true;

        while (orIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)orIterator.next();
            String output = "";
            for(int k=0;k<nl.getLength();k++){
                output =  getTagByName((Node)nl.item(k), (String) mapElement.getKey(), "");
                if (!output.equals("")) break;
            }
            if (output.equals("")){
                evaluate |= false;
            }else {
                String predictedOutput = getRealValue((String)mapElement.getValue());
                if (output.equals(predictedOutput)) {
                    evaluate |= true;
                }
                else  {
                    evaluate |= false;
                }
            }

        }
        return evaluate;
    }


    public String getTagByName(Node nodes, String tagName, String value){
        if (nodes.getNodeName().equals(tagName)){
            System.out.println("found :) ");
            System.out.println(nodes.getNodeName()+" : "+nodes.getTextContent());
            value =  nodes.getTextContent();
        }
        if(nodes.hasChildNodes()  || nodes.getNodeType()!=3){

            NodeList nl=nodes.getChildNodes();
            for(int j=0;j<nl.getLength();j++) value =  getTagByName(nl.item(j), tagName, value);
        }
        return value;
    }


    private String getRealValue(String value){
        if (value.equals("id")) {
            return user.getId()+"";
        }else if (value.equals("RoleName")){
            return user.getRoleName();
        }else if (value.equals("UserRoleCode")){
            return user.getRoleCode();
        }
        return value;

    }

    public SecurityQuery setWebservice(JsonNode jsonNode){
        this.webservice = jsonNode;
        return this;
    }


    public void setSecurityType(int securityType){
        if (securityType == 4){
            this.securityType = SecurityType.WEBSERVICE;
        }else if(securityType == 3){
            this.securityType = SecurityType.UNIT_TYPE_Code;
        } else if(securityType == 2){
            this.securityType = SecurityType.UNIT_CODE;
        } else if(securityType == 1){
            this.securityType = SecurityType.ROLE_CODE;
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



    public String prepareWebservice(){
        String template = securityType.getTemplate();
        template = updateWebserviceTemplate(template);
        return template;
    }


    private String updateWebserviceTemplate(String template) {

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

        return template;
    }

    public final boolean equals(Object other) { return this==other; }


}
