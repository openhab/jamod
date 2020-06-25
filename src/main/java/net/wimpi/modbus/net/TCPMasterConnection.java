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
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTCPTransport;
import net.wimpi.modbus.io.ModbusTransport;

/**
 * Class that implements a TCPMasterConnection.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class TCPMasterConnection implements ModbusSlaveConnection {
    private static final Logger logger = LoggerFactory.getLogger(TCPMasterConnection.class);

    // instance attributes
    private Socket m_Socket;
    private int m_Timeout = Modbus.DEFAULT_TIMEOUT;
    private boolean m_Connected;

    private InetAddress m_Address;
    private int m_Port = Modbus.DEFAULT_PORT;

    // private int m_Retries = Modbus.DEFAULT_RETRIES;
    private ModbusTCPTransport m_ModbusTransport;

    private int m_ConnectTimeoutMillis;

    private static StandardToStringStyle toStringStyle = new StandardToStringStyle();

    static {
        toStringStyle.setUseShortClassName(true);
    }

    /**
     * Constructs a <tt>TCPMasterConnection</tt> instance
     * with a given destination address.
     *
     * @param adr the destination <tt>InetAddress</tt>.
     */
    public TCPMasterConnection(InetAddress adr) {
        m_Address = adr;
    }// constructor

    public TCPMasterConnection(InetAddress adr, int port) {
        this(adr);
        setPort(port);
    }

    public TCPMasterConnection(InetAddress adr, int port, int connectTimeoutMillis) {
        this(adr, port);
        setConnectTimeoutMillis(connectTimeoutMillis);
    }

    /**
     * Opens this <tt>TCPMasterConnection</tt>.
     *
     * @throws Exception if there is a network failure.
     */
    @Override
    public synchronized boolean connect() throws Exception {
        if (!isConnected()) {
            logger.debug("connect()");
            m_Socket = new Socket();
            m_Socket.connect(new InetSocketAddress(m_Address, m_Port), this.m_ConnectTimeoutMillis);
            setTimeout(m_Timeout);
            m_Socket.setReuseAddress(true);
            m_Socket.setSoLinger(true, 1);
            m_Socket.setKeepAlive(true);
            prepareTransport();
            m_Connected = true;
        }
        return m_Connected;
    }// connect

    /**
     * Closes this <tt>TCPMasterConnection</tt>.
     */
    public void close() {
        if (m_Connected) {
            try {
                m_ModbusTransport.close();
            } catch (IOException ex) {
                logger.warn("close()", ex);
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
    }// getModbusTransport

    /**
     * Prepares the associated <tt>ModbusTransport</tt> of this
     * <tt>TCPMasterConnection</tt> for use.
     *
     * @throws IOException if an I/O related error occurs.
     */
    private void prepareTransport() throws IOException {
        if (m_ModbusTransport == null) {
            m_ModbusTransport = new ModbusTCPTransport(m_Socket);
        } else {
            m_ModbusTransport.setSocket(m_Socket);
        }
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
     * Sets the timeout for this <tt>TCPMasterConnection</tt>.
     *
     * @param timeout the timeout as <tt>int</tt>.
     */
    public void setTimeout(int timeout) {
        m_Timeout = timeout;
        if (m_Socket != null) {
            try {
                m_Socket.setSoTimeout(m_Timeout);
            } catch (IOException ex) {
                logger.warn("Could not set socket timeout on connection {} {}: {}", getAddress(), getPort(),
                        ex.getMessage());
            }
        }
    }// setReceiveTimeout

    /**
     * Returns the destination port of this
     * <tt>TCPMasterConnection</tt>.
     *
     * @return the port number as <tt>int</tt>.
     */
    public int getPort() {
        return m_Port;
    }// getPort

    /**
     * Sets the destination port of this
     * <tt>TCPMasterConnection</tt>.
     * The default is defined as <tt>Modbus.DEFAULT_PORT</tt>.
     *
     * @param port the port number as <tt>int</tt>.
     */
    public void setPort(int port) {
        m_Port = port;
    }// setPort

    /**
     * Returns the destination <tt>InetAddress</tt> of this
     * <tt>TCPMasterConnection</tt>.
     *
     * @return the destination address as <tt>InetAddress</tt>.
     */
    public InetAddress getAddress() {
        return m_Address;
    }// getAddress

    /**
     * Sets the destination <tt>InetAddress</tt> of this
     * <tt>TCPMasterConnection</tt>.
     *
     * @param adr the destination address as <tt>InetAddress</tt>.
     */
    public void setAddress(InetAddress adr) {
        m_Address = adr;
    }// setAddress

    /**
     * Tests if this <tt>TCPMasterConnection</tt> is connected.
     *
     * @return <tt>true</tt> if connected, <tt>false</tt> otherwise.
     */
    @Override
    public boolean isConnected() {
        // From j2mod originally. Sockets that are not fully open are closed.
        if (m_Connected && m_Socket != null) {
            // Call close() if the connection is not fully "open"
            if (!m_Socket.isConnected() || m_Socket.isClosed() || m_Socket.isInputShutdown()
                    || m_Socket.isOutputShutdown()) {
                close();
            }
        }
        return m_Connected;
    }// isConnected

    @Override
    public void resetConnection() {
        close();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, toStringStyle).append("socket", m_Socket).toString();
    }

    public int getConnectTimeoutMillis() {
        return m_ConnectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int m_ConnectTimeoutMillis) {
        this.m_ConnectTimeoutMillis = m_ConnectTimeoutMillis;
    }

}// class TCPMasterConnection
