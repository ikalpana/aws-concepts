package com.ses.mail.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

import com.ses.mail.common.ErrorMessages;

@Component 
public class EmailRequest {

	@Email(message = ErrorMessages.INVALID_EMAIL)
	private String email;

	@NotBlank(message = ErrorMessages.USERNAME_REQUIRED)
	private String username;

	@NotBlank(message = ErrorMessages.OTP_REQUIRED)
	private String otp;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

}
