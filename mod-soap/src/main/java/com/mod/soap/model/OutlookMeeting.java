package com.mod.soap.model;

import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.recurrence.pattern.Recurrence;

import java.net.URI;
import java.util.Date;

/**
 * Created by karim on 3/8/20.
 */
@Slf4j
public class OutlookMeeting {
    ExchangeService service = null;
    String status = "";
    Appointment appointment = null;
    String logoPath= "";


    public OutlookMeeting(String uniqueId, String username, String password, String exchangeServiceURL){

        try {
            log.info("Initialize Outlook Object");
            service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
            ExchangeCredentials credentials = new WebCredentials(username, password);
            service.setCredentials(credentials);
            //            service.autodiscoverUrl("karim.omaya@asset.com.eg");
//            service.setUrl(new URI(""));
            service.setUrl(new URI(exchangeServiceURL));
            if(uniqueId == null){
                appointment = new Appointment(service);
            }else{
                appointment = Appointment.bind(service,new ItemId(uniqueId));
            }

            status = "Initialized";
        } catch (Exception e) {
            log.error("Failed to initialized Outlook Object: "+ e.getMessage());
            status = "Failed to Initialized: " + e.getMessage();
        }
    }


    public OutlookMeeting setLogoPath(String path){
        this.logoPath = path;
        return this;
    }

    public void sendEmail(String html, String[] emails, String subject) throws Exception {
        String logo = this.logoPath;
        log.info("Send Email Function Started...");
        log.info("Create New Thread For Sending Email...");
        Thread thread = new Thread("Send Email Thread") {
            public void run(){
                try {
                    log.info("Create Email Message...");
                    EmailMessage msg= new EmailMessage(service);
                    log.info("Set Email Subject: "+ subject);
                    msg.setSubject(subject);

                    log.info("Set Email Body...");
                    MessageBody msgBody = new MessageBody();
                    msgBody.setBodyType(BodyType.HTML);
                    msgBody.setText(html);

                    msg.setBody(msgBody );
                    log.info("Set Email Recipients: "+ emails.toString());
                    for (int i=0; i< emails.length; i++){
                        System.out.println(emails[i]);
                        msg.getToRecipients().add(emails[i]);
                    }

                    log.info("Attach Logo...");
                    if (!logoPath.equals("")){
                        msg.getAttachments().addFileAttachment(logo);
                        msg.getAttachments().getItems().get(0).setIsInline(true);
                        msg.getAttachments().getItems().get(0).setContentId("UAElogo.png");
                    }

                    log.info("Send Email...");
                    msg.send();
                    log.info("Send Email Success...");
                }catch (ServiceLocalException e){
                    log.error("Error Add File Attachment in Send Email");
                }catch (Exception e){
                    log.error("Error Sending Email");
                }
            }
        };

        thread.start();
    }

    public OutlookMeeting setSubject(String subject){
        try {
            appointment.setSubject(subject);
            status = "set Subject to meeting";
        } catch (Exception e) {
            log.error("Failed to set Outlook Subject: "+ e.getMessage());
            status = "Failed to set Subject: "+ e.getMessage();
        }
        return this;
    }

    public OutlookMeeting setBody(String body){
        try {
            appointment.setBody(MessageBody.getMessageBodyFromText(body));
            status = "set Body to meeting";
        } catch (Exception e) {
            log.error("Failed to set Outlook Body: "+ e.getMessage());
            status = "Failed to set Body: "+ e.getMessage();
        }
        return this;
    }

    public OutlookMeeting setStartDate(Date startDate){
        try {
            appointment.setStart(startDate);//new Date(2010-1900,5-1,20,20,00));
            status = "set Start Date to meeting";
        } catch (Exception e) {
            log.error("Failed to set Outlook Start Date: "+ e.getMessage());
            status = "Failed to set Start Date: "+ e.getMessage();
        }
        return this;
    }

    public OutlookMeeting setEndDate(Date endDate){
        try {
            appointment.setEnd(endDate);//new Date(2010-1900,5-1,20,20,00));
            status = "set Start Date to meeting";
        } catch (Exception e) {
            log.error("Failed to set Outlook End Date: "+ e.getMessage());
            status = "Failed to set End Date: "+ e.getMessage();
        }
        return this;
    }

    public OutlookMeeting setPeridicDate(int type, int recurrence, int dayOfMonth){

        try {
            if (type == 1){
                appointment.setRecurrence(new Recurrence.DailyPattern(appointment.getStart(), recurrence));
                status = "Set Daily Meeting";
            }else if (type == 7){
                appointment.setRecurrence(new Recurrence.WeeklyPattern(appointment.getStart(), recurrence));
                status = "Set Weekly Meeting";
            }
            else if (type == 30){
                appointment.setRecurrence(new Recurrence.MonthlyPattern(appointment.getStart(), recurrence,  dayOfMonth));
                status = "Set Monthly Meeting";
            }else {
                status = "Failed to Set Periodic Type";
            }
            log.info("Outlook Meeting is periodic: "+ status);
        } catch (Exception e) {
            log.error("Failed to Set Periodic Date: "+ e.getMessage());
            status = "Failed to Set Periodic Date: " + e.getMessage();
        }
        return this;
    }


    public OutlookMeeting setPeriodicEndDate(Date date){
        try {
            appointment.getRecurrence().setStartDate(appointment.getStart());
            appointment.getRecurrence().setEndDate(date);
            status = "Set Periodic end Date";
        } catch (Exception e) {
            log.error("Failed to Set Periodic end Date: "+ e.getMessage());
            status = "Failed to set Periodic end Date: " + e.getMessage();
        }
        return this;
    }


    public OutlookMeeting setAttendeeEmail(String email){
        try {
            appointment.getRequiredAttendees().add(email);
            status = "Set Periodic end Date";
        } catch (Exception e) {
            log.error("Failed to Set Email: "+ e.getMessage());
            status = "Failed to set Email: " + e.getMessage();
        }
        return this;
    }


    public OutlookMeeting send(){
        try {
            appointment.save();
            status = "Success";
        } catch (Exception e) {
            log.error("Failed to send Outlook Meeting: "+ e.getMessage());
            status = "Failed to send Outlook Meeting: " + e.getMessage();
        }
        return this;
    }

    public OutlookMeeting update(){
        try {
            appointment.update(ConflictResolutionMode.AutoResolve);
            status = "Success";
        } catch (Exception e) {
            log.error("Failed to update Outlook Meeting: "+ e.getMessage());
            status = "Failed to set Periodic end Date: " + e.getMessage();
        }
        return this;
    }

    public String getUniqueId(){
        try {
            return appointment.getRootItemId().getUniqueId();
        }catch (Exception e){
            log.error("Failed to generate Root ItemId Unique Id: "+ e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    public void cancelMeeting() {
        try {
            this.appointment.cancelMeeting();
        } catch (Exception e) {
            log.error("Failed to cancel Meeting: "+ e.getMessage());
            e.printStackTrace();
        }
    }
}

