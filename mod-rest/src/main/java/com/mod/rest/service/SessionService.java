package com.mod.rest.service;

import com.mod.rest.entity.UserDetails;
import com.mod.rest.model.UserHelper;
import com.mod.rest.repository.UserHelperRepository;
import com.mod.rest.system.Config;
import com.mod.rest.system.Http;
import com.mod.rest.system.Utils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by karim.omaya on 12/24/2019.
 */
@Service
public class SessionService {

    @Autowired
    Config config;
    @Autowired
    UserHelperRepository userHelperRepository;


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

    public void setSession(String SAMLart, UserHelper user){
        UserSession userSession = concurrentHashMap.get(SAMLart);
        if (userSession == null) {
            userSession = new UserSession();
            userSession.setUser(user);
            addExtraTimeToSession(userSession);
            concurrentHashMap.put(SAMLart, userSession);
        }
    }

    public UserHelper getSession(String SAMLart){
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

    public void login(String SAMLart){
        Http http = new Http(SAMLart, config);
        UserDetails userDetails = new UserDetails();
        String res = http.cordysRequest(userDetails.getUserDetails());
        Document doc = Utils.convertStringToXMLDocument( res );
        Node node = doc.getElementsByTagName("GetUserDetailsResponse").item(0);
        String cn = node.getFirstChild().getChildNodes().item(0).getTextContent();
        cn = config.configureCN(cn);
        UserHelper userHelper =  userHelperRepository.getUserDetail(cn).get(0);
        setSession(SAMLart, userHelper);
    }

    @Getter
    @Setter
    private class UserSession{
        UserHelper user;
        Date lastActiveTime;
    }


}
