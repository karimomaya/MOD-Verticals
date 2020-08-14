package com.mod.rest.service;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.repository.UserRepository;
import com.mod.rest.system.Utils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

/**
 * Created by karim.omaya on 12/21/2019.
 */
@Service
public class ExcelWriterService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    Environment config;
    XSSFCellStyle style;


    public File generate(List<?> objectList){

        File tempFile = null;

        if (objectList.size() == 0) return tempFile;

        tempFile = executeGenerate(objectList, getSheetName(objectList.get(0)));

        return tempFile;
    }

    public File generate(List<?> objectList, String fileName){
        File tempFile = null;

        if (objectList.size() == 0) return tempFile;

        tempFile = executeGenerate(objectList, fileName);

        return tempFile;
    }

    public File executeGenerate(List<?> objectList, String fileName){

        File tempFile = null;

        if (objectList.size() == 0) return tempFile;

        style = null;
        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = fileName;
        XSSFSheet sheet = workbook.createSheet(sheetName);
        if (style == null){
            style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
        }

        Row row = sheet.createRow(0);
        createHeader(objectList.get(0), row);
        try {
            createBody(objectList, sheet);
//            tempFile = File.createTempFile(sheetName, ".xlsx");
            tempFile = new File(System.getProperty("java.io.tmpdir"), sheetName+".xlsx");
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            workbook.write(outputStream);
            workbook.close();

//            String pathOfFile = tempFile.getAbsolutePath();
//            pathOfFile = pathOfFile.substring(0, pathOfFile.lastIndexOf("\\")+1);

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    public String getSheetName(Object object){
        Class cls = object.getClass();
        String filename= "";
        try {
            filename = config.getProperty( cls.getSimpleName());
        }catch (Exception ex){
            filename = cls.getSimpleName();
        }
        if (filename == null) filename = cls.getSimpleName();
        return filename;
    }

    public void createHeader(Object object, Row row){
        int colNum = 0;
        Class cls = object.getClass();

        Method[] methods = cls.getMethods();
        for (Method method : methods){
            ColumnName annotation = method.getAnnotation(ColumnName.class);

            if (annotation ==null) continue;

            Cell cell = row.createCell(colNum++);
            cell.setCellValue((String) annotation.key());
            cell.setCellStyle(style);
        }
    }

    public void createBody(List<?> objects, XSSFSheet sheet) throws InvocationTargetException, IllegalAccessException {
        int rowNum = 1;

        for (Object object : objects){
            List<Object> helper = null;
            Row row = sheet.createRow(rowNum++);

            Class cls = object.getClass();




            Method[] methods = cls.getMethods();
            int colNum = 0;
            for (Method method : methods){
                ColumnName annotation = method.getAnnotation(ColumnName.class);
                if (annotation ==null) continue;

                Cell cell = row.createCell(colNum++);

                Object o = method.invoke(object);
                if (o == null) o ="";

                if (o instanceof String) {
                    cell.setCellValue((String) o);
                }else if (o instanceof Integer) {
                    cell.setCellValue((Integer) o);
                }else if (o instanceof Date) {
                    cell.setCellValue((String)Utils.dateFormat((Date) o, config.getProperty("date.format")));
                }else if (o instanceof Long) {
                    cell.setCellValue((Long) o);
                }

            }

        }
    }


}
