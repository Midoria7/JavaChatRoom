package javaChatRoom.server;

import javaChatRoom.server.ServerMain;
import org.junit.jupiter.api.Test;
import javaChatRoom.common.command.Command;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerMainTest {

    @Test
    void testServerMain() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        // Start the server in a new thread
        Thread serverThread = new Thread(() -> {
            ServerMain server = new ServerMain(8001);
            server.start();
            latch.countDown();  // Signal that the server has started
        });
        serverThread.start();

        // Wait for the server to start
        latch.await(5, TimeUnit.SECONDS);  // Wait up to 5 seconds for the server to start

        // Connect to the server
        Socket socket = new Socket("localhost", 8001);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        // Send a login request
        Command loginRequest = new Command(Command.CommandType.LOGINREQUEST, "user1", "password1");
        out.writeObject(loginRequest);
        out.flush();

        // Receive and check the response
        Command response = (Command) in.readObject();
        assertEquals(Command.CommandType.LOGINRESPONSE, response.getCommandType());
        assertEquals("SUCCESS", response.getArgs()[0]);

        System.out.println(response.getArgs()[0]);

        // Cleanup
        Command logoutRequest = new Command(Command.CommandType.QUIT);
        out.writeObject(logoutRequest);
        out.flush();

        socket.close();
        serverThread.interrupt(); // Signal the server thread to stop
    }
}
