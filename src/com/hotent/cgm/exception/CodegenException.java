package com.hotent.cgm.exception;

public class CodegenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1226430826318382469L;

	public CodegenException(String msg) {
		super(msg);
	}

	public CodegenException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public CodegenException(Throwable throwable) {
		super(throwable);
	}
}
