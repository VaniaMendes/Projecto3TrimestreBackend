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
