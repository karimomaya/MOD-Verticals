package com.mod.soap.endpoint;

import com.mod.soap.dao.model.Config;
import com.mod.soap.dao.repository.ConfigRepository;
import com.mod.soap.dao.repository.UserRepository;
import com.mod.soap.model.RequestNumber;
import com.mod.soap.model.SecurityAccess;
import com.mod.soap.repository.RequestNumberRepository;
import com.mod.soap.request.CustomSecurityRequest;
import com.mod.soap.request.CustomSecurityResponse;
import com.mod.soap.request.RequestNumberRequest;
import com.mod.soap.request.RequestNumberResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by karim.omaya on 12/7/2019.
 */
@Endpoint
public class RequestNumberEndPoint {
    private static final String NAMESPACE_URI = "http://www.mod.soap";

    private RequestNumberRepository RequestNumberRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Environment env;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ConfigRepository configRepository;


    @Autowired
    public RequestNumberEndPoint(RequestNumberRepository RequestNumberRepository) {
        this.RequestNumberRepository = RequestNumberRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RequestNumberRequest")
    @ResponsePayload
    public RequestNumberResponse generateRequestNumber(@RequestPayload RequestNumberRequest request) {


        RequestNumberResponse response = new RequestNumberResponse();

        String userId = request.getUserId();
        String type = request.getType();

        RequestNumber requestNumber = new RequestNumber(entityManager, userRepository);
        List<Config> configs = configRepository.findDistinctByCategory("patternConfig");
        if (configs.size() > 0){
            requestNumber.setPattern(configs.get(0).getValue());
        }
        requestNumber.setUserId(userId);
        requestNumber.setType(type);
        requestNumber.generateRequestNumber();
        response.setRequestNumber(requestNumber);

        return response;
    }



    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CustomSecurityRequest")
    @ResponsePayload
    public CustomSecurityResponse getCustomSecurity(@RequestPayload CustomSecurityRequest request) {

        CustomSecurityResponse customSecurityResponse = new CustomSecurityResponse();
        SecurityAccess securityAccess = new SecurityAccess();
        securityAccess.setAccess(true);
        securityAccess.setTarget("test");
        SecurityAccess securityAccess2 = new SecurityAccess();
        securityAccess2.setAccess(true);
        securityAccess2.setTarget("test");
        customSecurityResponse.setSecurityAccess(securityAccess);
        customSecurityResponse.setSecurityAccess(securityAccess2);


        return customSecurityResponse;
//        return response;
    }

    public String getProperty(String pPropertyKey) {
        return env.getProperty(pPropertyKey);
    }
}
