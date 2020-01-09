package com.mod.rest.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.*;

import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.FileRetrieveImpl;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.LinkProvider;
import com.mod.rest.model.Meeting;
import com.mod.rest.model.Test;
import com.mod.rest.repository.MeetingRepository;
import com.mod.rest.service.PDFService;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.*;
import org.w3c.dom.Element;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by omar.sabry on 1/8/2020.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/pdf")
public class PDFController {

    @Autowired
    PDFService pdfService;

    @Autowired
    MeetingRepository meetingRepository;

    @GetMapping("export/{text}")
    @ResponseBody
    public String export(@PathVariable("text") String InputString) {
        Optional<Meeting> m = meetingRepository.getMeetingData(Long.parseLong("245763"));
//        convertHTMLToPDFCSSandIMG("pdf-template/meeting-template.html");
//        convertHTMLToPDF("pdf-template/meeting-template.html");

//        testReflection();
        return m.get().toString();
    }

    private void convertHTMLToPDF(String html){
        String dest = "C:/Users/omar.sabry/Desktop/test.pdf";
        File file = new File(dest);
        file.getParentFile().mkdirs();
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new FileInputStream(html), Charset.forName("UTF-8"));
        }
        catch (DocumentException e){
            System.out.println(e);
        }
        catch (IOException e){
            System.out.println(e);
        }
        document.close();
    }

    private void testPDF() throws Exception{
        String dest = "C:/Users/omar.sabry/Desktop/test.pdf";
        File file = new File(dest);
        file.getParentFile().mkdirs();

        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(8);
        for(int i = 0; i < 16; i++){
            table.addCell("hi");
        }
        document.add(table);
        document.close();
    }


    private void testReflection() {
        Test t1 = new Test();
        t1.setName("omar");
        t1.setId(1);
        t1.setStartDate(new Date());
        Test t2 = new Test();
        t2.setName("omar2");
        t2.setId(2);
        t2.setStartDate(new Date());
        Test t3 = new Test();
        t3.setName("omar3");
        t3.setId(3);
        t3.setStartDate(new Date());

        ArrayList<Test> tests = new ArrayList<>();

        tests.add(t1);
        tests.add(t2);
        tests.add(t3);

        try {
//            pdfService.generate(tests);
        }catch(Exception e){
            e.printStackTrace();
//            System.out.print(e);
        }
    }
}
