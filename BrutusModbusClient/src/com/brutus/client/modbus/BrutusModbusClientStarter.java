package com.brutus.client.modbus;
import java.util.ArrayList;

import net.modbus.ModbusCoupler;
import net.modbus.procimg.Register;
import net.modbus.procimg.SimpleInputRegister;
import net.modbus.procimg.SimpleProcessImage;
import net.modbus.procimg.SimpleRegister;

import com.brutus.adapter.ClientAdapter;
import com.brutus.adapter.CoreAdapter;
import com.brutus.base.Param;
import com.brutus.client.modbus.shared.BytesMath;
import com.brutus.client.modbus.shared.MasterModbus;
import com.brutus.client.modbus.shared.BrutusModbusClient;
import com.brutus.shared.BrutusConf;

public class BrutusModbusClientStarter extends ClientAdapter{
	public static CoreAdapter coreAdt;
	public static ArrayList<Param> param;
	public static BrutusModbusClient plugin;
	public static MasterModbus master;

	public BrutusModbusClientStarter(CoreAdapter core) {
		super(core);
		coreAdt = core;
		init();
	}

	public void init(){
		try {
			param = BrutusConf.getInstance().getCore().getParam();
			plugin = (BrutusModbusClient) BrutusConf.loadPluginClass(BrutusModbusClient.class, "BrutusModbusClient");
			master = plugin.getDevice();
		} catch (Exception e) {
			System.err.println("No configuration found for BrutusModbusClient use default settings!");
		}
	}

	@Override
	public void run(){
		super.run();
		if(plugin != null)
		if(!plugin.isEnable())
			return;
		if(BrutusConf.getInstance() == null || BrutusConf.getInstance().getCore() == null)
			return;
		SimpleProcessImage spi = new SimpleProcessImage();
		for(int k = 0 ; k < param.size(); k++){
			spi.addRegister(new SimpleInputRegister(0));
		}

		ModbusCoupler.getReference().setMaster(false);
		ModbusCoupler.getReference().setProcessImage(spi);
		ModbusCoupler.getReference().setUnitID(master != null ? master.getId() : 1);   
		ModbusTCPListenerGateway listener = new ModbusTCPListenerGateway(5);
		listener.setPort(master != null ?  master.getPort() : 502);
		listener.start();
		System.out.println("BrutusModbusClient start listen on port: "+(master != null ?  master.getPort() : 502));

	}

	public static void setRegistersRange(Register[] regs, int ref, int count){
		ArrayList<Param> requestParam = new ArrayList<>();
		int tot = ref+count;
		if(param.size() < tot) return;
		for(int f = ref; f < ref+count ; f++){
			System.out.println("setting param name: "+param.get(f).getTag()+" with value: "+regs[f].getValue());
			requestParam.add(new Param(param.get(f).getTag(), regs[f].getValue()));
		}
		coreAdt.setValues(requestParam);
	
	}
	
	public static void setRegisters(Register reg, int ref){
		ArrayList<Param> requestParam = new ArrayList<Param>();
//		if(ref > param.size()) return; 
		Param par = new Param(param.get(ref).getTag(), reg.getValue());
		requestParam.add(par);
		coreAdt.setValues(requestParam);
	
	}
	
	
	public static Register[] getRegistersRange(int ref, int count){
		ArrayList<Param> requestParam = new ArrayList<>();
		ArrayList<Register> regs = new ArrayList<>();
		int tot = ref+count;
		if(param.size() < tot) return null;
		for(int k = ref; k < tot ; k++){
			requestParam.add(param.get(k));
		}
		requestParam = coreAdt.getValues(requestParam);
		for(Param par : requestParam){
			if(par.getValue() instanceof Short){
				regs.add(new SimpleRegister(BytesMath.shortToBytes((short) par.getValue())[1],BytesMath.shortToBytes((short) par.getValue())[0]));
			}
			else if(par.getValue() instanceof Integer){
				regs.add(new SimpleRegister((int) par.getValue()));
			}
			else if(par.getValue() instanceof Float){
				regs.add(new SimpleRegister(BytesMath.floatToBytes( (float) par.getValue())[0],BytesMath.floatToBytes((float) par.getValue())[1]));
				regs.add(new SimpleRegister(BytesMath.floatToBytes( (float) par.getValue())[2],BytesMath.floatToBytes((float) par.getValue())[3]));
			}
			else if(par.getValue() instanceof Double){

			}
			else 
				regs.add(new SimpleRegister(0));
		}
		Register[] registersValue = new Register[regs.size()];
		registersValue = regs.toArray(registersValue);
		return registersValue;
	}


}