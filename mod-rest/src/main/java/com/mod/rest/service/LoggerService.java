package com.mod.rest.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by karim on 2/3/20.
 */

@Service
public class LoggerService {
    FileHandler fh;
    Logger logger;

    public LoggerService(){
        logger = Logger.getLogger("MyLog");
        try {
            fh = new FileHandler("MyLogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(char type, String text){ // e, i
        switch (type){
            case 'e':
                logger.severe(text);
                break;
            case 'w':
                logger.warning(text);
                break;
            case 'i':
                logger.info(text);
                break;
            default:
                logger.info(text);
        }
    }
}
