package javaChatRoom.common.command;

import java.io.Serializable;

public class Command implements Serializable {
    public enum CommandType {
        USERLIST, QUIT
    }

    private CommandType commandType;

    public Command(CommandType commandType) {
        this.commandType = commandType;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
