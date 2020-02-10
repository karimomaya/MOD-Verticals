package com.mod.soap.entity;

/**
 * Created by karim.omaya on 12/24/2019.
 */
public class UserDetails extends Entity {

    public String getUserDetailsWithTicket(String ticket){
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Header>\n" +
                "    <OTAuthentication xmlns=\"urn:api.bpm.opentext.com\">\n" +
                "      <AuthenticationToken>"+ticket+"</AuthenticationToken>\n" +
                "    </OTAuthentication>\n" +
                "  </SOAP:Header>\n" +
                "  <SOAP:Body>\n" +
                "    <GetUserDetails xmlns=\"http://schemas.cordys.com/UserManagement/1.0/User\">PARAMETER</GetUserDetails>\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }

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

    public String impersonateUser(String ticket, String username){
//        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
//                "  <SOAP:Body>\n" +
//                "    <ImpersonateUser xmlns=\"urn:Core.service.livelink.opentext.com\">\n"+
//                "      <userName>"+username+"</userName>\n" +
//                "    </ImpersonateUser>\n"+
//                "  </SOAP:Body>\n" +
//                "</SOAP:Envelope>";

        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "  <SOAP:Header>\n" +
                "    <OTAuthentication xmlns=\"urn:api.ecm.opentext.com\">\n" +
                "      <AuthenticationToken>"+ticket+"</AuthenticationToken>\n" +
                "    </OTAuthentication>\n" +
                "  </SOAP:Header>\n" +
                "  <SOAP:Body>\n" +
                "    <ImpersonateUser xmlns=\"urn:Core.service.livelink.opentext.com\">\n"+
                "      <userName>"+username+"</userName>\n" +
                "    </ImpersonateUser>\n"+
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }

    public String getAssetionArtifactMessage(String ticket) {
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "<SOAP:Header>\n" +
                "  <OTAuthentication xmlns=\"urn:api.bpm.acme.com\">\n" +
                "    <AuthenticationToken>" + ticket + "</AuthenticationToken>\n" +
                "  </OTAuthentication>\n" +
                "</SOAP:Header>\n" +
                "<SOAP:Body>\n" +
                "  <samlp:Request xmlns:samlp=\"urn:oasis:names:tc:SAML:1.0:protocol\" MajorVersion=\"1\" MinorVersion=\"1\" IssueInstant=\"2014-05-20T15:29:49.156Z\" RequestID=\"a5470c392e-264e-9537-56ac-4397b1b416d\">\n" +
                "    <samlp:AuthenticationQuery>\n" +
                "      <saml:Subject xmlns:saml=\"urn:oasis:names:tc:SAML:1.0:assertion\">\n" +
                "        <saml:NameIdentifier Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\"></saml:NameIdentifier>\n" +
                "      </saml:Subject>\n" +
                "    </samlp:AuthenticationQuery>\n" +
                "  </samlp:Request>\n" +
                "</SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }

    public String getSAMLAssertions(String SAMLart) {
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "<SOAP:Body>\n" +
                "<samlp:Request xmlns:samlp=\"urn:oasis:names:tc:SAML:1.0:protocol\"\n" +
                "MajorVersion=\"1\"\n" +
                "MinorVersion=\"1\">\n" +
                "<samlp:AssertionArtifact xmlns:samlp=\"urn:oasis:names:tc:SAML:1.0:protocol\">"+SAMLart+"</samlp:AssertionArtifact>\n" +
                "</samlp:Request>\n" +
                "</SOAP:Body>\n" +
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
