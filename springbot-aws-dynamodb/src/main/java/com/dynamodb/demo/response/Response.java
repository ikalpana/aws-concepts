package com.dynamodb.demo.response;

public class Response {

	private String id;
	private String status;
	private String message;

	public Response(String id, String status, String message) {
		super();
		this.id = id;
		this.status = status;
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
