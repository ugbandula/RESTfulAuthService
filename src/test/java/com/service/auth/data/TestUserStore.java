package com.service.auth.data;

import com.service.auth.model.SessionObject;
import com.service.auth.model.User;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test general user authentication
 * Created by Bandula Gamage on 6/09/2015.
 */
public class TestUserStore {

    private User testUser = new User();

    // Test user authentication
    @Test
    public void testAuthenticate() {
        testUser.setUserName("demouser");
        testUser.setPwd("1234");
        testUser.setRole("USER");

        SessionObject session = UserStore.authenticate(testUser.getUserName(), testUser.getPwd());
        assertNotNull(session);
    }
}
