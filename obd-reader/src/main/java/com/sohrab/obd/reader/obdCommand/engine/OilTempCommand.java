package com.sohrab.obd.reader.obdCommand.engine;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.obdCommand.temperature.TemperatureCommand;

/**
 * Displays the current engine Oil temperature.
 *
 */
public class OilTempCommand extends TemperatureCommand {

    /**
     * Default ctor.
     */
    public OilTempCommand() {
        super("01 5C");
    }

    /**
     * Copy ctor.
     *
     */
    public OilTempCommand(OilTempCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_OIL_TEMP.getValue();
    }

}
