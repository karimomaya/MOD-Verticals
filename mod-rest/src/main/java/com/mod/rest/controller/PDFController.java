package com.mod.rest.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.*;

import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
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
import com.mod.rest.model.DesignSeal;
import com.mod.rest.model.IdentificationCard;
import com.mod.rest.model.Meeting;
import com.mod.rest.model.Test;
import com.mod.rest.repository.DesignSealRepository;
import com.mod.rest.repository.IdentificationCardRepository;
import com.mod.rest.repository.MeetingRepository;
import com.mod.rest.service.PDFService;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    IdentificationCardRepository identificationCardRepository;
    @Autowired
    DesignSealRepository designSealRepository;

    @GetMapping("export/{text}")
    @ResponseBody
    public String export(@PathVariable("text") String InputString) {
        Optional<Meeting> m = meetingRepository.getMeetingData(Long.parseLong("245763"));
//        convertHTMLToPDFCSSandIMG("pdf-template/meeting-template.html");
//        convertHTMLToPDF("pdf-template/meeting-template.html");

//        testReflection();

        generatePDF("");
        return m.get().toString();
    }

    public byte[] generatePDF(String filename) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String source = "C:/Users/karim.omaya/Desktop/test.html";
        String dest = "C:/Users/karim.omaya/Desktop/test.pdf";
        File file = new File(source);
        File tempFile = null;
        try {
            Document document = new Document();

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
            writer.setInitialLeading(12.5f);

            XMLWorkerFontProvider fontProvider =
                    new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);

//            fontProvider.register("resources/fonts/NotoNaskhArabic-Regular.ttf");
            fontProvider.register("C:/Windows/Fonts/arial.ttf");
            CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);

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
            p.parse(new FileInputStream(file), Charset.forName("UTF-8"));

            document.close();
        }
        catch (IOException e){
            System.out.println(e);
        }
        catch (DocumentException e){
            System.out.println(e);
        }
        return byteArrayOutputStream.toByteArray();


    }

    private void convertHTMLToPDF(String html){
        String source = "C:/Users/karim.omaya/Desktop/test.html";
        String dest = "C:/Users/karim.omaya/Desktop/test.pdf";
        File file = new File(source);
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new FileInputStream(file), Charset.forName("UTF-8"));
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

    @GetMapping("IdentificationCard/{identificationCardId}")
    @ResponseBody
    public ResponseEntity<byte[]> generateIdentificationCardPDF(@PathVariable("identificationCardId") long identificationCardId){

        HttpHeaders respHeaders = new HttpHeaders();

        Optional<IdentificationCard> identificationCardOptional = identificationCardRepository.getIdentificationCardData(identificationCardId);

        if (identificationCardOptional.isPresent()) {

            IdentificationCard identificationCard = identificationCardOptional.get();

            ArrayList<IdentificationCard> arrayList = new ArrayList();
            arrayList.add(identificationCard);

            String templateName = pdfService.getTemplateName(identificationCard);
            System.out.println("get template name: " + templateName);

            try {
                File file = pdfService.generate(arrayList,"pdf-template/"+templateName+".html", "card-identification");
                file = pdfService.generate(arrayList, file.toURI().getPath(), "card-identification-note");

                byte[] bytes = pdfService.generatePDF(file.getAbsolutePath());
                respHeaders.setContentLength(bytes.length);
                respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attachment.pdf" );

                return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"null\"").body(null); // used to download file
    }

    @GetMapping("DesignSeal/{designSealId}")
    @ResponseBody
    public ResponseEntity<byte[]> generateDesignSealPDF(@PathVariable("designSealId") long designSealId){

        HttpHeaders respHeaders = new HttpHeaders();

        Optional<DesignSeal> designSealOptional = designSealRepository.getDesignSealData(designSealId);

        if (designSealOptional.isPresent()) {

            DesignSeal designSeal = designSealOptional.get();

            ArrayList<DesignSeal> arrayList = new ArrayList();
            arrayList.add(designSeal);

            String templateName = pdfService.getTemplateName(designSeal);
            System.out.println("get template name: " + templateName);

            try {
                File file = pdfService.generate(arrayList,"pdf-template/"+templateName+".html", "design-seal");
                file = pdfService.generate(arrayList, file.toURI().getPath(), "design-seal-note");

                byte[] bytes = pdfService.generatePDF(file.getAbsolutePath());
                respHeaders.setContentLength(bytes.length);
                respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attachment.pdf" );

                return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"null\"").body(null); // used to download file
    }
}
