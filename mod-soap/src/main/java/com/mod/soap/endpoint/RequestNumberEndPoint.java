package com.mod.soap.endpoint;

import com.mod.soap.dao.model.Config;
import com.mod.soap.dao.model.User;
import com.mod.soap.dao.repository.ConfigRepository;
import com.mod.soap.dao.repository.UserRepository;
import com.mod.soap.model.RequestNumber;
import com.mod.soap.model.SecurityAccess;
import com.mod.soap.model.SecurityRequest;
import com.mod.soap.repository.RequestNumberRepository;
import com.mod.soap.request.CustomSecurityRequest;
import com.mod.soap.request.CustomSecurityResponse;
import com.mod.soap.request.RequestNumberRequest;
import com.mod.soap.request.RequestNumberResponse;
import com.mod.soap.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

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
    SessionService sessionService;


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

        List<SecurityRequest> securityRequests = request.getSecurityRequest();

        for (SecurityRequest securityRequest : securityRequests){
//            User user = sessionService.getSession(securityRequest.getSamlart());
//
//            if (user == null) user = sessionService.login();

            User user = sessionService.login();


            System.out.println("Voila you can find it by target and type");


            SecurityAccess securityAccess = new SecurityAccess();
            securityAccess.setAccess(true);

            customSecurityResponse.setSecurityAccess(securityAccess);
        }



        HttpServletRequest httpServletRequest = getHttpServletRequest();
        Enumeration<String> enums =  httpServletRequest.getHeaderNames();
        while (enums.hasMoreElements()) {
            String param = enums.nextElement();
            System.out.println(param);
            System.out.println(getHttpHeaderValue(param));
        }


        return customSecurityResponse;
//        return response;
    }



    protected HttpServletRequest getHttpServletRequest() {
        TransportContext ctx = TransportContextHolder.getTransportContext();
        return ( null != ctx ) ? ((HttpServletConnection) ctx.getConnection()).getHttpServletRequest() : null;
    }

    protected String getHttpHeaderValue( final String headerName ) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();


        return ( null != httpServletRequest ) ? httpServletRequest.getHeader( headerName ) : null;
    }

    public String getProperty(String pPropertyKey) {
        return env.getProperty(pPropertyKey);
    }
}
