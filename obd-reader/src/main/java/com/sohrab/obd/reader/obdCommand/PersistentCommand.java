package com.sohrab.obd.reader.obdCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Base persistent OBD command.
 *
 * @author pires
 * @version $Id: $Id
 */
public abstract class PersistentCommand extends ObdCommand {

    private static Map<String, String> knownValues = new HashMap<>();
    private static Map<String, ArrayList<Integer>> knownBuffers = new HashMap<>();

    /**
     * <p>Constructor for PersistentCommand.</p>
     *
     * @param command a {@link String} object.
     */
    public PersistentCommand(String command) {
        super(command);
    }

    /**
     * <p>Constructor for PersistentCommand.</p>
     *
     */
    public PersistentCommand(ObdCommand other) {
        this(other.cmd);
    }

    /**
     * <p>reset.</p>
     */
    public static void reset() {
        knownValues = new HashMap<>();
        knownBuffers = new HashMap<>();
    }

    /**
     * <p>knows.</p>
     *
     * @param cmd a {@link Class} object.
     * @return a boolean.
     */
    public static boolean knows(Class cmd) {
        String key = cmd.getSimpleName();
        return knownValues.containsKey(key);
    }


}
