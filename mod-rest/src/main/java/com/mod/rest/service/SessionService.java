package com.mod.rest.service;

import com.mod.rest.model.UserHelper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

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

    @Getter
    @Setter
    private class UserSession{
        UserHelper user;
        Date lastActiveTime;
    }


}
