package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.SettingsBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dao.AppSettingsDao;
import aor.paj.proj_final_aor_backend.dto.AppSettings;
import aor.paj.proj_final_aor_backend.dto.User;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/settings")
public class SettingsService {
    private static final Logger logger = LogManager.getLogger(ResourceService.class);

    @EJB
    SettingsBean settingsBean;
    @EJB
    UserBean userBean;

    /**
     * This method is used to retrieve the number of open notifications for a specific user.
     * It first logs the request to get open notifications.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If the user is found, it retrieves the number of open notifications for the user from the database.
     * If the operation is successful and there are open notifications, it logs the success and returns a response with status OK and the number of open notifications.
     * If the operation is successful but there are no open notifications, it logs the success and returns a response with status OK and the number of open notifications (which is 0).
     * If an exception occurs during the operation, it logs the exception message and returns a response with status BAD_REQUEST and the exception message.
     *
     * @param token The token of the user trying to retrieve the number of open notifications.
     * @return Response The response of the operation, containing the number of open notifications for the user or an error message.
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSettings(@HeaderParam("token") String token, @Context HttpServletRequest request) {
        logger.info("Received request to get settings");

        User user = userBean.getUserByToken(token);

        if (user == null) {
            logger.warn("IP Adress " + request.getRemoteAddr() + " tried to get settings with invalid token");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        // Check if the user is an admin
        String typeUser = user.getUserType().toString();
        if(!typeUser.equals("ADMIN")){
            logger.warn("IP Adress " + request.getRemoteAddr() + " tried to get settings without admin rights");
            return Response.status(Response.Status.UNAUTHORIZED).entity("You do not have the rights to get settings").build();
        }

        AppSettings settings = settingsBean.getSettings();
        if (settings != null) {
            logger.info("IP Adress " + request.getRemoteAddr() + " got settings successfully by user with id " + user.getId());
            return Response.status(Response.Status.OK).entity(settings).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to get settings").build();
        }
    }

    /**
     * This method is used to retrieve the session timeout setting.
     * It first logs the request to get the session timeout.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the warning and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it retrieves the session timeout from the settings.
     * If the operation is successful and the session timeout is found, it logs the success and returns a response with status OK and the session timeout.
     * If the operation is successful but the session timeout is not found, it returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to retrieve the session timeout.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the session timeout or an error message.
     */
    @GET
    @Path("/session-timeout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSessionTimeout(@HeaderParam("token") String token, @Context HttpServletRequest request) {
        logger.info("Received request to get session timeout");

        User user = userBean.getUserByToken(token);

        if (user == null) {
            logger.warn("IP Adress " + request.getRemoteAddr() + " tried to get session timeout with invalid token");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        Integer sessionTimeout = settingsBean.getSessionTimeout();
        if (sessionTimeout != null) {
            logger.info("IP Adress " + request.getRemoteAddr() + " got session timeout successfully by user with id " + user.getId());
            return Response.status(Response.Status.OK).entity(sessionTimeout).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to get session timeout").build();
        }
    }


    /**
     * This method is used to update the settings for a specific user.
     * It first logs the request to update settings.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the warning and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found but is not an admin, it logs the warning and returns a response with status UNAUTHORIZED and an error message.
     * If the user is an admin, it attempts to update the settings in the database.
     * If the operation is successful and the settings are updated, it logs the success and returns a response with status OK and a success message.
     * If the operation fails, it returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to update the settings.
     * @param settings The new settings to be updated.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
     */
    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSettings(@HeaderParam("token") String token, AppSettings settings, @Context HttpServletRequest request) {
        logger.info("Received request to update settings");

        User user = userBean.getUserByToken(token);

        if (user == null) {
            logger.warn("IP Adress " + request.getRemoteAddr() + " tried to update settings with invalid token");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }
        // Check if the user is an admin
        String typeUser = user.getUserType().toString();
        if(!typeUser.equals("ADMIN")){
            logger.warn("IP Adress " + request.getRemoteAddr() + " tried to update settings without admin rights");
            return Response.status(Response.Status.UNAUTHORIZED).entity("You do not have the rights to update settings").build();
        }


        // Check if the settings were updated successfully
        if (settingsBean.updateSettings(settings)) {
            logger.info("IP Adress " + request.getRemoteAddr() + " updated settings successfully by user with id " + user.getId());
            return Response.status(Response.Status.OK).entity("Settings updated successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update settings").build();
        }
    }


    /**
     * This method is used to retrieve the maximum number of members allowed in a project.
     * It first logs the request to get the maximum number of members.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the warning and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it retrieves the maximum number of members from the settings.
     * If the operation is successful and the maximum number of members is found, it logs the success and returns a response with status OK and the maximum number of members.
     * If the operation is successful but the maximum number of members is not found, it returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to retrieve the maximum number of members.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the maximum number of members or an error message.
     */
    @GET
    @Path("/max-members")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMaxMembers(@HeaderParam("token") String token, @Context HttpServletRequest request) {
        logger.info("Received request to get max members");

        User user = userBean.getUserByToken(token);

        if (user == null) {
            logger.warn("IP Adress " + request.getRemoteAddr() + " tried to get max members with invalid token");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        Integer maxMembers = settingsBean.getMaxUsersPerProject();
        if (maxMembers != null) {
            logger.info("IP Adress " + request.getRemoteAddr() + " got max members successfully by user with id " + user.getId());
            return Response.status(Response.Status.OK).entity(maxMembers).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to get max members").build();
        }
    }

}
