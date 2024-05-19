package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.ResourceBean;
import aor.paj.proj_final_aor_backend.bean.SupplierBean;
import aor.paj.proj_final_aor_backend.dto.Resource;
import aor.paj.proj_final_aor_backend.dto.Supplier;
import aor.paj.proj_final_aor_backend.entity.ResourceEntity;
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

@Path("/resources")
public class ResourceService {

    private static final Logger logger = LogManager.getLogger(ResourceService.class);

    @EJB
    private ResourceBean resourceBean;

    @EJB
    private SupplierBean supplierBean;

    @Context
    private HttpServletRequest request;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerResource(Resource resource) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to register resource from IP: " + ip);

        for (Supplier supplier : resource.getSuppliers()) {
            if (!supplierBean.exists(supplier.getName())) {
                logger.error("Supplier does not exist: " + supplier.getName());
                return Response.status(Response.Status.BAD_REQUEST).entity("Supplier does not exist, please create supplier first").build();
            }
        }


        // Create the resource
        ResourceEntity createdResource = resourceBean.createResource(resource);

        // Check if the resource was created successfully
        if (createdResource != null) {
            logger.info("Resource registered successfully: " + resource.getName());
            return Response.status(Response.Status.CREATED).entity("Resource registered successfully").build();
        } else {
            logger.error("Failed to register resource: " + resource.getName());
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to register resource").build();
        }
    }




}
