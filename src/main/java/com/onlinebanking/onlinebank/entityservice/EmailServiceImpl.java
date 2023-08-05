package com.onlinebanking.onlinebank.entityservice;

import com.onlinebanking.onlinebank.entitydto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;
    @Override
    public void sendSuccessEmail(EmailDetails emailDetails) {
           try{
               SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(senderEmail);
                mailMessage.setTo(emailDetails.getRecipient());
                mailMessage.setText(emailDetails.getMessageBody());
                mailMessage.setSubject(emailDetails.getEmailSubject());

                javaMailSender.send(mailMessage);
               System.out.println("Thank you for opening an Account with us");
           } catch (MailException e) {
               throw new RuntimeException(e);
           }
    }
}
