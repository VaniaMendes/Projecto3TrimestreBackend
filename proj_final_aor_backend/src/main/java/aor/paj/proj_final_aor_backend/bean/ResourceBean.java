package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ProjectResourceDao;
import aor.paj.proj_final_aor_backend.dao.ResourceDao;
import aor.paj.proj_final_aor_backend.dao.ResourceSupplierDao;
import aor.paj.proj_final_aor_backend.dto.Resource;
import aor.paj.proj_final_aor_backend.dto.ResourceSmallInfo;
import aor.paj.proj_final_aor_backend.dto.ResourceSmallInfoUser;
import aor.paj.proj_final_aor_backend.dto.Supplier;
import aor.paj.proj_final_aor_backend.entity.ResourceEntity;
import aor.paj.proj_final_aor_backend.entity.ResourceSupplierEntity;
import aor.paj.proj_final_aor_backend.entity.SupplierEntity;
import aor.paj.proj_final_aor_backend.util.enums.ResourceType;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The ResourceBean class provides methods for creating and managing resources.
 * It is a stateless EJB and implements Serializable for distributed computing.
 */
@Stateless
public class ResourceBean implements Serializable {

    // Logger for this class
    private static Logger logger = LogManager.getLogger(ResourceBean.class);

    // Injected ResourceDao EJB
    @EJB
    private ResourceDao resourceDao;

    @EJB
    private ProjectResourceDao projectResourceDao;

    @EJB
    private ResourceSupplierDao resourceSupplierDao;

    // Injected SupplierBean EJB
    @EJB
    private SupplierBean supplierBean;

    /**
     * Default constructor.
     */
    public ResourceBean() {
    }

