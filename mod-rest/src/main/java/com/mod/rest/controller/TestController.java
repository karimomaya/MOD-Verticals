package com.mod.rest.controller;

import com.mod.rest.model.Task;
import com.mod.rest.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by karim.omaya on 12/10/2019.
 */
@RestController
public class TestController {



    @Autowired
    TaskRepository taskRepository;


    @GetMapping(path="/", produces = "application/json")
    public Task greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return taskRepository.findById(new Long(163954689)).get();
    }

    @GetMapping(path = "/requestNumber", produces = "application/json")
    public String requestNumber(@RequestParam(value="name", defaultValue="World") String name) {

        String pattern = "date:MM-YYYY-dd#user:id#type:Task#split:-";

        Pattern splitPattern = Pattern.compile("#split:");
        Matcher matcher = splitPattern.matcher(pattern);
        String split = "-";

        while (matcher.find()) {

            split = pattern.substring(matcher.end(), pattern.length());
            System.out.println(split);
            pattern = pattern.substring(0, matcher.start());
            System.out.print("Start index: " + matcher.start());
            System.out.print(" End index: " + matcher.end());
            System.out.println(" Found: " + matcher.group());
        }

        String[] patternArray = pattern.split("#");

        Pattern datePattern = Pattern.compile("date:");
        Pattern userPattern = Pattern.compile("user:");
        Pattern typePattern = Pattern.compile("type:");
        String output = "";


        for (String word : patternArray) {

            Matcher dateMattacher = datePattern.matcher(word);
            Matcher userMattacher = userPattern.matcher(word);
            Matcher typeMattacher = typePattern.matcher(word);

            if (dateMattacher.matches()) {
                word = word.substring(dateMattacher.end(), word.length());
                output = convertPatternToDate(word);
            } else if (userMattacher.matches()) {
                word = word.substring(userMattacher.end(), word.length());
            }else if (typeMattacher.matches()) {
                word = word.substring(typeMattacher.end(), word.length());
            }
        }
        return output;
    }

    public String convertPatternToDate(String pattern){
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        return dateFormat.format(date); //2016/11/16 12:08:43
    }

    public String userPattern(){
        return null;
    }
}
