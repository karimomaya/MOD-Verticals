package com.mod.rest.controller;

import com.mod.rest.service.LoggerService;
import com.mod.rest.service.MinificationService;
import com.mod.rest.system.ResponseBuilder;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by karim.omaya on 1/16/2020.
 */

// https://javascript-minifier.com/java
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/compress")
public class CompressJSController {

    @Autowired
    LoggerService loggerService;


    @GetMapping("/file/{projectName}/{entityName}/{controllerName}")
    @ResponseBody
    public ResponseBuilder<String> getProjectsReportTable(@PathVariable("projectName") String projectName,
                                                          @PathVariable("entityName") String entityName,
                                                          @PathVariable("controllerName") String controllerName) {

        ResponseBuilder<String> responseBuilder = new ResponseBuilder<String>();

        StringBuilder fileContent = new StringBuilder();
        MinificationService minificationService = new MinificationService(projectName, entityName, controllerName, loggerService);
        minificationService.execute();

        return responseBuilder.data(fileContent.toString()).build();

    }

    /*public String content(String filename, ArrayList<String> downloadable, ArrayList<String> contentFile, Queue<String> waitingQueue) throws IOException {


        String fileContent = "";


        if (downloadable.contains(filename)) {
            int index = downloadable.indexOf(filename);
            downloadable.remove(index);
            contentFile.remove(index);
        }

        downloadable.add(filename);

        fileContent = Utils.minifier(filename);
        contentFile.add(fileContent);
//        String regexForControllerName = "(?<=dependency.execute\\(\")(.*)(?=\")";
        String regexForControllerName = "dependency.execute\\(\"[A-Za-z]+\"\\)";


        Pattern pattern = Pattern.compile(regexForControllerName);

        Matcher matcher = pattern.matcher(fileContent);
        String controllerName = null;
        while (matcher.find()) {
            controllerName = matcher.group();
//            System.out.println("I found the text "+matcher.group()+" starting at index "+
//                    matcher.start()+" and ending at index "+matcher.end());
        }

        if (controllerName == null) return fileContent;



        controllerName = controllerName.replaceAll("'", "\"");
        controllerName = controllerName.replaceAll("dependency.execute", "");
        controllerName = controllerName.replaceAll("[()]", "");
        controllerName = controllerName.replaceAll("\"", "");

        System.out.println("found Controller Name: "+ controllerName);

// 'dadad.js', "dasa.js"
        String regexForScripts = "(?<="+controllerName+"Scripts=)(.*)(?=.js(\"|')\\](,|;)+)";
        pattern = Pattern.compile(regexForScripts);

        matcher = pattern.matcher(fileContent);
        String scriptsArray = "";
        while (matcher.find()) {
            scriptsArray = matcher.group();
//            System.out.println("I found the text "+matcher.group()+" starting at index "+
//                    matcher.start()+" and ending at index "+matcher.end());
        }

        scriptsArray = scriptsArray.replaceAll("'", "");
        scriptsArray = scriptsArray.replaceAll("\"", "");
        scriptsArray = scriptsArray.replace("[", "");
        scriptsArray+= ".js";

        fileContent = fileContent.replace(scriptsArray, "");


        System.out.println("Detect Extra Scripts: "+scriptsArray+ " on controller : "+  controllerName);

        if (scriptsArray.equals(".js") ) return fileContent;

        String[] scripts = scriptsArray.split(",");
        for (int i=scripts.length -1; i>= 0 ; i--){
            String file = scripts[i].replace("/cordys/html5/demo/", "/home/karim/Desktop/Projects/UAE - MOD/demo/");
            content( file , downloadable, contentFile);
        }
        return fileContent;

    }*/

}
