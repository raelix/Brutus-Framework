package com.brutus.driver.modbus.shared;
import java.util.StringTokenizer;

import javax.xml.bind.annotation.XmlAttribute;

import net.modbus.util.Function;

import com.brutus.base.Param;
import com.brutus.shared.BrutusConf;

public class ParamModbus extends Param implements Function{

	private String type = "INT,S,B" ;
	private int address;
	private boolean unsigned;
	private boolean littleEndian;
	private int prototype;

	public ParamModbus() {
		super();
	}

	public ParamModbus(String tag){
		super(tag);
	}

	@Override
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@XmlAttribute
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	
	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	public boolean isUnsigned() {
		reload();
		return unsigned;
	}

	public void setUnsigned(boolean unsigned) {
		this.unsigned = unsigned;
	}

	@XmlAttribute
	public boolean isLittleEndian() {
		reload();
		return littleEndian;
	}

	public void setLittleEndian(boolean littleEndian) {
		this.littleEndian = littleEndian;
	}
	@XmlAttribute
	public int getPrototype() {
		reload();
		return prototype;
	}

	public void setPrototype(int prototype) {
		this.prototype = prototype;
	}


	public boolean isRetentant() {
		return BrutusConf.getParamByName(getTag()).isRetentant();
	}

	public void setRetentant(boolean retentant) {
		BrutusConf.getParamByName(getTag()).setRetentant(retentant);
	}

	private void reload(){
		String full = getType();
		full = full.toLowerCase();
		String key;
		StringTokenizer token = new StringTokenizer(full, ",");
		while(token.hasMoreTokens()){
			key = token.nextToken();
			key = key.toLowerCase();
			switch(key){
			case "int":
				setPrototype(INT);
				break;
			case "word":
				setPrototype(WORD);
				break;
			case "float":
				setPrototype(FLOAT);
				break;
			case "double":
				setPrototype(DOUBLE);
				break;
			case "i":
				setPrototype(INT);
				break;
			case "w":
				setPrototype(WORD);
				break;
			case "f":
				setPrototype(FLOAT);
				break;
			case "d":
				setPrototype(DOUBLE);
				break;
			case "s":
				setUnsigned(false);
				break;
			case "u":
				setUnsigned(true);
				break;
			case "b":
				setLittleEndian(false);
				break;
			case "l":
				setLittleEndian(true);
				break;
			}
		}
	}
}
