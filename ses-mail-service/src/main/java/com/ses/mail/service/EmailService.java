package com.ses.mail.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.ses.mail.common.Constants;
import com.ses.mail.request.EmailRequest;
import com.ses.mail.response.Response;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class EmailService {

	private final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Value("${email.charset}")
	private String charSet;

	@Value("${email.sender}")
	private String sender;

	@Value("${email.subject}")
	private String emailSubject;

	@Autowired
	private Configuration configuration;

	@Autowired
	private AmazonSimpleEmailService emailService;

	public Response sendEmail(EmailRequest emailRequest) {

		int requestTimeout = 3000;
		Map<String, Object> model = new HashMap<>();
		model.put("username", emailRequest.getUsername());
		model.put("otp", emailRequest.getOtp());
		Template t;
		try {
			t = configuration.getTemplate("verify-email.ftlh");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			SendEmailRequest sendEmailRequest = new SendEmailRequest()
					.withDestination(new Destination().withToAddresses(emailRequest.getEmail()))
					.withMessage(new Message()
							.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(html)))
							.withSubject(new Content().withCharset("UTF-8").withData(emailSubject)))
					.withSource(sender).withSdkRequestTimeout(requestTimeout);
			emailService.sendEmail(sendEmailRequest);
			logger.info("Email sent successfully");

		} catch (IOException | TemplateException e) {

			e.printStackTrace();
		}

		return emailResponse();
	}

	public Response emailResponse() {
		Response response = new Response();
		response.setMessage(Constants.MAIL_SENT_SUCCESSFULLY);
		response.setStatus(Constants.SUCCESS);
		return response;
	}

}
