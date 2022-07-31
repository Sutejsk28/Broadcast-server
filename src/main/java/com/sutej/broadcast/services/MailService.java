package com.sutej.broadcast.services;

import com.sutej.broadcast.exception.BroadcastExceptiion;
import com.sutej.broadcast.modals.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    public void sendMail(NotificationEmail notificationEmail) throws BroadcastExceptiion {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("broadcast@gmail.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
//        try{
//            mailSender.send(messagePreparator);
//            log.info("Activate email sent!");
//        }catch(MailException e){
//            throw new BroadcastExceptiion("Exception occured while sending email to " + notificationEmail);
//        }
        mailSender.send(messagePreparator);
        log.info("Activate email sent!");
    }
}
