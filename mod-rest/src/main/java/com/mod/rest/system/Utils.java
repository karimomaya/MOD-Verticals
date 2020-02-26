package com.mod.rest.system;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by karim.omaya on 10/30/2019.
 */
public class Utils {
    public static String getHoursFromDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);
        String hour = (h <= 9)? "0"+ h: h+"";
        String minutes = (m <= 9)? "0"+ m: m+"";
        return hour +":" + minutes;
    }

    public static String getArabicNameOfDay(Date date){
        String[] days= {"الأحد", "الأثنين", "الثلاثاء" , "الأربعاء" , "الخميس" , "الجمعة", "السبت"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int day = cal.get(Calendar.DAY_OF_WEEK);

        return days[day-1];
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
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }

    public static int compareDates(Date date1,Date date2) {

        //date object is having 3 methods namely after,before and equals for comparing
        //after() will return true if and only if date1 is after date 2
        if(date1.after(date2)){
            System.out.println("Date1 is after Date2");
            return 1;
        }

        //before() will return true if and only if date1 is before date2
        if(date1.before(date2)){
            System.out.println("Date1 is before Date2");
            return 2;
        }

        //equals() returns true if both the dates are equal
        if(date1.equals(date2)){
            System.out.println("Date1 is equal Date2");
            return 1;
        }

        return 0;
    }

    public static String dateFormat(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }


    public static Document convertFileToXMLDocument(String filename){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(filename);
            return doc;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            return doc;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Pagination generatePagination(int pageNumber, int pageSize, long numberOfResult){
        Pagination pagination = new Pagination();
        pagination.setPageNumber(pageNumber).setNumberOfResults(numberOfResult).setPageSize(pageSize).build();
        return pagination;
    }

    public static int getDayNameFromDate(Date date){ // ["Sun", "Mon", "Tues", "Wed", "Thu", "Fri", "Sat"]
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) -1; //,
    }

    public static int getDayFromDate(Date date){ // ["Sun", "Mon", "Tues", "Wed", "Thu", "Fri", "Sat"]
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH)- 1; // to start from 0 not one
    }

    public static Date convertStringToDate(String dateStr){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date result = new Date();
        try {
            result = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long differenceBetweenTwoDates(Date one, Date two){
        long diffInMillies = Math.abs(two.getTime() - one.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static long differenceBetweenTwoDatesWithoutABS(Date one, Date two){
        return (long)((two.getTime()-one.getTime())/(3600*24*1000));
    }

    public static java.sql.Date convertJavaDateToSQLDate(Date date){
        return new java.sql.Date(date.getTime());
    }

    public static int getMonthFromDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static String writeObjectIntoString(Object object){
        String result = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
