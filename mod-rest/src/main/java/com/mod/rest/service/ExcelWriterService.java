package com.mod.rest.service;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.model.Task;
import com.mod.rest.model.TaskReport;
import com.mod.rest.repository.UserRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by karim.omaya on 12/21/2019.
 */
@Service
public class ExcelWriterService {

    @Autowired
    UserRepository userRepository;


    public File generate(List<?> objectList){

        File tempFile = null;

        if (objectList.size() == 0) return tempFile;

        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = getSheetName(objectList.get(0));
        XSSFSheet sheet = workbook.createSheet(sheetName);

        Row row = sheet.createRow(0);
        createHeader(objectList.get(0), row);
        try {
            createBody(objectList, sheet);
            tempFile = File.createTempFile(sheetName, ".xlsx");
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            workbook.write(outputStream);
            workbook.close();
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
        return cls.getName();
    }

    public void createHeader(Object object, Row row){
        int colNum = 0;
        Class cls = object.getClass();


        cls = classFactory(cls, object);

        Method[] methods = cls.getMethods();
        for (Method method : methods){
            ColumnName annotation = method.getAnnotation(ColumnName.class);

            if (annotation ==null) continue;

            Cell cell = row.createCell(colNum++);
            cell.setCellValue((String) annotation.key());
        }
    }

    public void createBody(List<?> objects, XSSFSheet sheet) throws InvocationTargetException, IllegalAccessException {
        int rowNum = 1;

        for (Object object : objects){
            List<Object> helper = null;
            Row row = sheet.createRow(rowNum++);

            Class cls = object.getClass();

            if (cls.getName().equals("com.mod.rest.model.Task")){

                helper = new ArrayList<>();
                TaskReport taskReport = new TaskReport((Task) object, userRepository);
                helper.add(taskReport);
                cls = taskReport.getClass();
                object = taskReport;
            }


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
                    cell.setCellValue((Date) o);
                }else if (o instanceof Long) {
                    cell.setCellValue((Long) o);
                }

            }

        }
    }


    private  Class<?> classFactory(Class<?> cls, Object object){
        if (cls.getName().equals("com.mod.rest.model.Task")){
            TaskReport taskReport = new TaskReport((Task) object, userRepository);
            return taskReport.getClass();
        }

        return cls;
    }
}
