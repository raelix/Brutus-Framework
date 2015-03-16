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

package net.modbus.net;
import net.modbus.Modbus;
import net.modbus.ModbusCoupler;
import net.modbus.ModbusIOException;
import net.modbus.io.ModbusTransport;
import net.modbus.msg.ModbusRequest;
import net.modbus.msg.ModbusResponse;

/**
 * Class implementing a handler for incoming Modbus/TCP requests.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class TCPConnectionHandler implements Runnable {
	protected TCPSlaveConnection m_Connection;
	protected ModbusTransport m_Transport;
	SerialConnection con;
	/**
	 * Constructs a new <tt>TCPConnectionHandler</tt> instance.
	 *
	 * @param con an incoming connection.
	 */
	public TCPConnectionHandler(TCPSlaveConnection con) {
		setConnection(con);
	}//constructor


	/**
	 * Sets a connection to be handled by this <tt>
	 * TCPConnectionHandler</tt>.
	 *
	 * @param con a <tt>TCPSlaveConnection</tt>.
	 */
	public void setConnection(TCPSlaveConnection con) {
		m_Connection = con;
		//    propertyChangeSupport = new PropertyChangeSupport(this);
		m_Transport = m_Connection.getModbusTransport();
	}//setConnection


	public TCPConnectionHandler setConnections(TCPSlaveConnection con) {
		m_Connection = con;
		//	    propertyChangeSupport = new PropertyChangeSupport(this);
		m_Transport = m_Connection.getModbusTransport();
		return this;
	}//setConnection

	public void run() {
		try {
			do {
				ModbusRequest request = m_Transport.readRequest();
				ModbusResponse response = null;
				  if (ModbusCoupler.getReference().getProcessImage() == null) {
			          response =
			              request.createExceptionResponse(Modbus.ILLEGAL_FUNCTION_EXCEPTION);
			        } else {
			          response = request.createResponse();
			        }
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

