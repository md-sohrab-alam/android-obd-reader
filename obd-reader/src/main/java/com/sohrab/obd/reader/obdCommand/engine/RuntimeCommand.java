package com.sohrab.obd.reader.obdCommand.engine;


import android.util.Log;

import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.obdCommand.ObdCommand;

import java.util.Locale;

/**
 * Engine runtime.
 *
 * @author pires
 * @version $Id: $Id
 */
public class RuntimeCommand extends ObdCommand {

    private int value = 0;

    /**
     * Default ctor.
     */
    public RuntimeCommand() {
        super("01 1F");
    }

    /**
     * Copy ctor.
     */
    public RuntimeCommand(RuntimeCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [01 0C] of the response
        value = buffer.get(2) * 256 + buffer.get(3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedResult() {
        // determine time
        Log.d("RUNTIME VALUE", String.valueOf(value));
        final String hh = String.format(Locale.ENGLISH, "%02d", value / 3600);
        final String mm = String.format(Locale.ENGLISH, "%02d", (value % 3600) / 60);
        final String ss = String.format(Locale.ENGLISH, "%02d", value % 60);
        return String.format(Locale.ENGLISH , "%s:%s:%s", hh, mm, ss);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalculatedResult() {
        return String.valueOf(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResultUnit() {
        return "s";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_RUNTIME.getValue();
    }

}

