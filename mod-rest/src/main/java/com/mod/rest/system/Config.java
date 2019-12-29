package com.mod.rest.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by karim.omaya on 12/12/2019.
 */
@Configuration
public class Config {

    @Autowired
    private Environment env;


    public String getProperty(String pPropertyKey) {
        return env.getProperty(pPropertyKey);
    }
}
