package com.mod.rest.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.FileRetrieveImpl;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.LinkProvider;
import com.mod.rest.annotation.PDFResources;
import com.mod.rest.system.Utils;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//import org.w3c.dom.ls.DOMImplementationLS;
//import org.w3c.dom.ls.LSSerializer;

import javax.xml.transform.*;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.util.regex.Pattern;

/**
 * Created by omar.sabry on 1/8/2020.
 */

@Service
public class PDFService implements PDFServiceI {

    @Autowired
    Environment config;

    public File removeNodeByTagName(String filename, String tagname) throws TransformerException, IOException {
        org.w3c.dom.Document document = Utils.convertFileToXMLDocument(filename);
        try {
            Node element = document.getElementsByTagName(tagname).item(0);
            element.getParentNode().removeChild(element);
        } catch (Exception ex) {
        }
        return Utils.writeXMLDocumentToTempFile(document);

    }

    protected void handlePDFTable(org.w3c.dom.Document document, List<?> objects, NodeList nodes) {
        Node tableBody = nodes.item(0).getParentNode();
        handleTableBody(document, objects, nodes);

        int length = nodes.getLength();
        for (int i = 0; i < length; i++) {
            tableBody.removeChild(nodes.item(0));
        }
    }

    public String getFileName(Object object){
            Class cls = object.getClass();
            String filename= "";
            try {
                filename = config.getProperty( cls.getSimpleName());
            }catch (Exception ex){
                filename = cls.getSimpleName();
            }
            return filename;
    }

    public File generate(List<?> objects, String filename, String tagName) throws Exception {
        System.out.println("generate tag name: " + tagName + " using filename: " + filename);

        org.w3c.dom.Document document = Utils.convertFileToXMLDocument(filename);
        NodeList nodes = document.getElementsByTagName(tagName + "-replacer");

        if (objects.size() == 0 || nodes.getLength() == 0)
            return removeNodeByTagName(filename, tagName);

        String nodeName = nodes.item(0).getParentNode().getNodeName();
        if (nodeName.equals("tbody")) {
            handlePDFTable(document, objects, nodes);
        } else if (nodeName.equals("tr")) {
            handlePDFTableRow(document, objects, nodes);
        } else if (nodeName.equals("img")){
            handlePDFImage(document,objects,nodes);
        }else {
            handlePDFTableParag(document, objects, nodes);
        }


        return Utils.writeXMLDocumentToTempFile(document);
    }

