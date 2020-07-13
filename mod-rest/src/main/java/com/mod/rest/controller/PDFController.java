package com.mod.rest.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.*;

import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
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
import com.mod.rest.service.DocManagementService;
import com.mod.rest.service.PDFService;
import com.mod.rest.system.Config;
import com.mod.rest.system.Utils;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    @Autowired
    Config config;

//    @Autowired
//    DocManagementService docManService;


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
        String source = "F:\\test HTML.html";

        org.jsoup.nodes.Document document = Jsoup.parse(filename);
        document.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        document.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml);
        document.outputSettings().charset("UTF-8");
        System.out.print(document.html());
        Path path = Paths.get(source);
        byte[] strToBytes = document.html().getBytes();

        try {
            Files.write(path, strToBytes);
            String dest = "F:\\new.pdf";
            File file = new File(source);
            File tempFile = null;
            try {
                Document document2 = new Document();
//                Rectangle two = new Rectangle(297,420);
//                document2.setPageSize(two);
                document2.setPageSize(PageSize.A3);
                document2.setMargins(5, 5, 5, 5);

                PdfWriter writer = PdfWriter.getInstance(document2, new FileOutputStream(dest));
                writer.setInitialLeading(15.5f);

                XMLWorkerFontProvider fontProvider =
                        new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);

//            fontProvider.register("resources/fonts/NotoNaskhArabic-Regular.ttf");
//                fontProvider.register("F:\\MOD-Verticals-dev\\mod-rest\\resources\\fonts\\NotoNaskhArabic-Regular.ttf");
                fontProvider.register("C:\\Users\\ahmed\\WebstormProjects\\appworks-dev-server-ftp\\commons\\styles\\fonts\\univers-next-arabic-regular.ttf", "univers");
                CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);

                document2.open();

                CSSResolver cssResolver =
                        XMLWorkerHelper.getInstance().getDefaultCssResolver(false);
                FileRetrieve retrieve = new FileRetrieveImpl("pdf-template");
                cssResolver.setFileRetrieve(retrieve);


//                CSSResolver cssResolver = new StyleAttrCSSResolver();
//
//                CssFile cssFile = XMLWorkerHelper.getCSS(new FileInputStream("C:\\Users\\ahmed\\WebstormProjects\\appworks-dev-server-ftp\\commons\\styles\\bootstrap.min_ar.css"));
//                cssResolver.addCss(cssFile);
//                cssFile = XMLWorkerHelper.getCSS(new FileInputStream("C:\\Users\\ahmed\\WebstormProjects\\appworks-dev-server-ftp\\commons\\styles\\MOD_ar.css"));
//                cssResolver.addCss(cssFile);
//                cssFile = XMLWorkerHelper.getCSS(new FileInputStream("F:\\displayTables.css"));
//                cssResolver.addCss(cssFile);

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

                PdfWriterPipeline pdf = new PdfWriterPipeline(document2, writer);
                HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
                CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

                XMLWorker worker = new XMLWorker(css, true);
                XMLParser p = new XMLParser(worker);


                p.parse(new FileInputStream(file), Charset.forName("UTF-8"));

                document2.close();
            } catch (IOException e) {
                System.out.println(e);
            } catch (DocumentException e) {
                System.out.println(e);
            }
            return byteArrayOutputStream.toByteArray();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
//

    }

    private void convertHTMLToPDF(String html) {
        String source = "C:/Users/karim.omaya/Desktop/test.html";
        String dest = "C:/Users/karim.omaya/Desktop/test.pdf";
        File file = new File(source);
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new FileInputStream(file), Charset.forName("UTF-8"));
        } catch (DocumentException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
        document.close();
    }

    private void testPDF() throws Exception {
        String dest = "C:/Users/omar.sabry/Desktop/test.pdf";
        File file = new File(dest);
        file.getParentFile().mkdirs();

        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(8);
        for (int i = 0; i < 16; i++) {
            table.addCell("hi");
        }
        document.add(table);
        document.close();
    }