    /**
     * Constructor that allows injection of a ResourceDao.
     *
     * @param resourceDao The ResourceDao to be used by this bean.
     */
    public ResourceBean (ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    public ResourceBean(ResourceDao resourceDao, SupplierBean supplierBean, Logger logger) {
        this.resourceDao = resourceDao;
        this.supplierBean = supplierBean;
        this.logger = logger;
    }

    /**
     * This method is used to create a new resource.
     * It first validates the resource, then converts it to a ResourceEntity and adds any suppliers.
     * If the resource is valid and all suppliers exist, it is persisted in the database.
     *
     * @param resource The Resource object to be created.
     * @return The created ResourceEntity, or null if the resource is invalid or a supplier does not exist.
     */
    public ResourceEntity createResource(Resource resource) {

        // Validate the resource
        if (isInvalidResource(resource)) {
            return null;
        }

        // Convert the resource to a ResourceEntity
        ResourceEntity resourceEntity = convertToEntity(resource);

        // Add any suppliers to the ResourceEntity
        for (Supplier supplier : resource.getSuppliers()) {
            SupplierEntity supplierEntity = supplierBean.findSupplierByName(supplier.getName());
            if (supplierEntity != null) {
                persistResourceSupplierConnection(resourceEntity, supplierEntity);
            } else {
                logger.error("Supplier does not exist: " + supplier.getName());
                return null;
            }
        }

        // Persist the ResourceEntity in the database
        resourceDao.persist(resourceEntity);
        return resourceEntity;
    }

    /**
     * This method is used to find a resource by its ID.
     * It first calls the findResourceById method of the resourceDao to fetch the ResourceEntity from the database.
     * If the ResourceEntity is not found, it logs an error and returns null.
     * If the ResourceEntity is found, it converts it to a Resource object using the convertToDTO method and returns it.
     *
     * @param id The ID of the resource to be found. It should be a valid Long.
     * @return The Resource object corresponding to the found ResourceEntity. If the ResourceEntity is not found, it returns null.
     */
    public Resource getResourceById(Long id) {
        ResourceEntity resourceEntity = resourceDao.findResourceById(id);
        if (resourceEntity == null) {
            logger.error("Resource with id '" + id + "' does not exist");
            return null;
        }
        return convertToDTO(resourceEntity);
    }


    /**
     * Fetches all resources from the database.
     *
     * @return A list of all resources, converted to DTOs.
     */
    public List<Resource> getAllResources() {
        return resourceDao.findAllResources().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    /**
     * Searches for resources based on a partial or full search term.
     * This method queries the database for resources that match the search criteria in their name or brand,
     * converts each matching ResourceEntity to a ResourceSmallInfoUser DTO, and collects them into a list.
     * The search is case-insensitive and allows partial matches due to the use of the LIKE operator in the query.
     *
     * @param search The search term used to find resources. It can be a partial or full name or brand of the resource.
     * @return A list of ResourceSmallInfoUser DTOs that match the search criteria. Returns an empty list if no matches are found.
     */
    public List<ResourceSmallInfoUser> searchResources(String search) {
        return resourceDao.searchResources(search).stream()
                .map(resourceEntity -> convertToDTOInfoUser(resourceEntity, null))
                .collect(Collectors.toList());
    }

    /**
     * This method retrieves all resources from the database and converts them into a list of ResourceSmallInfo objects.
     * For each resource, it also counts the number of projects that use the resource by calling the countProjectsFromResource method from the ProjectResourceDao.
     * The conversion of each resource into a ResourceSmallInfo object is done by the convertToDTOInfo method.
     *
     * @return A list of ResourceSmallInfo objects representing all resources in the database. Each ResourceSmallInfo object includes the number of projects that use the corresponding resource.
     */
    private List<ResourceSmallInfo> getAllResourcesInfo() {
        return resourceDao.findAllResources().stream()
                .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                .collect(Collectors.toList());
    }

    /**
     * This method retrieves all resources from the database and sorts them based on the provided parameters.
     * The method name is dynamically constructed based on the sort parameters and then invoked on the ResourceDao class.
     * If the constructed method name does not correspond to a method that actually exists in the ResourceDao class, a NoSuchMethodException will be thrown.
     *
     * @param sort The sort order for the resources. If "asc", resources are sorted in ascending order. If "desc", resources are sorted in descending order.
     * @param nameSort The sort order for the resources based on name. If "asc", resources are sorted in ascending order. If "desc", resources are sorted in descending order.
     * @param projectsSort The sort order for the resources based on projects. If "asc", resources are sorted in ascending order. If "desc", resources are sorted in descending order.
     * @return A list of resources sorted according to the provided parameters, converted to ResourceSmallInfo DTOs. If an exception occurs, an empty list is returned.
     */
    public List<ResourceSmallInfo> getAllResourcesInfoSorted(String sort, String nameSort, String projectsSort) {
        String methodName = "";
        if (sort != null) {
            methodName = sort.equals("asc") ? "findAllResources" : "findAllResourcesOrderedDESC";
        } else if (nameSort != null) {
            methodName = nameSort.equals("asc") ? "findAllResourcesOrderedByNameASC" : "findAllResourcesOrderedByNameDESC";
        } else if (projectsSort != null) {
            methodName = projectsSort.equals("asc") ? "findAllResourcesOrderedByProjectsASC" : "findAllResourcesOrderedByProjectsDESC";
        }

        try {
            Method method = resourceDao.getClass().getMethod(methodName);
            List<ResourceEntity> resourceEntities = (List<ResourceEntity>) method.invoke(resourceDao);
            return resourceEntities.stream()
                    .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                    .collect(Collectors.toList());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    /**
     * Fetches resources from the database that match the given type, brand, and supplier name.
     *
     * @param type The type of the resources to fetch.
     * @param brand The brand of the resources to fetch.
     * @param supplierId The id of the supplier of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    private List<ResourceSmallInfo> findResourcesByTypeAndBrandAndSupplier(ResourceType type, String brand, long supplierId) {
        return resourceDao.findResourcesByTypeAndBrandAndSupplier(type, brand, supplierId).stream()
                .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                .collect(Collectors.toList());
    }

    public List<ResourceSmallInfo> findResourcesByTypeAndBrandAndSupplierSorted(ResourceType type, String brand, long supplierId, String sort, String nameSort, String projectsSort) {
        String methodName = "";
        if (sort != null) {
            methodName = sort.equals("asc") ? "findResourcesByTypeAndBrandAndSupplier" : "findResourcesByTypeAndBrandAndSupplierOrderedDESC";
        } else if (nameSort != null) {
            methodName = nameSort.equals("asc") ? "findResourcesByTypeAndBrandAndSupplierOrderedByNameASC" : "findResourcesByTypeAndBrandAndSupplierOrderedByNameDESC";
        } else if (projectsSort != null) {
            methodName = projectsSort.equals("asc") ? "findResourcesByTypeAndBrandAndSupplierOrderedByProjectsASC" : "findResourcesByTypeAndBrandAndSupplierOrderedByProjectsDESC";
        }

        try {
            Method method = resourceDao.getClass().getMethod (methodName, ResourceType.class, String.class, long.class);
            List<ResourceEntity> resourceEntities = (List<ResourceEntity>) method.invoke(resourceDao, type, brand, supplierId);
            return resourceEntities.stream()
                    .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                    .collect(Collectors.toList());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    /**
     * Fetches resources from the database that match the given type and brand.
     *
     * @param type The type of the resources to fetch.
     * @param brand The brand of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    private List<ResourceSmallInfo> findResourcesByTypeAndBrand(ResourceType type, String brand) {
        return resourceDao.findResourcesByTypeAndBrand(type, brand).stream()
                .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                .collect(Collectors.toList());
    }

    public List<ResourceSmallInfo> findResourcesByTypeAndBrandSorted(ResourceType type, String brand, String sort, String nameSort, String projectsSort) {
        String methodName = "";
        if (sort != null) {
            methodName = sort.equals("asc") ? "findResourcesByTypeAndBrand" : "findResourcesByTypeAndBrandOrderedDESC";
        } else if (nameSort != null) {
            methodName = nameSort.equals("asc") ? "findResourcesByTypeAndBrandOrderedByNameASC" : "findResourcesByTypeAndBrandOrderedByNameDESC";
        } else if (projectsSort != null) {
            methodName = projectsSort.equals("asc") ? "findResourcesByTypeAndBrandOrderedByProjectsASC" : "findResourcesByTypeAndBrandOrderedByProjectsDESC";
        }

        try {
            Method method = resourceDao.getClass().getMethod (methodName, ResourceType.class, String.class);
            List<ResourceEntity> resourceEntities = (List<ResourceEntity>) method.invoke(resourceDao, type, brand);
            return resourceEntities.stream()
                    .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                    .collect(Collectors.toList());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    /**
     * Fetches resources from the database that match the given type and supplier name.
     *
     * @param type The type of the resources to fetch.
     * @param supplierId The id of the supplier of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    public List<ResourceSmallInfo> findResourcesByTypeAndSupplier(ResourceType type, long supplierId) {
        return resourceDao.findResourcesByTypeAndSupplier(type, supplierId).stream()
                .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                .collect(Collectors.toList());
    }

    public List<ResourceSmallInfo> findResourcesByTypeAndSupplierSorted(ResourceType type, long supplierId, String sort, String nameSort, String projectsSort) {
        String methodName = "";
        if (sort != null) {
            methodName = sort.equals("asc") ? "findResourcesByTypeAndSupplier" : "findResourcesByTypeAndSupplierOrderedDESC";
        } else if (nameSort != null) {
            methodName = nameSort.equals("asc") ? "findResourcesByTypeAndSupplierOrderedByNameASC" : "findResourcesByTypeAndSupplierOrderedByNameDESC";
        } else if (projectsSort != null) {
            methodName = projectsSort.equals("asc") ? "findResourcesByTypeAndSupplierOrderedByProjectsASC" : "findResourcesByTypeAndSupplierOrderedByProjectsDESC";
        }

        try {
            Method method = resourceDao.getClass().getMethod (methodName, ResourceType.class, long.class);
            List<ResourceEntity> resourceEntities = (List<ResourceEntity>) method.invoke(resourceDao, type, supplierId);
            return resourceEntities.stream()
                    .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                    .collect(Collectors.toList());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    /**
     * Fetches resources from the database that match the given brand and supplier name.
     *
     * @param brand The brand of the resources to fetch.
     * @param supplierId The name of the supplier of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    public List<ResourceSmallInfo> findResourcesByBrandAndSupplier(String brand, long supplierId) {
        return resourceDao.findResourcesByBrandAndSupplier(brand, supplierId).stream()
                .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                .collect(Collectors.toList());
    }

    /**
     * Fetches resources from the database that match the given type.
     *
     * @param type The type of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    private List<ResourceSmallInfo> findResourcesByType(ResourceType type) {
        return resourceDao.findResourcesByType(type).stream()
                .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                .collect(Collectors.toList());
    }

    /**
     * This method is used to fetch resources from the database based on the resource type and sort them based on the provided parameters.
     * The method name is dynamically constructed based on the sort parameters and then invoked on the ResourceDao class.
     * If the constructed method name does not correspond to a method that actually exists in the ResourceDao class, a NoSuchMethodException will be thrown.
     *
     * @param type The type of the resources to fetch.
     * @param sort The sort order for the resources. If "asc", resources are sorted in ascending order. If "desc", resources are sorted in descending order.
     * @param nameSort The sort order for the resources based on name. If "asc", resources are sorted in ascending order. If "desc", resources are sorted in descending order.
     * @param projectsSort The sort order for the resources based on projects. If "asc", resources are sorted in ascending order. If "desc", resources are sorted in descending order.
     * @return A list of resources matching the type and sorted according to the provided parameters, converted to ResourceSmallInfo DTOs. If an exception occurs, an empty list is returned.
     */
    public List<ResourceSmallInfo> findResourcesByTypeSorted(ResourceType type, String sort, String nameSort, String projectsSort) {
        String methodName = "";
        if (sort != null) {
            methodName = sort.equals("asc") ? "findResourcesByType" : "findResourcesByTypeOrderedDESC";
        } else if (nameSort != null) {
            methodName = nameSort.equals("asc") ? "findResourcesByTypeOrderedByNameASC" : "findResourcesByTypeOrderedByNameDESC";
        } else if (projectsSort != null) {
            methodName = projectsSort.equals("asc") ? "findResourcesByTypeOrderedByProjectsASC" : "findResourcesByTypeOrderedByProjectsDESC";
        }

        try {
            Method method = resourceDao.getClass().getMethod(methodName, ResourceType.class);
            List<ResourceEntity> resourceEntities = (List<ResourceEntity>) method.invoke(resourceDao, type);
            return resourceEntities.stream()
                    .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                    .collect(Collectors.toList());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    /**
     * Fetches resources from the database that match the given brand.
     *
     * @param brand The brand of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    private List<ResourceSmallInfo> findResourcesByBrand(String brand) {
        return resourceDao.findResourcesByBrand(brand).stream()
                .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                .collect(Collectors.toList());
    }

    /**
     * This method is used to fetch resources from the database based on the brand and sort them based on the provided parameters.
     * The method name is dynamically constructed based on the sort parameters and then invoked on the ResourceDao class.
     * If the constructed method name does not correspond to a method that actually exists in the ResourceDao class, a NoSuchMethodException will be thrown.
     *
     * @param brand The brand of the resources to fetch.
     * @param sort The sort order for the resources. If "asc", resources are sorted in ascending order. If "desc", resources are sorted in descending order.
     * @param nameSort The sort order for the resources based on name. If "asc", resources are sorted in ascending order. If "desc", resources are sorted in descending order.
     * @param projectsSort The sort order for the resources based on projects. If "asc", resources are sorted in ascending order. If "desc", resources are sorted in descending order.
     * @return A list of resources matching the brand and sorted according to the provided parameters, converted to ResourceSmallInfo DTOs. If an exception occurs, an empty list is returned.
     */
    public List<ResourceSmallInfo> findResourcesByBrandSorted(String brand, String sort, String nameSort, String projectsSort) {
        String methodName = "";
        if (sort != null) {
            methodName = sort.equals("asc") ? "findResourcesByBrand" : "findResourcesByBrandOrderedDESC";
        } else if (nameSort != null) {
            methodName = nameSort.equals("asc") ? "findResourcesByBrandOrderedByNameASC" : "findResourcesByBrandOrderedByNameDESC";
        } else if (projectsSort != null) {
            methodName = projectsSort.equals("asc") ? "findResourcesByBrandOrderedByProjectsASC" : "findResourcesByBrandOrderedByProjectsDESC";
        }

        try {
            Method method = resourceDao.getClass().getMethod(methodName, String.class);
            List<ResourceEntity> resourceEntities = (List<ResourceEntity>) method.invoke(resourceDao, brand);
            return resourceEntities.stream()
                    .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                    .collect(Collectors.toList());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    /**
     * Fetches resources from the database that match the given supplier name.
     *
     * @param supplierId The id of the supplier of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    private List<ResourceSmallInfo> findResourcesBySupplier(long supplierId) {
        return resourceDao.findResourcesBySupplier(supplierId).stream()
                .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                .collect(Collectors.toList());
    }


    /**
     * This method is used to fetch resources from the database based on the supplier ID and sort them based on the provided parameters.
     * The method name is dynamically constructed based on the sort parameters and then invoked on the ResourceDao class.
     * If the constructed method name does not correspond to a method that actually exists in the ResourceDao class, a NoSuchMethodException will be thrown.
     *
     * @param supplierId The ID of the supplier of the resources to fetch.
     * @param sort The sort order for the resources. If "asc", resources are sorted in ascending order. If "desc", resources are sorted in descending order.
     * @param nameSort The sort order for the resources based on name. If "asc", resources are sorted in ascending order. If "desc", resources are sorted in descending order.
     * @param projectsSort The sort order for the resources based on projects. If "asc", resources are sorted in ascending order. If "desc", resources are sorted in descending order.
     * @return A list of resources matching the supplier ID and sorted according to the provided parameters, converted to ResourceSmallInfo DTOs. If an exception occurs, an empty list is returned.
     */
    public List<ResourceSmallInfo> findResourcesBySupplierSorted(long supplierId, String sort, String nameSort, String projectsSort) {
        String methodName = "";
        if (sort != null) {
            methodName = sort.equals("asc") ? "findResourcesBySupplier" : "findResourcesBySupplierOrderedDESC";
        } else if (nameSort != null) {
            methodName = nameSort.equals("asc") ? "findResourcesBySupplierOrderedByNameASC" : "findResourcesBySupplierOrderedByNameDESC";
        } else if (projectsSort != null) {
            methodName = projectsSort.equals("asc") ? "findResourcesBySupplierOrderedByProjectsASC" : "findResourcesBySupplierOrderedByProjectsDESC";
        }

        try {
            Method method = resourceDao.getClass().getMethod(methodName, long.class);
            List<ResourceEntity> resourceEntities = (List<ResourceEntity>) method.invoke(resourceDao, supplierId);
            return resourceEntities.stream()
                    .map(resourceEntity -> convertToDTOInfo(resourceEntity, projectResourceDao.countProjectsFromResource(resourceEntity.getId())))
                    .collect(Collectors.toList());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    /**
     * Fetches resources from the database that match the given source ID.
     *
     * @param sourceId The source ID of the resources to fetch.
     * @return A list of matching resources, converted to DTOs.
     */
    public List<Resource> findResourceBySourceId(String sourceId) {
        return resourceDao.findResourcesBySourceId(sourceId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    /**
     * This method is used to validate a Resource object.
     * It checks if any of the required fields in the Resource object are null or invalid.
     *
     * @param resource The Resource object to be validated.
     * @return true if the Resource object is invalid, false otherwise.
     */
    private boolean isInvalidResource(Resource resource) {
        if (resource == null) {
            return true;
        }
        if (resource.getName() == null || resource.getName().isEmpty()) {
            return true;
        }
        if (resource.getDescription() == null || resource.getDescription().isEmpty()) {
            return true;
        }
        if (resource.getType() == null) {
            return true;
        }
        if (resource.getBrand() == null || resource.getBrand().isEmpty()) {
            return true;
        }
        if (resource.getSourceId() == null || resource.getSourceId().isEmpty()) {
            return true;
        }
        if (resource.getSuppliers() == null || resource.getSuppliers().isEmpty()) {
            return true;
        }
        for (Supplier supplier : resource.getSuppliers()) {
            if (supplier.getName() == null || supplier.getName().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a resource with the given name exists in the database.
     *
     * @param name The name of the resource.
     * @return true if the resource exists, false otherwise.
     */
    public boolean exists(String name) {
        return resourceDao.findResourceByName(name) != null;
    }

    public ResourceSmallInfoUser findResourceById(Long id, int quantity) {
        ResourceEntity resourceEntity = resourceDao.findResourceById(id);
        if (resourceEntity == null) {
            logger.error("Resource with id '" + id + "' does not exist");
            return null;
        }
        return convertToDTOInfoUser(resourceEntity, quantity);
    }


    /**
     * Updates the properties of a resource in the database.
     * The method first finds the resource in the database using the name from the provided parameters.
     * If the resource is found, it updates its properties with the non-null values from the parameters.
     * Finally, it persists the updated resource back to the database.
     *
     * @param id The id of the resource to be updated.
     * @param description The new description of the resource. If null, the description is not updated.
     * @param brand The new brand of the resource. If null, the brand is not updated.
     * @param observation The new observation of the resource. If null, the observation is not updated.
     * @param photo The new photo of the resource. If null, the photo is not updated.
     * @param sourceId The new sourceId of the resource. If null, the sourceId is not updated.
     * @return The updated ResourceEntity, or null if the resource does not exist.
     */
    public ResourceEntity updateResource(Long id, String description, String brand, String observation, String photo, String sourceId) {
        ResourceEntity resourceEntity = resourceDao.findResourceById(id);
        if (resourceEntity == null) {
            logger.error("Resource with id '" + id + "' does not exist");
            return null;
        }

        if (description != null) {
            resourceEntity.setDescription(description);
        }

        if (brand != null) {
            resourceEntity.setBrand(brand);
        }

        if (observation != null) {
            resourceEntity.setObservation(observation);
        }

        if (photo != null) {
            resourceEntity.setPhoto(photo);
        }

        if (sourceId != null) {
            resourceEntity.setSourceId(sourceId);
        }

        resourceEntity.setUpdatedAt(LocalDateTime.now());
        resourceDao.merge(resourceEntity);
        return resourceEntity;
    }

    /**
     * Adds a supplier to a resource in the database.
     * The method first finds the resource and the supplier in the database using the name from the provided Resource object and the supplier name.
     * If both the resource and the supplier are found, it adds the supplier to the resource's suppliers set.
     * Finally, it persists the updated resource back to the database.
     *
     * @param resourceId The ID of the Resource object to which the supplier should be added.
     * @param supplierId The ID of the supplier to be added.
     * @return The updated ResourceEntity, or null if the resource or the supplier does not exist.
     */
    public boolean addSupplierToResource(Long resourceId, Long supplierId) {
        ResourceEntity resourceEntity = resourceDao.findResourceById(resourceId);
        if (resourceEntity == null) {
            logger.error("Resource with id '" + resourceId + "' does not exist");
            return false;
        }

        SupplierEntity supplierEntity = supplierBean.findSupplierById(supplierId);
        if (supplierEntity == null) {
            logger.error("Supplier does not exist: " + supplierId);
            return false;
        }

        persistResourceSupplierConnection(resourceEntity, supplierEntity);

        resourceEntity.setUpdatedAt(LocalDateTime.now());
        resourceDao.merge(resourceEntity);
        return true;
    }

    /**
     * This method is used to create a new ResourceSupplierEntity object and persist it in the database.
     * It sets the provided ResourceEntity and SupplierEntity objects to the new ResourceSupplierEntity object,
     * sets the active status to true, and then persists the new ResourceSupplierEntity object in the database.
     *
     * @param resourceEntity The ResourceEntity object to be set in the new ResourceSupplierEntity object.
     * @param supplierEntity The SupplierEntity object to be set in the new ResourceSupplierEntity object.
     */
    private void persistResourceSupplierConnection(ResourceEntity resourceEntity, SupplierEntity supplierEntity) {
        ResourceSupplierEntity resourceSupplier = new ResourceSupplierEntity();
        resourceSupplier.setResource(resourceEntity);
        resourceSupplier.setSupplier(supplierEntity);
        resourceSupplier.setActiveStatus(true);

        resourceSupplierDao.persist(resourceSupplier);
    }

    public List<String> getAllBrands() {
        return resourceDao.findAllBrands();
    }

    /**
     * This method is used to convert a Resource object into a ResourceEntity object.
     * It copies the properties of the Resource object into a new ResourceEntity object.
     *
     * @param resource The Resource object to be converted.
     * @return The newly created ResourceEntity object.
     */
    private ResourceEntity convertToEntity(Resource resource) {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setName(resource.getName());
        resourceEntity.setDescription(resource.getDescription());
        resourceEntity.setType(resource.getType());
        resourceEntity.setBrand(resource.getBrand());
        resourceEntity.setObservation(resource.getObservation());
        resourceEntity.setPhoto(resource.getPhoto());
        resourceEntity.setSourceId(resource.getSourceId());
        return resourceEntity;
    }

    /**
     * This method is used to convert a ResourceEntity object into a Resource object.
     * It copies the properties of the ResourceEntity object into a new Resource object.
     * If the ResourceEntity object has any suppliers, it also adds them to the Resource object.
     *
     * @param resourceEntity The ResourceEntity object to be converted.
     * @return The newly created Resource object.
     */
    private Resource convertToDTO(ResourceEntity resourceEntity) {
        Resource resource = new Resource();
        resource.setId(resourceEntity.getId());
        resource.setName(resourceEntity.getName());
        resource.setDescription(resourceEntity.getDescription());
        resource.setType(resourceEntity.getType());
        resource.setBrand(resourceEntity.getBrand());
        resource.setObservation(resourceEntity.getObservation());
        resource.setPhoto(resourceEntity.getPhoto());
        resource.setCreatedAt(resourceEntity.getCreatedAt());
        resource.setUpdatedAt(resourceEntity.getUpdatedAt());
        resource.setSourceId(resourceEntity.getSourceId());
        if (!resourceEntity.getSuppliers().isEmpty()){
            resource.setSuppliers(resourceEntity.getSuppliers().stream()
                    .map(ResourceSupplierEntity::getSupplier)
                    .map(supplierBean::convertToDTO)
                    .collect(Collectors.toList()));
        }
        return resource;
    }

    /**
     * Converts a ResourceEntity object into a ResourceSmallInfoUser DTO.
     * This method is primarily used for creating a simplified data transfer object
     * that contains essential information about a resource, including its quantity,
     * which is not stored within the ResourceEntity itself but is provided externally.
     *
     * @param resourceEntity The ResourceEntity to convert.
     * @param quantity The quantity of the resource, which may come from an external source or calculation.
     * @return A ResourceSmallInfoUser DTO containing the resource's ID, name, brand, quantity, and type.
     */
    private ResourceSmallInfoUser convertToDTOInfoUser(ResourceEntity resourceEntity, Integer quantity) {
        ResourceSmallInfoUser r = new ResourceSmallInfoUser();
        r.setId(resourceEntity.getId());
        r.setName(resourceEntity.getName());
        r.setBrand(resourceEntity.getBrand());
        r.setQuantity(quantity);
        r.setType(resourceEntity.getType());
        return r;
    }

    /**
     * Converts a ResourceEntity object into a ResourceSmallInfo DTO.
     * This method is used for creating a data transfer object that encapsulates
     * the basic information of a resource along with the number of projects it is associated with.
     * The number of projects is not stored within the ResourceEntity but is provided externally,
     * typically calculated based on relationships defined in the database.
     *
     * @param resourceEntity The ResourceEntity to convert.
     * @param numberProjects The number of projects associated with the resource, provided externally.
     * @return A ResourceSmallInfo DTO containing the resource's ID, name, brand, photo, type, and the number of associated projects.
     */
    private ResourceSmallInfo convertToDTOInfo(ResourceEntity resourceEntity, Long numberProjects) {
        ResourceSmallInfo r = new ResourceSmallInfo();
        r.setId(resourceEntity.getId());
        r.setName(resourceEntity.getName());
        r.setBrand(resourceEntity.getBrand());
        r.setPhoto(resourceEntity.getPhoto());
        r.setType(resourceEntity.getType());
        r.setProjectsNumber(numberProjects);
        return r;
    }

}
