package com.mod.soap.entity;

/**
 * Created by karim.omaya on 12/24/2019.
 */
public class UserDetails extends Entity {

    public String getUserDetails(){
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Body>\n" +
                "    <GetUserDetails xmlns=\"http://schemas.cordys.com/UserManagement/1.0/User\">PARAMETER</GetUserDetails>\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }

    public String getSubUsersMessage(int pageNumber, int pageSize, long userId, String input){
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Body>\n" +
                "    <MOD_TM_SP_task_get_subusers_of_user xmlns=\"http://schemas.cordys.com/MOD_TM_SP_task_get_subusers_of_user\">\n" +
                "      <RETURN_VALUE>PARAMETER</RETURN_VALUE>\n" +
                "      <PageNumber>"+pageNumber+"</PageNumber>\n" +
                "      <PageSize>"+pageSize+"</PageSize>\n" +
                "      <userId>"+userId+"</userId>\n" +
                "      <input>"+input+"</input>\n" +
                "    </MOD_TM_SP_task_get_subusers_of_user>\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }

    @Override
    String createMessage() {
        return null;
    }

    @Override
    String updateMessage() {
        return null;
    }
}
