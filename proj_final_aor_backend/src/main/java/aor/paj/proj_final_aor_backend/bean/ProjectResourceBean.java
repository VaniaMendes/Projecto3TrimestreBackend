package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ProjectResourceDao;
import aor.paj.proj_final_aor_backend.dto.ResourceSmallInfoUser;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import aor.paj.proj_final_aor_backend.entity.ProjectResourceEntity;
import aor.paj.proj_final_aor_backend.entity.ResourceEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stateless session bean for managing project resources.
 */
@Stateless
public class ProjectResourceBean implements Serializable {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(ProjectResourceBean.class);

    /**
     * DAO for project resources.
     */
    @EJB
    private ProjectResourceDao projectResourceDao;

    @EJB
    private ResourceBean resourceBean;
    @EJB
    private ProjectBean projectBean;

    /**
     * Default constructor.
     */
    public ProjectResourceBean() {
    }

    /**
     * Constructor with project resource DAO.
     *
     * @param projectResourceDao the project resource DAO
     */
    public ProjectResourceBean(ProjectResourceDao projectResourceDao) {
        this.projectResourceDao = projectResourceDao;
    }

    /**
     * Checks if a resource exists in a project.
     *
     * @param projectEntity  the project entity
     * @param resourceEntity the resource entity
     * @return true if the resource exists in the project, false otherwise
     */
    public boolean exists(ProjectEntity projectEntity, ResourceEntity resourceEntity) {
        return projectResourceDao.findResourceFromProject(projectEntity.getId(), resourceEntity.getId()) != null;
    }

    /**
     * Merges a project resource connection.
     *
     * @param projectEntity  the project entity
     * @param resourceEntity the resource entity
     * @param quantity       the quantity of the resource
     */
    public void mergeProjectResourceConnection(ProjectEntity projectEntity, ResourceEntity resourceEntity, int quantity) {
        ProjectResourceEntity projectResourceEntity = projectResourceDao.findResourceFromProject(projectEntity.getId(), resourceEntity.getId());

        if (projectResourceEntity == null) {
            logger.error("ProjectResourceEntity not found.");
            return;
        }

        projectResourceEntity.setQuantity(projectResourceEntity.getQuantity() + quantity);

        projectResourceDao.merge(projectResourceEntity);
    }

    /**
     * Persists a project resource connection.
     *
     * @param projectEntity  the project entity
     * @param resourceEntity the resource entity
     * @param quantity       the quantity of the resource
     */
    public void persistProjectResourceConnection(ProjectEntity projectEntity, ResourceEntity resourceEntity, int quantity) {

        if (quantity <= 0) {
            logger.error("Quantity is less than or equal to 0.");
            return;
        }

        ProjectResourceEntity projectResourceEntity = new ProjectResourceEntity();
        projectResourceEntity.setProject(projectEntity);
        projectResourceEntity.setResource(resourceEntity);
        projectResourceEntity.setQuantity(quantity);

        projectResourceDao.persist(projectResourceEntity);
    }

    public List<ResourceSmallInfoUser> getAllResourcesFromProject(long projectId) {
        ProjectEntity projectEntity = projectBean.findProject(projectId);

        return projectResourceDao.findAllResourcesFromProject(projectEntity.getId())
                .stream()
                .map(projectResourceEntity -> {
                    ResourceEntity resourceEntity = projectResourceEntity.getResource();
                    int quantity = projectResourceEntity.getQuantity();
                    return resourceBean.findResourceById(resourceEntity.getId(), quantity);
                })
                .collect(Collectors.toList());
    }

}