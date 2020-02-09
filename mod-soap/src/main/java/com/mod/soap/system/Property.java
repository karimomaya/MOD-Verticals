package com.mod.soap.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by karim.omaya on 12/12/2019.
 */
@Configuration
public class Property {

    @Autowired
    private Environment env;

    public String configureCN(String name){

        String organizationName = env.getProperty("organizationName");
        String o = env.getProperty("o");
        String organzationCN = "o=" + organizationName + ",cn=cordys,cn=defaultInst,o=" + o;
        String cn = "cn=$name,cn=organizational $type," + organzationCN;
        return cn.replace("$name", name).replace("$type", "users");
    }


    public String getProperty(String pPropertyKey) {
        return env.getProperty(pPropertyKey);
    }
}
