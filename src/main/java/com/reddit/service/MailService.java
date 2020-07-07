package com.reddit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.reddit.exception.SpringRedditException;
import com.reddit.model.NotificationEmail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
	
	private final JavaMailSender javaMailSender;
	private final MailContentBuilder mailContentBuilder;
	
	@Value("${spring.mail.username}")
	private String from;

	@Async
	public void sendMail(NotificationEmail email) {
		MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom(from);
			messageHelper.setTo(email.getRecipient());
			messageHelper.setSubject(email.getSubject());
			messageHelper.setText(mailContentBuilder.build(email.getBody()), true);
		};
		
		try {
			javaMailSender.send(mimeMessagePreparator);
			log.info("Activation e-mail sent!!");
		}catch (MailException e) {
			throw new SpringRedditException("Error occured while sending an email to " + email.getRecipient());
		}
	}
	
}
