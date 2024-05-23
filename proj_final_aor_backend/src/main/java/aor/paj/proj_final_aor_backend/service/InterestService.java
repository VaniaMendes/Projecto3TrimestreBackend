package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.InterestBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dto.Interest;
import aor.paj.proj_final_aor_backend.dto.Skill;
import aor.paj.proj_final_aor_backend.dto.User;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
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
    @Path("/new-interest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSkill(@HeaderParam("token") String token, Interest interest, @Context HttpServletRequest request) {
        User user = userBean.getUSerByToken(token);

        if(user == null) {
            logger.error("User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        // Create the skill
        boolean created = interestBean.createNewInterest(interest);

        // Check if the skill was created successfully
        if (created) {
            logger.info("IPAdress: " + request.getRemoteAddr() + " Skill created successfully: " + interest.getName() + "by user with ");
            return Response.status(Response.Status.CREATED).entity("Skill created successfully").build();
        } else {
            logger.error("Failed to create skill: " + interest.getName());
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create skill").build();
        }
    }

    @POST
    @Path("/associate-user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response associateSkillToUser(@HeaderParam("token") String token, @QueryParam("userId") Long userId, @QueryParam("interestId") Long interestId) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to associate interest to user from IP: " + ip);

        // Authentication and authorization
        User user = userBean.getUSerByToken(token);
        if(user == null || user.getId() != userId) {
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        // Associate the skill to the user
        boolean isAssociated = interestBean.associateInterestToUser(userId, interestId);

        // Check if the skill was associated successfully
        if (isAssociated) {
            logger.info("Interest with the id: " + interestId + " associated successfully to user: " + userId);
            return Response.status(Response.Status.OK).entity("Skill associated successfully").build();
        } else {
            logger.error("Failed to associate interest with id:" + interestId + " to user: " + userId);
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to associate interest").build();
        }
    }
}
