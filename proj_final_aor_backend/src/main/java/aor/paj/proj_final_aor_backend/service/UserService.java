package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.dto.UserRegistration;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/users")
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @EJB
    UserBean userBean;


    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response registerUser(UserRegistration user, @Context HttpServletRequest request) {

        try {
            User registeredUser = userBean.registerUser(user.getEmail(), user.getPassword(), user.getConfirmPassword());

            if (registeredUser != null) {
                logger.info("IP Address: " + request.getRemoteAddr() + " - User registered: " + registeredUser.getEmail() + " at " + System.currentTimeMillis());
                return Response.status(Response.Status.CREATED).entity("User registered").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").build();
            }
        } catch (Exception e) {
            logger.error("IP Address: " + request.getRemoteAddr() + " - Failed to register user: " + user.getEmail() + " at " + System.currentTimeMillis() + " - " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to register user").build();
        }


    }


}
