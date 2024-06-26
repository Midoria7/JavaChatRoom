package javaChatRoom.server.userManager;

import org.junit.jupiter.api.Test;

public class UserManagerTest {

    @Test
    void testUserManager() {
        UserManager userManager = new UserManager();
        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");
        userManager.addUser(user1);
        userManager.addUser(user2);
        assert userManager.isValidUser("user1", "password1");
        assert userManager.isValidUser("user2", "password2");
        assert !userManager.isValidUser("user1", "password2");
    }
}
