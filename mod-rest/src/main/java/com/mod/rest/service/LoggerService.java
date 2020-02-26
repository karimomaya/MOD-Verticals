package com.mod.rest.service;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by karim on 2/3/20.
 */

@Service
public class LoggerService {
//    FileHandler fh;
//    Logger logger;
    File file;
    FileWriter fr;
    BufferedWriter br;

    public LoggerService(){
//        logger = Logger.getLogger("MyLog");
//        try {
//            fh = new FileHandler("MyLogFile.log");
//            logger.addHandler(fh);
//            SimpleFormatter formatter = new SimpleFormatter();
//            fh.setFormatter(formatter);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void write(char type, String text){ // e, i
        try {
            file = new File("Logs/MyLogFile.log");
            file.getParentFile().mkdirs();
            fr = new FileWriter(file, true);
            br = new BufferedWriter(fr);

            switch (type){
                case 'e':
                    br.write("ERROR: ");
                    break;
                case 'w':
                    br.write("WARNING: ");
                    break;
                case 'i':
                    br.write("INFO: ");
                    break;
                default:
                    br.write("INFO: ");
            }
            br.newLine();
            br.write(text);
            br.newLine();

            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
