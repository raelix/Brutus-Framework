package com.brutus.driver.modbus.shared;

import java.util.ArrayList;
import java.util.HashMap;

import net.modbus.facade.ModbusTCPMaster;
import net.modbus.procimg.InputRegister;
import net.modbus.procimg.Register;
import net.modbus.util.BitVector;

import com.brutus.base.Param;

public class GenericTcpRequest extends GenericRequest{

	public GenericTcpRequest(DeviceModbus parent) {
		super(parent);
	}

	@Override
	public ArrayList<ParamModbus> request(ArrayList<Param> buffer,boolean isWriting) {
		HashMap<Integer ,byte[]> response = new HashMap<Integer ,byte[]>();
//		GC.callFree();bug???
		Register[] regs = createRegisterWriter( buffer ,isWriting);
		masterTcp = new ModbusTCPMaster(getHostAddress(), getHostPort());
		try {
			masterTcp.connect();
			switch(functionId){
			case READ_INPUT_DISCRETES:
				BitVector bits = masterTcp.readInputDiscretes(frontRegister, countRegister);
				int counter = frontRegister;
				for(int i = 0; i < bits.size(); i++){
					response.put(counter, new byte[]{(byte)(bits.getBit(i) ? 1 : 0)});
					counter++;
				}
				break;

			case READ_MULTIPLE_REGISTERS:
				regs = masterTcp.readMultipleRegisters(frontRegister, countRegister);
				counter = frontRegister;
				for(Register reg : regs){
					response.put(counter, reg.toBytes());
					counter++;
				}
	
				break;

			case READ_INPUT_REGISTERS:
				InputRegister[] inregs = masterTcp.readInputRegisters(frontRegister, countRegister);
				counter = frontRegister;
				for(InputRegister inreg : inregs){
					response.put(counter, inreg.toBytes());
					counter++;
				}
				break;

			case WRITE_COIL:
				masterTcp.writeCoil(parent.getId(), frontRegister, regs[0].getValue() == 1 ? true : false);
				return null;

			case WRITE_SINGLE_REGISTER:
				masterTcp.writeSingleRegister(frontRegister, regs[0]);
				return null;

			case WRITE_MULTIPLE_COILS:
				bits = new BitVector(regs.length);
				for(int k = 0 ; k < bits.size(); k++)
					bits.setBit(k, regs[k].getValue() == 1 ? true : false);
				masterTcp.writeMultipleCoils(frontRegister, bits);
				return null;

			case WRITE_MULTIPLE_REGISTERS:
				masterTcp.writeMultipleRegisters(frontRegister, regs);
				return null;
			}
		} catch (Exception e) {
//			e.printStackTrace();
			System.err.println("GenericTCPRequest error connection to "+getHostAddress());
			return saveRightElement(response, 0);
		}
//		if(masterTcp != null)
//			masterTcp.disconnect();
		return saveRightElement(response, 180);
	}


}