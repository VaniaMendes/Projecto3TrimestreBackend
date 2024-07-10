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

    /**
     * This method is used to retrieve all tasks associated with a specific project, ordered by date.
     * It first logs the request to get tasks.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the error and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it attempts to retrieve all tasks associated with the provided project id, ordered by date.
     * If the operation is successful and the tasks are found, it logs the success and returns a response with status OK and the list of tasks.
     * If the operation fails, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to retrieve tasks.
     * @param projectId The id of the project whose tasks are to be retrieved.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the list of tasks or an error message.
     */
    @GET
    @Path("/{projectId}/tasks/order")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getTasksOrderByDate(@HeaderParam("token") String token, @PathParam("projectId") long projectId, @Context HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.debug("Received request to get tasks");
        User user = userBean.getUserByToken(token);

        try {
            if (user == null) {
                logger.error("IP Address " + ip + ": Error getting tasks: User not found");
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            }

            logger.info("IP Address " + ip + ": Tasks retrieved successfully");
            return Response.status(Response.Status.OK).entity(taskBean.getTasksFromProjectOrderByDate(projectId)).build();
        } catch (Exception e) {
            logger.error("IP Address " + ip + ": Error getting tasks: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error getting tasks: " + e.getMessage()).build();
        }
    }

    /**
     * This method is used to retrieve minimal information about all tasks associated with a specific project.
     * It first logs the request to get tasks.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the error and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it attempts to retrieve all tasks associated with the provided project id with minimal information.
     * If the operation is successful and the tasks are found, it logs the success and returns a response with status OK and the list of tasks.
     * If the operation fails, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to retrieve tasks.
     * @param projectId The id of the project whose tasks are to be retrieved.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the list of tasks or an error message.
     */
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


    /**
     * This method is used to register a new task for a specific project.
     * It first logs the request to register a new task.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the error and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it attempts to register the task in the database.
     * If the operation is successful and the task is registered, it logs the success and returns a response with status CREATED and a success message.
     * If the operation fails, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to register the task.
     * @param projectId The id of the project for which the task is to be registered.
     * @param requestBody The request body, containing the task to be registered and a list of task ids.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
     */
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

    /**
     * This method is used to update the status of a task for a specific project.
     * It first logs the request to update the task status.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the error and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it attempts to update the task status in the database.
     * If the operation is successful and the task status is updated, it logs the success and returns a response with status OK and a success message.
     * If the operation fails, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to update the task status.
     * @param projectId The id of the project for which the task status is to be updated.
     * @param taskId The id of the task whose status is to be updated.
     * @param status The new status of the task.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
     */
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

    /**
     * This method is used to soft delete a task for a specific project.
     * It first logs the request to soft delete the task.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the error and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it attempts to soft delete the task in the database.
     * If the operation is successful and the task is soft deleted, it logs the success and returns a response with status OK and a success message.
     * If the operation fails, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to soft delete the task.
     * @param projectId The id of the project for which the task is to be soft deleted.
     * @param taskId The id of the task to be soft deleted.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
     */
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
     * This method is used to update a task for a specific project.
     * It first logs the request to update the task.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the error and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it attempts to update the task in the database.
     * If the operation is successful and the task is updated, it logs the success and returns a response with status CREATED and a success message.
     * If the operation fails, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to update the task.
     * @param projectId The id of the project for which the task is to be updated.
     * @param taskId The id of the task to be updated.
     * @param requestBody The request body, containing the task to be updated and a list of task ids.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing a success or error message.
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
     * This method is used to retrieve information about a specific task.
     * It first logs the request to get task info.
     * Then it retrieves the user associated with the provided token.
     * If the user is not found, it logs the error and returns a response with status UNAUTHORIZED and an error message.
     * If the user is found, it attempts to retrieve the task information from the database.
     * If the operation is successful and the task is found, it logs the success and returns a response with status OK and the task information.
     * If the operation fails, it logs the error and returns a response with status INTERNAL_SERVER_ERROR and an error message.
     *
     * @param token The token of the user trying to retrieve the task information.
     * @param projectId The id of the project to which the task belongs.
     * @param taskId The id of the task whose information is to be retrieved.
     * @param request The HTTP request.
     * @return Response The response of the operation, containing the task information or an error message.
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
