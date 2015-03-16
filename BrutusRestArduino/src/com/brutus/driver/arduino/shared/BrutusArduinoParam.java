package com.brutus.driver.arduino.shared;

import javax.xml.bind.annotation.XmlAttribute;

import com.brutus.base.Param;

public class BrutusArduinoParam extends Param{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pin;
	private String mode;
	private int delay;
	private String direction;
	private String variable;
	private String devAddress;
	private int devTimeout;
	private boolean inverted;
	private int error;
	
	public BrutusArduinoParam() {
		super();
	}

	public BrutusArduinoParam(String name) {
		super(name);
	}
	@XmlAttribute
	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}
	@XmlAttribute
	public String getMode() {
		if(mode.contentEquals("a"))
			return "analog";
		if(mode.contentEquals("d"))
			return "digital";
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	@XmlAttribute
	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	@XmlAttribute(name="direction")
	public String getDirection() {
		if(direction != null){
			if(direction.contentEquals("input"))
				return "i";
			if(direction.contentEquals("output"))
				return "o";
		}
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	@XmlAttribute(name="variable")
	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getDevAddress() {
		return devAddress;
	}

	public void setDevAddress(String devAddress) {
		this.devAddress = devAddress;
	}

	public int getDevTimeout() {
		return devTimeout;
	}

	public void setDevTimeout(int devTimeout) {
		this.devTimeout = devTimeout;
	}
	@Override
	public String getTag() {
		return tag;
	}
	@Override
	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}


}
