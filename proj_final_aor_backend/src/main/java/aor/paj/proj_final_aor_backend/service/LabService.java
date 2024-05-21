package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.LabBean;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/labs")
public class LabService {

    private static final Logger logger = LogManager.getLogger(LabService.class);

    @EJB
    private LabBean labBean;

    @Context
    private HttpServletRequest request;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLabs() {
        String ip = request.getRemoteAddr();
        logger.info("Received request to retrieve all labs from IP: " + ip);

        // Retrieve all labs
        return Response.ok(labBean.getAllLabs()).build();
    }

}
