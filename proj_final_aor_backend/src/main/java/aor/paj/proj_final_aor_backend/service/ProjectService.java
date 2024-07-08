package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.*;
import aor.paj.proj_final_aor_backend.dto.ObservationRequest;
import aor.paj.proj_final_aor_backend.dto.Project;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.util.enums.UserTypeInProject;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Path("/projects")
public class ProjectService {

    private static final Logger logger = LogManager.getLogger(ProjectService.class);

    @EJB
    private ProjectBean projectBean;
    @EJB
    private UserProjectBean userProjectBean;
    @EJB
    private ActivityBean activityBean;
    @EJB
    private UserBean userBean;
    @EJB
    private ProjectResourceBean projectResourceBean;
    @EJB
    private ResourceBean resourceBean;

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
    public Response getAllProjects(@QueryParam("order") String order, @QueryParam("vacancies") Boolean vacancies, @QueryParam("state") Integer state) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to get all projects from IP: " + ip);

        List<Project> projects = projectBean.getAllProjects(order, vacancies, state);
        return Response.status(Response.Status.OK).entity(projects).build();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchProjects(@QueryParam("name") String name,
                                   @QueryParam("state") Integer state,
                                   @QueryParam("orderByVacancies") Boolean orderByVacancies,
                                   @QueryParam("order") String order) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to search projects from IP: " + ip + " with parameters - Name: " + name + ", State: " + state + ", OrderByVacancies: " + orderByVacancies + ", Order: " + order);

