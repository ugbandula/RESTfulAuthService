package com.service.auth.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Bandula Gamage on 5/09/2015.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // User Name
    @XmlAttribute(name = "userName")
    private String userName;

    // User Role
    @XmlAttribute(name = "role")
    private String role;

    // Password
    private String pwd;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName1) {
        this.userName = userName1;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role1) {
        this.role = role1;
    }

    public boolean authenticate(String plainPass) {
        if (this.pwd.equals(plainPass))
            return true;
        else
            return false;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
