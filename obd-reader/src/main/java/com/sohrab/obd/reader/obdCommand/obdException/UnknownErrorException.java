package com.sohrab.obd.reader.obdCommand.obdException;

/**
 * Thrown when there is "ERROR" in the response
 *
 * @author pires
 * @version $Id: $Id
 */
public class UnknownErrorException extends ResponseException {

    /**
     * <p>Constructor for UnknownErrorException.</p>
     */
    public UnknownErrorException() {
        super("ERROR");
    }

}
