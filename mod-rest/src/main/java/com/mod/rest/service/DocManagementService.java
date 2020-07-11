package com.mod.rest.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPFaultException;


import com.opentext.ecm.api.OTAuthentication;
import com.opentext.livelink.service.core.*;
import com.opentext.livelink.service.docman.DocumentManagement;
import com.opentext.livelink.service.docman.DocumentManagement_Service;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.developer.JAXWSProperties;
import com.sun.xml.ws.developer.WSBindingProvider;
import org.springframework.stereotype.Service;

@SuppressWarnings("Duplicates")
@Service
public class DocManagementService {
    public final int CHUNK_SIZE = 10240;


    public void uploadDocument(File file, long parentId) {
        // --------------------------------------------------------------------------
        // 1) Authenticate the user
        // --------------------------------------------------------------------------

        // Create the Authentication service client
        Authentication_Service authService = new Authentication_Service();
        Authentication authClient = authService.getBasicHttpBindingAuthentication();

        // Store the authentication token
        String authToken = null;

        // Call the AuthenticateUser() method to get an authentication token
        try {
            System.out.print("Authenticating User...");
            authToken = authClient.authenticateUser("admin", "Asset99a");
            System.out.println("SUCCESS!\n");
        } catch (SOAPFaultException e) {
            System.out.println("FAILED!\n");
            System.out.println(e.getFault().getFaultCode() + " : " + e.getMessage());
            return;
        }

        // --------------------------------------------------------------------------
        // 2) Store the metadata for the upload in a method context on the server
        // --------------------------------------------------------------------------

        // Store the information for the local file

        if (!file.exists()) {
            System.out.println("ERROR!\n");
            System.out.println("File does not exist");
            return;
        }

        // Create the DocumentManagement service client
        DocumentManagement_Service docManService = new DocumentManagement_Service();
        DocumentManagement docManClient = docManService.getBasicHttpBindingDocumentManagement();

        // Create the OTAuthentication object and set the authentication token
        OTAuthentication otAuth = new OTAuthentication();
        otAuth.setAuthenticationToken(authToken);

        // We need to manually set the SOAP header to include the authentication token
        // Namespaces for the SOAP headers
        String ECM_API_NAMESPACE = "urn:api.ecm.opentext.com";
        try {
            // Create a SOAP header
            SOAPHeader header = MessageFactory.newInstance().createMessage().getSOAPPart().getEnvelope().getHeader();

            // Add the OTAuthentication SOAP header element
            SOAPHeaderElement otAuthElement = header.addHeaderElement(new QName(ECM_API_NAMESPACE, "OTAuthentication"));

            // Add the AuthenticationToken SOAP element
            SOAPElement authTokenElement = otAuthElement.addChildElement(new QName(ECM_API_NAMESPACE, "AuthenticationToken"));
            authTokenElement.addTextNode(otAuth.getAuthenticationToken());

            // Set the SOAP header on the docManClient
            ((WSBindingProvider) docManClient).setOutboundHeaders(Headers.create(otAuthElement));
        } catch (SOAPException e) {
            System.out.println("Failed to set authentication SOAP header!\n");
            System.out.println(e.getMessage());
            return;
        }

        // Store the context ID for the upload
        String contextID = null;

        // Call the createDocumentContext() method to create the context ID
        try {
            System.out.print("Generating context ID...");
            contextID = docManClient.createDocumentContext(parentId, file.getName(), null, false, null);

            System.out.println("SUCCESS!\n");
        } catch (SOAPFaultException e) {
            System.out.println("FAILED!\n");
            System.out.println(e.getFault().getFaultCode() + " : " + e.getMessage());
            return;
        }

        // --------------------------------------------------------------------------
        // 3) Upload the file
        // --------------------------------------------------------------------------

        // Create the ContentService client
        // NOTE: ContentService is the only service that requires MTOM support
        ContentService_Service contentService = new ContentService_Service();
        ContentService contentServiceClient = contentService.getBasicHttpBindingContentService(new MTOMFeature());

        // Enable streaming and use chunked transfer encoding to send the request body to support large files
        ((BindingProvider) contentServiceClient).getRequestContext().put(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE, CHUNK_SIZE);

        // Get the file attributes
        BasicFileAttributes fileAttributes;
        try {
            fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        } catch (IOException e) {
            System.out.println("Failed to read file attributes!\n");
            System.out.println(e.getMessage());
            return;
        }

        // Create the FileAtts object to send in the upload call
        FileAtts fileAtts = new FileAtts();

        try {
            fileAtts.setCreatedDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(fileAttributes.creationTime().toString()));
            fileAtts.setFileName(file.getName());
            fileAtts.setFileSize(file.length());
            fileAtts.setModifiedDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(fileAttributes.lastModifiedTime().toString()));
        } catch (DatatypeConfigurationException e) {
            System.out.println("Failed to set file attributes!\n");
            System.out.println(e.getMessage());
            return;
        }

        // We need to manually set the SOAP headers to include the authentication token, context ID, and file attributes
        try {
            // Create a SOAP header
            SOAPHeader header = MessageFactory.newInstance().createMessage().getSOAPPart().getEnvelope().getHeader();

            // Add the OTAuthentication SOAP header element
            SOAPHeaderElement otAuthElement = header.addHeaderElement(new QName(ECM_API_NAMESPACE, "OTAuthentication"));

            // Add the AuthenticationToken
            SOAPElement authTokenElement = otAuthElement.addChildElement(new QName(ECM_API_NAMESPACE, "AuthenticationToken"));
            authTokenElement.addTextNode(otAuth.getAuthenticationToken());

            // Add the ContextID SOAP header element
            String CORE_NAMESPACE = "urn:Core.service.livelink.opentext.com";
            SOAPHeaderElement contextIDElement = header.addHeaderElement(new QName(CORE_NAMESPACE, "contextID"));
            contextIDElement.addTextNode(contextID);

            // Add the FileAtts SOAP header element
            SOAPHeaderElement fileAttsElement = header.addHeaderElement(new QName(CORE_NAMESPACE, "fileAtts"));

            // Add the CreatedDate element
            SOAPElement createdDateElement = fileAttsElement.addChildElement(new QName(CORE_NAMESPACE, "CreatedDate"));
            createdDateElement.addTextNode(fileAtts.getCreatedDate().toString());

            // Add the ModifiedDate element
            SOAPElement modifiedDateElement = fileAttsElement.addChildElement(new QName(CORE_NAMESPACE, "ModifiedDate"));
            modifiedDateElement.addTextNode(fileAtts.getModifiedDate().toString());

            // Add the FileSize element
            SOAPElement fileSizeElement = fileAttsElement.addChildElement(new QName(CORE_NAMESPACE, "FileSize"));
            fileSizeElement.addTextNode(fileAtts.getFileSize().toString());

            // Add the FileName element
            SOAPElement fileNameElement = fileAttsElement.addChildElement(new QName(CORE_NAMESPACE, "FileName"));
            fileNameElement.addTextNode(fileAtts.getFileName());

            // Set the headers on the binding provider
            List<Header> headers = new ArrayList<>();
            headers.add(Headers.create(otAuthElement));
            headers.add(Headers.create(contextIDElement));
            headers.add(Headers.create(fileAttsElement));

            ((WSBindingProvider) contentServiceClient).setOutboundHeaders(headers);
        } catch (SOAPException e) {
            System.out.println("Failed to set SOAP headers!\n");
            System.out.println(e.getMessage());
            return;
        }

        // Call the UploadContent() method to upload the file
        try {
            System.out.print("Uploading document...");
            String objectID = contentServiceClient.uploadContent(new DataHandler(new FileDataSource(file)));
            System.out.println("SUCCESS!\n");
            System.out.println("New document uploaded with ID = " + objectID);

        } catch (SOAPFaultException e) {
            System.out.println("FAILED!\n");
            System.out.println(e.getFault().getFaultCode() + " : " + e.getMessage());
            return;
        }
    }

    /**
     * Sets the OTAuthentication SOAP header on the binding provider.
     *
     * @param bindingProvider The binding provider to set the header on.
     * @param otAuth          The OTAuthentication object containing the authentication token.
     * @throws SOAPException
     */
//    public static void setSoapHeader(WSBindingProvider bindingProvider, OTAuthentication otAuth) throws SOAPException {
//        final String ECM_API_NAMESPACE = "urn:api.ecm.opentext.com";
//
//        // Create a SOAP header
//        SOAPHeader header = MessageFactory.newInstance().createMessage().getSOAPPart().getEnvelope().getHeader();
//
//        // Add the OTAuthentication SOAP header element
//        SOAPHeaderElement otAuthElement = header.addHeaderElement(new QName(ECM_API_NAMESPACE, "OTAuthentication"));
//
//        // Add the AuthenticationToken SOAP element
//        SOAPElement authTokenElement = otAuthElement.addChildElement(new QName(ECM_API_NAMESPACE, "AuthenticationToken"));
//        authTokenElement.addTextNode(otAuth.getAuthenticationToken());
//
//        // Set the header on the binding provider
//        bindingProvider.setOutboundHeaders(Headers.create(otAuthElement));
//    }

}
