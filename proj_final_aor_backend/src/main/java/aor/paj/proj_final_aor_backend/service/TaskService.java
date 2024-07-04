package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.TaskBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dto.Task;
import aor.paj.proj_final_aor_backend.dto.User;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ejb.EJB;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Path("/project")
public class TaskService {
    private static final Logger logger = LogManager.getLogger(TaskService.class);
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @EJB
    UserBean userBean;

    @EJB
    TaskBean taskBean;


    @GET
    @Path("/{projectId}/tasks")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getTasks(@HeaderParam("token") String token, @PathParam("projectId") long projectId, @Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.debug("Received request to get tasks");
        User user = userBean.getUserByToken(token);

        try {
            if (user == null) {
                logger.error("IP Address " + ip + ": Error getting tasks: User not found");
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            }

            logger.info("IP Address " + ip + ": Tasks retrieved successfully");
            return Response.status(Response.Status.OK).entity(taskBean.getTasksFromProject(projectId)).build();
        } catch (Exception e) {
            logger.error("IP Address " + ip + ": Error getting tasks: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error getting tasks: " + e.getMessage()).build();
        }
    }
    @GET
    @Path("/{projectId}/tasks-info")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getTasksInfo(@HeaderParam("token") String token, @PathParam("projectId") long projectId, @Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.debug("Received request to get tasks");
        User user = userBean.getUserByToken(token);

        try {
            if (user == null) {
                logger.error("IP Address " + ip + ": Error getting tasks: User not found");
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            }

            logger.info("IP Address " + ip + ": Tasks retrieved successfully");
            return Response.status(Response.Status.OK).entity(taskBean.getTasksFromProjectMinimalInfo(projectId)).build();
        } catch (Exception e) {
            logger.error("IP Address " + ip + ": Error getting tasks: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error getting tasks: " + e.getMessage()).build();
        }
    }


