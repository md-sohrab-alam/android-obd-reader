package com.sohrab.obd.reader.obdCommand.obdException;

/**
 * Thrown when there is a "?" message.
 *
 * @author pires
 * @version $Id: $Id
 */
public class MisunderstoodCommandException extends ResponseException {

    /**
     * <p>Constructor for MisunderstoodCommandException.</p>
     */
    public MisunderstoodCommandException() {
        super("?");
    }

}
