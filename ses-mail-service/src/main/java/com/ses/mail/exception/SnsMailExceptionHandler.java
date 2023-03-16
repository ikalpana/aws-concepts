package com.ses.mail.exception;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.amazonaws.services.simpleemail.model.AmazonSimpleEmailServiceException;
import com.ses.mail.common.Constants;

@ControllerAdvice
public class SnsMailExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<ObjectError> allErrors = ex.getAllErrors();
		ErrorResponse errorDetails = new ErrorResponse(Constants.FAILED, allErrors.get(0).getDefaultMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AmazonSimpleEmailServiceException.class)
	public ResponseEntity<ErrorResponse> handleMaxAmazonEmailServiceeException(AmazonSimpleEmailServiceException ex) {
		ErrorResponse errorDetails = new ErrorResponse(Constants.FAILED, Constants.CHECK_SENDER_EMAIL_ID);
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

	}

}
