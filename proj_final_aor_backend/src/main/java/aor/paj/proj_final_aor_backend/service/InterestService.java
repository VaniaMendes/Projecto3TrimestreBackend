package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.InterestBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dto.Interest;
import aor.paj.proj_final_aor_backend.dto.User;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/interests")
public class InterestService {
    private static final Logger logger = LogManager.getLogger(SkillService.class);

    @EJB
    UserBean userBean;

    @EJB
    InterestBean interestBean;
    @Context
    private HttpServletRequest request;

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSkill(@HeaderParam("token") String token, Interest interest, @Context HttpServletRequest request) {
        User user = userBean.getUserByToken(token);

        if(user == null) {
            logger.error("User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        // Create the skill
        boolean created = interestBean.createNewInterest(token,interest);

        // Check if the skill was created successfully
        if (created) {
            logger.info("IPAdress: " + request.getRemoteAddr() + " Interest created successfully: " + interest.getName() + "by user with ");
            return Response.status(Response.Status.CREATED).entity("Interest created successfully").build();
        } else {
            logger.error("Failed to create skill: " + interest.getName());
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create Interest").build();
        }
    }

    @POST
    @Path("/associate-user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response associateInterestToUser(@HeaderParam("token") String token, @QueryParam("userId") Long userId, @QueryParam("interestId") Long interestId) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to associate interest to user from IP: " + ip);

        // Authentication and authorization
        User user = userBean.getUserByToken(token);
        if(user == null || user.getId() != userId) {
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        // Associate the skill to the user
        boolean isAssociated = interestBean.associateInterestToUser(userId, interestId);

        // Check if the skill was associated successfully
        if (isAssociated) {
            logger.info("Interest with the id: " + interestId + " associated successfully to user: " + userId);
            return Response.status(Response.Status.OK).entity("Interest associated successfully").build();
        } else {
            logger.error("Failed to associate interest with id:" + interestId + " to user: " + userId);
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to associate interest").build();
        }
    }

    @PUT
    @Path("/{interestId}/users/{userId}/soft-delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeInterestfromUser(@HeaderParam("token") String token, @PathParam("userId") Long userId,
                                           @PathParam("interestId") Long interestId, @Context HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        logger.info("Received request to remove interest from user from IP: " + ip + "  by user with id: " + userId);

        // Authentication and authorization
        User user = userBean.getUserByToken(token);
        if(user == null || user.getId() != userId){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        // Associate the skill to the user
        boolean isInactive = interestBean.removeInterestfromUser(userId, interestId);

        // Check if the skill was associated successfully
        if (isInactive) {
            logger.info( " Ip Adress " + ip  + " Interest with the id: " + interestId + " removed successfully from user: " + userId);
            return Response.status(Response.Status.OK).entity("Interest removed successfully").build();
        } else {
            logger.error("Failed to remove interest with id:" + interestId + " from user: " + userId);
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to remove interest").build();
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getAllInterests(@Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        /*User user = userBean.getUSerByToken(token);
        if(user== null){
            logger.error("User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        logger.info("Received request to retrieve all interests from IP: " + ip);*/

        // Retrieve all interests
        return Response.status(Response.Status.OK).entity(interestBean.getAllInterests()).build();

    }


    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInsterestsByUserId(@HeaderParam("token") String token, @PathParam("userId") Long userId) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to get skills by user id from IP: " + ip);

        // Authentication and authorization
        User user = userBean.getUserByToken(token);
        if (user == null) {
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        // Get the skills by user id
        logger.info("IP Adress: " + ip + "Interests retrieved successfully for user with id: " + userId);
        return Response.status(Response.Status.OK).entity(interestBean.getAllInterestsByUser(userId)).build();
    }

}
