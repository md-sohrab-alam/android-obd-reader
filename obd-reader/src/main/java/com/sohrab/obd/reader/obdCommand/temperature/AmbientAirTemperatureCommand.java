package com.sohrab.obd.reader.obdCommand.temperature;

import com.sohrab.obd.reader.enums.AvailableCommandNames;

/**
 * Ambient Air Temperature.
 *
 */
public class AmbientAirTemperatureCommand extends TemperatureCommand {

    /**
     * <p>Constructor for AmbientAirTemperatureCommand.</p>
     */
    public AmbientAirTemperatureCommand() {
        super("01 46");
    }

    /**
     * <p>Constructor for AmbientAirTemperatureCommand.</p>
     *
     */
    public AmbientAirTemperatureCommand(TemperatureCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.AMBIENT_AIR_TEMP.getValue();
    }

}
