package com.mod.soap.request;

import org.w3c.dom.Document;

import javax.xml.bind.annotation.*;

/**
 * Created by karim on 5/6/20.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "template",  "emails", "inputs", "subject"
})
@XmlRootElement(name = "SendEmailRequest")
public class SendEmailRequest {
    @XmlElement(name = "template", required = true)
    protected String template;
    @XmlElement(name = "emails", required = true)
    protected String emails;

    @XmlElement(name = "inputs", required = true)
    protected String inputs;

    @XmlElement(name = "subject", required = true)
    protected String subject;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }


    public String getInputs() {
        return inputs;
    }

    public void setInputs(String inputs) {
        this.inputs = inputs;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
