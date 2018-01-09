package com.sohrab.obd.reader.obdCommand;


import com.sohrab.obd.reader.enums.AvailableCommandNames;

import java.util.Locale;

/**
 * Current speed.
 *
 * @author pires
 * @version $Id: $Id
 */
public class SpeedCommand extends ObdCommand implements SystemOfUnits {

    private int metricSpeed = 0;

    /**
     * Default ctor.
     */
    public SpeedCommand() {
        super("01 0D");
    }

    /**
     * Copy ctor.
     */
    public SpeedCommand(SpeedCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // Ignore first two bytes [hh hh] of the response.
        metricSpeed = buffer.get(2);
        System.out.println("metricSpeed  =  " + metricSpeed);
    }

    /**
     * <p>Getter for the field <code>metricSpeed</code>.</p>
     *
     * @return the speed in metric units.
     */
    public int getMetricSpeed() {
        return metricSpeed;
    }

    /**
     * <p>getImperialSpeed.</p>
     *
     * @return the speed in imperial units.
     */
    public float getImperialSpeed() {
        return getImperialUnit();
    }

    /**
     * Convert from km/h to mph
     *
     * @return a float.
     */
    public float getImperialUnit() {
        return metricSpeed * 0.621371192F;
    }

    /**
     * <p>getFormattedResult.</p>
     *
     * @return a {@link String} object.
     */
    public String getFormattedResult() {
        return useImperialUnits ? String.format(Locale.ENGLISH, "%.2f%s", getImperialUnit(), getResultUnit())
                : String.format(Locale.ENGLISH, "%d%s", getMetricSpeed(), getResultUnit());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return useImperialUnits ? String.valueOf(getImperialUnit()) : String.valueOf(getMetricSpeed());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResultUnit() {
        return useImperialUnits ? "mph" : "km/h";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.SPEED.getValue();
    }


}
