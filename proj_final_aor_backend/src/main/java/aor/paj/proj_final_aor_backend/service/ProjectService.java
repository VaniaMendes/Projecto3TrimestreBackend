package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.ActivityBean;
import aor.paj.proj_final_aor_backend.bean.ProjectBean;
import aor.paj.proj_final_aor_backend.bean.UserProjectBean;
import aor.paj.proj_final_aor_backend.dto.Project;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import aor.paj.proj_final_aor_backend.util.enums.UserTypeInProject;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/projects")
public class ProjectService {

    private static final Logger logger = LogManager.getLogger(ProjectService.class);

    @EJB
    private ProjectBean projectBean;

    @EJB
    private UserProjectBean userProjectBean;

    @EJB
    private ActivityBean activityBean;

    @Context
    private HttpServletRequest request;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerProject(@HeaderParam("token") String token, Project project) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to create project from IP: " + ip);

        if (projectBean.createProject(project, token)) {
            logger.info("Project created successfully: " + project.getName());
            return Response.status(Response.Status.CREATED).entity("Project created successfully").build();
        } else {
            logger.error("Failed to create project: " + project.getName());
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create project").build();
        }

    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProjects() {
        String ip = request.getRemoteAddr();
        logger.info("Received request to get all projects from IP: " + ip);

        // Get all projects
        return Response.status(Response.Status.OK).entity(projectBean.getAllProjectsLatestToOldest()).build();
    }

    @PUT
    @Path("/{id}/state")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateState(@PathParam("id") long projectId, @HeaderParam("stateId") int stateId, @HeaderParam("token") String token) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to update project from IP: " + ip);

