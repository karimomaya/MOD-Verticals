package com.mod.rest.system;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.StringReader;
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
