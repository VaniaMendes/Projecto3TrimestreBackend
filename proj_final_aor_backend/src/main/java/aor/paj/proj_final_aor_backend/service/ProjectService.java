package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.ProjectBean;
import aor.paj.proj_final_aor_backend.dto.Project;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
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

    @Context
    private HttpServletRequest request;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerProject(Project project) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to create project from IP: " + ip);

        // Create the project
        ProjectEntity createdProject=projectBean.createProject(project);

        if (createdProject != null) {
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
    public Response updateState(@PathParam("id") long projectId, @HeaderParam("stateId") int stateId) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to update project from IP: " + ip);

        if (projectBean.updateState(projectId, stateId)){
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
                                      @HeaderParam("description") String description) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to update project description from IP: " + ip);

        if (projectBean.updateDescription(projectId, description)){
            logger.info("Project description updated successfully");
            return Response.status(Response.Status.OK).entity("Project description updated successfully").build();
        } else {
            logger.error("Failed to update project description");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update project description").build();
        }

    }

    @PUT
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

    @PUT
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


}
