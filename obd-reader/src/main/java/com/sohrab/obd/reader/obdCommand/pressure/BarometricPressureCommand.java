package com.sohrab.obd.reader.obdCommand.pressure;

import com.sohrab.obd.reader.enums.AvailableCommandNames;

/**
 * Barometric pressure.
 *
 */
public class BarometricPressureCommand extends PressureCommand {

    /**
     * <p>Constructor for BarometricPressureCommand.</p>
     */
    public BarometricPressureCommand() {
        super("01 33");
    }

    /**
     * <p>Constructor for BarometricPressureCommand.</p>
     *
     */
    public BarometricPressureCommand(PressureCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.BAROMETRIC_PRESSURE.getValue();
    }

}
