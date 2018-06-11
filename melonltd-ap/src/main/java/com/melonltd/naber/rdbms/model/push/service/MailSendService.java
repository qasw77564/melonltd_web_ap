package com.melonltd.naber.rdbms.model.push.service;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.melonltd.naber.endpoint.util.Tools;

@Service("mailSendService")
@PropertySource("classpath:/config.properties")
public class MailSendService {
	private static final Logger LOGGERO = LoggerFactory.getLogger(MailSendService.class);

	private static Session SESSION = null;
	@Value("${spring.mail.stl}")
	private boolean IS_STL = false;
	@Value("${spring.mail.host}")
	private String HOST;
	@Value("${spring.mail.port}")
	private String port;
	@Value("${spring.mail.username}")
	private String USERNAME;
	@Value("${spring.mail.password}")
	private String PASSWORD;

	private Session getSession() {
		if (SESSION == null) {
			Properties props = new Properties();
			props.put("mail.smtp.host", HOST);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", "true");
			if (IS_STL) {
				props.put("mail.smtp.socketFactory.port", HOST);
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			} else {
				props.put("mail.smtp.starttls.enable", "true");
			}

			SESSION = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(USERNAME, PASSWORD);
				}
			});
		}
		return SESSION;
	}

	public void sendEmail(String toEmail, String subject, String body) {
		try {
			
			MimeMessage msg = new MimeMessage(getSession());
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress(USERNAME, "NABER"));
			msg.setReplyTo(InternetAddress.parse(USERNAME, false));
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "UTF-8");
//			msg.setSentDate(new Date(Tools.getGMTDate("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'")));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			System.out.println("Message is ready");
			Transport.send(msg);
			System.out.println("EMail Sent Successfully!!");
		} catch (Exception e) {
			LOGGERO.error("send mail fail to mail : {}", toEmail);
		}
	}

}
