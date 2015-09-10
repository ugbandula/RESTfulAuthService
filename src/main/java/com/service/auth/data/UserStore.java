package com.service.auth.data;

import com.service.auth.model.SessionObject;
import com.service.auth.model.User;

import java.util.HashMap;

/**
 * Created by Bandula Gamage on 5/09/2015.
 */
public class UserStore {

    public static HashMap<String, User> users = new HashMap<String, User>();

    // TODO - Temp Impl
    static {
        User user = new User();
        user.setUserName("demouser");
        user.setRole("USER");
        user.setPwd("1234");
        users.put(user.getUserName(), user);
    }

    /**
     * Authenticates the incoming user attempt if it provides a valid user name and a password. If not return NULL.
     * @param userName
     * @param password
     * @return
     */
    public static SessionObject authenticate(String userName, String password) {
        User user = users.get(userName);

        if (user == null) {
            return null;
        } else {
            if (user.authenticate(password)) {
                return UserSessions.createSession(user);
            } else {
                return null;
            }
        }
    }


}
