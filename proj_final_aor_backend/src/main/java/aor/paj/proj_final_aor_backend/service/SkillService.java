package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.ProjectSkillBean;
import aor.paj.proj_final_aor_backend.bean.SkillBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dto.Project;
import aor.paj.proj_final_aor_backend.dto.Skill;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Path("/skills")
public class SkillService {

    private static final Logger logger = LogManager.getLogger(SkillService.class);

    @EJB
    private SkillBean skillBean;
    @EJB
    private UserBean userBean;
    @EJB
    private ProjectSkillBean projectSkillBean;
    @Context
    private HttpServletRequest request;


    @POST
    @Path("/new-skill")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSkill(@HeaderParam("token") String token, Skill skill, @Context HttpServletRequest request) {
        User user = userBean.getUserByToken(token);

        if(user == null) {
            logger.error("User not found");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        // Create the skill
        Skill skillCreated = skillBean.createNewSkill(token,skill);

        // Check if the skill was created successfully
        if (skillCreated!=null) {
            logger.info("IPAdress: " + request.getRemoteAddr() + " Skill created successfully: " + skill.getName() + " by user with id: " + user.getId());
            return Response.status(Response.Status.CREATED).entity(skillCreated).build();
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
        User user = userBean.getUserByToken(token);
        if(user == null || user.getId() != userId) {
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if(userId == null || skillId == null) {
            logger.error("User or skill not found");
            return Response.status(Response.Status.BAD_REQUEST).entity("User or skill not found").build();
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
    @Path("/{skillId}/users/{userId}/soft-delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSkill(@HeaderParam("token") String token, @PathParam("skillId") long skillId,
                                @PathParam("userId") long userId, @Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to update skill from IP: " + ip);

        // Authentication and authorization
        User user = userBean.getUserByToken(token);
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
    public Response getAllSkills(@Context HttpServletRequest request) {

        logger.info("IP Adress: " + request.getRemoteAddr() + "Skills retrieved successfully");
        return Response.status(Response.Status.OK).entity(skillBean.getSkills()).build();
    }

    @GET
    @Path("/{skillId}/projects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectsBySkillId(@PathParam("skillId") Long skillId,
                                         @QueryParam("order") String order,
                                         @QueryParam("state") Integer state,
                                         @Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to get projects by skill id from IP: " + ip);

        // Get the projects by skill id
        List<Project> listProjects = projectSkillBean.getProjectsBySkill(skillId, order, state);

        // Check if the projects were found
        if (listProjects != null) {
            logger.info("IP Adress: " + ip + "Projects retrieved successfully");
            return Response.status(Response.Status.OK).entity(listProjects).build();
        } else {
            logger.error("IP Adress: " + ip + "Projects not found");
            return Response.status(Response.Status.NOT_FOUND).entity("Projects not found").build();
        }
    }

    @GET
    @Path("/with-projects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSkillsWithProjects(@Context HttpServletRequest request) {

        logger.info("IP Adress: " + request.getRemoteAddr() + "Skills retrieved successfully");
        return Response.status(Response.Status.OK).entity(skillBean.getSkillsWithProjects()).build();
    }

    @GET
    @Path("/with-projects/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchSkillsWithProjects(@QueryParam("name") String name, @Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to search skills with projects from IP: " + ip);

        // Search the skills with projects
        List<SkillEntity> listSkills = skillBean.searchSkillsWithProjects(name);

        // Check if the skills were found
        if (listSkills != null) {
            logger.info("IP Adress: " + ip + "Skills with projects retrieved successfully");
            return Response.status(Response.Status.OK).entity(listSkills).build();
        } else {
            logger.error("IP Adress: " + ip + "Skills with projects not found");
            return Response.status(Response.Status.NOT_FOUND).entity("Skills with projects not found").build();
        }
    }


    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkillsByUserId(@HeaderParam("token") String token, @PathParam("userId") Long userId) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to get skills by user id from IP: " + ip);

        // Authentication and authorization
        User user = userBean.getUserByToken(token);
        if (user == null) {
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        List<Skill> listUserSkills = skillBean.getSkillsByUserId(userId);

        if(listUserSkills == null) {
            logger.error("Skills not found");
            return Response.status(Response.Status.NOT_FOUND).entity("Skills not found").build();
        }
        return Response.status(Response.Status.OK).entity(listUserSkills).build();

    }

    @GET
    @Path("/project/{projectId}/not-associated")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkillsNotInProject(@HeaderParam("token") String token, @PathParam("projectId") Long projectId) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to get skills not associated with project from IP: " + ip);

        // Authentication and authorization
        User user = userBean.getUserByToken(token);
        if (user == null) {
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        // Get the skills not associated with the project
        logger.info("IP Adress: " + ip + "Skills not associated with project retrieved successfully");
        return Response.status(Response.Status.OK).entity(projectSkillBean.getSkillsNotInProject(projectId)).build();
    }


}
