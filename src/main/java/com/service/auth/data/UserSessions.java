package com.service.auth.data;

import com.service.auth.model.SessionObject;
import com.service.auth.model.User;
import com.service.auth.shared.Constants;

import java.util.Collection;
import java.util.HashMap;

/**
 * Holds the already authenticated user sessions.
 * Created by Bandula Gamage on 5/09/2015.
 */
public class UserSessions {
    public static HashMap<String, SessionObject> activeSessions = new HashMap<String, SessionObject>();
    public static HashMap<String, SessionObject> markedForDeletion = new HashMap<String, SessionObject>();

    /**
     * Based on the current implementation a new authentication attempt will generate a new session.
     * this will logout the previously connected user.
     * @param user
     * @return
     */
    public static SessionObject createSession(User user) {
        SessionObject userSession = null;

        // If user already logged in. The previous session will be invalidated and create a new one
        // Generate a new session for the new attempt
        userSession = new SessionObject(user);

        // Update session registry with the new Session details
        activeSessions.put(userSession.getSessionID(), userSession);

        return userSession;
    }

    /**
     * Validates the incoming session ID to ensure it represents an active and a valid Session in the server.
     * It does check whether the session ID represents a valid session entry and checks whether any applicable
     * session timed out occurs. If the user idle for a defined period of time - 5 minutes - the current
     * session get discarded and user has to login again.
     *
     * @param sessionID
     * @return Valid SessionObject else null;
     */
    public static SessionObject isValidSession(String sessionID) {
        // Retrieve the session object
        SessionObject sessionObject = activeSessions.get(sessionID);

        // Validate the retrieved object
        if (sessionObject != null) {
            // Check the session validity
            if ((System.currentTimeMillis() - sessionObject.getSessionRefreshedTime()) < Constants.SESSION_TIMED_OUT) {
                // If valid session - refresh it
                sessionObject.setSessionRefreshedTime(System.currentTimeMillis());
            } else {
                // If timed out occurs force to log out
                activeSessions.remove(sessionID);

                // Dispose the object
                sessionObject = null;
            }
        }

        return sessionObject;
    }

    private void clearDeletedSessions() {
        // TODO - To be implemented
    }

    public static boolean userLogout(String sessionID) {
        activeSessions.remove(sessionID);

        return true;
    }

    /**
     * Checks whether any valid session available for a user
     * @param userName
     * @return
     */
    private static boolean isLoggedIn(String userName) {
        boolean         isSessionFound  = false;
        SessionObject   prevSession     = null;
        Collection<SessionObject> sessionObjects = activeSessions.values();

        for (SessionObject sessionObject : sessionObjects) {
            if (sessionObject.getUserName().equalsIgnoreCase(userName)) {
                isSessionFound = true;
                prevSession = sessionObject;
                break;
            }
        }

        return isSessionFound;
    }

    /**
     * This can be used if we want to disable multiple login support and alert the previously logged in user
     * when it tries to access teh services
     * @param userObj
     * @return
     */
    private static SessionObject generateNewSession(User userObj) {
        SessionObject   newSession   = null;
        Collection<SessionObject> sessionObjects = activeSessions.values();

        // Traverse existing sessions and invalidate if any
        for (SessionObject sessionObject : sessionObjects) {
            if (sessionObject.getUserName().equalsIgnoreCase(userObj.getUserName())) {
                markedForDeletion.put(sessionObject.getUserName(), sessionObject);
                break;
            }
        }

        // Generate a new session for the new attempt
        newSession = new SessionObject(userObj);

        // Update session registry with the new Session details
        activeSessions.put(newSession.getSessionID(), newSession);

        return newSession;
    }

}
