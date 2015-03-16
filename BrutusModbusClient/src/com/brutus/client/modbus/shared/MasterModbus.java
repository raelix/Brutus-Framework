package com.brutus.client.modbus.shared;
import javax.xml.bind.annotation.XmlAttribute;

import com.brutus.base.Device;
import com.brutus.shared.BrutusConf;

public class MasterModbus extends Device{

	private int port = 502;
	private int id = 1;
	private int clientLimit = 10;
	private boolean showAll = true;

	public MasterModbus() {
		super();
	}

	public MasterModbus(String tag) {
		super(tag);
	}
	@Override
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	@Override
	@XmlAttribute
	public String getAddress() {
		return BrutusConf.getDeviceByName(getTag()).getAddress();
	}
	@Override
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	@XmlAttribute
	public String getType() {
		return BrutusConf.getDeviceByName(getTag()).getType();
	}

	public void setType(String type) {
		this.type = type;
	}
	@XmlAttribute
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	@XmlAttribute
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@XmlAttribute
	public int getClientLimit() {
		return clientLimit;
	}

	public void setClientLimit(int clientLimit) {
		this.clientLimit = clientLimit;
	}
	@XmlAttribute
	public boolean isShowAll() {
		return showAll;
	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}


}