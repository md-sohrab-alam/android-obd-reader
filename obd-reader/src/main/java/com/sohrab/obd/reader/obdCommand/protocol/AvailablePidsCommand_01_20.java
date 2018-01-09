package com.sohrab.obd.reader.obdCommand.protocol;


import com.sohrab.obd.reader.enums.AvailableCommandNames;

/**
 * Retrieve available PIDs ranging from 01 to 20.
 *
 * @author pires
 * @version $Id: $Id
 */
public class AvailablePidsCommand_01_20 extends AvailablePidsCommand {

    /**
     * Default ctor.
     */
    public AvailablePidsCommand_01_20() {
        super("01 00");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return AvailableCommandNames.PIDS_01_20.getValue();
    }

}
