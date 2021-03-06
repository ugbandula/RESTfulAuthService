package com.service.auth.exception;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by demo on 5/09/2015.
 */
@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(Throwable ex) {
        System.out.println("<ThrowableExceptionMapper> "+ex.getClass());
        ex.printStackTrace();
        //usually you don't pass detailed info out (don't do this here in production environments)
        //JSON.setErrorMsg(ex.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
    }

}