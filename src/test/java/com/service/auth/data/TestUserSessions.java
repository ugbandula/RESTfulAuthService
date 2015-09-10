package com.service.auth.data;

import com.service.auth.model.SessionObject;
import com.service.auth.model.User;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests user session management activities
 * Created by Bandula Gamage on 6/09/2015.
 */
public class TestUserSessions {

    private static User testUser = new User();
    private static SessionObject session;

    @BeforeClass
    public static void createPreRequisites() {
        testUser.setUserName("demouser");
        testUser.setPwd("1234");
        testUser.setRole("USER");

        session = UserStore.authenticate(testUser.getUserName(), testUser.getPwd());
    }

    @Test
    public void testCreateSession() {
        System.out.println("<TestUserSessions> Executing testCreateSession");
        SessionObject newSession = UserSessions.createSession(testUser);
        assertNotNull(newSession);
    }

    @Test
    public void testIsValidSession() {
        System.out.println("<TestUserSessions> Executing testIsValidSession");
        SessionObject newSession = UserSessions.isValidSession(session.getSessionID());
        assertNotNull(newSession);
    }

    @Test
    public void testUserLogout() {
        System.out.println("<TestUserSessions> Executing testUserLogout");
        assertTrue(UserSessions.userLogout(session.getSessionID()));
    }


}
