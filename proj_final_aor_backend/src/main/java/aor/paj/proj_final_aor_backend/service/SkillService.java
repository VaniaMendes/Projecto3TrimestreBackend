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

    /**
     * This method is used to associate a skill to a user.
     * It first logs the request to associate a skill to a user.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found or the user's id does not match the provided user id, it logs the error and returns a response with status UNAUTHORIZED and an error message.
     * If the user id or skill id is not provided, it logs the error and returns a response with status BAD_REQUEST and an error message.
     * If the user is found and the user id and skill id are provided, it attempts to associate the skill to the user.
     * If the operation is successful and the skill is associated, it logs the success and returns a response with status OK and a success message.
     * If the operation fails, it logs the error and returns a response with status BAD_REQUEST and an error message.
     *
     * @param token The token of the user trying to associate a skill to a user.
     * @param userId The id of the user to whom the skill is to be associated.
     * @param skillId The id of the skill to be associated to the user.
     * @return Response The response of the operation, containing a success or error message.
     */
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

    /**
     * This method is used to soft delete a skill for a specific user.
     * It first logs the request to update the skill.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the error and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it attempts to soft delete the skill in the database.
     * If the operation is successful and the skill is soft deleted, it logs the success and returns a response with status OK and a success message.
     * If the operation fails, it logs the error and returns a response with status BAD_REQUEST and an error message.
     *
     * @param token The token of the user trying to soft delete the skill.
     * @param skillId The id of the skill to be soft deleted.
     * @param userId The id of the user for whom the skill is to be soft deleted.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
     */
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

    /**
     * This method is used to retrieve all skills.
     * It first logs the request to get all skills.
     * Then it retrieves all skills from the database.
     * If the operation is successful and the skills are found, it logs the success and returns a response with status OK and the list of skills.
     *
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the list of skills.
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSkills(@Context HttpServletRequest request) {

        logger.info("IP Adress: " + request.getRemoteAddr() + "Skills retrieved successfully");
        return Response.status(Response.Status.OK).entity(skillBean.getSkills()).build();
    }

    /**
     * This method is used to retrieve all projects associated with a specific skill.
     * It first logs the request to get projects by skill id.
     * Then it retrieves all projects associated with the provided skill id from the database.
     * If the operation is successful and the projects are found, it logs the success and returns a response with status OK and the list of projects.
     * If the operation fails or no projects are found associated with the provided skill id, it logs the error and returns a response with status NOT_FOUND and an error message.
     *
     * @param skillId The id of the skill for which the projects are to be retrieved.
     * @param order The order in which the projects are to be retrieved.
     * @param state The state of the projects to be retrieved.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the list of projects or an error message.
     */
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

    /**
     * This method is used to retrieve all skills that are associated with projects.
     * It first logs the request to get all skills with projects.
     * Then it retrieves all skills with projects from the database.
     * If the operation is successful and the skills are found, it logs the success and returns a response with status OK and the list of skills with projects.
     *
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the list of skills with projects.
     */
    @GET
    @Path("/with-projects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSkillsWithProjects(@Context HttpServletRequest request) {

        logger.info("IP Adress: " + request.getRemoteAddr() + "Skills retrieved successfully");
        return Response.status(Response.Status.OK).entity(skillBean.getSkillsWithProjects()).build();
    }

    /**
     * This method is used to search for skills that are associated with projects by name.
     * It first logs the request to search for skills with projects.
     * Then it searches for skills with projects that match the provided name from the database.
     * If the operation is successful and the skills are found, it logs the success and returns a response with status OK and the list of skills with projects.
     * If the operation fails or no skills are found that match the provided name, it logs the error and returns a response with status NOT_FOUND and an error message.
     *
     * @param name The name of the skills to be searched.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the list of skills with projects or an error message.
     */
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


    /**
     * This method is used to retrieve all skills associated with a specific user.
     * It first logs the request to get skills by user id.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the error and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it retrieves all skills associated with the provided user id from the database.
     * If the operation is successful and the skills are found, it logs the success and returns a response with status OK and the list of skills.
     * If the operation fails or no skills are found associated with the provided user id, it logs the error and returns a response with status NOT_FOUND and an error message.
     *
     * @param token The token of the user trying to retrieve the skills.
     * @param userId The id of the user for whom the skills are to be retrieved.
     * @return Response The response of the operation, containing the list of skills or an error message.
     */
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

    /**
     * This method is used to retrieve all skills that are not associated with a specific project.
     * It first logs the request to get skills not associated with the project.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the error and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it retrieves all skills not associated with the provided project id from the database.
     * If the operation is successful and the skills are found, it logs the success and returns a response with status OK and the list of skills not associated with the project.
     *
     * @param token The token of the user trying to retrieve the skills.
     * @param projectId The id of the project for which the skills not associated are to be retrieved.
     * @return Response The response of the operation, containing the list of skills not associated with the project.
     */
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