    @POST
    @Path("/{projectId}/add-task")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response registerTask(@HeaderParam("token") String token,
                                 @PathParam("projectId") long projectId,
                                 JsonObject requestBody,
                                 @Context HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        logger.debug("Received request to register a new task");
        User user = userBean.getUserByToken(token);

        try {

            JsonObject taskJson = requestBody.getJsonObject("task");
            Task task = objectMapper.readValue(taskJson.toString(), Task.class);

            JsonArray tasksIdListJson = requestBody.getJsonArray("tasksIdList");
            List<Long> tasksIdList = objectMapper.readValue(tasksIdListJson.toString(), new TypeReference<List<Long>>() {});

            if (task == null) {
                logger.error("IP Address " + ip + ": Error registering task: Task object is null");
                return Response.status(Response.Status.BAD_REQUEST).entity("Task object is null").build();
            }
            if (user == null) {
                logger.error("IP Address " + ip + ": Error registering task: User not found");
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            }

            boolean taskRegistered = taskBean.registerTask(task, projectId, user, tasksIdList);

            if (!taskRegistered) {
                logger.error("IP Address " + ip + ": Error registering task");
                return Response.status(Response.Status.BAD_REQUEST).entity("Error registering task").build();
            }

            logger.info("IP Address " + ip + ": Task registered successfully");
            return Response.status(Response.Status.CREATED).entity("Task registered successfully").build();
        } catch (Exception e) {
            logger.error("IP Address " + ip + ": Error registering task: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error registering task: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{projectId}/{taskId}/update-status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTaskState(@HeaderParam("token") String token, @PathParam("projectId") long projectId,
                                    @PathParam("taskId") long taskId, @QueryParam("status") int status,
                                    @Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.debug("Received request to update task state");
        User user = userBean.getUserByToken(token);

        try {
            if (user == null) {
                logger.error("IP Address " + ip + ": Error updating task state: User not found");
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            }


            boolean taskStateUpdated = taskBean.updateTaskStatus(taskId, status, projectId);

            if (!taskStateUpdated) {
                logger.error("IP Address " + ip + ": Error updating task state");
                return Response.status(Response.Status.BAD_REQUEST).entity("Error updating task state").build();
            }

            logger.info("IP Address " + ip + ": Task state updated successfully");
            return Response.status(Response.Status.OK).entity("Task state updated successfully").build();
        } catch (Exception e) {
            logger.error("IP Address " + ip + ": Error updating task state: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating task state: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{projectId}/{taskId}/soft-delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response softDeleteTask(@HeaderParam("token") String token, @PathParam("projectId") long projectId,
                                  @PathParam("taskId") long taskId, @Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.debug("Received request to soft delete task");
        User user = userBean.getUserByToken(token);

        try {
            if (user == null) {
                logger.error("IP Address " + ip + ": Error soft deleting task: User not found");
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            }

            boolean taskDeleted = taskBean.softDeleteTask(taskId, projectId);

            if (!taskDeleted) {
                logger.error("IP Address " + ip + ": Error soft deleting task");
                return Response.status(Response.Status.BAD_REQUEST).entity("Error soft deleting task").build();
            }

            logger.info("IP Address " + ip + ": Task soft deleted successfully");
            return Response.status(Response.Status.OK).entity("Task soft deleted successfully").build();
        } catch (Exception e) {
            logger.error("IP Address " + ip + ": Error soft deleting task: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error soft deleting task: " + e.getMessage()).build();
        }
    }

    /**
     * Method to update a task
     * @param token - user token
     * @param projectId - project id
     * @param taskId - task id
     * @param requestBody - request body
     * @param request - http request
     * @return
     */
    @PUT
    @Path("/{projectId}/{taskId}/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response updateTask(@HeaderParam("token") String token,
                                 @PathParam("projectId") long projectId,
                                 @PathParam("taskId") long taskId,
                                 JsonObject requestBody,
                                 @Context HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        logger.debug("Received request to register a new task");
        User user = userBean.getUserByToken(token);

        try {

            JsonObject taskJson = requestBody.getJsonObject("task");
            Task task = objectMapper.readValue(taskJson.toString(), Task.class);

            JsonArray tasksIdListJson = requestBody.getJsonArray("tasksIdList");
            List<Long> tasksIdList = objectMapper.readValue(tasksIdListJson.toString(), new TypeReference<List<Long>>() {});

            if (task == null) {
                logger.error("IP Address " + ip + ": Error updating task: Task object is null");
                return Response.status(Response.Status.BAD_REQUEST).entity("Task object is null").build();
            }
            if (user == null) {
                logger.error("IP Address " + ip + ": Error updating task: User not found");
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            }

            boolean taskUpdated = taskBean.updateTask(taskId, task, tasksIdList, projectId);

            if (!taskUpdated) {
                logger.error("IP Address " + ip + ": Error updating task");
                return Response.status(Response.Status.BAD_REQUEST).entity("Error updating task").build();
            }

            logger.info("IP Address " + ip + ": Task updating successfully");
            return Response.status(Response.Status.CREATED).entity("Task updating successfully").build();
        } catch (Exception e) {
            logger.error("IP Address " + ip + ": Error updating task: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating task: " + e.getMessage()).build();
        }
    }

    /**
     * Method to get the task info for fill the update task modal
     * @param token - user token
     * @param projectId - project id
     * @param taskId - task id
     * @param request - http request
     * @return - task info
     */
    @GET
    @Path("/{projectId}/tasks/{taskId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaskInfo(@HeaderParam("token") String token, @PathParam("projectId") long projectId,
                                @PathParam("taskId") long taskId, @Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.debug("Received request to get task info");
        User user = userBean.getUserByToken(token);

        try {
            if (user == null) {
                logger.error("IP Address " + ip + ": Error getting task info: User not found");
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            }

            Task task = taskBean.getTaskInfo(taskId);

            if (task == null) {
                logger.error("IP Address " + ip + ": Error getting task info: Task not found");
                return Response.status(Response.Status.BAD_REQUEST).entity("Task not found").build();
            }

            logger.info("IP Address " + ip + ": Task info retrieved successfully");
            return Response.status(Response.Status.OK).entity(task).build();
        } catch (Exception e) {
            logger.error("IP Address " + ip + ": Error getting task info: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error getting task info: " + e.getMessage()).build();
        }
    }

}
