package com.mod.soap.model;

import com.mod.soap.dao.model.User;
import com.mod.soap.dao.repository.UserRepository;

import javax.persistence.EntityManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by karim.omaya on 12/7/2019.
 */
public class RequestNumber {
    String number = "";
    String pattern = "processType:#type:type#user:id#date:dd-M-yyyy#split:-";
    String split = "-";
    String type= "";
    String userId = "";
    UserRepository userRepository;
    EntityManager entityManager;

    public RequestNumber(EntityManager entityManager, UserRepository userRepository){
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }
    public RequestNumber(){
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getUserId() {
        return userId;
    }

    public void generateRequestNumber(){


        Pattern splitPattern = Pattern.compile("#split:");
        Matcher matcher = splitPattern.matcher(pattern);

        while (matcher.find()) {
            split = pattern.substring(matcher.end(), pattern.length());
            System.out.println(split);
            pattern = pattern.substring(0, matcher.start());
        }

        String[] patternArray = pattern.split("#");
        Pattern datePattern = Pattern.compile("(date:)");
        Pattern userPattern = Pattern.compile("(user:)");
        Pattern typePattern = Pattern.compile("(type:)");
        Pattern processTypePattern = Pattern.compile("(processType:)");

        for (String word : patternArray) {

            Matcher dateMattacher = datePattern.matcher(word);
            Matcher userMattacher = userPattern.matcher(word);
            Matcher typeMattacher = typePattern.matcher(word);
            Matcher processTypeMatcher = processTypePattern.matcher(word);

            if (typeMattacher.find()) {
                word = word.substring(typeMattacher.end(), word.length());
                word = word.replace("type", this.type);
                javax.persistence.Query q = entityManager.createNativeQuery("SELECT count(*) FROM "+word);
                String count = ((Number) q.getSingleResult()).intValue()+"";
                buildOutput(count);

            }else if (dateMattacher.find()) {
                word = word.substring(dateMattacher.end(), word.length());
                buildOutput(convertPatternToDate(word));
            } else if (userMattacher.find()) {
                word = word.substring(userMattacher.end(), word.length());
                Optional<User> userOptional = userRepository.getUserDetail(Long.parseLong(this.userId));
                if (!userOptional.isPresent()) continue;
                User user = userOptional.get();

                if (word.equals("id")){
                    buildOutput(user.getId()+"");
                }else if(word.equals("username")){
                    buildOutput(user.getUsername());
                }else if(word.equals("displayName")){
                    buildOutput(user.getDisplayName());
                }
            }else if(processTypeMatcher.find()){
                word = word.substring(processTypeMatcher.end(), word.length());

            }
        }
    }

    public String convertPatternToDate(String pattern){
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        return dateFormat.format(date); //2016/11/16 12:08:43
    }

    public void buildOutput(String output){
        if (number.length() > 0){
            number += split;
        }
        number += output;
    }
}
