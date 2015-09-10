package com.service.auth.service;

import com.service.auth.data.UserSessions;
import com.service.auth.data.UserStore;
import com.service.auth.exception.ServiceException;
import com.service.auth.model.SessionObject;
import com.service.auth.shared.Constants;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 * Created by Bandula Gamage on 5/09/2015.
 */
@Path("/auth-service")
public class AuthService {

    /**
     * Authenticates the incoming request if the request contains valid User and Password as Form parameters
     * in a HTTP POST request.
     *
     * @param userName
     * @param password
     * @param req
     * @return
     * @throws ServiceException
     */
    @PermitAll
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(@FormParam("username") String userName,
                              @FormParam("password") String password,
                              @Context HttpServletRequest req)
            throws ServiceException {

        SessionObject userSession   = null;
        Response.ResponseBuilder rb = null;

        System.out.println("<userLogin.HTTP.POST.FORM> User Login " + userName + ", " + password);

        try {
            if (password != null)
                userSession = UserStore.authenticate(userName, password);

            if (userSession == null) {
                // Invalid authentication attempt
                String message = "{\"id\":-1, \"description\": \"User Authentication Failed!\"}";
                rb = Response.status(Response.Status.UNAUTHORIZED).entity(message);
            } else {
                // Authentication Successful
                rb = Response.ok(userSession);
            }
        } catch (Exception e) {
            throw new ServiceException("{\"id\":-1, \"description\": \"User authentication failed due to an error!\"}");
        }

        return rb.build();
    }

    /**
     * Authenticates the incoming request if the request contains valid User and Password as Query parameters
     * in a HTTP GET request.
     *
     * @param userName
     * @param password
     * @param req
     * @return
     * @throws ServiceException
     */
    @PermitAll
    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(@QueryParam("username") String userName,
                              @QueryParam("password") String password,
                              @Context Request req)
            throws ServiceException {
        SessionObject userSession   = null;
        Response.ResponseBuilder rb = null;

        System.out.println("<userLogin.HTTP.POST.FORM> User Login " + userName + ", " + password);

        try {
            if (password != null)
                userSession = UserStore.authenticate(userName, password);

            if (userSession == null) {
                // Invalid authentication attempt
                String message = "{\"id\":-1, \"description\": \"User Authentication Failed!\"}";
                rb = Response.status(Response.Status.UNAUTHORIZED).entity(message);
            } else {
                // Authentication Successful
                rb = Response.ok(userSession);
            }
        } catch (Exception e) {
            throw new ServiceException("{\"id\":-1, \"description\": \"User authentication failed due to an error!\"}");
        }

        return rb.build();
    }

    /**
     * This method returns the Welcome Message if the valid token returns to back-end by the authenticated user.
     * The authorization had been disabled as it's out of scope to the project. However, if this is enabled,
     * this wil invoke the SecurityInterceptor to validate the HTTP Header parameters to ensure the call is from
     * a remote client who had already autehticated.
     *
     * @param security
     * @param req
     * @return
     * @throws ServiceException
     */
//    @PermitAll
    @RolesAllowed({"ADMIN", "USER"})
    @GET
    @Path("/welcome/{security}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWelcomeMessage(@PathParam("security") String security, @Context Request req)
            throws ServiceException {
        String message = null;
        Response.ResponseBuilder rb = null;

        try {
            System.out.println("<getWelcomeMessage.HTTP.GET> Request to obtain Welcome Message for: " + security);
            if (Constants.VALID_SECURITY_CODE.equals(security)) {
                message = Constants.WELCOME_MESSAGE;
            } else
                throw new ServiceException("{\"id\":-1, \"description\": \"User not authorized\"}");

            rb = Response.ok("{\"message\":\"" + message + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("{\"id\":-1, \"description\": \"User not authorized\"}");
        }
        return rb.build();
    }

    /**
     * Implements the requested User logout functionality. The oncoming request will be check against the authenticity
     * before performing the loggin out.
     *
     * @param session
     * @param req
     * @return
     * @throws ServiceException
     */
    @RolesAllowed({"ADMIN", "USER"})
    @GET
    @Path("/logout/{session}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logoutUser(@PathParam("session") String session, @Context Request req)
            throws ServiceException {
        String message = null;
        Response.ResponseBuilder rb = null;

        try {
            System.out.println("<logoutUser.HTTP.GET> Request to logout the user: " + session);
            if (UserSessions.userLogout(session)) {
                message = "Logout successful";
            } else
                throw new ServiceException("{\"id\":-1, \"description\": \"User logout failed\"}");

            rb = Response.ok("{\"message\":\"" + message + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("{\"id\":-1, \"description\": \"User not authorized\"}");
        }
        return rb.build();
    }

}
