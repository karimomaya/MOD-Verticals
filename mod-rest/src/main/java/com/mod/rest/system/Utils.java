package com.mod.rest.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by karim.omaya on 10/30/2019.
 */
public class Utils {


    public static String convertXMLDocumentToString(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "html");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }


    public static File writeXMLDocumentToTempFile(Document document) throws TransformerException, IOException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        DOMSource source = new DOMSource(document);
        File file = File.createTempFile("template", ".html");
        System.out.println("create temp file on: " + file.getAbsolutePath());
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
//        BufferedWriter out = null;
//        FileWriter writer = new FileWriter(file);
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);

        return file;

    }

    public static String getInnerHTML(Node node) {
        DOMImplementationLS lsImpl = (DOMImplementationLS) node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
        LSSerializer lsSerializer = lsImpl.createLSSerializer();
        lsSerializer.getDomConfig().setParameter("xml-declaration", false);
        NodeList childNodes = node.getChildNodes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < childNodes.getLength(); i++) {
            sb.append(lsSerializer.writeToString(childNodes.item(i)));
        }
        return sb.toString();
    }


    public static String getHoursFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);
        String hour = (h <= 9) ? "0" + h : h + "";
        String minutes = (m <= 9) ? "0" + m : m + "";
        return hour + ":" + minutes;
    }

    public static String getArabicNameOfDay(Date date) {
        String[] days = {"الأحد", "الأثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int day = cal.get(Calendar.DAY_OF_WEEK);

        return days[day - 1];
    }

    public static String minifier(String fileLocation) throws IOException {
        final URL url = new URL("https://javascript-minifier.com/raw");
        String output = null;

        System.out.println("try to minify: " + fileLocation);

        byte[] bytes = Files.readAllBytes(Paths.get(fileLocation));

        final StringBuilder data = new StringBuilder();
        data.append(URLEncoder.encode("input", "UTF-8"));
        data.append('=');
        data.append(URLEncoder.encode(new String(bytes), "UTF-8"));

        bytes = data.toString().getBytes("UTF-8");

        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(bytes.length));

        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write(bytes);
        }

        final int code = conn.getResponseCode();


        if (code == 200) {
            System.out.println("----");
            final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {

                output = line;
            }
            in.close();

            System.out.println("\n----");
        } else {
            System.out.println("Oops");
        }
        return output;
    }

    public static boolean isArabicText(String s) {
        for (int i = 0; i < s.length(); ) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }

    public static int compareDates(Date date1, Date date2) {

        //date object is having 3 methods namely after,before and equals for comparing
        //after() will return true if and only if date1 is after date 2
        if (date1.after(date2)) {
            System.out.println("Date1 is after Date2");
            return 1;
        }

        //before() will return true if and only if date1 is before date2
        if (date1.before(date2)) {
            System.out.println("Date1 is before Date2");
            return 2;
        }

        //equals() returns true if both the dates are equal
        if (date1.equals(date2)) {
            System.out.println("Date1 is equal Date2");
            return 1;
        }

        return 0;
    }

    public static boolean isDateBetweenDates(Date date, Date startDate, Date endDate){
        if((date.after(startDate) || date.equals(startDate))
        && (date.before(endDate) || date.equals(endDate))) {
            return true;
        }

        return false;
    }

    public static String dateFormat(Date date, String format) {
        if (date == null) return "";
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }


    public static Document convertFileToXMLDocument(String filename) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(filename);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  static String getProgressColor(int progress, Date startDate, Date endDate ){

        String result = "neural"; // 0 neural; 1

        Date toddayDate = new Date();

        long diffTotal = Utils.differenceBetweenTwoDatesWithoutABS(startDate, endDate);
        long diffnow = Utils.differenceBetweenTwoDatesWithoutABS(startDate, toddayDate );


        if(diffTotal == 0){
            diffTotal= 1;
        }
        long  expectedProgress = (diffnow/ diffTotal)*100;



        if (progress == 100  ) {
            result = "neural";
        }
        else {
            result = getProgressHelper(progress, startDate, endDate);
        }

        return  result;

    }

    public static String getProgressHelper(int progress, Date startDate, Date endDate ) {

        String result = "neural"; // 0 neural; 1

        Date toddayDate = new Date();

        long diffTotal = Utils.differenceBetweenTwoDatesWithoutABS(startDate, endDate);
        long diffnow = Utils.differenceBetweenTwoDatesWithoutABS(startDate, toddayDate);


        if (diffTotal == 0) {
            diffTotal = 1;
        }
        if (diffnow == 0) {
            diffnow = 1;
        }
        long expectedProgress = (long)(((float)diffnow / (float)diffTotal) * 100);


//        if (startDate.compareTo(toddayDate) > 0 ) {
//            result = "neural";
//        }
        if(progress == 100){
            result = "neural";
        }
        else  if (progress < expectedProgress + 10 && progress > expectedProgress - 10) {
            result = "expected";
        } else if (progress > expectedProgress) {
            result = "exceed";
        } else if (progress < expectedProgress) {
            result = "underestimated";
        } else result = "expected";

        return result;

    }
    public static Document convertStringToXMLDocument(String xmlString) {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            //here the img tag exception rise on org.w3c.dom.Document xmlString: <img> & after parse: <img />
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Pagination generatePagination(int pageNumber, int pageSize, long numberOfResult) {
        Pagination pagination = new Pagination();
        pagination.setPageNumber(pageNumber).setNumberOfResults(numberOfResult).setPageSize(pageSize).build();
        return pagination;
    }

    public static int getDayNameFromDate(Date date) { // ["Sun", "Mon", "Tues", "Wed", "Thu", "Fri", "Sat"]
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1; //,
    }

    public static int getDayFromDate(Date date) { // ["Sun", "Mon", "Tues", "Wed", "Thu", "Fri", "Sat"]
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);


        return cal.get(Calendar.DAY_OF_MONTH) - 1; // to start from 0 not one
    }

    public static Date convertStringToDate(String dateStr) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date result = new Date();
        try {
            result = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long differenceBetweenTwoDates(Date one, Date two) {
        long diffInMillies = Math.abs(two.getTime() - one.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static long differenceBetweenTwoDatesWithoutABS(Date one, Date two) {
        return (long) ((two.getTime() - one.getTime()) / (3600 * 24 * 1000));
    }

    public static java.sql.Date convertJavaDateToSQLDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static int getMonthFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static String writeObjectIntoString(Object object) {
        String result = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String convertDateToArabic(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd");
        String dateString = sdf.format(date);
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < dateString.length(); i++) {
//            if (dateString.charAt(i) == '-') {
//                builder.append(" , ");
//            } else
            if (Character.isDigit(dateString.charAt(i))) {
                builder.append(arabicChars[(int) (dateString.charAt(i)) - 48]);
            } else {
                builder.append(dateString.charAt(i));
            }
        }
        dateString = builder.toString();
        return dateString;
//        Locale arabicLocale = Locale.forLanguageTag("ar");
//        DateTimeFormatter arabicDateFormatter
//                = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
//                .withLocale(arabicLocale)
//                .withDecimalStyle(DecimalStyle.of(arabicLocale));
//        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        return localDate.format(arabicDateFormatter);
    }

    public static String convertDateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    public static String convertDateToString(Date date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public static String removeNullValue(String value){
        if (value == null) return "";
        if (value.equals("null")){
            return "";
        }
        return value;
    }

    public static ArrayList<String> getYearlyMonthBetweenDates(Date startDate, Date endDate){
        DateFormat formater = new SimpleDateFormat("MMM-yyyy");
        //endDate.setMonth(endDate.getMonth()+1);

        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();

        ArrayList<String> Dates = new ArrayList<>();
//        try {
            beginCalendar.setTime(startDate);
            beginCalendar.set(Calendar.DAY_OF_MONTH,1);
            finishCalendar.setTime(endDate);
            int lastDayOfMonth = finishCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            finishCalendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
//            if(((Timestamp) startDate).toLocalDateTime().getMonth().getValue() != ((Timestamp) endDate).toLocalDateTime().getMonth().getValue()) {
//                if(((Timestamp) startDate).toLocalDateTime().getMonth().getValue() != 12 )
//                    finishCalendar.add(Calendar.MONTH, 1);
//            }

//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        while (beginCalendar.before(finishCalendar)) {
            // add one month to date per loop
            String date = formater.format(beginCalendar.getTime()).toUpperCase();
            Dates.add(date);
            beginCalendar.add(Calendar.MONTH, 1);
        }
        return Dates;
    }
}
