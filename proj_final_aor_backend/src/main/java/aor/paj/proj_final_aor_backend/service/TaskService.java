package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.TaskBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dto.Task;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.dto.UserRegistration;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/project")
public class TaskService {
    private static final Logger logger = LogManager.getLogger(TaskService.class);
    @EJB
    UserBean userBean;

    @EJB
    TaskBean taskBean;


    @POST
    @Path("/{projectId}/add-task")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response registerUser(@HeaderParam("token") String token, @PathParam("projectId") long projectId, Task task, @Context HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        logger.debug("Received request to register a new task");
        User user = userBean.getUserByToken(token);

        try {
            if (task == null) {
                logger.error("IP Address " + ip + ": Error registering task: Task object is null");
                return Response.status(Response.Status.BAD_REQUEST).entity("Task object is null").build();
            }
            if (user == null) {
                logger.error("IP Address " + ip + ": Error registering task: User not found");
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            }

            boolean taskRegistered = taskBean.registerTask(task, projectId);

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

}
