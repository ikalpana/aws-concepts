package com.ses.mail.command;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ses.mail.request.EmailRequest;
import com.ses.mail.response.Response;
import com.ses.mail.service.EmailService;

@Service
public class SendCommand implements Command<EmailRequest, ResponseEntity<Response>> {

	@Autowired
	EmailService emailService;

	@Override
	public ResponseEntity<Response> execute(EmailRequest request) {

		return ResponseEntity.status(HttpStatus.SC_OK).body(emailService.sendEmail(request));
	}
}