package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.ResourceBean;
import aor.paj.proj_final_aor_backend.bean.SupplierBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dto.Resource;
import aor.paj.proj_final_aor_backend.dto.ResourceSmallInfo;
import aor.paj.proj_final_aor_backend.dto.Supplier;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.entity.ResourceEntity;
import aor.paj.proj_final_aor_backend.entity.SupplierEntity;
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

    @EJB
    private UserBean userBean;

    @Context
    private HttpServletRequest request;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerResource(Resource resource,
                                     @HeaderParam("token") String token){

        String ip = request.getRemoteAddr();

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        logger.info("Received request to register resource from IP: " + ip);

        if (resourceBean.exists(resource.getName())) {
            logger.error("Resource already exists: " + resource.getName());
            return Response.status(Response.Status.BAD_REQUEST).entity("Resource with this name already exists").build();
        }

        for (Supplier supplier : resource.getSuppliers()) {
            if (!supplierBean.exists(supplier.getName())) {

                Supplier newSupplier = new Supplier();
                newSupplier.setName(resource.getSuppliers().get(0).getName());
                newSupplier.setContact(resource.getSuppliers().get(0).getContact());

                SupplierEntity createdSupplier = supplierBean.createSupplier(newSupplier);

                if (createdSupplier == null) {
                    logger.error("Failed to register supplier: " + supplier.getName());
                    return Response.status(Response.Status.BAD_REQUEST).entity("Failed to register supplier").build();
                }else {
                    logger.info("Supplier registered successfully: " + supplier.getName());
                }
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
    public Response getAllResources(@QueryParam("sort") String sort,
                                    @QueryParam("name") String name,
                                    @QueryParam("projects") String projects) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to retrieve all resources from IP: " + ip);

        // Retrieve all resources
        return Response.status(Response.Status.OK).entity(resourceBean.getAllResourcesInfoSorted(sort,name,projects)).build();
    }

    @GET
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterResources(@QueryParam("type") ResourceType type,
                                    @QueryParam("brand") String brand,
                                    @QueryParam("supplier") Long supplierId,
                                    @QueryParam("sort") String sort,
                                    @QueryParam("name") String name,
                                    @QueryParam("projects") String projects){
        String ip = request.getRemoteAddr();
        logger.info("Received request to filter resources from IP: " + ip);

        List<ResourceSmallInfo> resources;
        if (type != null && brand != null && supplierId != null) {
            resources = resourceBean.findResourcesByTypeAndBrandAndSupplierSorted(type, brand, supplierId, sort, name, projects);
        } else if (type != null && brand != null) {
            resources = resourceBean.findResourcesByTypeAndBrandSorted(type, brand, sort, name, projects);
        } else if (type != null && supplierId != null) {
            resources = resourceBean.findResourcesByTypeAndSupplierSorted(type, supplierId, sort, name, projects);
        } else if (brand != null && supplierId != null) {
            resources = resourceBean.findResourcesByBrandAndSupplier(brand, supplierId);
        } else if (type != null) {
            resources = resourceBean.findResourcesByTypeSorted(type, sort, name, projects);
        } else if (brand != null) {
            resources = resourceBean.findResourcesByBrandSorted(brand, sort, name, projects);
        } else if (supplierId != null) {
            resources = resourceBean.findResourcesBySupplierSorted(supplierId, sort, name, projects);
        } else {
            resources = new ArrayList<>();
        }

        return Response.status(Response.Status.OK).entity(resources).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResourceById(@PathParam("id") long id, @HeaderParam("token") String token) {
        String ip = request.getRemoteAddr();
        logger.info("Received request to retrieve resource with ID " + id + " from IP: " + ip);

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        // Retrieve the resource
        Resource resource = resourceBean.getResourceById(id);

        // Check if the resource was found
        if (resource != null) {
            return Response.status(Response.Status.OK).entity(resource).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Resource not found").build();
        }
    }

    @GET
    @Path("/brands")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBrands() {
        String ip = request.getRemoteAddr();
        logger.info("Received request to retrieve all brands from IP: " + ip);

        // Retrieve all brands
        return Response.status(Response.Status.OK).entity(resourceBean.getAllBrands()).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateResource(@PathParam("id") long id,
                                   @HeaderParam("token") String token,
                                   Resource resource) {

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (resourceBean.updateResource(id, resource.getDescription(), resource.getBrand(), resource.getObservation(), resource.getPhoto(), resource.getSourceId()) != null) {
            return Response.status(Response.Status.OK).entity("Resource updated successfully").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update resource").build();
    }

    @PUT
    @Path("/{id}/supplier/{supplierId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateResourceSupplier(@PathParam("id") Long resourceId,
                                           @PathParam("supplierId") Long supplierId,
                                           @HeaderParam("token") String token){

        User user = userBean.getUserByToken(token);
        if(user == null){
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        if (supplierId != null) {
            resourceBean.addSupplierToResource(resourceId, supplierId);
            return Response.status(Response.Status.OK).entity("Resource supplier updated successfully").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update resource supplier").build();
    }



}
