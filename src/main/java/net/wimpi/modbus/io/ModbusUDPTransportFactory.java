package net.wimpi.modbus.io;

import net.wimpi.modbus.net.UDPTerminal;

/**
 * <p>ModbusUDPTransportFactory interface.</p>
 *
 * @author Sami Salonen
 */
public interface ModbusUDPTransportFactory {
    public ModbusTransport create(UDPTerminal terminal);
}
