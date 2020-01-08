package com.mod.rest.service;

import com.mod.rest.entity.UserDetails;
import com.mod.rest.model.UserHelper;
import com.mod.rest.system.Config;
import com.mod.rest.system.Http;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by karim.omaya on 12/27/2019.
 */
@Service
public class UserHelperService {
    @Autowired
    SessionService sessionService;
    @Autowired
    Config config;

    public NodeList getSubUsers(String SAMLart) {
        Http http = new Http(SAMLart, config);
        UserDetails userDetails = new UserDetails();
        UserHelper userHelper = sessionService.getSession(SAMLart);
        String res = http.cordysRequest(userDetails.getSubUsersMessage(1, Integer.MAX_VALUE,userHelper.getId(), ""));
        System.out.println(res);
        Document doc = Utils.convertStringToXMLDocument( res );

        return doc.getElementsByTagName("UserEntityId");

    }

    public  Document getSubUsersDocument(String SAMLart){
        Http http = new Http(SAMLart, config);
        UserDetails userDetails = new UserDetails();
        UserHelper userHelper = sessionService.getSession(SAMLart);
        String res = http.cordysRequest(userDetails.getSubUsersMessage(1, Integer.MAX_VALUE,userHelper.getId(), ""));
        System.out.println(res);
        return  Utils.convertStringToXMLDocument( res );
    }
}
