package com.brutus.driver.arduino.shared;

public class Response {

	private String message;
	private String id;
	private String name;
	private boolean connected;
	private int return_value;

	public Response() {
		// TODO Auto-generated constructor stub
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getReturn_value() {
		return return_value;
	}

	public void setReturn_value(int return_value) {
		this.return_value = return_value;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}


}
