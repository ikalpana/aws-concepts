package com.ses.mail.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ses.mail.command.SendCommand;
import com.ses.mail.request.EmailRequest;
import com.ses.mail.response.Response;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class EmailController {

	@Autowired
	SendCommand sendCommand;

	@ApiOperation(value = "This operation is responsible to send email through SES  ")
	@ApiResponses({ @ApiResponse(code = 200, response = Response.class, message = "Success"),
			@ApiResponse(code = 400, response = Response.class, message = "Failed") })
	@PostMapping(value = "/sendMail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> sendMail(@Valid @RequestBody EmailRequest request) {
		return sendCommand.execute(request);
	}

}
