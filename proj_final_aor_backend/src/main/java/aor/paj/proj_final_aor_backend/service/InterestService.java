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



    /**
     * This method is used to create a new interest.
     * It first checks if the user is authenticated by checking the token.
     * If the user is not authenticated, it returns a response with status UNAUTHORIZED.
     * If the user is authenticated, it attempts to create a new interest.
     * If the interest is created successfully, it returns a response with status CREATED.
     * If the interest creation fails, it returns a response with status BAD_REQUEST.
     *
     * @param token The token of the user trying to create the interest.
     * @param interest The interest to be created.
     * @param request The HTTP request.
     * @return Response The response of the operation.
     */
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


    /**
     * This method is used to associate an interest to a user.
     * It first logs the IP address of the request.
     * Then it checks if the user is authenticated by checking the token and if the user id matches the id in the token.
     * If the user is not authenticated or the ids do not match, it returns a response with status UNAUTHORIZED.
     * If the user is authenticated and the ids match, it attempts to associate the interest to the user.
     * If the interest is associated successfully, it logs the success and returns a response with status OK.
     * If the interest association fails, it logs the failure and returns a response with status BAD_REQUEST.
     *
     * @param token The token of the user trying to associate the interest.
     * @param userId The id of the user to whom the interest is to be associated.
     * @param interestId The id of the interest to be associated.
     * @return Response The response of the operation.
     */
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


    /**
     * This method is used to remove an interest from a user.
     * It first logs the IP address of the request.
     * Then it checks if the user is authenticated by checking the token and if the user id matches the id in the token.
     * If the user is not authenticated or the ids do not match, it returns a response with status UNAUTHORIZED.
     * If the user is authenticated and the ids match, it attempts to remove the interest from the user.
     * If the interest is removed successfully (i.e., the interest is set to inactive), it logs the success and returns a response with status OK.
     * If the interest removal fails, it logs the failure and returns a response with status BAD_REQUEST.
     *
     * @param token The token of the user trying to remove the interest.
     * @param userId The id of the user from whom the interest is to be removed.
     * @param interestId The id of the interest to be removed.
     * @param request The HTTP request.
     * @return Response The response of the operation.
     */
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

    /**
     * This method is used to retrieve all interests.
     * It first logs the IP address of the request.
     * Then it retrieves all interests from the database.
     * It returns a response with status OK and the list of all interests.
     *
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the list of all interests.
     */
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


    /**
     * This method is used to retrieve all interests of a specific user.
     * It first logs the IP address of the request.
     * Then it checks if the user is authenticated by checking the token.
     * If the user is not authenticated, it returns a response with status UNAUTHORIZED.
     * If the user is authenticated, it retrieves all interests of the user from the database.
     * It returns a response with status OK and the list of all interests of the user.
     *
     * @param token The token of the user trying to retrieve the interests.
     * @param userId The id of the user whose interests are to be retrieved.
     * @return Response The response of the operation, containing the list of all interests of the user.
     */
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
