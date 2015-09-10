package com.service.auth.security;

/**
 * Created by Bandula Gamage on 5/09/2015.
 */

import com.service.auth.data.UserSessions;
import com.service.auth.model.SessionObject;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.*;

/**
 * This interceptor verify the access permissions for a user based on username
 * and passowrd provided in request
 * */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class SecurityInterceptor implements javax.ws.rs.container.ContainerRequestFilter {

  	private static final String AUTHORIZATION_TOKEN      = "x-access-token";  // Contains the authentication token

    private static final ServerResponse ACCESS_DENIED    = new ServerResponse("{\"id\":401,\"description\":\"Access denied\"}", 401, new Headers<Object>());
    private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("{\"id\":403,\"description\":\"Access forbidden\"}", 403, new Headers<Object>());
    private static final ServerResponse SERVER_ERROR     = new ServerResponse("{\"id\":500,\"description\":\"Internal Server Error\"}", 500, new Headers<Object>());

    /**
     * <p>Extracts the Authorization token from the request header and further analyze the request session do have
     * sufficient privilege to access the protected resource.</p>
     * @param requestContext
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {
        System.out.println("<SecurityInterceptor> Invoking SecurityInterceptor filter");
        ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext
                .getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();

        // Access allowed for all
        if (!method.isAnnotationPresent(PermitAll.class)) {
            // Access denied for all
            if (method.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }

            // Get request headers
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();

            // Only to display header items
//            Iterator<String> it = headers.keySet().iterator();
//            while(it.hasNext()){
//                String theKey = (String)it.next();
//                System.out.println(">> " + theKey + ", " + headers.getFirst(theKey));
//            }

            // Fetch authorization header
            /*
                Authorization header format:
                    Authorization: AuthSub token="token"

                    GET /calendar/feeds/default/private/full HTTP/1.1
                    Content-Type: application/x-www-form-urlencoded
                    x-access-token, 46194778-70e0-4505-8c84-ebece8179b6d
                    User-Agent: Java/1.5.0_06
                    Host: www.google.com
                    Accept: text/html, image/gif, image/jpeg, *; q=.2, *; q=.2
                    sConnection: keep-alive
             */
            final List<String> authorization = headers.get(AUTHORIZATION_TOKEN);

            // If no authorization information present; block access
            if (authorization == null || authorization.isEmpty()) {
                System.out.println("<SecurityInterceptor> The request does not contain required authorization headers. Access will be denied");
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }

            // Get encoded username and password
            final String userSessionID = authorization.get(0).replaceFirst(AUTHORIZATION_TOKEN + "=", "");

            if ((userSessionID == null) || (userSessionID.length() < 10)) {
                System.out.println("<SecurityInterceptor> Session ID not found. Access will be denied");
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }
            System.out.println("<SecurityInterceptor> Received Session ID: " + userSessionID);

            // Verify user access
            if (method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

                // Is user valid?
                if (!isUserAllowed(userSessionID, rolesSet)) {
                    requestContext.abortWith(ACCESS_DENIED);
                    System.out.println("<SecurityInterceptor> No access privileges. Access will be denied");
                    return;
                }
            }
        }
    }

    /**
     * Verify the extracted session against the UserSessions store. It checks whether a valid session exists and
     * if available further checks to understand that user do have sufficient privilege to access the service.
     * @param userSessionID Extracted Session ID from the header
     * @param rolesSet  Extracted roles from the request header
     * @return The user ability to access the resource
     */
    private boolean isUserAllowed(final String userSessionID, final Set<String> rolesSet) {
        boolean       isAllowed     = false;
        SessionObject sessionObject = null;

        // Step 1: Load session details from the store
        sessionObject = UserSessions.isValidSession(userSessionID);

        if (sessionObject != null) {
            System.out.println("<SecurityInterceptor> Valid session: " + userSessionID + ", Role: " + sessionObject.getRole());
            // Step 2: Verify user role
            if (rolesSet.contains(sessionObject.getRole())) {
                System.out.println("<SecurityInterceptor> Authorized user: " + sessionObject.getUserName());
                isAllowed = true;
            }
        }
        return isAllowed;
    }

}
