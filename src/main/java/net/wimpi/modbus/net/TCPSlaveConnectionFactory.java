package net.wimpi.modbus.net;

import java.net.Socket;

/**
 * <p>TCPSlaveConnectionFactory interface.</p>
 *
 * @author Sami Salonen
 */
public interface TCPSlaveConnectionFactory {
    public TCPSlaveConnection create(Socket socket);
}
