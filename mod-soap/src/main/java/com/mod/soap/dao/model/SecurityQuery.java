package com.mod.soap.dao.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mod.soap.system.Utils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.Instant;
import java.util.*;

/**
 * Created by karim on 2/10/20.
 */
@Data
@Slf4j
public class SecurityQuery {
    private SecurityType securityType;
    private Map<String, Object> orOperator;
    private Map<String, Object> andOperator;
    private Map<String, Object> anonymousAssertion;

    Stack<QueryHandler> stackHolder = new Stack<>();
    Queue<String> operator = new LinkedList<>();
    private String newKey;

    ArrayList<String> functionsKeywords = new ArrayList<>(Arrays.asList("$biggerThanOrEqual", "$biggerThan", "$smallerThanOrEqual", "$smallerThan",
            "$biggerThanDate", "$smallerThanOrEqualToDate", "$smallerThanDate" , "$biggerThanOrEqualToDate"));



    private String template;
    private User user;
    JsonNode webservice;
    private String key;

    public SecurityQuery() {
        orOperator = new HashMap<>();
        andOperator = new HashMap<>();
        anonymousAssertion = new HashMap<>();
    }

    public String getLastElement() {
        QueryHandler queryHandler = stackHolder.peek();
        return queryHandler.getKey();
    }


    public SecurityQuery addNewKey(String key, int depth){
        System.out.println(key + " found on depth: " + depth);
        stackHolder.add(new QueryHandler(key, depth));
        return this;
    }

    public SecurityQuery addNewValue(Object object, int depth){
        QueryHandler queryHandler = stackHolder.peek();
        if (queryHandler.getValue() != null){
            QueryHandler newQueryHandler = new QueryHandler(queryHandler.getKey(), object, depth);
            stackHolder.add(newQueryHandler);
        }
        else {
            queryHandler.setValue(object);
        }

        return this;
    }


    public SecurityQuery setUser(User user){
        this.user = user;
        return this;
    }


    public boolean evaluateNewWebserviceResponse(String res){

        NodeList nl= getsNodeList(res);
        Stack<QueryHandler> queryHandlers =  produceQueryHandlerStack(nl);

        QueryHandler oldValue = null;
        QueryHandler oldOperator = null;
        int lastDepth = 0;
        PriorityQueue<QueryHandler> finalDecision = new PriorityQueue<>(new QueryHandlerCompartor());
        PriorityQueue<QueryHandler> finalDecisionOperator = new PriorityQueue<>(new QueryHandlerCompartor());
        if (queryHandlers.size() == 1) {
            QueryHandler qhandler = queryHandlers.pop();
            return qhandler.getKey() == "true";
        } else if(queryHandlers.size() > 0){
            while (queryHandlers.size() > 0){
                QueryHandler qhandler = queryHandlers.pop();

                if (oldValue == null && !qhandler.getKey().equals("or") && !qhandler.getKey().equals("and") ){
                    oldValue = qhandler;
                }else if (oldOperator == null && (qhandler.getKey().equals("or") || qhandler.getKey().equals("and")) ) {
                    oldOperator = qhandler;
                }else if (oldOperator == null && oldValue != null){
                    String key  = (evaluate(oldValue.getKey(), qhandler.getKey(), "and"))? "true" : "false";
                    oldValue = new QueryHandler(key, "", qhandler.getDepth() );


                    oldOperator = null;

                }else {
                    if (lastDepth < qhandler.getDepth()) {
                        String key  = (evaluate(oldValue.getKey(), qhandler.getKey(), oldOperator.getKey()))? "true" : "false";
                        oldValue = new QueryHandler(key, "", qhandler.getDepth() );
                        lastDepth = qhandler.getDepth();
                    }else {
                        finalDecision.add(oldValue);
                        finalDecisionOperator.add(oldOperator);

                        oldValue = qhandler;
                        lastDepth = 0;
                    }

                    oldOperator = null;
                }


            }
        }else {
            return false;
        }


        if (finalDecision.size() > 0){
            finalDecision.add(oldValue);
        }

        while (finalDecision.size() > 1){

            QueryHandler t =  finalDecisionOperator.poll();
            String o = t.getKey();
            Integer d = t.getDepth();
            oldValue.setKey((evaluate(finalDecision.poll().getKey(), finalDecision.poll().getKey(),o ))? "true" : "false");
            oldValue.setDepth(d);
            finalDecision.add(oldValue);

        }
        boolean evaluator = oldValue.getKey() == "true";

        if (!evaluator) evaluator = oldValue.getKey().equals("true");

        return evaluator;
    }


