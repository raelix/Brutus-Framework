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

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.modbus.Modbus;
import net.modbus.util.ThreadPool;

/**
 * Class that implements a ModbusTCPListener.<br>
 * If listening, it accepts incoming requests
 * passing them on to be handled.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class ModbusTCPListener
    implements Runnable {

	protected static int c_RequestCounter = 0;
  protected ServerSocket m_ServerSocket = null;
  protected ThreadPool m_ThreadPool;
  protected Thread m_Listener;
  protected int m_Port = Modbus.DEFAULT_PORT;
  protected int m_FloodProtection = 5;
  protected boolean m_Listening;
  protected InetAddress m_Address;
  public  TCPConnectionHandler connectionHandler; 
  /**
   * Constructs a ModbusTCPListener instance.<br>
   *
   * @param poolsize the size of the <tt>ThreadPool</tt> used to handle
   *        incoming requests.
   */
  public ModbusTCPListener(int poolsize) {
	  Thread.currentThread().setName("ModbusTCPListener");
    m_ThreadPool = new ThreadPool(poolsize);
    try {
      m_Address = InetAddress.getLocalHost();
    } catch (UnknownHostException ex) {

    }
  }//constructor

  /**
   * Constructs a ModbusTCPListener instance.<br>
   *
   * @param poolsize the size of the <tt>ThreadPool</tt> used to handle
   *        incoming requests.
   * @param addr the interface to use for listening.
   */
  public ModbusTCPListener(int poolsize, InetAddress addr) {
    m_ThreadPool = new ThreadPool(poolsize);
    m_Address = addr;
  }//constructor


  /**
   * Sets the port to be listened to.
   *
   * @param port the number of the IP port as <tt>int</tt>.
   */
  public void setPort(int port) {
    m_Port = port;
  }//setPort


  /**
   * Sets the address of the interface to be listened to.
   *
   * @param addr an <tt>InetAddress</tt> instance.
   */
  public void setAddress(InetAddress addr) {
    m_Address = addr;
  }//setAddress

  /**
   * Starts this <tt>ModbusTCPListener</tt>.
   */
  public void start() {
    m_Listener = new Thread(this);
    m_Listener.start();
    m_Listening = true;
  }//start

  /**
   * Stops this <tt>ModbusTCPListener</tt>.
   */
  public void stop() {
    m_Listening = false;
    try {
      m_ServerSocket.close();
      m_Listener.join();
      m_ThreadPool.close();
    } catch (Exception ex) {
      //?
    }
  }//stop

  /**
   * Accepts incoming connections and handles then with
   * <tt>TCPConnectionHandler</tt> instances.
   */
  public void run() {
    try {
      /*
          A server socket is opened with a connectivity queue of a size specified
          in int floodProtection.  Concurrent login handling under normal circumstances
          should be allright, denial of service attacks via massive parallel
          program logins can probably be prevented.
      */
//      m_ServerSocket = new ServerSocket(m_Port, m_FloodProtection, m_Address);
      m_ServerSocket = new ServerSocket(m_Port);
      if(Modbus.debug) System.out.println("Listenening to " + m_ServerSocket.toString() + "(Port " + m_Port + ")");

      //Infinite loop, taking care of resources in case of a lot of parallel logins
      do {
        Socket incoming = m_ServerSocket.accept();
        if (Modbus.debug) System.out.println("Making new connection " + incoming.toString());
        if (m_Listening) {
          //FIXME: Replace with object pool due to resource issues
          connectionHandler = new TCPConnectionHandler(new TCPSlaveConnection(incoming));
//          connectionHandler.addPropertyChangeListener(MainServer.listeners);
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

  /**
   * Tests if this <tt>ModbusTCPListener</tt> is listening
   * and accepting incoming connections.
   *
   * @return true if listening (and accepting incoming connections),
   *          false otherwise.
   */
  public boolean isListening() {
    return m_Listening;
  }//isListening

  protected void count() {
    c_RequestCounter++;
    if (c_RequestCounter == REQUESTS_TOGC) {
      System.gc();
      c_RequestCounter = 0;
    }
  }//count

  private static final int REQUESTS_TOGC = 10;

}//class ModbusTCPListener
