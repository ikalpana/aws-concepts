package com.fileapis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.fileapis.common.Constants;

@RestControllerAdvice
public class CustomizedExceptionHandler {

	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ErrorResponse handleSizeLimitExceededException(IllegalStateException ex) {
		ErrorResponse error = new ErrorResponse();
		error.setStatus(HttpStatus.UNAUTHORIZED.value());
		error.setMessage(Constants.FILE_SIZE_EXCEEDED);
		return error;
	}
	
	@ExceptionHandler(AmazonS3Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleAmazonS3Exception(AmazonS3Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(Constants.BUCKET_NOT_EXIST);
        return error;
    }

}
