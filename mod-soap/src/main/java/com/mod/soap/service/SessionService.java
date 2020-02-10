package com.mod.soap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mod.soap.dao.model.User;
import com.mod.soap.entity.UserDetails;
import com.mod.soap.dao.repository.UserRepository;
import com.mod.soap.system.Property;
import com.mod.soap.system.Http;
import com.mod.soap.system.Utils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by karim on 2/9/20.
 */
@Service
public class SessionService {
    @Autowired
    Property property;
    @Autowired
    UserRepository userHelperRepository;


    ConcurrentHashMap<String, UserSession> concurrentHashMap = new ConcurrentHashMap();

    public SessionService(){
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {

                checkToDelete();
            }
        });
        timer.start();
    }

    public void setSession(String SAMLart, User user){
        UserSession userSession = concurrentHashMap.get(SAMLart);
        if (userSession == null) {
            userSession = new UserSession();
            userSession.setUser(user);
            addExtraTimeToSession(userSession);
            concurrentHashMap.put(SAMLart, userSession);
        }
    }

    public User getSession(String SAMLart){
        UserSession userSession = concurrentHashMap.get(SAMLart);
        if (userSession != null){
            addExtraTimeToSession(userSession);
            return userSession.getUser();
        }
        return null;

    }

    private void addExtraTimeToSession(UserSession userSession){
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.MINUTE, 15);
        userSession.setLastActiveTime(calendar.getTime());
    }

    public void checkToDelete(){
        for (String key : concurrentHashMap.keySet()) {
            UserSession userSession = concurrentHashMap.get(key);
            Date now = new Date();
            if (now.compareTo(userSession.getLastActiveTime()) > 0) {
                System.out.println( "********************* " +key+ " will be deleted *********************");
                concurrentHashMap.remove(key);
            }
        }
    }

    public User login(){
        Utils utils = new Utils();
        Http http = new Http(property);
        String data = "{\"userName\" : \"admin\", \"password\" : \"" + property.getProperty("password") + "\"}";
        String res = http.cordysRequestWithContentType(property.getProperty("otds.url"),"application/json",data );

        String ticket = utils.readJSONField(res,"ticket");

        String token = utils.readJSONField(res,"token");

        UserDetails userDetails = new UserDetails();
//        res = http.cordysRequest(userDetails.getAssetionArtifactMessage(ticket));

//        Document doc = utils.convertStringToXMLDocument(res);
//        java.lang.System.out.println(doc);

//        Node node = doc.getElementsByTagName("samlp:AssertionArtifact").item(0);
//        String SAMLart = node.getTextContent();
//        http.setSAMLart("MDEQ7kYAUafouLmLceBrw6AE1Uo8oCCqIf813YKy1brQsvifASbqXmxJ");
//        http.setSAMLart(ticket);

        res = http.cordysRequest(userDetails.impersonateUser(ticket,"omarsabry"));

        java.lang.System.out.println(res);



//        res = http.cordysRequest(userDetails.getUserDetails());
//        doc = Utils.convertStringToXMLDocument( res );
//        node = doc.getElementsByTagName("GetUserDetailsResponse").item(0);
//        String cn = node.getFirstChild().getChildNodes().item(0).getTextContent();
//        cn = property.configureCN(cn);
//        User userHelper =  userHelperRepository.getUserDetail(cn).get(0);
//        setSession(SAMLart, userHelper);
//        return userHelper;
        return null;
    }

    public User login(String SAMLart){
        Http http = new Http(SAMLart, property);
        UserDetails userDetails = new UserDetails();
        String res = http.cordysRequest(userDetails.getUserDetails());
        Document doc = Utils.convertStringToXMLDocument( res );
        Node node = doc.getElementsByTagName("GetUserDetailsResponse").item(0);
        String cn = node.getFirstChild().getChildNodes().item(0).getTextContent();
        cn = property.configureCN(cn);
        User userHelper =  userHelperRepository.getUserDetail(cn).get(0);
        setSession(SAMLart, userHelper);
        return userHelper;
    }

    @Getter
    @Setter
    private class UserSession{
        User user;
        Date lastActiveTime;
    }
}
