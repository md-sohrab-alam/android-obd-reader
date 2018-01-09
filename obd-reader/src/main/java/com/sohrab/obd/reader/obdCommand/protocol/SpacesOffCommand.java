package com.sohrab.obd.reader.obdCommand.protocol;

/**
 * Turn-off spaces.
 */
public class SpacesOffCommand extends ObdProtocolCommand {

    public SpacesOffCommand() {
        super("ATS0");
    }

    /**
     * <p>Constructor for SpacesOffCommand.</p>
     *
     * @param other a {@link SpacesOffCommand} object.
     */
    public SpacesOffCommand(SpacesOffCommand other) {
        super(other);
    }

    @Override
    public String getFormattedResult() {
        return getResult();
    }

    @Override
    public String getName() {
        return "Spaces Off";
    }
}
