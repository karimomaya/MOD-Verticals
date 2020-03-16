package com.mod.soap.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mod.soap.dao.model.*;
import com.mod.soap.dao.repository.*;
import com.mod.soap.model.OutlookMeeting;
import com.mod.soap.model.RequestNumber;
import com.mod.soap.model.SecurityAccess;
import com.mod.soap.request.*;
import com.mod.soap.repository.RequestNumberRepository;
import com.mod.soap.service.SessionService;
import com.mod.soap.system.Http;
import com.mod.soap.system.Property;
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
import java.util.*;

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
    MeetingRepository meetingRepository;
    @Autowired
    MeetingAttendeeRepository meetingAttendeeRepository;

    @Autowired
    public RequestNumberEndPoint(RequestNumberRepository RequestNumberRepository) {
        this.RequestNumberRepository = RequestNumberRepository;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendMeetingRequest")
    @ResponsePayload
    public SendMeetingResponse sendMeeting(@RequestPayload SendMeetingRequest request) {


        SendMeetingResponse response = new SendMeetingResponse();

        long meetingId = request.getMeetingId();
        List<MeetingAttendee> meetingAttendees = meetingAttendeeRepository.getMeetingAttendeeData(meetingId);
        Optional<Meeting> meetingOptional = meetingRepository.getMeetingData(meetingId);
        if (!meetingOptional.isPresent()) return response.setStatus("Meeting-Not-Present");

        Meeting meeting = meetingOptional.get();


        sendMeeting(meeting, meetingAttendees);



        if (meeting.getIsPeriodic() && !request.isUpdate()){
            ArrayList<Meeting> meetings = meetingRepository.getPeriodicMeeting(meetingId, meeting.getSubject());
            for (Meeting meeting1: meetings){
                if (meeting1.getId() != meetingId && !meeting1.getConflict()){
                    sendMeeting(meeting1, meetingAttendees);
                }
            }

        }

        return response.setStatus("done");
    }

    public void sendMeeting(Meeting meeting, List<MeetingAttendee> meetingAttendees){
        OutlookMeeting outlookMeeting = new OutlookMeeting().setSubject(meeting.getSubject())
                .setBody(meeting.getDescription())
                .setStartDate(meeting.getStartDate())
                .setEndDate(meeting.getEndDate());

        for (MeetingAttendee meetingAttendee : meetingAttendees){
            if (meetingAttendee.getIsExternal()){

            }else {
                Optional<User> userOptional = userRepository.getUserDetail(meetingAttendee.getAttendeeID());
                if (userOptional.isPresent()){
                    outlookMeeting.setAttendeeEmail(userOptional.get().getEmail());
                }
            }
        }
        outlookMeeting.send();
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

        User user = sessionService.loginWithUsername(request.getUsername());

        if (user == null){
            SecurityAccess securityAccess = new SecurityAccess();
            securityAccess.setAccess(false);
            customSecurityResponse.setSecurityAccess(securityAccess);
            return customSecurityResponse;
        }


        HashMap<String, String > previousResponse = new HashMap<>();


        for (SecurityRequest securityRequest : securityRequests){

            Optional<Security> securityOptional = securityRepository.findByTarget(securityRequest.getTarget());


            if (!securityOptional.isPresent()){
                customSecurityResponse.setSecurityAccess(addResponseToSecurity(false, securityRequest.getTarget()));
                continue;
            }

            Security security = securityOptional.get();

            String config = prepareSecurityConfig(security, securityRequest,user);

            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            try {
                SecurityConfig securityConfig = null;
// 1:unit type code, 2: unit code,3: role name, 4: webservice, stored procedure
                if(security.getType() == 4){
                    securityConfig = objectMapper.readValue(config, SecurityConfig.class);
                }else if(security.getType() == 1)  {
                    securityConfig = new SecurityConfig(security.getConfig(), null, null);
                }else if(security.getType() == 2)  {
                    securityConfig = new SecurityConfig(null, security.getConfig(), null);
                } else if(security.getType() == 3)  {
                    securityConfig = new SecurityConfig(null, null, security.getConfig());
                }


                SecurityQuery securityQuery = securityConfig.setSecurityType(security.getType()).newBuilder(user);

                boolean canAccess = false;

                if (securityQuery.getSecurityType() == SecurityType.WEBSERVICE){
                    canAccess = evaluateWebservice(request, securityRequest, securityConfig,securityQuery,  previousResponse);
                } else if (securityQuery.getSecurityType() == SecurityType.UNIT_TYPE_Code){
                    canAccess = securityQuery.evaluateUnitTypeCode();
                } else if (securityQuery.getSecurityType() == SecurityType.UNIT_CODE){
                    canAccess = securityQuery.evaluateUnitCode();
                } else if (securityQuery.getSecurityType() == SecurityType.ROLE_CODE){
                    canAccess = securityQuery.evaluateRoleCode();
                }

                customSecurityResponse.setSecurityAccess(addResponseToSecurity(canAccess, security.getTarget()));

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }

        return customSecurityResponse;
    }

    private SecurityAccess addResponseToSecurity(boolean response, String target) {
        SecurityAccess securityAccess = new SecurityAccess();
        securityAccess.setAccess(response);
        securityAccess.setTarget(target);
        return securityAccess;
    }


    private boolean evaluateWebservice(CustomSecurityRequest request, SecurityRequest securityRequest, SecurityConfig securityConfig, SecurityQuery securityQuery,  HashMap<String, String > previousResponse){
        String response = "";

        if (request.getPreviousTargetTag() != null){

            if (previousResponse.containsKey(request.getPreviousTargetTag())){
                response = previousResponse.get(request.getPreviousTargetTag());
            }else {
                response = getResponseFromWebservice(securityQuery, securityConfig.getWebservice());
                previousResponse.put(securityRequest.getTarget(), response);

            }
        }else {
            response = getResponseFromWebservice(securityQuery, securityConfig.getWebservice());
            previousResponse.put(securityRequest.getTarget(), response);
        }

        return securityQuery.evaluateNewWebserviceResponse(response);
    }


    private String prepareSecurityConfig(Security security, SecurityRequest securityRequest, User user){
        String config = security.getConfig();

        if ((!securityRequest.getInput().equals("?") || !securityRequest.getInput().equals("PARAMETER") || !securityRequest.equals(" ")) && securityRequest.getInput().length() > 1 ){

            String[] inputs = securityRequest.getInput().split(",");

            for (int i =0; i< inputs.length; i++){
                config = config.replaceAll("\\$"+i , inputs[i] );
            }
        }

        config = config.replaceAll("\\$userId" , user.getId()+"" );
        config = config.replaceAll("\\$userPath" , user.getUnitPathById());
        config = config.replaceAll("\\$roleName" , user.getRoleName());
        config = config.replaceAll("\\$userRoleCode" , user.getRoleCode());
        config = config.replaceAll("\\$unitTypeCode" , user.getUserUnitTypeCode());
        config = config.replaceAll("\\$unitCode" , user.getUserUnitCode());
        config = config.replaceAll("\\$username" , user.getUsername());
        return config.replaceAll("\\$displayName" , user.getDisplayName());
    }

    private String getResponseFromWebservice(SecurityQuery securityQuery, JsonNode jsonWebservice){
        String webservice  = securityQuery.setWebservice(jsonWebservice).prepareWebservice();
        Http http = new Http(property);
        return http.cordysRequest(webservice);
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
