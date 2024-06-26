package javaChatRoom.server.serverLogger;

import java.io.IOException;
import java.util.logging.*;

public class ServerLogger {
    private static final Logger LOGGER = Logger.getLogger(ServerLogger.class.getName());

    static {
        setupLoggers();
    }

    private static void setupLoggers() {
        LOGGER.setUseParentHandlers(false);
        CustomFormatter customFormatter = new CustomFormatter();

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINE);
        consoleHandler.setFormatter(customFormatter);

        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("server.log", true);
            fileHandler.setLevel(Level.INFO);
            fileHandler.setFormatter(customFormatter);
        } catch (SecurityException | IOException e) {
            LOGGER.log(Level.SEVERE, "File logger not working.", e);
        }

        LOGGER.addHandler(consoleHandler);
        if (fileHandler != null) {
            LOGGER.addHandler(fileHandler);
        }

        LOGGER.setLevel(Level.FINE);
    }

    public static void writeInfo(String message) {
        LOGGER.logp(Level.INFO, getCallerClassName(), "", message);
    }

    public static void writeWarning(String message) {
        LOGGER.logp(Level.WARNING, getCallerClassName(), "", message);
    }

    public static void writeError(String message) {
        LOGGER.logp(Level.SEVERE, getCallerClassName(), "", message);
    }

    public static void writeDebug(String message) {
        LOGGER.logp(Level.FINE, getCallerClassName(), "", message);
    }

    private static String getCallerClassName() {
        return new Throwable().getStackTrace()[2].getClassName();
    }
}
