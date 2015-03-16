package com.brutus.driver.modbus.shared;

import java.util.ArrayList;

import net.modbus.util.Function;

abstract public class DeviceRequest  implements Function{

abstract public ArrayList<ParamModbus> request(DeviceModbus parent);
	
}
