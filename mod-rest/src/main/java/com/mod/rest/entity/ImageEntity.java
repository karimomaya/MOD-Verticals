package com.mod.rest.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.mod.rest.model.Image;

/**
 * Created by karim.omaya on 12/11/2019.
 */
public class ImageEntity extends Entity {
    Image image;
    public ImageEntity(Image image){
        this.image = image;
    }
    @Override
    String createMessage() {
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <SOAP:Body>\n" +
                "    <CreateMOD_SYS_entity_image xmlns=\"http://schemas/MyCompanyGeneralSYS_GENERAL/MOD_SYS_entity_image/operations\">\n" +
                "      <ns0:MOD_SYS_entity_image-create xmlns:ns0=\"http://schemas/MyCompanyGeneralSYS_GENERAL/MOD_SYS_entity_image\">\n" +
                "        <ns0:parentItemId>"+image.getParentItemId()+"</ns0:parentItemId>\n" +
                "        <ns0:path>"+image.getPath()+"</ns0:path>\n" +
                "        <ns0:type>"+image.getType()+"</ns0:type>\n" +
                "      </ns0:MOD_SYS_entity_image-create>\n" +
                "    </CreateMOD_SYS_entity_image>\n" +
                "  </SOAP:Body>\n" +
                "</SOAP:Envelope>";
    }

    @Override
    String updateMessage() {
        return null;
    }
}
