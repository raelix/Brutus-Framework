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
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import net.modbus.Modbus;
import net.modbus.net.ModbusTCPListener;
import net.modbus.net.TCPSlaveConnection;


public class ModbusTCPListenerGateway  extends ModbusTCPListener {
	private PropertyChangeListener listener;
	public  TCPConnectionHandlerGateway connectionHandler; 

	public ModbusTCPListenerGateway(int poolsize) {
		super(poolsize);
	}//constructor

	public ModbusTCPListenerGateway(int poolsize, InetAddress addr) {
		super(poolsize, addr);
	}//constructor

	public void run() {
		try {
			//      m_ServerSocket = new ServerSocket(m_Port, m_FloodProtection, m_Address);
			m_ServerSocket = new ServerSocket(m_Port);
			if(Modbus.debug) System.out.println("Listenening to " + m_ServerSocket.toString() + "(Port " + m_Port + ")");
			//Infinite loop, taking care of resources in case of a lot of parallel logins
			do {
				Socket incoming = m_ServerSocket.accept();
				if (Modbus.debug) System.out.println("Making new connection " + incoming.toString());
				if (m_Listening) {
					connectionHandler = new TCPConnectionHandlerGateway(new TCPSlaveConnection(incoming));
					if(listener != null) // set registerListener
						connectionHandler.addRegisterChangeListener(listener);
					m_ThreadPool.execute(connectionHandler);
					count();
				} else {
					//just close the socket
					incoming.close();
				}
			} while (m_Listening);
		} catch (SocketException iex) {
			if (!m_Listening) {
				return;
			} else {
				iex.printStackTrace();
			}
		} catch (IOException e) {
			//FIXME: this is a major failure, how do we handle this
		}
	}//run

	public void setOnRegistersChange(PropertyChangeListener listener){
		if(connectionHandler == null)
			this.listener = listener;
		else  
			connectionHandler.addRegisterChangeListener(listener);
	}

}//class ModbusTCPListenerGateway
