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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.rmi.CORBA.Util;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * Created by omar.sabry on 1/8/2020.
 */

@Service
public class PDFService {

    @Autowired
    Environment config;

    public File generate(List<?> objects, String filename, String tagName) throws Exception {
        if (objects.size() == 0)
            return new File(filename);
        System.out.println("generate tag name: " + tagName + " using filename: " + filename);

        org.w3c.dom.Document document = Utils.convertFileToXMLDocument(filename);
        NodeList nodes = document.getElementsByTagName(tagName+"-replacer");
        if (nodes.getLength() == 0) return new File(filename);

        String nodeName = nodes.item(0).getParentNode().getNodeName();

        if (nodeName.equals("tbody")){
            Node tableBody =  nodes.item(0).getParentNode();

            for(Object object : objects) {
                Class cls = object.getClass();
                // create TR
                Element tr = document.createElement("tr");
                boolean notAddd = false;
                for(int i = 0 ; i < nodes.getLength() ; i++){
                    // create TD
                    Element td = document.createElement("td");

//                    if (i ==0 ) td.setAttribute("class", "padding");

                    try {
                        Method method =  cls.getMethod(nodes.item(i).getTextContent());
                        Object o = method.invoke(object);

                        if (o == null)  o = "";

                        String innerText = "";

                        if (o instanceof Date) {
                            innerText = Utils.dateFormat( (Date) o,config.getProperty("date.format"));
                        }else {
                            innerText = o.toString();
                        }

                        if (!o.equals("")){
                            if (Utils.isArabicText(innerText)){
//                                td.setAttribute("dir", "rtl");
                            }else {
                                td.setAttribute("class", "english-font");
                            }

                        }

                        td.setTextContent(innerText);
                        tr.appendChild(td);
                    }catch (Exception ex){
                        notAddd = true;
                    }

                }
                if (!notAddd) tableBody.appendChild(tr);
            }


            int length = nodes.getLength();
            for(int i = 0 ; i < length ; i++){
                tableBody.removeChild(nodes.item(0));
            }
        }
        else {
            for(Object object: objects){
                Class cls = object.getClass();
                int length = nodes.getLength();
                for(int i = 0 ; i < length ; i++){
                    Node parent = nodes.item(0).getParentNode();

                    Element div = document.createElement("span");
                    div.setAttribute("dir", "rtl");
                    Method method =  cls.getMethod(nodes.item(0).getTextContent());
                    Object o = method.invoke(object);

                    if (o == null)  o = "";

                    String innerText = "";

                    if (o instanceof Date) {
                        innerText = Utils.dateFormat( (Date) o,config.getProperty("date.format"));
                    }else {
                        innerText = o.toString();
                    }


                    div.setTextContent(innerText);

                    parent.appendChild(div);
                    parent.removeChild(nodes.item(0));
                }
            }
//            System.out.println("else");
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        DOMSource source = new DOMSource(document);

        File file = File.createTempFile("template", ".html");


        System.out.println("create temp file on: " + file.getAbsolutePath());

        Writer writer = null;
        BufferedWriter out = null;

        writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
//        FileWriter writer = new FileWriter(file);
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);





        return file;
    }

    public String getTemplateName(Object object){
        Class cls = object.getClass();
        Method[] methods = cls.getMethods();

        Annotation annotation =  cls.getAnnotation(PDFResources.class);

        String templateName = "";

        if(annotation instanceof PDFResources){
            templateName = ((PDFResources) annotation).key();
        }
        return templateName;
    }

    public byte[] convertHTMLToPDF(String filename){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();


        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document,byteArrayOutputStream);

            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new FileInputStream(filename), Charset.forName("UTF-8"));

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] generatePDF(String filename) {
        System.out.println("generate PDF using file name: " + filename);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        File tempFile = null;
        try {
            Document document = new Document();

            PdfWriter writer = PdfWriter.getInstance(document,byteArrayOutputStream);
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
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println(e);
        }
        catch (DocumentException e){
            e.printStackTrace();
            System.out.println(e);
        }
        return byteArrayOutputStream.toByteArray();


    }
}