    private Stack<QueryHandler> produceQueryHandlerStack(NodeList results){
        ArrayList<AbstractMap.SimpleEntry<String, Object>> conditions = new ArrayList<>();

        Stack<QueryHandler> queryHandlers = new Stack<>();

        String operator = "";

        while (stackHolder.size() > 0){
            QueryHandler queryHandler = stackHolder.pop();
            if (queryHandler.getKey().equals("or") || queryHandler.getKey().equals("and")){

                if (conditions.size() > 0) {
                    boolean result = false;

                    if ((queryHandler.getKey().equals("or") || queryHandler.getKey().equals("and")) && conditions.size() == 1){
                        result = evaluateOrOperatorFromXMLUsingHashMap(results, conditions);

                        String key = (result) ? "true"  : "false";
                        queryHandlers.add(new QueryHandler(key, operator,  queryHandler.getDepth(), conditions));

                        queryHandlers.add(queryHandler);

                    }
                    else {
                        if (queryHandler.getKey().equals("or")) {
                            result = evaluateOrOperatorFromXMLUsingHashMap(results, conditions);
                            operator = "or";
                        }else if (queryHandler.getKey().equals("and")){
                            result = evaluateAndOperatorFromXMLUsingHashMap(results, conditions);
                            operator = "and";
                        }

                        String key = (result) ? "true"  : "false";
                        queryHandlers.add(new QueryHandler(key, operator,  queryHandler.getDepth(), conditions));
                    }


                        conditions = new ArrayList<>();
                }else {
                    queryHandlers.add(queryHandler);
                }

            }else {
                AbstractMap.SimpleEntry<String, Object> condition = new AbstractMap.SimpleEntry<String, Object>(queryHandler.getKey(), queryHandler.getValue());
                conditions.add(condition);
            }
        }

        return queryHandlers;
    }

    private boolean evaluate(String val1, String val2, String operator){
        boolean v1 = (val1.equals("true"))? true : false;
        boolean v2 = (val2.equals("true"))? true : false;

        if (operator.equals("and")) {
            return v1 & v2;
        }else {
            return v1 || v2;
        }
    }

    private boolean evaluateOrOperatorFromXMLUsingHashMap(NodeList nl, ArrayList<AbstractMap.SimpleEntry<String, Object>> evaluator){

        boolean evaluate = false;

        if(evaluator.size() == 0) return true;

        for (AbstractMap.SimpleEntry<String, Object> s : evaluator){
            String output = "";
            for(int k=0;k<nl.getLength();k++){
                output = getTagByName((Node)nl.item(k), (String) s.getKey(), "");
                    if (!output.equals("")) break;
            }
            if (output == ""){
                evaluate |= false;
            }else {
                String predictedOutput = getRealValue((String) s.getValue());
                if (containsFunctionsKeywords(predictedOutput)) {
                    evaluate |= executeFunction(predictedOutput, output);
                } else if (output.equals(predictedOutput)) {
                    evaluate |= true;
                } else {
                    evaluate |= false;
                }
            }

        }
        return evaluate;
    }

    private boolean evaluateAndOperatorFromXMLUsingHashMap(NodeList nl, ArrayList<AbstractMap.SimpleEntry<String, Object>> evaluator){
        boolean evaluate = true;

        for (AbstractMap.SimpleEntry<String, Object> s : evaluator){
            String output = "";
            for(int k=0;k<nl.getLength();k++){
                output =  getTagByName((Node)nl.item(k), (String) s.getKey(), "");
                if (!output.equals("")) break;
            }
            if (output.equals("")){
                evaluate = evaluate && false;
            }else {
                String predictedOutput = getRealValue((String)s.getValue());
                if (containsFunctionsKeywords(predictedOutput)){
                    evaluate = evaluate && executeFunction(predictedOutput, output);
                }
                else if (output.equals(predictedOutput)) {
                    evaluate = evaluate && true;
                }
                else  {
                    evaluate = evaluate && false;
                }
            }

        }
        return evaluate;
    }