        if (projectBean.updateState(projectId, stateId, token)){
            logger.info("Project updated successfully");
            return Response.status(Response.Status.OK).entity("Project updated successfully").build();
        } else {
            logger.error("Failed to update project");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update project").build();
        }

    }

    @PUT
    @Path("/{id}/description")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDescription(@PathParam("id") long projectId,
                                      @HeaderParam("description") String description,
                                      @HeaderParam("token") String token){
        String ip = request.getRemoteAddr();
        logger.info("Received request to update project description from IP: " + ip);

        if (projectBean.updateDescription(projectId, description, token)){
            logger.info("Project description updated successfully");
            return Response.status(Response.Status.OK).entity("Project description updated successfully").build();
        } else {
            logger.error("Failed to update project description");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update project description").build();
        }

    }

    @POST
    @Path("/{id}/resource")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addResource(@PathParam("id") long projectId,
                                @HeaderParam("resourceId") long resourceId,
                                @HeaderParam("quantity") int quantity) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to add resource to project from IP: " + ip);

        if (projectBean.addResource(projectId, resourceId, quantity)){
            logger.info("Resource added to project successfully");
            return Response.status(Response.Status.OK).entity("Resource added to project successfully").build();
        } else {
            logger.error("Failed to add resource to project");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to add resource to project").build();
        }

    }

    @POST
    @Path("/{id}/skill")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSkill(@PathParam("id") long projectId,
                             @HeaderParam("skillId") long skillId) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to add skill to project from IP: " + ip);

        if (projectBean.addSkill(projectId, skillId)){
            logger.info("Skill added to project successfully");
            return Response.status(Response.Status.OK).entity("Skill added to project successfully").build();
        } else {
            logger.error("Failed to add skill to project");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to add skill to project").build();
        }

    }

    @PUT
    @Path("/{id}/skill/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSkillState(@PathParam("id") long projectId,
                                     @HeaderParam("skillId") long skillId) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to update skill state in project from IP: " + ip);

        if (projectBean.removeSkill(projectId, skillId)){
            logger.info("Skill removed from Project with id '" + projectId + "' successfully");
            return Response.status(Response.Status.OK).entity("Skill removed successfully").build();
        } else {
            logger.error("Failed to remove skill from project");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to remove skill from project").build();
        }

    }

    @PUT
    @Path("/{id}/keyword/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addKeyword(@PathParam("id") long projectId,
                               @HeaderParam("keyword") String keyword) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to add keyword to project from IP: " + ip);

        if (projectBean.addKeyword(projectId, keyword)){
            logger.info("Keyword added to project successfully");
            return Response.status(Response.Status.OK).entity("Keyword added to project successfully").build();
        } else {
            logger.error("Failed to add keyword to project");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to add keyword to project").build();
        }

    }

    @PUT
    @Path("/{id}/keyword/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeKeyword(@PathParam("id") long projectId,
                                  @HeaderParam("keyword") String keyword) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to remove keyword from project from IP: " + ip);

        if (projectBean.removeKeyword(projectId, keyword)){
            logger.info("Keyword removed from project successfully");
            return Response.status(Response.Status.OK).entity("Keyword removed from project successfully").build();
        } else {
            logger.error("Failed to remove keyword from project");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to remove keyword from project").build();
        }

    }

    @POST
    @Path("/{id}/user/{userId}/add/{userType}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUserToProject(@PathParam("id") long projectId,
                                     @PathParam("userId") long userId,
                                     @PathParam("userType") String userType,
                                     @HeaderParam("token") String token){
        String ip = request.getRemoteAddr();
        logger.info("Received request to add user to project from IP: " + ip);

        if (projectBean.addUser(projectId, userId, UserTypeInProject.valueOf(userType), token)){
            logger.info("User added to Project with id '" + projectId + "' successfully");
            return Response.status(Response.Status.OK).entity("User added successfully").build();
        } else {
            logger.error("Failed to add user to project");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to add user to project").build();
        }
    }

    @PUT
    @Path("/{id}/user/{userId}/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUserFromProject(@PathParam("id") long projectId,
                                          @PathParam("userId") long userId,
                                          @HeaderParam("token") String token){
        String ip = request.getRemoteAddr();
        logger.info("Received request to remove user from project from IP: " + ip);

        if (userProjectBean.removeUserFromProject(userId, projectId, token)){
            logger.info("User removed from Project with id '" + projectId + "' successfully");
            return Response.status(Response.Status.OK).entity("User removed successfully").build();
        } else {
            logger.error("Failed to remove user from project");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to remove user from project").build();
        }

    }

    @PUT
    @Path("/{id}/user/{userId}/approve/{userType}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveUserInProject(@PathParam("id") long projectId,
                                         @PathParam("userId") long userId,
                                         @PathParam("userType") String userType,
                                         @HeaderParam("token") String token){
        String ip = request.getRemoteAddr();
        logger.info("Received request to approve user in project from IP: " + ip);

        if (projectBean.approveUser(userId, projectId, UserTypeInProject.valueOf(userType), token)){
            logger.info("User approved in Project with id '" + projectId + "' successfully");
            return Response.status(Response.Status.OK).entity("User approved successfully").build();
        } else {
            logger.error("Failed to approve user in project");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to approve user in project").build();
        }

    }

    @PUT
    @Path("/{id}/user/{userId}/update/{userType}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserTypeInProject(@PathParam("id") long projectId,
                                           @PathParam("userId") long userId,
                                           @PathParam("userType") String userType) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to update user type in project from IP: " + ip);

        if (userProjectBean.updateUserTypeInProject(userId, projectId, UserTypeInProject.valueOf(userType))){
            logger.info("User type updated in Project with id '" + projectId + "' successfully");
            return Response.status(Response.Status.OK).entity("User type updated successfully").build();
        } else {
            logger.error("Failed to update user type in project");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update user type in project").build();
        }

    }

    @GET
    @Path("/{id}/activity/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivitiesFromProject(@PathParam("id") long projectId) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to get activities from project from IP: " + ip);

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }else {
            logger.info("Activities from Project with id '" + projectId + "' retrieved successfully");
            return Response.status(Response.Status.OK).entity(activityBean.getActivitiesFromProject(projectId)).build();
        }
    }


}