package com.brutus.core.rest;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public class GenericStatus {

	 String message;
	 Status status;

	public GenericStatus(String message, Status status) {
		this.message = message;
		this.status = status;
	}
	
	public StatusType getStatus() {
		return new StatusType() {

			@Override
			public int getStatusCode() {
				return status.getStatusCode();
			}

			@Override
			public String getReasonPhrase() {
				return message;
			}

			@Override
			public Family getFamily() {
				return null;
			}
		};
	}
}
