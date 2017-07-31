package net.wimpi.modbus.net;

import net.wimpi.modbus.util.SerialParameters;

/**
 * <p>SerialConnectionFactory interface.</p>
 *
 * @author Sami Salonen
 */
public interface SerialConnectionFactory {
    public SerialConnection create(SerialParameters parameters);
}
