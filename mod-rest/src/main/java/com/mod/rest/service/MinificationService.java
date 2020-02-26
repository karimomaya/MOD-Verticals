package com.mod.rest.service;

import com.mod.rest.system.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by karim on 2/3/20.
 */
public class MinificationService {
    ArrayList<String> downloadedFiles;
    Queue<String> waitingQueue;
    LoggerService loggerService;


    static final String outputFile = "newController.js";

    public MinificationService(String projectName, String entityName, String mainController, LoggerService loggerService){
        this.loggerService = loggerService;
        downloadedFiles = new ArrayList<String>();
        waitingQueue = new LinkedList<String>();

        String[] dependency = new String[]{"/cordys/thirdparty/jquery/jquery.js",
                "/cordys/html5/cordys.html5sdk.js",
                "/cordys/html5/demo/GeneralSYS_GENERAL/pages/navbar/controller/navbar-controller.js",
                "/cordys/html5/demo/commons/javascripts/ui/bootstrap.bundle.min.js",
                "/cordys/html5/demo/commons/javascripts/ui/selectize.js",
                "/cordys/html5/demo/commons/javascripts/ui/main.js",
                "/cordys/html5/demo/commons/javascripts/ui/owl.carousel.min.js",
                "/cordys/html5/demo/commons/javascripts/ui/sidebar.js",
                "/cordys/html5/demo/commons/javascripts/ui/wow.min.js",
                "/cordys/html5/demo/commons/javascripts/request.js",
                "/cordys/html5/demo/commons/javascripts/translate.js",
                "/cordys/html5/demo/commons/javascripts/security.js",
                "/cordys/html5/demo/commons/javascripts/cordys.js",
                "/cordys/html5/demo/commons/javascripts/system.js",
                "/cordys/html5/demo/commons/javascripts/dom.js",
                "/cordys/html5/demo/commons/javascripts/knockout-3.5.0-min.js",
                "/cordys/html5/demo/commons/javascripts/knockout.validation.js",
                "/cordys/html5/demo/commons/javascripts/validation.js",
                "/cordys/html5/demo/commons/javascripts/configURL.js",
                "/cordys/html5/demo/commons/javascripts/crypto.js",
                "/cordys/html5/demo/commons/javascripts/ui/gijgo.min.js",
                "/cordys/html5/demo/commons/javascripts/ui/jquery.flexslider.js"};
        //    "/cordys/html5/demo/commons/javascripts/ui/snackbar.js"]


        String controllerPath = "/home/karim/Desktop/Projects/UAE - MOD/demo/"+projectName+"/pages/"+entityName+"/controller/"+mainController+".js";
        waitingQueue.add(controllerPath);
        init();


    }

    public MinificationService(String projectName, String entityName, String mainController){

        String controllerName = "/home/karim/Desktop/Projects/UAE - MOD/demo/"+projectName+"/pages/"+entityName+"/controller/"+mainController+".js";
        waitingQueue.add(controllerName);
    }

    public boolean execute(){

        if (waitingQueue.size() == 0){
            return true;
        }

        String fileLocation = waitingQueue.poll();

        try {

            if (!downloadedFiles.contains(fileLocation)){
                String fileContent = Utils.minifier(fileLocation);
                writeToLog('i',  "minimized file: "+ fileLocation );
                downloadedFiles.add(fileLocation);


                String controllerName = getControllerName(fileContent);

                if (controllerName != null){
                    String scriptsStr = getScripts(fileContent, controllerName);
                    fileContent = fileContent.replace(scriptsStr, "[]");
                    if (scriptsStr.equals(".js") ){
                        writeToLog('i', "No Scripts found for:  " + controllerName);
                    }else {
                        String[] scripts = scriptsStr.split(",");
                        for (int i=0 ; i<  scripts.length ; i++){
                            String fileLoc = scripts[i].replace("/cordys/html5/demo/", "/home/karim/Desktop/Projects/UAE - MOD/demo/");
                            waitingQueue.add(fileLoc);
                            writeToLog('i', "found Scripts: " + fileLoc);

                        }
                    }
                }

                writeOutput(fileContent);
                execute();


//                if (controllerName == null){
//                    writeOutput(fileContent);
//                    execute();
//                }else {
//
//                    String scriptsStr = getScripts(fileContent, controllerName);
//
//                    fileContent = fileContent.replace(scriptsStr, "[]");
//                    writeOutput(fileContent);
//
//                    if (scriptsStr.equals(".js") ){
//                        writeToLog('i', "No Scripts found for:  " + controllerName);
//                        execute();
//                    }else {
//                        String[] scripts = scriptsStr.split(",");
//                        for (int i=0 ; i<  scripts.length ; i++){
//                            String fileLoc = scripts[i].replace("/cordys/html5/demo/", "/home/karim/Desktop/Projects/UAE - MOD/demo/");
//                            waitingQueue.add(fileLoc);
//                            writeToLog('i', "found Scripts: " + fileLoc);
//
//                        }
//                        execute();
//                    }
//                }




                System.out.println("found Controller Name: "+ controllerName);
            }
            else {
                writeToLog('i', "redundancy file: " + fileLocation );
            }
///home/karim/Desktop/Projects/UAE - MOD/demo/TaskManagement/pages/task/controller/task-index-controller.js.js

        } catch (IOException e) {
            writeToLog('e',e.toString());
        }

        return true;
    }

    private String getScripts(String fileContent, String controllerName){
        String regexForScripts = "(?<="+controllerName+"Scripts=)(.*)(?=.js(\"|')\\](,|;)+)";
        Pattern pattern = Pattern.compile(regexForScripts);

        Matcher matcher = pattern.matcher(fileContent);
        String scriptsStr = "";
        while (matcher.find()) {
            scriptsStr = matcher.group();
        }

        scriptsStr = scriptsStr.replaceAll("'", "");
        scriptsStr = scriptsStr.replaceAll("\"", "");
        scriptsStr = scriptsStr.replace("[", "");
        scriptsStr+= ".js";
        return scriptsStr;
    }


    private String getControllerName(String fileContent){
        String regexForControllerName = "dependency.execute\\(\"[A-Za-z]+\"\\)";
        Pattern pattern = Pattern.compile(regexForControllerName);

        Matcher matcher = pattern.matcher(fileContent);
        String controllerName = null;

        while (matcher.find()) {
            controllerName = matcher.group();
        }

        if (controllerName == null) return null;

        controllerName = controllerName.replaceAll("'", "\"");
        controllerName = controllerName.replaceAll("dependency.execute", "");
        controllerName = controllerName.replaceAll("[()]", "");
        controllerName = controllerName.replaceAll("\"", "");
        return controllerName;
    }

    private boolean writeOutput(String data){
        File file = new File("newController.js");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fr.write(data);
                fr.close();
                return true;
            } catch (IOException e) {
                writeToLog('e',e.toString());
                e.printStackTrace();
            }

        }

        return false;
    }

    private void init(){

        writeToLog('i', "Start Minification Service");
        File file = new File(outputFile);

        if(file.delete())
        {
            System.out.println("File deleted successfully");
        }
        else
        {
            File yourFile = new File(outputFile);
            try {
                yourFile.createNewFile(); // if file already exists will do nothi
            } catch (IOException e) {
                writeToLog('e',e.toString());
                e.printStackTrace();
            }
        }

    }

    private void writeToLog(char type,String text){
        if (loggerService == null) return;

        loggerService.write(type, text);
    }
}
