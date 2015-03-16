/***
 * Copyright 2002-2010 jamod development team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***/

package com.brutus.client.modbus;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import net.modbus.Modbus;
import net.modbus.ModbusCoupler;
import net.modbus.ModbusIOException;
import net.modbus.msg.ExceptionResponse;
import net.modbus.msg.ModbusRequest;
import net.modbus.msg.ModbusResponse;
import net.modbus.msg.ReadMultipleRegistersRequest;
import net.modbus.msg.ReadMultipleRegistersResponse;
import net.modbus.msg.WriteMultipleRegistersRequest;
import net.modbus.msg.WriteMultipleRegistersResponse;
import net.modbus.msg.WriteSingleRegisterRequest;
import net.modbus.msg.WriteSingleRegisterResponse;
import net.modbus.net.TCPConnectionHandler;
import net.modbus.net.TCPSlaveConnection;
import net.modbus.procimg.Register;

/**
 * Class implementing handler for incoming Modbus RTU/TCP requests as gateway modbus.
 *
 * @author raelix
 * @version @version@ (@date@)
 */
public class TCPConnectionHandlerGateway extends TCPConnectionHandler {
	private PropertyChangeSupport propertyChangeSupport;

	public TCPConnectionHandlerGateway(TCPSlaveConnection con) {
		super(con);
		propertyChangeSupport = new PropertyChangeSupport(this);
	}

	public void addRegisterChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void run() {
		try {
			do {
				ModbusRequest request = m_Transport.readRequest();
				ModbusResponse response = null;
				if (ModbusCoupler.getReference().getProcessImage() == null) {
					response = request.createExceptionResponse(Modbus.ILLEGAL_FUNCTION_EXCEPTION);
				}
				else 
					switch (request.getFunctionCode()) {
					case Modbus.READ_MULTIPLE_REGISTERS:
						int unitId = request.getUnitID();
						int transId = request.getTransactionID();
						Register[] regs = BrutusModbusClientStarter.getRegistersRange(((ReadMultipleRegistersRequest)request).getReference(),((ReadMultipleRegistersRequest)request).getWordCount());
						if(regs != null) {
							response = new ReadMultipleRegistersResponse(regs);
							response.setTransactionID(transId);
							response.setUnitID(unitId);
						}
						else response = request.createExceptionResponse(Modbus.ILLEGAL_ADDRESS_EXCEPTION);
						break;

					case Modbus.WRITE_MULTIPLE_REGISTERS:
						unitId = request.getUnitID();
						transId = request.getTransactionID();
						BrutusModbusClientStarter.setRegistersRange(((WriteMultipleRegistersRequest)request).getRegisters(),((WriteMultipleRegistersRequest)request).getReference(),((WriteMultipleRegistersRequest)request).getWordCount());
						response = new WriteMultipleRegistersResponse(((WriteMultipleRegistersRequest)request).getReference(),((WriteMultipleRegistersRequest)request).getWordCount());
//						response = (WriteMultipleRegistersResponse) request.createResponse();
						response.setTransactionID(transId);
						response.setUnitID(unitId);
						break;
					case Modbus.WRITE_SINGLE_REGISTER:
						unitId = request.getUnitID();
						transId = request.getTransactionID();
						BrutusModbusClientStarter.setRegisters(((WriteSingleRegisterRequest)request).getRegister(),((WriteSingleRegisterRequest)request).getReference());
						response = new WriteSingleRegisterResponse(((WriteSingleRegisterRequest)request).getReference(),((WriteSingleRegisterRequest)request).getRegister().getValue());
//						response = (WriteSingleRegisterResponse) request.createResponse();
						response.setTransactionID(transId);
						response.setUnitID(unitId);
						break;
					default:
						response = new ExceptionResponse();
						break;
					}
				//createModbusRequest
				/*DEBUG*/
				//				if (true) System.out.println("Request:" + request.getHexMessage());
				//				if (true) System.out.println("Response:" + response.getHexMessage());
				m_Transport.writeMessage(response);
			} while (true);
		} catch (ModbusIOException ex) {
			if (!ex.isEOF()) {
				ex.printStackTrace();
			}
		} finally {
			try {
				m_Connection.close();
			} catch (Exception ex) {}
		}
	}//run
}//TCPConnectionHandler