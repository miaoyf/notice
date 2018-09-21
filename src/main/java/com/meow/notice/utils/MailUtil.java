package com.meow.notice.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailUtil {
	private static Logger logger = LoggerFactory.getLogger(MailUtil.class);
	private static Properties properties = new Properties();
	static {
	    try {
	    	// 使用ClassLoader加载properties配置文件生成对应的输入流
	    	InputStream in = MailUtil.class.getClassLoader().getResourceAsStream("email.properties");
	    	// 使用properties对象加载输入流
			properties.load(in);
		} catch (IOException e) {
			logger.error("", e);
		}
	} 


	public static void sendMail(String subject, String mailBody) {
		logger.info("****send email******begin****");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.126.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.ssl.enable", "true");

		Session session = Session.getInstance(props);

		Message msg = new MimeMessage(session);
		Transport transport = null;
		try {
			msg.setSubject(transSubject(subject));

			msg.setText(mailBody);

			msg.setFrom(new InternetAddress(properties.getProperty("email.sender")));

			transport = session.getTransport();

			transport.connect(properties.getProperty("email.username"), properties.getProperty("email.password"));

			transport.sendMessage(msg, new Address[] { new InternetAddress(properties.getProperty("email.receiver"))});
			logger.info("Mail successfully sent!");
			try {
				transport.close();
			} catch (MessagingException e) {
				logger.error("Close Transport Exception", e);
			}
			logger.info("****send email******end****");
		} catch (Exception e) {
			logger.error("Send email Exception:", e);
		} finally {
			try {
				transport.close();
			} catch (MessagingException e) {
				logger.error("Close Transport Exception", e);
			}
		}
	}

	private static String transSubject(String subject) {
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(subject);
		if (!m.find()) {
			try {
				subject = new String(subject.getBytes("ISO8859_1"), "GBK");
				subject = MimeUtility.decodeText(subject);
			} catch (UnsupportedEncodingException e) {
				logger.error("Encoding Exception:", e);
			}
		}
		return subject;
	}
}
