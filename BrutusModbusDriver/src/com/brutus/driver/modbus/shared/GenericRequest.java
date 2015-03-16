package com.brutus.driver.modbus.shared;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.modbus.Modbus;
import net.modbus.ModbusCoupler;
import net.modbus.facade.ModbusSerialMaster;
import net.modbus.facade.ModbusTCPMaster;
import net.modbus.procimg.Register;
import net.modbus.procimg.SimpleRegister;
import net.modbus.util.Function;
import net.modbus.util.SerialParameters;

import com.brutus.base.Param;
import com.brutus.shared.Debug;


abstract public class GenericRequest implements Function{
	protected ModbusSerialMaster masterSerial;
	protected ModbusTCPMaster masterTcp;
	protected int type;
	protected DeviceModbus parent;
	protected int functionId;
	protected int countRegister;
	protected int frontRegister;
	protected int offset;

	abstract public ArrayList<ParamModbus> request(ArrayList<Param> buffer, boolean isWriting);

	public GenericRequest() {
		super();
		type = RTU;
	}

	public GenericRequest(DeviceModbus parent) {
		super();
		this.parent = parent;
		if(parent.getType().toLowerCase().contentEquals("tcp"))
			this.type = TCP;
		else 
			this.type = RTU;
	}

	public String getHostAddress(){
		return parent.getAddress();
	}

	public int getHostPort(){
		return parent.getPort();
	}
	
	public int getTimeout(){
		return parent.getTimeout();
	}

	public  SerialParameters getSerialParameter(){
		ModbusCoupler.getReference().setUnitID(1);
		if(!System.getProperty ("os.name").toLowerCase().contains("win")){
			String add = parent.getAddress();
			if(add.contains("/dev/ttyAMA") || add.contains("dev/ttyAMA")){
				String ress = "/dev/ttyUSB00"+parent.getId();
				parent.setAddress(ress);
				Runtime rt = Runtime.getRuntime();
				if(!new File(ress).exists())
					try {
						rt.exec("sudo ln -s "+add+" "+ress);
					} catch (IOException e) {
					}
			}
		}
		SerialParameters params = new SerialParameters();
		params.setPortName(parent.getAddress());
		params.setBaudRate(parent.getBaudrate());
		params.setDatabits(8);
		params.setParity("None");
		params.setStopbits(1);
		params.setEncoding(Modbus.SERIAL_ENCODING_RTU);
		params.setEcho(false);
		Debug.print("new serial connection to "+parent.getAddress(), 3);
		return params;
	}

	public  Register[] createRegisterWriter(ArrayList<Param> values,boolean isWriting){
		ArrayList<ParamModbus> registers =  parent.getRequestParam();
		ArrayList<Register> reg = new ArrayList<Register>();
		for(int i = 0 ; i < registers.size(); i++){
			ParamModbus temp = registers.get(i);
			searchLimit(temp);
			if(isWriting){
				Object value = findParamValueByTag(values, temp.getTag());
				temp.setValue(value);
				if(value == null)break;
				if(value instanceof Double)
					reg.add(new SimpleRegister(((int) ((Double) value).intValue())));
				else if(value instanceof Short)
					reg.add(new SimpleRegister(((int) ((Short) value).intValue())));
				else if(value instanceof Long)
					reg.add(new SimpleRegister(((int) ((Long) value).intValue())));
				else if(value instanceof Float)
					reg.add(new SimpleRegister(((int) ((Float) value).intValue())));
				else if(value instanceof String)
					reg.add(new SimpleRegister((int) Integer.parseInt((String) value)));
				else
					reg.add(new SimpleRegister(((int) (value))));
			}
		}
		countRegister -= frontRegister;
		countRegister++;
		if(isWriting){
			if(frontRegister > 40000 && frontRegister < 49999){ //Holding Register
				frontRegister -= 40001;
				if(countRegister == 1)
					functionId = WRITE_SINGLE_REGISTER;
				else
					functionId = WRITE_MULTIPLE_REGISTERS;
				offset = 40001;
			}
			if(frontRegister > 10000 && frontRegister < 19999){ //Input Status
				frontRegister -= 10001;
				functionId = WRITE_COIL; 
				offset = 10001;
			}
		}
		else {
			if(frontRegister > 40000 && frontRegister < 49999){ //Holding Register
				frontRegister -= 40001;functionId = READ_MULTIPLE_REGISTERS; offset = 40001;
			}
			if(frontRegister > 30000 && frontRegister < 39999){ //Input Register
				frontRegister -= 30001;functionId = READ_INPUT_REGISTERS; offset = 30001;
			}
			if(frontRegister > 10000 && frontRegister < 19999){ //Input Status
				frontRegister -= 10001;functionId = READ_INPUT_DISCRETES; offset = 10001;
			}
		}
		//Default Coil Status
		Register[] registersValue = new Register[reg.size()];
		registersValue = reg.toArray(registersValue);
		return registersValue;
	}

