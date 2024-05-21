package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.ResourceBean;
import aor.paj.proj_final_aor_backend.bean.SupplierBean;
import aor.paj.proj_final_aor_backend.dto.Resource;
import aor.paj.proj_final_aor_backend.dto.Supplier;
import aor.paj.proj_final_aor_backend.entity.ResourceEntity;
import aor.paj.proj_final_aor_backend.util.enums.ResourceType;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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

        if (resourceBean.exists(resource.getName())) {
            logger.error("Resource already exists: " + resource.getName());
            return Response.status(Response.Status.BAD_REQUEST).entity("Resource with this name already exists").build();
        }

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

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllResources() {
        String ip = request.getRemoteAddr();
        logger.info("Received request to retrieve all resources from IP: " + ip);

        // Retrieve all resources
        return Response.status(Response.Status.OK).entity(resourceBean.getAllResources()).build();
    }

    @GET
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterResources(@QueryParam("type") ResourceType type,
                                    @QueryParam("brand") String brand,
                                    @QueryParam("supplier") String supplierName,
                                    @QueryParam("sourceId") String sourceId) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to filter resources from IP: " + ip);

        List<Resource> resources;
        if (type != null && brand != null && supplierName != null) {
            resources = resourceBean.findResourcesByTypeAndBrandAndSupplier(type, brand, supplierName);
        } else if (type != null && brand != null) {
            resources = resourceBean.findResourcesByTypeAndBrand(type, brand);
        } else if (type != null && supplierName != null) {
            resources = resourceBean.findResourcesByTypeAndSupplier(type, supplierName);
        } else if (brand != null && supplierName != null) {
            resources = resourceBean.findResourcesByBrandAndSupplier(brand, supplierName);
        } else if (type != null) {
            resources = resourceBean.findResourcesByType(type);
        } else if (brand != null) {
            resources = resourceBean.findResourcesByBrand(brand);
        } else if (supplierName != null) {
            resources = resourceBean.findResourcesBySupplier(supplierName);
        } else if (sourceId != null) {
            resources = resourceBean.findResourceBySourceId(sourceId);
        } else {
            resources = new ArrayList<>();
        }

        return Response.status(Response.Status.OK).entity(resources).build();
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateResource(@QueryParam("name") String name,
                                   @QueryParam("description") String description,
                                   @QueryParam("brand") String brand,
                                   @QueryParam("observation") String observation,
                                   @QueryParam("photo") String photo,
                                   @QueryParam("sourceId") String sourceId,
                                   @QueryParam("supplier") String supplierName) {
        if (supplierName != null) {
            resourceBean.addSupplierToResource(name, supplierName);
            return Response.status(Response.Status.OK).entity("Resource supplier updated successfully").build();
        }

        if (resourceBean.updateResource(name, description, brand, observation, photo, sourceId) != null) {
            return Response.status(Response.Status.OK).entity("Resource updated successfully").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update resource").build();
    }


}
