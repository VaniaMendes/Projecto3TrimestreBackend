package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.ActivityBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dto.User;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Path("/activities")
public class ActivityService {

    private static final Logger logger = LogManager.getLogger(ActivityService.class);

    @EJB
    private ActivityBean activityBean;

    @EJB
    private UserBean userBean;

    @Context
    private HttpServletRequest request;

    @POST
    @Path("/project/{projectId}/member-comment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerActivityMemberComment(String input,
                                     @PathParam("projectId") Long projectId,
                                     @HeaderParam("token") String token) {

        String ip = request.getRemoteAddr();

        logger.info("Received request to register activity from IP: " + ip);

        User user = userBean.getUserByToken(token);
        if (user == null) {
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        activityBean.registerActivityTypeMemberComment(projectId, user, input);

        return Response.status(Response.Status.CREATED).entity("Activity registered").build();
    }
}