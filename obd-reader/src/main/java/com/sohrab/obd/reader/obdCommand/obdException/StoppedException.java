package com.sohrab.obd.reader.obdCommand.obdException;

/**
 * Sent when there is a "STOPPED" message.
 *
 * @author pires
 * @version $Id: $Id
 */
public class StoppedException extends ResponseException {

    /**
     * <p>Constructor for StoppedException.</p>
     */
    public StoppedException() {
        super("STOPPED");
    }

}
