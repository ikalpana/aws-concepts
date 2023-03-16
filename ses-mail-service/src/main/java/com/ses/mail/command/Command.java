package com.ses.mail.command;

public interface Command<E, T> {

	public T execute(E request);	

}
