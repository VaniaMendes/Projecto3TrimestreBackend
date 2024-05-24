package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.SkillBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
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

@Path("/skills")
public class SkillService {

    private static final Logger logger = LogManager.getLogger(SkillService.class);

    @EJB
    private SkillBean skillBean;
    @EJB
    private UserBean userBean;
    @Context
    private HttpServletRequest request;


    @POST
    @Path("/new-skill")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSkill(@HeaderParam("token") String token, Skill skill, @Context HttpServletRequest request) {
        User user = userBean.getUSerByToken(token);

        if(user == null) {
            logger.error("User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        // Create the skill
        boolean created = skillBean.createNewSkill(skill);

        // Check if the skill was created successfully
        if (created) {
            logger.info("IPAdress: " + request.getRemoteAddr() + " Skill created successfully: " + skill.getName() + " by user with ");
            return Response.status(Response.Status.CREATED).entity("Skill created successfully").build();
        } else {
            logger.error("Failed to create skill: " + skill.getName());
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create skill").build();
        }
    }

    @POST
    @Path("/associate-user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response associateSkillToUser(@HeaderParam("token") String token, @QueryParam("userId") Long userId, @QueryParam("skillId") Long skillId) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to associate skill to user from IP: " + ip);

        // Authentication and authorization
        User user = userBean.getUSerByToken(token);
        if(user == null || user.getId() != userId) {
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        // Associate the skill to the user
        boolean isAssociated = skillBean.associateSkillToUser(userId, skillId);

        // Check if the skill was associated successfully
        if (isAssociated) {
            logger.info("Skill associated successfully to user: " + userId);
            return Response.status(Response.Status.OK).entity("Skill associated successfully").build();
        } else {
            logger.error("Failed to associate skill to user: " + userId);
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to associate skill").build();
        }
    }

    @PUT
    @Path("/softDelete-user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSkill(@HeaderParam("token") String token, @QueryParam("skillId") long skillId,
                                @QueryParam("userId") long userId, @Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to update skill from IP: " + ip);

        // Authentication and authorization
        User user = userBean.getUSerByToken(token);
        if(user == null) {
            logger.error("IP Adress: " + ip +  "User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        // Update the skill
        boolean isUpdated = skillBean.softDeleteSkill(userId, skillId);

        System.out.println("isUpdated: " + isUpdated);
        // Check if the skill was updated successfully
        if (isUpdated) {
            logger.info("IP Adress: " + ip + "Skill updated successfully with the id: " + skillId + " by user with id: " + user.getId());
            return Response.status(Response.Status.OK).entity("Skill updated successfully").build();
        } else {
            logger.error("IP Adress: " + ip + "Failed to update skill with the id: " + skillId + " by user with id: " + user.getId());
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update skill").build();
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSkills(@HeaderParam("token") String token, @Context HttpServletRequest request) {
        User user = userBean.getUSerByToken(token);
        if(user == null) {
            logger.error("IP Adress " + request.getRemoteAddr() +  "User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        logger.info("IP Adress: " + request.getRemoteAddr() + "Skills retrieved successfully by user "  + user.getId());
        return Response.status(Response.Status.OK).entity(skillBean.getSkills()).build();
    }

}