        List<Project> projects = projectBean.searchProjects(name, state, orderByVacancies, order);
        return Response.status(Response.Status.OK).entity(projects).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectById(@PathParam("id") long projectId, @HeaderParam("token") String token) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to get project by id from IP: " + ip);

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        Project project = projectBean.getProjectById(projectId);
        if (project == null) {
            logger.error("Project not found");
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        } else {
            logger.info("Project with id '" + projectId + "' retrieved successfully");
            return Response.status(Response.Status.OK).entity(project).build();
        }
    }

    @GET
    @Path("/keyword/{keyword}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectsByKeyword(@PathParam("keyword") String keyword,
                                         @QueryParam("order") String order,
                                         @QueryParam("vacancies") Boolean vacancies,
                                         @QueryParam("state") Integer state) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to get projects by keyword from IP: " + ip);

        List<Project> projects = projectBean.getProjectsByKeywordOrSkill(keyword, order, vacancies, state);
        return Response.status(Response.Status.OK).entity(projects).build();
    }

    @GET
    @Path("/keywords")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllKeywords() {
        String ip = request.getRemoteAddr();
        logger.info("Received request to get all keywords from IP: " + ip);

        List<String> keywords = projectBean.getAllKeywords();
        return Response.status(Response.Status.OK).entity(keywords).build();
    }

    @GET
    @Path("/keywords/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchKeywords(@QueryParam("keyword") String keyword) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to search keywords from IP: " + ip);

        List<String> keywords = projectBean.searchKeywords(keyword);
        return Response.status(Response.Status.OK).entity(keywords).build();
    }

    @PUT
    @Path("/{id}/state")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateState(@PathParam("id") long projectId, @HeaderParam("stateId") int stateId, @HeaderParam("token") String token) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to update project from IP: " + ip);

        boolean valid=projectBean.updateState(projectId, stateId, token);

        if (valid){
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

    @PUT
    @Path("/{id}/observation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateObservation(@PathParam("id") long projectId,
                                      ObservationRequest observationRequest,
                                      @HeaderParam("token") String token){
        String ip = request.getRemoteAddr();
        logger.info("Received request to update project observation from IP: " + ip);

        if (projectBean.updateObservations(projectId, observationRequest.getObservation(), token)){
            logger.info("Project observation updated successfully");
            return Response.status(Response.Status.OK).entity("Project observation updated successfully").build();
        } else {
            logger.error("Failed to update project observation");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update project observation").build();
        }
    }

    @POST
    @Path("/{id}/resource")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addResource(@PathParam("id") long projectId,
                                @HeaderParam("quantity") int quantity,
                                @HeaderParam("token") String token,
                                @HeaderParam("resourceId") long resourceId) {

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }

        if (resourceBean.getResourceById(resourceId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Resource not found").build();
        }

        if (projectBean.addResource(projectId, resourceId, quantity, token)){
            return Response.status(Response.Status.OK).entity("Resource added to project successfully").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to add resource to project").build();
        }
    }

    @POST
    @Path("/{id}/skill")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response joinSkill(@PathParam("id") long projectId,
                             @HeaderParam("skillId") long skillId,
                             @HeaderParam("token") String token) {
        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }

        logger.info("Received request to associate skill to project from IP: " + ip);

        if (projectBean.joinSkill(projectId, skillId)){
            logger.info("Skill associated to project successfully");
            return Response.status(Response.Status.OK).entity("Skill added to project successfully").build();
        } else {
            logger.error("Failed to associate skill to project");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to add skill to project").build();
        }

    }

    @PUT
    @Path("/{id}/skill/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSkill(@PathParam("id") long projectId,
                                     @HeaderParam("skillId") long skillId,
                                     @HeaderParam("token") String token) {
        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }

        logger.info("Received request to update skill state in project from IP: " + ip);

        if (projectBean.editSkillActiveStatus(projectId, skillId, true)){
            logger.info("Skill added to Project with id '" + projectId + "' successfully");
            return Response.status(Response.Status.OK).entity("Skill added successfully").build();
        } else {
            logger.error("Failed to add skill to project");
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to add skill to project").build();
        }

    }

    @PUT
    @Path("/{id}/skill/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSkill(@PathParam("id") long projectId,
                                     @HeaderParam("skillId") long skillId,
                                     @HeaderParam("token") String token) {
        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }

        logger.info("Received request to update skill state in project from IP: " + ip);

        if (projectBean.editSkillActiveStatus(projectId, skillId, false)){
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
                               @HeaderParam("keyword") String keyword,
                               @HeaderParam("token") String token) {
        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }

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
                                  @HeaderParam("keyword") String keyword,
                                  @HeaderParam("token") String token){
        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }

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

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
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

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
        logger.info("Received request to remove user from project from IP: " + ip);

        if (projectBean.removeUser(userId, projectId, token)){
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

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
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
                                            @PathParam("userType") String userType,
                                            @HeaderParam("token") String token){
        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
        logger.info("Received request to update user type in project from IP: " + ip);

        if (projectBean.updateUserRole(userId, projectId, UserTypeInProject.valueOf(userType), token)){
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
    public Response getActivitiesFromProject(@PathParam("id") long projectId, @HeaderParam("token") String token) {
        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }

        logger.info("Received request to get activities from project from IP: " + ip);

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }else {
            logger.info("Activities from Project with id '" + projectId + "' retrieved successfully");
            return Response.status(Response.Status.OK).entity(activityBean.getActivitiesFromProject(projectId)).build();
        }
    }

    @GET
    @Path("/{id}/activity/last/{maxResults}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLastXActivitiesFromProject(@PathParam("id") long projectId, @PathParam("maxResults") int maxResults, @HeaderParam("token") String token) {
        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }

        logger.info("Received request to get last activities from project from IP: " + ip);

        return Response.status(Response.Status.OK).entity(activityBean.getLastXActivitiesFromProject(projectId, maxResults)).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countProjects(@QueryParam("state") Integer state, @QueryParam("keyword") String keyword, @QueryParam("search") String search) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to count projects from IP: " + ip);

        if (keyword != null) {
            logger.info("Projects counted by keyword " + keyword + " successfully");
            return Response.status(Response.Status.OK).entity(projectBean.countProjectsByKeywordOrSkill(keyword, state)).build();
        } else if (search!= null) {
            logger.info("Projects counted by search " + search + " successfully");
            return Response.status(Response.Status.OK).entity(projectBean.countSearchProjectsByName(search, state)).build();
        } else {
            return Response.status(Response.Status.OK).entity(projectBean.countProjects(state)).build();
        }
    }


    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectsByUserId(@HeaderParam("token") String token,
                                        @PathParam("userId") Long userId,
                                        @QueryParam("order") String order,
                                        @QueryParam("vacancies") Boolean vacancies,
                                        @QueryParam("state") Integer state) {

        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null ){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        logger.info("Received request to get projects by user id from IP: " + ip);

        return Response.status(Response.Status.OK).entity(userProjectBean.getActiveProjectsOfAUser(userId)).build();
    }

    @GET
    @Path("/user/{userId}/info/full")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectsInfoByUserId(@HeaderParam("token") String token,
                                            @PathParam("userId") Long userId,
                                            @QueryParam("order") String order,
                                            @QueryParam("vacancies") Boolean vacancies,
                                            @QueryParam("state") Integer state) {

        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null || user.getId() != userId){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        logger.info("Received request to get projects by user id from IP: " + ip);

        return Response.status(Response.Status.OK).entity(projectBean.getAllProjectsWithUser(userId, order, vacancies, state)).build();
    }

    @GET
    @Path("/{projectId}/users/available")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableUsersForProject(@PathParam("projectId") Long projectId,
                                                @HeaderParam("token") String token){
        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
        logger.info("Received request to get available users for project from IP: " + ip);

        return Response.status(Response.Status.OK).entity(userProjectBean.getAllUsersAvailableForProject(projectId)).build();
    }

    @GET
    @Path("/{projectId}/candidates")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCandidatesForProject(@PathParam("projectId") Long projectId,
                                            @HeaderParam("token") String token){
        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
        logger.info("Received request to get candidates for project from IP: " + ip);

        return Response.status(Response.Status.OK).entity(userProjectBean.getAllUsersNotApprovedInProject(projectId)).build();
    }


    @GET
    @Path("/{id}/resources")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllResources(@HeaderParam("token") String token,
                                    @PathParam("id") long projectId) {
        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (projectBean.findProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }

        logger.info("Received request from user with ID: '" + user.getId() + "' to get all resources from Project with id: " + projectId + " from IP: " + ip);

        return Response.status(Response.Status.OK).entity(projectResourceBean.getAllResourcesFromProject(projectId)).build();
    }
}