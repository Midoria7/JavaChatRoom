package javaChatRoom.client.loginHandler;

import javaChatRoom.client.clientConfig.ClientConfig;
import javaChatRoom.client.clientConnection.ClientConnection;
import javaChatRoom.common.command.Command;
import java.util.Scanner;

public class LoginHandler {
    private ClientConnection connection;
    private ClientConfig config;
    private Scanner scanner;

    public LoginHandler(ClientConnection connection, ClientConfig config, Scanner scanner) {
        this.connection = connection;
        this.config = config;
        this.scanner = scanner;
    }

    public boolean performLogin() {
        while (true) {
            System.out.println("Please enter username:");
            String username = scanner.nextLine();
            System.out.println("Please enter password:");
            String password = scanner.nextLine();

            connection.send(new Command(Command.CommandType.LOGINREQUEST, username, password));
            Object response = connection.receive();

            if (response instanceof Command) {
                Command command = (Command) response;
                if ("SUCCESS".equals(command.getArgs()[0])) {
                    System.out.println("Login successful.");
                    config.setUsername(username);
                    return true; // 登录成功，退出循环
                } else {
                    System.out.println("Login failed, wrong username or password.");
                }
            } else {
                System.out.println("Invalid response from server.");
            }

            // 提问用户是否想要再次尝试
            System.out.println("Do you want to try again? (yes/no)");
            String answer = scanner.nextLine();
            if (!"yes".equalsIgnoreCase(answer)) {
                return false; // 如果用户不想重试，返回失败
            }
        }
    }
}
