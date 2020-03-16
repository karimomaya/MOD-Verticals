package com.mod.rest.controller;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.recurrence.pattern.Recurrence;
import org.springframework.web.bind.annotation.*;

/**
 * Created by karim on 3/8/20.
 */

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/mail")
public class SendOutlook {
    @GetMapping("send")
    @ResponseBody
    public String Send(){
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        ExchangeCredentials credentials = new WebCredentials("karim.omaya@asset.com.eg", "K@omaya08");
        service.setCredentials(credentials);
        try {
//            service.autodiscoverUrl("karim.omaya@asset.com.eg");
            service.setUrl(new URI("https://mail.asset.com.eg/ews/exchange.asmx"));
            Appointment appointment = new Appointment(service);
            appointment.setSubject("Recurrence Appointment for JAVA XML TEST");
            appointment.setBody(MessageBody.getMessageBodyFromText("Recurrence Test Body Msg"));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = formatter.parse("2020-03-22 12:00:00");
            Date endDate = formatter.parse("2020-03-22 13:00:00");

            appointment.setStart(startDate);//new Date(2010-1900,5-1,20,20,00));
            appointment.setEnd(endDate); //new Date(2010-1900,5-1,20,21,00));

            formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date recurrenceEndDate = formatter.parse("2020-03-25");

            appointment.setRecurrence(new Recurrence.DailyPattern(appointment.getStart(), 3));

            appointment.getRecurrence().setStartDate(appointment.getStart());
            appointment.getRecurrence().setEndDate(recurrenceEndDate);
            appointment.save();
            return "done";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "fail";


    }
}
