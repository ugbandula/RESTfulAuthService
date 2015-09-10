package com.service.auth.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.UUID;

/**
 * Created by Bandula Gamage on 5/09/2015.
 */
@JsonAutoDetect
public class SessionObject {

    // Generated Session ID
    @JsonProperty("id")
    private String  sessionID;

    // Authenticated User Name
    @JsonProperty("username")
    private String  userName;

    // Assigned User Role
    @JsonProperty("role")
    private String  role;

    // Logged in time details. Those can be used to timed-out invalid users
    private long    loggedInTime;
    private long    sessionRefreshedTime;       // Used to timed out unused sessions

    public SessionObject() {
    }

    public SessionObject(User user) {
        this.userName = user.getUserName();
        this.role     = user.getRole();
        this.sessionID= UUID.randomUUID().toString();

        this.loggedInTime           = System.currentTimeMillis();
        this.sessionRefreshedTime   = System.currentTimeMillis();

        System.out.println("<Session> User Session created " + this.userName + ", " + this.sessionID + ", " + this.loggedInTime);
    }

    public String toString() {
        return "{id:" + sessionID + ", user:" + userName + ", role:" + role + "}";
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getLoggedInTime() {
        return loggedInTime;
    }

    public void setLoggedInTime(long loggedInTime) {
        this.loggedInTime = loggedInTime;
    }

    public long getSessionRefreshedTime() {
        return sessionRefreshedTime;
    }

    public void setSessionRefreshedTime(long sessionRefreshedTime) {
        this.sessionRefreshedTime = sessionRefreshedTime;
    }
}
