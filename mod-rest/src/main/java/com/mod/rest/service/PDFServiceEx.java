package com.mod.rest.service;

import com.mod.rest.system.Utils;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service
public class PDFServiceEx implements PDFServiceI {
    @Autowired
    PDFService pdfService;

    public File generate(List<?> objects, String filename, String tagName) throws Exception {

        org.w3c.dom.Document document = Utils.convertFileToXMLDocument(filename);
        NodeList nodeList = document.getElementsByTagName(tagName);

        if (objects.size() == 0 || nodeList.getLength() == 0)
            return removeNodeByTagName(filename, tagName);

        String htmlTemplate = getInnerHTML(nodeList);


        deleteNodeList(document.getElementsByTagName(tagName + "-remover"));


        if (htmlTemplate == null) return removeNodeByTagName(filename, tagName);


        for (Object object : objects) {
            org.w3c.dom.Document tempDoc = Utils.convertStringToXMLDocument(htmlTemplate);

            NodeList tempNodeList = tempDoc.getElementsByTagName("attribute");
            ArrayList<String> attributes = new ArrayList<>();
            int tempNodeListSize = tempNodeList.getLength();
            for (int i = 0; i < tempNodeListSize; i++) {
                attributes.add(tempNodeList.item(i).getTextContent().trim());
            }


            deleteNodeList(tempNodeList);


            for (String attr : attributes) {
                Class cls = object.getClass();
                Method method = null;
                try {
                    method = cls.getMethod(attr);
                } catch (NoSuchMethodException ex) {
                    System.out.println("Attribute is not a list");
                }

                if (method == null) {
                    NodeList attrNodeList = tempDoc.getElementsByTagName(attr);
                    String nodeName = attrNodeList.item(0).getParentNode().getNodeName();

                    List<Object> o = new ArrayList<>();
                    o.add(object);

                    if (nodeName.equals("tbody")) {
                        pdfService.handlePDFTable(tempDoc, o, attrNodeList);
                    } else if (nodeName.equals("tr")) {
                        pdfService.handlePDFTableRow(tempDoc, o, attrNodeList);
                    } else if (nodeName.equals("img")) {
                        pdfService.handlePDFImage(tempDoc, o, attrNodeList);
                    } else {
                        pdfService.handlePDFTableParag(tempDoc, o, attrNodeList);
                    }

                    deleteNodeList(attrNodeList);

                } else {
                    tempDoc = getAttributeFromSubList(tempDoc, (List<?>) method.invoke(object), attr);

                }

            }

            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(Utils.convertXMLDocumentToString(document), "", Parser.xmlParser());


            org.jsoup.nodes.Element element = jsoupDoc.getElementsByTag(tagName + "-holder").get(0);

            element.append(Utils.getInnerHTML(tempDoc.getFirstChild()));
            document = Utils.convertStringToXMLDocument(jsoupDoc.html());
            System.out.println("updated document object");

        }


        deleteNodeList(document.getElementsByTagName(tagName));


        return Utils.writeXMLDocumentToTempFile(document);
    }


    public File removeNodeByTagName(String filename, String tagname) throws TransformerException, IOException {

        return pdfService.removeNodeByTagName(filename, tagname);
    }


    private org.w3c.dom.Document getAttributeFromSubList(org.w3c.dom.Document tempDoc, List<?> objects, String attr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        NodeList attrNodeList = tempDoc.getElementsByTagName(attr + "-attribute");


        ArrayList<String> attributes = new ArrayList<>();

        for (int i = 0; i < attrNodeList.getLength(); i++) {
            attributes.add(attrNodeList.item(i).getTextContent().trim());
        }


        deleteNodeList(attrNodeList);


        for (String attribute : attributes) {

            attrNodeList = tempDoc.getElementsByTagName(attribute);

            String nodeName = attrNodeList.item(0).getParentNode().getNodeName();
            if (attrNodeList.item(0).getAttributes().getLength() > 0) {
                tempDoc = pdfService.handleRedundancyTable(tempDoc, objects, attrNodeList, attribute);
            } else if (nodeName.equals("tbody")) {
                pdfService.handlePDFTable(tempDoc, objects, attrNodeList);

            } else if (nodeName.equals("tr")) {
                pdfService.handlePDFTableRow(tempDoc, objects, attrNodeList);
            } else {
                pdfService.handlePDFTableParag(tempDoc, objects, attrNodeList);
            }

            deleteNodeList(attrNodeList);

        }
        return tempDoc;
    }


    public byte[] generatePDF(String filename) {
        return pdfService.generatePDF(filename);

    }


    private String getInnerHTML(NodeList nodeList) {
        String htmlTemplate = "";
        boolean isFound = false;
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals("#text")) continue;

            htmlTemplate = Utils.getInnerHTML(nodeList.item(i)).trim();

            if (htmlTemplate.indexOf("<") != -1) {
                isFound = true;
                break;
            }
        }

        return (isFound) ? htmlTemplate : null;

    }


    private void deleteNodeList(NodeList nodeList) {
        while (nodeList.getLength() > 0) {
            nodeList.item(0).getParentNode().removeChild(nodeList.item(0));
        }
    }


}
