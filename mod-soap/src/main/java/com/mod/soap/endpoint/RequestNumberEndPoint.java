package com.mod.soap.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mod.soap.dao.model.*;
import com.mod.soap.dao.repository.ConfigRepository;
import com.mod.soap.dao.repository.SecurityRepository;
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
import com.mod.soap.system.Http;
import com.mod.soap.system.Property;
import lombok.Data;
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
import java.util.Optional;

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
    SecurityRepository securityRepository;
    @Autowired
    Property property;

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
            User user = sessionService.getSession(securityRequest.getSamlart());
//
            if (user == null) user = sessionService.login(securityRequest.getSamlart());

//            User user = sessionService.login();

            if (user == null){
                SecurityAccess securityAccess = new SecurityAccess();
                securityAccess.setAccess(false);
                customSecurityResponse.setSecurityAccess(securityAccess);
                continue;
            }


            Optional<Security> securityOptional = securityRepository.findByTargetAndType(securityRequest.getTarget(), securityRequest.getType());


            if (!securityOptional.isPresent()){
                SecurityAccess securityAccess = new SecurityAccess();
                securityAccess.setAccess(false);
                customSecurityResponse.setSecurityAccess(securityAccess);
                continue;
            }

            Security security = securityOptional.get();

            String config = security.getConfig();



            if ((!securityRequest.getInput().equals("?") || !securityRequest.getInput().equals("PARAMETER") || !securityRequest.equals(" ")) && securityRequest.getInput().length() > 1 ){

                String[] inputs = securityRequest.getInput().split(",");

                for (int i =0; i< inputs.length; i++){
                    config = config.replace("$"+i , inputs[i] );
                }
            }

            System.out.println("Voila you can find it by target and type");
            System.out.println(config);
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;
            try {
                SecurityConfig securityConfig = objectMapper.readValue(config, SecurityConfig.class);



                SecurityQuery securityQuery = securityConfig.setSecurityType(security.getType()).build(user);

                if (securityQuery.getSecurityType() == SecurityType.WEBSERVICE){
                    String webservice  = securityQuery.setWebservice(securityConfig.getWebservice()).prepareWebservice();
                    Http http = new Http(property);
                    String res = http.cordysRequest(webservice);
                    boolean canAccess = securityQuery.evaluateWebserviceResponse(res);
                    SecurityAccess securityAccess = new SecurityAccess();
                    securityAccess.setAccess(canAccess);

                    customSecurityResponse.setSecurityAccess(securityAccess);

                }

//                securityQuery.execute();
                System.out.print(securityConfig.getOutput());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }



        }



        return customSecurityResponse;
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
