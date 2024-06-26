package javaChatRoom.server.serverLogger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return String.format("%1$tF %1$tT %2$s %3$s: %4$s %n",
                new java.util.Date(record.getMillis()),
                record.getLevel().getLocalizedName(),
                record.getSourceClassName() + " " + record.getSourceMethodName(),
                record.getMessage());
    }
}