    public boolean evaluateWebserviceResponse(String res){

        Document dom = Utils.convertStringToXMLDocument(res);
        NodeList nl=dom.getDocumentElement().getChildNodes();

        boolean evaluator = evaluateOrOperatorFromXML(nl);
        evaluator &= evaluateAndOperatorFromXML(nl);
        return evaluator;
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
                if (functionsKeywords.contains(predictedOutput)){
                    evaluate = evaluate && executeFunction(predictedOutput, (String)mapElement.getValue());
                } else if (output.equals(predictedOutput)) {
                    evaluate = evaluate && true;
                } else  {
                    evaluate = evaluate && false;
                }
            }

        }
        return evaluate;
    }


    private boolean containsFunctionsKeywords(String input){
        if (input.contains("$biggerThanOrEqual") || input.contains("$equal") || input.contains("$biggerThan") || input.contains("$smallerThanOrEqual") || input.contains("$smallerThan") )
            return true;
        return false;
    }

    private boolean executeFunction(String input, String value){

        if (input.contains("$biggerThanOrEqualToDate")){
            input = getValueFromFunction(input, "$biggerThanOrEqualToDate");
            Date fstVal = Date.from( Instant.parse((input)));
            Date secondVal = Date.from(Instant.parse((value)));
            return (secondVal.compareTo(fstVal) == 1 ||  secondVal.compareTo(fstVal) == 0);
        }else if(input.contains("$biggerThanDate")){
            input = getValueFromFunction(input, "$biggerThanDate");
            Date fstVal = Date.from( Instant.parse((input)));
            Date secondVal = Date.from(Instant.parse((value)));
            return (secondVal.compareTo(fstVal) == 1 );
        }
        else if(input.contains("$smallerThanOrEqualToDate")){
            input = getValueFromFunction(input, "$smallerThanOrEqualToDate");
            Date fstVal = Date.from( Instant.parse((input)));
            Date secondVal = Date.from(Instant.parse((value)));
            return (secondVal.compareTo(fstVal) == -1  ||  secondVal.compareTo(fstVal) == 0);
        } else if(input.contains("$smallerThanDate")){
            input = getValueFromFunction(input, "$smallerThanDate");
            Date fstVal = Date.from( Instant.parse((input)));
            Date secondVal = Date.from(Instant.parse((value)));
            return (secondVal.compareTo(fstVal) == -1  );
        } else if (input.contains("$biggerThanOrEqual")){
            input = getValueFromFunction(input, "$biggerThanOrEqual");
            int fstVal = convertStringToInt(input);
            int secondVal = convertStringToInt(value);
            if (fstVal == -1111 || secondVal == -1111 ) return false;
            return (fstVal <= secondVal);
        } else if (input.contains("$biggerThan")){
            input = getValueFromFunction(input, "$biggerThan");
            int fstVal = convertStringToInt(input);
            int secondVal = convertStringToInt(value);
            if (fstVal == -1111 || secondVal == -1111 ) return false;
            return (fstVal < secondVal);
        } else if (input.contains("$smallerThanOrEqual")){
            input = getValueFromFunction(input, "$smallerThanOrEqual");
            int fstVal = convertStringToInt(input);
            int secondVal = convertStringToInt(value);
            if (fstVal == -1111 || secondVal == -1111 ) return false;
            return (fstVal >= secondVal);
        } else if (input.contains("$smallerThan")){
            input = getValueFromFunction(input, "$smallerThan");
            int fstVal = convertStringToInt(input);
            int secondVal = convertStringToInt(value);
            if (fstVal == -1111 || secondVal == -1111 ) return false;
            return (fstVal > secondVal);
        }else if (input.contains("$equal")){
            input = getValueFromFunction(input, "$equal");
            int fstVal = convertStringToInt(input);
            int secondVal = convertStringToInt(value);
            if (fstVal == -1111 || secondVal == -1111 ) return false;
            return (fstVal == secondVal);
        }
        return false;
    }

    private String getValueFromFunction(String val, String split) {
        val = val.replace(split+"(", "");
        return val.replace(")", "");
    }


    private int convertStringToInt(String val){
        int result = -1111;
        try {
            result = Integer.parseInt(val.trim());

        }
        catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException: " + nfe.getMessage());
        }
        return result;
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

    public ArrayList<String> getTagsByName(Node nodes, String tagName, ArrayList<String> value){
        if (nodes.getNodeName().equals(tagName)){
            System.out.println("found :) ");
            System.out.println(nodes.getNodeName()+" : "+nodes.getTextContent());
            value.add(nodes.getTextContent());
        }
        if(nodes.hasChildNodes()  || nodes.getNodeType()!=3){

            NodeList nl=nodes.getChildNodes();
            for(int j=0;j<nl.getLength();j++) value =  getTagsByName(nl.item(j), tagName, value);
        }
        return value;
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

    public boolean evaluateRoleCode(){
        String[] targets = template.split(",");
        boolean evaluate = false;
        if (user.getRoleCode() == null) return false;
        for (int i= 0; i< targets.length; i++){
            if (user.getRoleCode().equals(targets[i])) evaluate = true;
        }
        return evaluate;
    }

    public boolean evaluateUnitCode(){
        String[] targets = template.split(",");
        boolean evaluate = false;
        if (user.getUserUnitCode() == null) return false;
        for (int i= 0; i< targets.length; i++){
            if (user.getUserUnitCode().equals(targets[i])) evaluate = true;
        }
        return evaluate;
    }

    public boolean evaluateUnitTypeCode(){
        String[] targets = template.split(",");
        boolean evaluate = false;
        if (user.getUserUnitTypeCode() == null) return false;
        for (int i= 0; i< targets.length; i++){
            if (user.getUserUnitTypeCode().equals(targets[i])) evaluate = true;
        }
        return evaluate;
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


    private String getRealValue(String value){
        if (value.equals("$userId")) {
            return user.getId()+"";
        }else if (value.equals("$userPath")){
            return user.getUnitPathById();
        }else if (value.equals("$roleName")){
            return user.getRoleName();
        }else if (value.equals("$userRoleCode")){
            return user.getRoleCode();
        }else if (value.equals("$unitTypeCode")){
            return user.getUserUnitTypeCode();
        }else if (value.equals("$unitCode")){
            return user.getUserUnitCode();
        }else if (value.equals("$username")){
            return user.getUsername();
        }else if (value.equals("$displayName")){
            return user.getDisplayName();
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
            this.securityType = SecurityType.ROLE_CODE;
        } else if(securityType == 2){
            this.securityType = SecurityType.UNIT_CODE;
        } else if(securityType == 1){
            this.securityType = SecurityType.UNIT_TYPE_Code;
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

    public NodeList getsNodeList(String xml) {
        Document dom = Utils.convertStringToXMLDocument(xml);
        return dom.getDocumentElement().getChildNodes();
    }



    public class QueryHandler{
        String key;
        Object value;
        int depth;
        ArrayList<AbstractMap.SimpleEntry<String, Object>> eval;

        public QueryHandler(String key, Object value, int depth){
            this.key = key;
            this.value = value;
            this.depth = depth;

        }

        public QueryHandler(String key, Object value, int depth,  ArrayList<AbstractMap.SimpleEntry<String, Object>> eval){
            this.key = key;
            this.value = value;
            this.depth = depth;
            this.eval = eval;
        }

        public QueryHandler(String key,  int depth){
            this.key = key;
            this.depth = depth;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }
    }


    class QueryHandlerCompartor implements Comparator<QueryHandler> {
        public int compare(QueryHandler q1, QueryHandler q2)
        {
            Integer firstDepth;
            Integer secondDepth;
            firstDepth = q1.depth;
            secondDepth = q2.depth;
            return secondDepth.compareTo(firstDepth);
        }
    }

}
