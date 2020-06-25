/**
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

package net.wimpi.modbus.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusSocketBasedTransportFactory;
import net.wimpi.modbus.io.ModbusTCPTransport;
import net.wimpi.modbus.io.ModbusTransport;

/**
 * Class that implements a TCPSlaveConnection.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class TCPSlaveConnection {

    public static class ModbusTCPTransportFactory implements ModbusSocketBasedTransportFactory {

        @Override
        public ModbusTransport create(Socket socket) {
            return new ModbusTCPTransport(socket);
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(TCPSlaveConnection.class);

    // instance attributes
    private Socket m_Socket;
    private int m_Timeout = Modbus.DEFAULT_TIMEOUT;
    private boolean m_Connected;
    private ModbusTransport m_ModbusTransport;

    private ModbusSocketBasedTransportFactory m_TransportFactory;

    /**
     * Constructs a <tt>TCPSlaveConnection</tt> instance
     * using a given socket instance.
     *
     * @param socket the socket instance to be used for communication.
     */
    public TCPSlaveConnection(Socket socket, ModbusSocketBasedTransportFactory transportFactory) {
        this.m_TransportFactory = transportFactory;
        try {
            setSocket(socket);
        } catch (IOException ex) {
            final String errMsg = "Socket invalid";
            logger.warn(errMsg);
            // @commentstart@
            throw new IllegalStateException(errMsg);
            // @commentend@
        }
    }// constructor

    public TCPSlaveConnection(Socket socket) {
        this(socket, new ModbusTCPTransportFactory());
    }

    /**
     * Closes this <tt>TCPSlaveConnection</tt>.
     */
    public void close() {
        if (m_Connected) {
            try {
                m_ModbusTransport.close();
                m_Socket.close();
            } catch (IOException ex) {
                if (Modbus.debug) {
                    ex.printStackTrace();
                }
            }
            m_Connected = false;
        }
    }// close

    /**
     * Returns the <tt>ModbusTransport</tt> associated with this
     * <tt>TCPMasterConnection</tt>.
     *
     * @return the connection's <tt>ModbusTransport</tt>.
     */
    public ModbusTransport getModbusTransport() {
        return m_ModbusTransport;
    }// getIO

    /**
     * Prepares the associated <tt>ModbusTransport</tt> of this
     * <tt>TCPMasterConnection</tt> for use.
     *
     * @param socket the socket to be used for communication.
     * @throws IOException if an I/O related error occurs.
     */
    private void setSocket(Socket socket) throws IOException {
        m_Socket = socket;
        if (m_ModbusTransport == null) {
            m_ModbusTransport = this.m_TransportFactory.create(m_Socket);
        } else {
            throw new IllegalStateException("socket cannot be re-set");
        }
        m_Connected = true;
    }// prepareIO

    /**
     * Returns the timeout for this <tt>TCPMasterConnection</tt>.
     *
     * @return the timeout as <tt>int</tt>.
     */
    public int getTimeout() {
        return m_Timeout;
    }// getReceiveTimeout

    /**
     * Sets the timeout for this <tt>TCPSlaveConnection</tt>.
     *
     * @param timeout the timeout as <tt>int</tt>.
     */
    public void setTimeout(int timeout) {
        m_Timeout = timeout;
        try {
            m_Socket.setSoTimeout(m_Timeout);
        } catch (IOException ex) {
            // handle?
        }
    }// setReceiveTimeout

    /**
     * Returns the destination port of this
     * <tt>TCPMasterConnection</tt>.
     *
     * @return the port number as <tt>int</tt>.
     */
    public int getPort() {
        return m_Socket.getLocalPort();
    }// getPort

    /**
     * Returns the destination <tt>InetAddress</tt> of this
     * <tt>TCPMasterConnection</tt>.
     *
     * @return the destination address as <tt>InetAddress</tt>.
     */
    public InetAddress getAddress() {
        return m_Socket.getLocalAddress();
    }// getAddress

    /**
     * Tests if this <tt>TCPMasterConnection</tt> is connected.
     *
     * @return <tt>true</tt> if connected, <tt>false</tt> otherwise.
     */
    public boolean isConnected() {
        return m_Connected;
    }// isConnected

}// class TCPSlaveConnection
