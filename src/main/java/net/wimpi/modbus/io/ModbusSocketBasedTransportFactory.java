package net.wimpi.modbus.io;

import java.net.Socket;

/**
 * <p>ModbusSocketBasedTransportFactory interface.</p>
 *
 * @author Sami Salonen
 */
public interface ModbusSocketBasedTransportFactory {
    public ModbusTransport create(Socket socket);
}