	public Object findParamValueByTag(ArrayList<Param> values,String tag){
		if(values != null)
			for(Param par : values){
				if(par.getTag().contentEquals(tag)){
					return par.getValue();
				}
			}
		return null;
	}

	public  void searchLimit(ParamModbus reg){
		int address = reg.getAddress();
		if(frontRegister == 0) frontRegister = address; // first case when init frontRegister
		if(address < frontRegister) frontRegister = address;
		if(address > countRegister) countRegister = address;
		//if last prototype size is more than 1 register size , get next too
		if(reg.getPrototype() == WORD || reg.getPrototype() == FLOAT ) 
			countRegister++;
		else if(reg.getPrototype() == DOUBLE )
			countRegister += 3;
	}

	public  ArrayList<ParamModbus> saveRightElement(HashMap<Integer,byte[]> response,int quality){
		ArrayList<ParamModbus> buffer =  parent.getRequestParam();
		for (int k = 0; k < buffer.size() ; k++){
			buffer.get(k).getType();
			int index = buffer.get(k).getAddress() - offset; 
			boolean unsigned = buffer.get(k).isUnsigned();
			boolean isLittleEndian = buffer.get(k).isLittleEndian();
			buffer.get(k).setQuality(quality);
			if(quality == 0){//no need to save value
				buffer.get(k).setValue(null);
				return buffer;
			}
			switch(buffer.get(k).getPrototype()){
			case INT:
				if(!unsigned)
					buffer.get(k).setValue(BytesMath.twoBytesToShort(response.get(index)));
				else if(unsigned)
					buffer.get(k).setValue(BytesMath.twoBytesToUnsignedShort(response.get(index)));
				break;
			case WORD:
				if(!unsigned){
					if(!isLittleEndian)
						buffer.get(k).setValue(BytesMath.fourBytesToInt(response.get(index),response.get(index+1)));
					else buffer.get(k).setValue(BytesMath.fourBytesToInt(response.get(index+1),response.get(index)));
				}
				else if(unsigned){
					if(!isLittleEndian)
						buffer.get(k).setValue(BytesMath.fourBytesToIntUnsigned(response.get(index),response.get(index+1)));
					else buffer.get(k).setValue(BytesMath.fourBytesToIntUnsigned(response.get(index+1),response.get(index)));
				}
				break;
			case FLOAT:
				if(!isLittleEndian)
					buffer.get(k).setValue(BytesMath.fourBytesToFloat(response.get(index),response.get(index+1)));
				else buffer.get(k).setValue(BytesMath.fourBytesToFloat(response.get(index+1),response.get(index)));
				break;
			case DOUBLE:
				if(!isLittleEndian)
					buffer.get(k).setValue(BytesMath.eightBytesArrayToDouble(response.get(index),response.get(index+1),response.get(index+2),response.get(index+3)));
				else buffer.get(k).setValue(BytesMath.eightBytesArrayToDouble(response.get(index+3),response.get(index+2),response.get(index+1),response.get(index)));
				break;
			}
		}
		return buffer;
	}
}