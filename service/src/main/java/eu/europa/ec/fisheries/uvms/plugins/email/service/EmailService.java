/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.plugins.email.service;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.EmailType;
import eu.europa.ec.fisheries.uvms.plugins.email.exception.PluginException;

/**
 **/
@Stateless
@LocalBean
public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    @Resource(mappedName = "java:jboss/mail/gmail")
    Session gmailSession;

    /**
     *
     * @param email
     * @throws
     * eu.europa.ec.fisheries.uvms.plugins.email.exception.PluginException
     */
    //@Asynchronous
    public void sendEmail(EmailType email) throws PluginException {

        LOG.info("Sending Email from " + email.getTo() + " to " + email.getTo() + " : " + email.getSubject());

        try {

            MimeMessage message = new MimeMessage(gmailSession);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getTo()));

            if (email.getCc() != null) {

                for (String cc : email.getCc()) {
                    Address adress = new InternetAddress(cc);
                    message.addRecipient(Message.RecipientType.CC, adress);
                }
            }

            message.setSubject(email.getSubject());
            message.setContent(email.getBody(), "text/html; charset=utf-8");
            
            Transport.send(message);

            LOG.info("Email was sent");

        } catch (MessagingException e) {
            LOG.error("Error while sending email : ", e);
        }
    }

}