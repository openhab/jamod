package net.wimpi.modbus.net;

import java.net.InetAddress;

/**
 * <p>
 * UDPSlaveTerminalFactory interface.
 * </p>
 *
 * @author Sami Salonen
 */
public interface UDPSlaveTerminalFactory {
    public UDPSlaveTerminal create(InetAddress interfac, int port);
}