//
//    private void testReflection() {
//        Test t1 = new Test();
//        t1.setName("omar");
//        t1.setId(1);
//        t1.setStartDate(new Date());
//        Test t2 = new Test();
//        t2.setName("omar2");
//        t2.setId(2);
//        t2.setStartDate(new Date());
//        Test t3 = new Test();
//        t3.setName("omar3");
//        t3.setId(3);
//        t3.setStartDate(new Date());
//
//        ArrayList<Test> tests = new ArrayList<>();
//
//        tests.add(t1);
//        tests.add(t2);
//        tests.add(t3);
//
//        try {
////            pdfService.generate(tests);
//        } catch (Exception e) {
//            e.printStackTrace();
////            System.out.print(e);
//        }
//    }

    @GetMapping("IdentificationCard/{identificationCardId}")
    @ResponseBody
    public ResponseEntity<byte[]> generateIdentificationCardPDF(@PathVariable("identificationCardId") long identificationCardId) {

        HttpHeaders respHeaders = new HttpHeaders();

        Optional<IdentificationCard> identificationCardOptional = identificationCardRepository.getIdentificationCardData(identificationCardId);

        if (identificationCardOptional.isPresent()) {

            IdentificationCard identificationCard = identificationCardOptional.get();

            ArrayList<IdentificationCard> arrayList = new ArrayList();
            arrayList.add(identificationCard);

            String templateName = pdfService.getTemplateName(identificationCard);
            System.out.println("get template name: " + templateName);

            try {
                File file = pdfService.generate(arrayList, "pdf-template/" + templateName + ".html", "card-identification");
                file = pdfService.generate(arrayList, file.toURI().getPath(), "card-identification-note");

                byte[] bytes = pdfService.generatePDF(file.getAbsolutePath());
                respHeaders.setContentLength(bytes.length);
                respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attachment.pdf");

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
    public ResponseEntity<byte[]> generateDesignSealPDF(@PathVariable("designSealId") long designSealId) {

        HttpHeaders respHeaders = new HttpHeaders();

        Optional<DesignSeal> designSealOptional = designSealRepository.getDesignSealData(designSealId);

        if (designSealOptional.isPresent()) {

            DesignSeal designSeal = designSealOptional.get();

            ArrayList<DesignSeal> arrayList = new ArrayList();
            arrayList.add(designSeal);

            String templateName = pdfService.getTemplateName(designSeal);
            System.out.println("get template name: " + templateName);

            try {
                File file = pdfService.generate(arrayList, "pdf-template/" + templateName + ".html", "design-seal");
                file = pdfService.generate(arrayList, file.toURI().getPath(), "design-seal-note");

                byte[] bytes = pdfService.generatePDF(file.getAbsolutePath());
                respHeaders.setContentLength(bytes.length);
                respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attachment.pdf");

                return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"null\"").body(null); // used to download file
    }


    //generate PDF file from HTML string and return it
    @SuppressWarnings("Duplicates")
    @PostMapping("generatePDF")
    @ResponseBody
    public void generatePDFFileFromHTML(@RequestParam String htmlString) {
//        HttpHeaders respHeaders = new HttpHeaders();


        generatePDF(htmlString);
//        byte[] bytes = convertHTMLToStringToPDF(htmlString);
//        if (bytes != null) {
//            respHeaders.setContentLength(bytes.length);
//            respHeaders.setContentType(MediaType.APPLICATION_PDF);
//            respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//            respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf");
//
//            return new ResponseEntity<>(bytes, respHeaders, HttpStatus.OK);

//        }


//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"null\"").body(null); // used to download file

    }


    @SuppressWarnings("Duplicates")
    public byte[] convertHTMLToStringToPDF(String htmlString) {

        File temp = null;
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");


        String fileName = df.format(date);

        try {
            temp = File.createTempFile(fileName, ".pdf");
            temp.deleteOnExit();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (temp != null) {


                try (OutputStream os = new FileOutputStream(temp.getAbsolutePath())) {
                    System.out.println(temp.getAbsolutePath());
//                    PdfRendererBuilder builder = new PdfRendererBuilder();
//                    builder.useFastMode();
//                    builder.useUnicodeBidiSplitter(new ICUBidiSplitter.ICUBidiSplitterFactory());
//                    builder.useUnicodeBidiReorderer(new ICUBidiReorderer());
//                    builder.defaultTextDirection(BaseRendererBuilder.TextDirection.RTL); // OR RTL
//                    builder.useFont(new File("F:\\univers-next-arabic-regular.ttf"), "univers");
                    //                builder.withHtmlContent(htmlString,"http://appworks-dev:81");
//                    builder.withHtmlContent(htmlString, config.getProperty("domain"));

                    //                builder.withW3cDocument()
                    //                builder.withUri("file:/F:/testsource.html");
//                    org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlString);
//                    org.w3c.dom.Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
//
//                    builder.withW3cDocument(w3cDoc, config.getProperty("domain"));
//                    builder.toStream(os);
//
//
//                    builder.run();
//
//                    return Files.readAllBytes(Path.of(temp.toURI()));

                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    //generate PDF file from HTML string and return it
    @SuppressWarnings("Duplicates")
    @PostMapping("saveToCS")
    @ResponseBody
    public void savePDFFileToCSNode(@RequestParam String htmlString, @RequestParam String nodeId) {
//        HttpHeaders respHeaders = new HttpHeaders();
//        nodeId = String.valueOf(2000);
        byte[] bytes = convertHTMLToStringToPDF(htmlString);
        File temp = null;
        if (bytes != null) {
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
            String fileName = df.format(date);
            try {
                temp = File.createTempFile(fileName + "-report", ".pdf");
                temp.deleteOnExit();

                Files.write(Path.of(temp.toURI()), bytes);
//                docManService.uploadDocument(temp, Long.parseLong(nodeId));

            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }


}
