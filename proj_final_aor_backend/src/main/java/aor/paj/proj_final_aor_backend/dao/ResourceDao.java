package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.ResourceEntity;
import aor.paj.proj_final_aor_backend.util.enums.ResourceType;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

/**
 * Stateless session bean that provides CRUD operations for ResourceEntity.
 */
@Stateless
public class ResourceDao extends AbstractDao<ResourceEntity>{

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ResourceDao() {
        super(ResourceEntity.class);
    }

    /**
     * Retrieves all resources from the database.
     * @return a list of all resources, or an empty list if no resources are found.
     */
    public List<ResourceEntity> findAllResources() {
        try {
            return em.createNamedQuery("Resource.findAllResources", ResourceEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Searches for resources based on a search string.
     * This method performs a database query to find resources that match the given search criteria. The search criteria
     * can be any string value that the resources might contain. If resources are found, they are returned as a list.
     * If no resources match the search criteria, an empty list is returned. This method handles the {@link NoResultException}
     * by returning an empty list, ensuring that the method caller receives a non-null result.
     *
     * @param search The search string used to query resources.
     * @return A list of {@link ResourceEntity} that match the search criteria, or an empty list if no matches are found.
     */
    public List<ResourceEntity> searchResources(String search) {
        try {
            return em.createNamedQuery("Resource.searchResources", ResourceEntity.class).setParameter("search", search)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all resources from the database, ordered in descending order.
     * @return a list of all resources, or an empty list if no resources are found.
     */
    public List<ResourceEntity> findAllResourcesOrderedDESC() {
        try {
            return em.createNamedQuery("Resource.findAllResourcesOrderedDESC", ResourceEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all resources from the database, ordered in ascending order by name.
     * @return a list of all resources, ordered in ascending order by name.
     * If no resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findAllResourcesOrderedByNameASC() {
        try {
            return em.createNamedQuery("Resource.findAllResourcesOrderedByNameASC", ResourceEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all resources from the database, ordered in descending order by name.
     * @return a list of all resources, ordered in descending order by name.
     * If no resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findAllResourcesOrderedByNameDESC() {
        try {
            return em.createNamedQuery("Resource.findAllResourcesOrderedByNameDESC", ResourceEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all resources from the database, ordered in ascending order by projects.
     * @return a list of all resources, ordered in ascending order by projects.
     * If no resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findAllResourcesOrderedByProjectsASC() {
        try {
            return em.createNamedQuery("Resource.findAllResourcesOrderedByProjectsASC", ResourceEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all resources from the database, ordered in descending order by projects.
     * @return a list of all resources, ordered in descending order by projects.
     * If no resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findAllResourcesOrderedByProjectsDESC() {
        try {
            return em.createNamedQuery("Resource.findAllResourcesOrderedByProjectsDESC", ResourceEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a resource by its ID.
     * @param id the ID of the resource.
     * @return the resource with the given ID, or null if no such resource is found.
     */
    public ResourceEntity findResourceById(Long id) {
        try {
            return (ResourceEntity) em.createNamedQuery("Resource.findResourceById").setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Retrieves a resource by its name.
     * @param name the name of the resource.
     * @return the resource with the given name, or null if no such resource is found.
     */
    public ResourceEntity findResourceByName(String name) {
        try {
            return (ResourceEntity) em.createNamedQuery("Resource.findResourceByName").setParameter("name", name)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Retrieves resources by their type.
     * @param type the type of the resources.
     * @return a list of resources with the given type, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesByType(ResourceType type) {
        try {
            return em.createNamedQuery("Resource.findResourcesByType", ResourceEntity.class).setParameter("type", type)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type, ordered in descending order.
     * @param type the type of the resources.
     * @return a list of resources with the given type, ordered in descending order.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeOrderedDESC(ResourceType type) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeOrderedDESC", ResourceEntity.class).setParameter("type", type)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type, ordered in ascending order by name.
     * @param type the type of the resources.
     * @return a list of resources with the given type, ordered in ascending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeOrderedByNameASC(ResourceType type) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeOrderedByNameASC", ResourceEntity.class).setParameter("type", type)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type, ordered in descending order by name.
     * @param type the type of the resources.
     * @return a list of resources with the given type, ordered in descending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeOrderedByNameDESC(ResourceType type) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeOrderedByNameDESC", ResourceEntity.class).setParameter("type", type)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type, ordered in ascending order by projects.
     * @param type the type of the resources.
     * @return a list of resources with the given type, ordered in ascending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeOrderedByProjectsASC(ResourceType type) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeOrderedByProjectsASC", ResourceEntity.class).setParameter("type", type)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type, ordered in descending order by projects.
     * @param type the type of the resources.
     * @return a list of resources with the given type, ordered in descending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeOrderedByProjectsDESC(ResourceType type) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeOrderedByProjectsDESC", ResourceEntity.class).setParameter("type", type)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand.
     * @param brand the brand of the resources.
     * @return a list of resources with the given brand, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesByBrand(String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrand", ResourceEntity.class).setParameter("brand", brand)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand, ordered in descending order.
     * @param brand the brand of the resources.
     * @return a list of resources with the given brand, ordered in descending order.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByBrandOrderedDESC(String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrandOrderedDESC", ResourceEntity.class).setParameter("brand", brand)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand, ordered in ascending order by name.
     * @param brand the brand of the resources.
     * @return a list of resources with the given brand, ordered in ascending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByBrandOrderedByNameASC(String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrandOrderedByNameASC", ResourceEntity.class).setParameter("brand", brand)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand, ordered in descending order by name.
     * @param brand the brand of the resources.
     * @return a list of resources with the given brand, ordered in descending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByBrandOrderedByNameDESC(String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrandOrderedByNameDESC", ResourceEntity.class).setParameter("brand", brand)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand, ordered in ascending order by projects.
     * @param brand the brand of the resources.
     * @return a list of resources with the given brand, ordered in ascending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByBrandOrderedByProjectsASC(String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrandOrderedByProjectsASC", ResourceEntity.class).setParameter("brand", brand)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand, ordered in descending order by projects.
     * @param brand the brand of the resources.
     * @return a list of resources with the given brand, ordered in descending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByBrandOrderedByProjectsDESC (String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrandOrderedByProjectsDESC", ResourceEntity.class).setParameter("brand", brand)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their supplier.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given supplier, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesBySupplier(long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesBySupplier", ResourceEntity.class).setParameter("supplierId", supplierId)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their supplier, ordered in descending order.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given supplier, ordered in descending order.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesBySupplierOrderedDESC(long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesBySupplierOrderedDESC", ResourceEntity.class).setParameter("supplierId", supplierId)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their supplier, ordered in ascending order by name.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given supplier, ordered in ascending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesBySupplierOrderedByNameASC(long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesBySupplierOrderedByNameASC", ResourceEntity.class).setParameter("supplierId", supplierId)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their supplier, ordered in descending order by name.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given supplier, ordered in descending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesBySupplierOrderedByNameDESC(long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesBySupplierOrderedByNameDESC", ResourceEntity.class).setParameter("supplierId", supplierId)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their supplier, ordered in ascending order by projects.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given supplier, ordered in ascending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesBySupplierOrderedByProjectsASC(long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesBySupplierOrderedByProjectsASC", ResourceEntity.class).setParameter("supplierId", supplierId)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their supplier, ordered in descending order by projects.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given supplier, ordered in descending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesBySupplierOrderedByProjectsDESC(long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesBySupplierOrderedByProjectsDESC", ResourceEntity.class).setParameter("supplierId", supplierId)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a resource by its source ID.
     * @param sourceId the source ID of the resource.
     * @return the resource with the given source ID, or null if no such resource is found.
     */
    public List<ResourceEntity> findResourcesBySourceId(String sourceId) {
        try {
            return em.createNamedQuery("Resource.findResourcesBySourceId", ResourceEntity.class).setParameter("sourceId", sourceId)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and brand.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @return a list of resources with the given type and brand, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrand(ResourceType type, String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrand", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and supplier.
     * @param type the type of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type and supplier, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesByTypeAndSupplier(ResourceType type, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndSupplier", ResourceEntity.class).setParameter("type", type)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand and supplier.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given brand and supplier, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesByBrandAndSupplier(String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrandAndSupplier", ResourceEntity.class).setParameter("brand", brand)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type, brand, and supplier.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type, brand, and supplier, or an empty list if no such resources are found.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrandAndSupplier(ResourceType type, String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrandAndSupplier", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and brand, ordered in descending order.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @return a list of resources with the given type and brand, ordered in descending order.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrandOrderedDESC(ResourceType type, String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrandOrderedDESC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and supplier, ordered in descending order.
     * @param type the type of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type and supplier, ordered in descending order.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndSupplierOrderedDESC(ResourceType type, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndSupplierOrderedDESC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand and supplier, ordered in descending order.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given brand and supplier, ordered in descending order.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByBrandAndSupplierOrderedDESC(String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrandAndSupplierOrderedDESC", ResourceEntity.class).setParameter("brand", brand)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type, brand, and supplier, ordered in descending order.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type, brand, and supplier, ordered in descending order.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrandAndSupplierOrderedDESC(ResourceType type, String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrandAndSupplierOrderedDESC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and brand, ordered in ascending order by name.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @return a list of resources with the given type and brand, ordered in ascending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrandOrderedByNameASC(ResourceType type, String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrandOrderedByNameASC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type, ordered in ascending order by name.
     * @param type the type of the resources.
     * @return a list of resources with the given type, ordered in ascending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrandOrderedByNameASC(ResourceType type) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrandOrderedByNameASC", ResourceEntity.class).setParameter("type", type)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and supplier, ordered in ascending order by name.
     * @param type the type of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type and supplier, ordered in ascending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndSupplierOrderedByNameASC(ResourceType type, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndSupplierOrderedByNameASC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand and supplier, ordered in ascending order by name.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given brand and supplier, ordered in ascending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByBrandAndSupplierOrderedByNameASC(String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrandAndSupplierOrderedByNameASC", ResourceEntity.class).setParameter("brand", brand)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type, brand, and supplier, ordered in ascending order by name.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type, brand, and supplier, ordered in ascending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrandAndSupplierOrderedByNameASC(ResourceType type, String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrandAndSupplierOrderedByNameASC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and brand, ordered in descending order by name.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @return a list of resources with the given type and brand, ordered in descending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrandOrderedByNameDESC(ResourceType type, String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrandOrderedByNameDESC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and supplier, ordered in descending order by name.
     * @param type the type of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type and supplier, ordered in descending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndSupplierOrderedByNameDESC(ResourceType type, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndSupplierOrderedByNameDESC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand and supplier, ordered in descending order by name.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given brand and supplier, ordered in descending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByBrandAndSupplierOrderedByNameDESC(String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrandAndSupplierOrderedByNameDESC", ResourceEntity.class).setParameter("brand", brand)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type, brand, and supplier, ordered in descending order by name.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type, brand, and supplier, ordered in descending order by name.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrandAndSupplierOrderedByNameDESC(ResourceType type, String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrandAndSupplierOrderedByNameDESC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and brand, ordered in ascending order by projects.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @return a list of resources with the given type and brand, ordered in ascending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrandOrderedByProjectsASC(ResourceType type, String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrandOrderedByProjectsASC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and supplier, ordered in ascending order by projects.
     * @param type the type of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type and supplier, ordered in ascending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndSupplierOrderedByProjectsASC(ResourceType type, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndSupplierOrderedByProjectsASC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand and supplier, ordered in ascending order by projects.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given brand and supplier, ordered in ascending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByBrandAndSupplierOrderedByProjectsASC(String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrandAndSupplierOrderedByProjectsASC", ResourceEntity.class).setParameter("brand", brand)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type, brand, and supplier, ordered in ascending order by projects.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type, brand, and supplier, ordered in ascending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrandAndSupplierOrderedByProjectsASC(ResourceType type, String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrandAndSupplierOrderedByProjectsASC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and brand, ordered in descending order by projects.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @return a list of resources with the given type and brand, ordered in descending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrandOrderedByProjectsDESC(ResourceType type, String brand) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrandOrderedByProjectsDESC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type and supplier, ordered in descending order by projects.
     * @param type the type of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type and supplier, ordered in descending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndSupplierOrderedByProjectsDESC(ResourceType type, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndSupplierOrderedByProjectsDESC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their brand and supplier, ordered in descending order by projects.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given brand and supplier, ordered in descending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByBrandAndSupplierOrderedByProjectsDESC(String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByBrandAndSupplierOrderedByProjectsDESC", ResourceEntity.class).setParameter("brand", brand)
                    .setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves resources by their type, brand, and supplier, ordered in descending order by projects.
     * @param type the type of the resources.
     * @param brand the brand of the resources.
     * @param supplierId the id of the supplier.
     * @return a list of resources with the given type, brand, and supplier, ordered in descending order by projects.
     * If no such resources are found, an empty list is returned.
     */
    public List<ResourceEntity> findResourcesByTypeAndBrandAndSupplierOrderedByProjectsDESC(ResourceType type, String brand, long supplierId) {
        try {
            return em.createNamedQuery("Resource.findResourcesByTypeAndBrandAndSupplierOrderedByProjectsDESC", ResourceEntity.class).setParameter("type", type)
                    .setParameter("brand", brand).setParameter("supplierId", supplierId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all unique brands of resources.
     * @return a list of all unique brands of resources.
     * If no brands are found, an empty list is returned.
     */
    public List<String> findAllBrands() {
        try {
            return em.createNamedQuery("Resource.findAllBrands").getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

}