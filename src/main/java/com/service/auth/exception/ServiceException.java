package com.service.auth.exception;

import java.io.Serializable;

/**
 * Created by demo on 5/09/2015.
 */
public class ServiceException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public ServiceException() {
        super();
    }
    public ServiceException(String msg)   {
        super(msg);
    }
    public ServiceException(String msg, Exception e)  {
        super(msg, e);
    }
}
