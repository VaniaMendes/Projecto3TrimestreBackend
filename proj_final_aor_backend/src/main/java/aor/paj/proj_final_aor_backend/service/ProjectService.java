package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.ProjectBean;
import aor.paj.proj_final_aor_backend.dto.Project;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProject(Project project) {
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

}
