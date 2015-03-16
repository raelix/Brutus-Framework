package com.brutus.driver.modbus;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.modbus.Modbus;
import net.modbus.ModbusCoupler;
import net.modbus.io.ModbusSerialTransaction;
import net.modbus.msg.ModbusRequest;
import net.modbus.msg.ModbusResponse;
import net.modbus.msg.ReadInputDiscretesRequest;
import net.modbus.msg.ReadInputDiscretesResponse;
import net.modbus.msg.ReadInputRegistersRequest;
import net.modbus.msg.ReadInputRegistersResponse;
import net.modbus.msg.ReadMultipleRegistersRequest;
import net.modbus.msg.ReadMultipleRegistersResponse;
import net.modbus.msg.WriteCoilRequest;
import net.modbus.msg.WriteCoilResponse;
import net.modbus.msg.WriteMultipleCoilsRequest;
import net.modbus.msg.WriteMultipleRegistersRequest;
import net.modbus.msg.WriteMultipleRegistersResponse;
import net.modbus.msg.WriteSingleRegisterRequest;
import net.modbus.msg.WriteSingleRegisterResponse;
import net.modbus.net.SerialConnection;
import net.modbus.procimg.Register;
import net.modbus.procimg.SimpleRegister;
import net.modbus.util.BitVector;
import net.modbus.util.SerialParameters;

import com.brutus.base.Param;
import com.brutus.driver.modbus.shared.BytesMath;
import com.brutus.driver.modbus.shared.DeviceModbus;
import com.brutus.driver.modbus.shared.DeviceRequest;
import com.brutus.driver.modbus.shared.ParamModbus;
import com.brutus.shared.Debug;

public class DeviceRequestRTU extends DeviceRequest {

//	msm = new ModbusSerialMaster(params);
//      msm.connect();
	int offset = 0;
	int functionId = 1;//default function Coil Status
	int frontRegister = 0;
	int countRegister = 0;
	String comType = "rtu";
	public SerialConnection con ;

	public void writeRequest(DeviceModbus parent,ArrayList<Param> values) {
		ModbusResponse res = null;
		ModbusRequest req = null ;
		Register[] valuesRegister;
		try {
			valuesRegister = createExecutorWriter(parent, values);
			int function = 0;
			if(functionId == WRITE_MULTIPLE_REGISTERS){
			if(valuesRegister.length > 1)
				function = WRITE_MULTIPLE_REGISTERS;//single register doesnt works
			else function = WRITE_MULTIPLE_REGISTERS;
			}
			else{
				if(valuesRegister.length > 1)
					function = WRITE_COIL;
				else function = WRITE_MULTIPLE_COILS;
			}
			switch(function){
			case WRITE_MULTIPLE_REGISTERS:
				req = new WriteMultipleRegistersRequest(frontRegister, valuesRegister);
				req.setUnitID(parent.getId());
				int transId = req.getTransactionID();
				ModbusSerialTransaction trans = rtuConnection(parent, req);
				res = (WriteMultipleRegistersResponse) trans.getResponse();
				res.setTransactionID(transId);
				req.setTransactionID(transId);
				break;
			case WRITE_SINGLE_REGISTER:
				req = new WriteSingleRegisterRequest(frontRegister, valuesRegister[0]);
				req.setUnitID(parent.getId());
				transId = req.getTransactionID();
				trans = rtuConnection(parent, req);
				res = (WriteSingleRegisterResponse) trans.getResponse();
				res.setTransactionID(transId);
				req.setTransactionID(transId);
				break;
			case WRITE_COIL:
				req = new WriteCoilRequest(frontRegister, valuesRegister[0].getValue() == 0 ? false : true);
				req.setUnitID(parent.getId());
				transId = req.getTransactionID();
				trans = rtuConnection(parent, req);
				res = (WriteCoilResponse) trans.getResponse();
				res.setTransactionID(transId);
				req.setTransactionID(transId);
				break;
			case WRITE_MULTIPLE_COILS:
				BitVector b = new BitVector(valuesRegister.length);
				for(int i = 0; i < valuesRegister.length ; i++){
					b.setBit(i, valuesRegister[i].getValue() == 0 ? true : false);
				}
				req = new WriteMultipleCoilsRequest(frontRegister, b);
				req.setUnitID(parent.getId());
				transId = req.getTransactionID();
				trans = rtuConnection(parent, req);
				res = (WriteCoilResponse) trans.getResponse();
				res.setTransactionID(transId);
				req.setTransactionID(transId);
				break;
			default: 
				break;
			}
			if(con != null)
				con.close();
		} catch (Exception e) {
			e.getStackTrace();
			Debug.printError("BrutoModbusDriver Plugin RTU Write error for "+parent.getTag(),1);
		}
	}
	
	public  Register[] createExecutorWriter(DeviceModbus parent,ArrayList<Param> values){
		ArrayList<ParamModbus> registers =  parent.getRequestParam();
		ArrayList<Register> reg = new ArrayList<Register>();
		for(int i = 0 ; i < registers.size(); i++){
			ParamModbus temp = registers.get(i);
			searchLimit(temp);
			temp.setValue(findParamValueByTag(values, temp.getTag()));
			reg.add(new SimpleRegister(((int) findParamValueByTag(values, temp.getTag()))));
		}

		
		countRegister -= frontRegister;
		countRegister++;//maybe lost data
		if(frontRegister > 40000 && frontRegister < 49999){ //Holding Register
			frontRegister -= 40001;functionId = WRITE_MULTIPLE_REGISTERS; offset = 40001;
		}
		if(frontRegister > 10000 && frontRegister < 19999){ //Input Status
			frontRegister -= 10001;functionId = WRITE_COIL; offset = 10001;
		}
		//Default Coil Status
		Register[] registersValue = new Register[reg.size()];
		registersValue = reg.toArray(registersValue);
		return registersValue;
	}