    protected void handlePDFTableParag(org.w3c.dom.Document document, List<?> objects, NodeList nodes) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Object object : objects) {
            Class cls = object.getClass();
            int length = nodes.getLength();
            for (int i = 0; i < length; i++) {
                Node parent = nodes.item(0).getParentNode();
                boolean isEnglishFont = false;

                Element div = document.createElement("p");
                div.setAttribute("dir", "rtl");
                Method method = cls.getMethod(nodes.item(0).getTextContent().trim());
                Object o = method.invoke(object);

                String innerText = extractText(o);

                if (o == null) innerText = "";
                else if (!o.equals("")) {
                    if (Utils.isArabicText(innerText)) {
                        div.setAttribute("dir", "rtl");
                    } else {
                        isEnglishFont = true;
                        div.setAttribute("class", "english-font");
                    }
                }

                String[]  innerTexts = innerText.split("\\n");

                if (innerTexts.length > 1){
                    for (int j=0 ; j< innerTexts.length; j++){
                        Element p = document.createElement("p");
                        p.setAttribute("dir", "rtl");
                        if (isEnglishFont) p.setAttribute("class","english-font");

                        p.setTextContent(innerTexts[j]);
                        parent.appendChild(p);
                    }
                }else {
                    div.setTextContent(innerText);
                    parent.appendChild(div);
                }


//                div.setNodeValue(innerText);


                parent.removeChild(nodes.item(0));
            }
        }
    }

    protected void handlePDFTableRow(org.w3c.dom.Document document, List<?> objects, NodeList nodes) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Object object : objects) {
            Class cls = object.getClass();

            int length = nodes.getLength();
            for (int i = 0; i < length; i++) {
                Node parent = nodes.item(0).getParentNode();
                Element td = document.createElement("td");

                Method method = cls.getMethod(nodes.item(0).getTextContent());
                Object o = method.invoke(object);
                if (o == null) o = "";

                String innerText = extractText(o);

                if (!o.equals("")) {
                    if (Utils.isArabicText(innerText)) {
                        td.setAttribute("dir", "rtl");
                    } else {
                        td.setAttribute("class", "english-font");
                    }

                }
                td.setTextContent(innerText);
                parent.appendChild(td);
                parent.removeChild(nodes.item(0));
            }
        }
    }

    protected void handlePDFImage(org.w3c.dom.Document document, List<?> objects, NodeList nodes) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Object object : objects) {
            Class cls = object.getClass();

            int length = nodes.getLength();
            for (int i = 0; i < length; i++) {
                Element parent = (Element) nodes.item(0).getParentNode();
//                Element img = document.createElement("img");

                Method method = cls.getMethod(nodes.item(0).getTextContent());
                Object o = method.invoke(object);
                if (o == null) o = "";

                if (!o.equals("")) {
                    String imgSource = extractText(o);
                    parent.setAttribute("src", imgSource);
                }
//                parent.appendChild(img);
                parent.removeChild(nodes.item(0));
            }
        }
    }

    private String extractText(Object o) {
        if (o == null) o = "";
        String innerText = "";
        if (o instanceof Date) {
            innerText = Utils.dateFormat((Date) o, config.getProperty("date.format"));
        } else {
            innerText = o.toString();
        }
        return innerText;
    }


    private void handleTableBody(org.w3c.dom.Document document, List<?> objects, NodeList nodes) {
        Node tableBody = nodes.item(0).getParentNode();
        for (Object object : objects) {
            Class cls = object.getClass();

            Element tr = document.createElement("tr");
            boolean notAddd = false;
            for (int i = 0; i < nodes.getLength(); i++) {
                Element td = document.createElement("td");

                try {
                    Method method = cls.getMethod(nodes.item(i).getTextContent());
                    Object o = method.invoke(object);

                    if (o == null) o = "";

                    String innerText = extractText(o);

                    if (!o.equals("")) {
                        if (Utils.isArabicText(innerText)) {
                            td.setAttribute("dir", "rtl");
                        } else {
                            td.setAttribute("class", "english-font");
                        }

                    }

                    td.setTextContent(innerText);
                    tr.appendChild(td);
                } catch (Exception ex) {
                    notAddd = true;
                }

            }
            if (!notAddd) tableBody.appendChild(tr);
        }

    }


    public String getTemplateName(Object object) {
        Class cls = object.getClass();
        Method[] methods = cls.getMethods();

        Annotation annotation = cls.getAnnotation(PDFResources.class);

        String templateName = "";

        if (annotation instanceof PDFResources) {
            templateName = ((PDFResources) annotation).key();
        }

        String templatePath = config.getProperty("pdf-path");
        return templatePath + templateName;
    }

    public byte[] generatePDF(String filename) {
        System.out.println("generate PDF using file name: " + filename);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        File tempFile = null;
        try {
            Document document = new Document();

            PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
            writer.setInitialLeading(12.5f);

            XMLWorkerFontProvider fontProvider =
                    new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);

            System.out.println("try to read font: NotoNaskhArabic");
            fontProvider.register("resources/fonts/NotoNaskhArabic-Regular.ttf");
//            fontProvider.register("C:/Windows/Fonts/arabtype.ttf");
            System.out.println("read Success");
            System.out.println("try to apply CSS");
            CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
            System.out.println("apply Success");

            document.open();

            CSSResolver cssResolver =
                    XMLWorkerHelper.getInstance().getDefaultCssResolver(false);
            FileRetrieve retrieve = new FileRetrieveImpl("pdf-template");
            cssResolver.setFileRetrieve(retrieve);

            HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            htmlContext.setImageProvider(new AbstractImageProvider() {
                public String getImageRootPath() {
                    return "pdf-template";
                }
            });
            htmlContext.setLinkProvider(new LinkProvider() {
                public String getLinkRoot() {
                    return "pdf-template";
                }
            });

            PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

            XMLWorker worker = new XMLWorker(css, true);
            XMLParser p = new XMLParser(worker);
            p.parse(new FileInputStream(filename), Charset.forName("UTF-8"));

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        } catch (DocumentException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return byteArrayOutputStream.toByteArray();


    }


}


