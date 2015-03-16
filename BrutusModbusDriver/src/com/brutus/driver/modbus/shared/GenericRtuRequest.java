package com.brutus.driver.modbus.shared;

import java.util.ArrayList;
import java.util.HashMap;

import net.modbus.facade.ModbusSerialMaster;
import net.modbus.procimg.InputRegister;
import net.modbus.procimg.Register;
import net.modbus.util.BitVector;

import com.brutus.base.Param;

public class GenericRtuRequest extends GenericRequest{

	public GenericRtuRequest(DeviceModbus parent) {
		super(parent);
	}

	@Override
	public ArrayList<ParamModbus> request(ArrayList<Param> buffer, boolean isWriting) {
		HashMap<Integer ,byte[]> response = new HashMap<Integer ,byte[]>();
		Register[] regs = createRegisterWriter( buffer, isWriting);
		masterSerial = new ModbusSerialMaster(getSerialParameter());
		try {
			masterSerial.connect();
			switch(functionId){

			case READ_INPUT_DISCRETES:
				BitVector bits = masterSerial.readInputDiscretes(parent.getId(),frontRegister, countRegister);
				int counter = frontRegister;
				for(int i = 0; i < bits.size(); i++){
					response.put(counter, new byte[]{(byte)(bits.getBit(i) ? 1 : 0)});
					counter++;
				}
				break;

			case READ_MULTIPLE_REGISTERS:
				regs = masterSerial.readMultipleRegisters(parent.getId(),frontRegister, countRegister);
				counter = frontRegister;
				for(Register reg : regs){
					response.put(counter, reg.toBytes());
					counter++;
				}
				break;

			case READ_INPUT_REGISTERS:
				InputRegister[] inregs = masterSerial.readInputRegisters(parent.getId(),frontRegister, countRegister);
				counter = frontRegister;
				for(InputRegister inreg : inregs){
					response.put(counter, inreg.toBytes());
					counter++;
				}
				break;

			case WRITE_COIL:
				masterSerial.writeCoil(parent.getId(), frontRegister, regs[0].getValue() == 1 ? true : false);
				return null;

			case WRITE_SINGLE_REGISTER:
				masterSerial.writeSingleRegister(parent.getId(),frontRegister, regs[0]);
				return null;

			case WRITE_MULTIPLE_COILS:
				bits = new BitVector(regs.length);
				for(int k = 0 ; k < bits.size(); k++)
					bits.setBit(k, regs[k].getValue() == 1 ? true : false);
				masterSerial.writeMultipleCoils(parent.getId(),frontRegister, bits);
				return null;

			case WRITE_MULTIPLE_REGISTERS:
				masterSerial.writeMultipleRegisters(parent.getId(),frontRegister, regs);
				return null;
			}
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("GenericRTURequest error connection to "+getHostAddress());
			return saveRightElement(response, 0);
		}
		if(masterSerial != null)
			masterSerial.disconnect();
		return saveRightElement(response, 180);
	}


}