	public Object findParamValueByTag(ArrayList<Param> values,String tag){
		if(values != null)
		for(Param par : values){
			if(par.getTag().contentEquals(tag))
				return par.getValue();
		}
		return null;
	}
	
	@Override
	public ArrayList<ParamModbus> request(DeviceModbus parent) {
		HashMap<Integer ,byte[]> response = new HashMap<Integer ,byte[]>();
		ModbusResponse res = null;
		ModbusRequest req = null ;
		try {
			createExecutor(parent);
			switch(functionId){
			case READ_MULTIPLE_REGISTERS:
				req = new ReadMultipleRegistersRequest(frontRegister, countRegister);
				req.setUnitID(parent.getId());
				int transId = req.getTransactionID();
				ModbusSerialTransaction trans = rtuConnection(parent, req);
				res = (ReadMultipleRegistersResponse) trans.getResponse();
				res.setTransactionID(transId);
				req.setTransactionID(transId);
				int counter = frontRegister;
				for(int i = 0; i < ((ReadMultipleRegistersResponse) res).getRegisters().length ; i++){
					response.put(counter,((ReadMultipleRegistersResponse)res).getRegister(i).toBytes()); 
					counter++;
				}
				break;
			case READ_INPUT_REGISTERS:
				req = new ReadInputRegistersRequest(frontRegister, countRegister);
				req.setUnitID(parent.getId());
				transId = req.getTransactionID();
				trans = rtuConnection(parent, req);
				res = (ReadInputRegistersResponse) trans.getResponse();
				res.setTransactionID(transId);
				req.setTransactionID(transId);
				counter = frontRegister;
				for(int i = 0; i < ((ReadInputRegistersResponse) res).getRegisters().length ; i++){
					response.put(counter,((ReadInputRegistersResponse)res).getRegister(i).toBytes()); 
					counter++;
				}
				break;
			case READ_INPUT_DISCRETES:
				req = new ReadInputDiscretesRequest(frontRegister, countRegister);
				req.setUnitID(parent.getId());
				transId = req.getTransactionID();
				trans = rtuConnection(parent, req);
				res = (ReadInputDiscretesResponse) trans.getResponse();
				res.setTransactionID(transId);
				req.setTransactionID(transId);
				counter = frontRegister;
				response.put(counter,((ReadInputDiscretesResponse)res).getDiscretes().getBytes()); 
				break;
			default: 
				break;
			}
			return 	saveRightElement(parent,response,180);
		} catch (Exception e) {
			e.getStackTrace();
			Debug.printError("BrutoModbusDriver Plugin RTU Connection Error for "+parent.getTag()+" with device id: "+parent.getId(),1);
			return 	saveRightElement(parent,response,0);
		}
	}

	public  DeviceRequestRTU(DeviceModbus parent){

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
		params.setEcho(false);//opzione true è buggata
		Debug.print("new serial connection to "+parent.getAddress(), 3);
		con = new SerialConnection(params);
		Debug.print("BrutoModbusDriver: modbus RTU connected with "+parent.getTag(), 3);

	}

	public void connect(){
		try {
			con.open();
		} catch (Exception e) {
			return;
		}
	}

	public void close(){
		
			con.close();
		
	}

	public ModbusSerialTransaction rtuConnection(DeviceModbus parent, ModbusRequest req) throws Exception{
		ModbusSerialTransaction trans = new ModbusSerialTransaction(con);
		trans.setRequest(req);
		con.setReceiveTimeout(parent.getTimeout());
		trans.setTransDelayMS(parent.getTransdelay());
		trans.setRetries(parent.getRetries());
		trans.execute();
		return trans;
	}

	public  ArrayList<ParamModbus> saveRightElement(DeviceModbus parent, HashMap<Integer,byte[]> response,int quality){
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


	public  void createExecutor(DeviceModbus parent){
		ArrayList<ParamModbus> registers =  parent.getRequestParam();
		for(int i = 0 ; i < registers.size(); i++){
			ParamModbus temp = registers.get(i);
			searchLimit(temp);
		}
		//request of params readed
		//		parent.resetRequest();
		countRegister -= frontRegister;
		countRegister++;//maybe lost data
		if(frontRegister > 40000 && frontRegister < 49999){ //Holding Register
			frontRegister -= 40001;functionId = READ_MULTIPLE_REGISTERS; offset = 40001;
		}
		if(frontRegister > 30000 && frontRegister < 39999){ //Input Register
			frontRegister -= 30001;functionId = READ_INPUT_REGISTERS; offset = 30001;
		}
		if(frontRegister > 10000 && frontRegister < 19999){ //Input Status
			frontRegister -= 10001;functionId = READ_INPUT_DISCRETES; offset = 10001;
		}
		//Default Coil Status
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


}
