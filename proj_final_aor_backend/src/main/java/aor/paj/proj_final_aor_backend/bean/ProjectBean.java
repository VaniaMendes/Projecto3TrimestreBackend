package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ProjectDao;
import aor.paj.proj_final_aor_backend.dao.ResourceDao;
import aor.paj.proj_final_aor_backend.dao.SkillDao;
import aor.paj.proj_final_aor_backend.dto.Project;
import aor.paj.proj_final_aor_backend.entity.LabEntity;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import aor.paj.proj_final_aor_backend.entity.ResourceEntity;
import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This class is a Stateless EJB that manages operations related to Project entities.
 * It implements Serializable interface to support serialization.
 */
@Stateless
public class ProjectBean implements Serializable {

    // Logger instance for logging events, info, errors etc.
    private static final Logger logger = LogManager.getLogger(ProjectBean.class);

    // EJB reference for ProjectDao to perform operations related to Project entities.
    @EJB
    private ProjectDao projectDao;

    // EJB reference for ResourceDao to perform operations related to Resource entities.
    @EJB
    private ResourceDao resourceDao;

    // EJB reference for SkillDao to perform operations related to Skill entities.
    // This bean is used to interact with the database for CRUD operations on Skill entities.
    @EJB
    private SkillDao skillDao;

    // EJB reference for ProjectSkillBean to perform operations related to ProjectSkill entities.
    // This bean is used to manage the relationship between Project and Skill entities,
    // such as adding a skill to a project or remove a skill from a project.
    @EJB
    private ProjectSkillBean projectSkillBean;

    // EJB reference for LabBean to perform operations related to Lab entities.
    @EJB
    private LabBean labBean;

    // EJB reference for ProjectResourceBean to perform operations related to ProjectResource entities.
    @EJB
    private ProjectResourceBean projectResourceBean;

    // Default constructor
    public ProjectBean() {
    }

    // Constructor with ProjectDao as parameter
    public ProjectBean(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    /**
     * This method creates a new Project entity.
     * It first validates the project, then sets its stateId, converts it to an entity,
     * fetches the associated LabEntity, sets it to the ProjectEntity and finally persists it.
     * @param project The Project DTO to be converted to an entity and persisted.
     * @return The persisted ProjectEntity, or null if the project is invalid or the associated Lab does not exist.
     */
    public ProjectEntity createProject(Project project) {
        if (isInvalidProject(project)) {
            return null;
        }

        project.setStateId(100);

        ProjectEntity projectEntity = convertToEntity(project);

        // Fetch the existing LabEntity
        logger.info("Project lab: " + project.getLab().getName());
        LabEntity labEntity = labBean.findLabByName(String.valueOf(project.getLab().getName()));
        logger.info("LabEntity: " + labEntity);
        if (labEntity == null) {
            logger.error("Lab does not exist: " + project.getLab().getName());
            return null;
        }

        // Set the existing LabEntity to the ProjectEntity
        projectEntity.setLab(labEntity);

        projectDao.persist(projectEntity);
        return projectEntity;
    }

    /**
     * This method validates a Project.
     * It checks if the project is null, if its name, description, keywords are null or empty,
     * if its maxMembers is less than or equal to 0, and if its lab is null.
     * @param project The Project to be validated.
     * @return true if the project is invalid, false otherwise.
     */
    private boolean isInvalidProject(Project project) {
        if (project == null) {
            logger.error("Project is null.");
            return true;
        }

        if (project.getName() == null || project.getName().isEmpty()) {
            logger.error("Project name is null or empty.");
            return true;
        }

        if (project.getDescription() == null || project.getDescription().isEmpty()) {
            logger.error("Project description is null or empty.");
            return true;
        }

        if (project.getKeywords() == null || project.getKeywords().isEmpty()) {
            logger.error("Project keywords are null or empty.");
            return true;
        }

        if (project.getMaxMembers() <= 0) {
            logger.error("Project max members is less than or equal to 0.");
            return true;
        }

        if (project.getLab() == null) {
            logger.error("Project lab is null.");
            return true;
        }

        return false;
    }

    /**
     * Updates the state of a project.
     * @param id The id of the project to be updated.
     * @param stateId The new state id to be set.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateState(long id, int stateId) {
        ProjectEntity projectEntity = projectDao.findProjectById(id);
        if (projectEntity == null || !isValidStateId(stateId)) {
            return false;
        }
        projectEntity.setStateId(stateId);
        projectEntity.setUpdatedAt(LocalDateTime.now());
        projectDao.merge(projectEntity);
        return true;
    }

    /**
     * Updates the description of a project.
     * @param id The id of the project to be updated.
     * @param description The new description to be set.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateDescription(long id, String description) {
        ProjectEntity projectEntity = projectDao.findProjectById(id);
        if (projectEntity == null || description == null || description.isEmpty()) {
            return false;
        }
        projectEntity.setDescription(description);
        projectEntity.setUpdatedAt(LocalDateTime.now());
        projectDao.merge(projectEntity);
        return true;
    }

    /**
     * Adds a resource to a project.
     * @param projectId The id of the project to which the resource will be added.
     * @param resourceId The id of the resource to be added.
     * @param quantity The quantity of the resource to be added.
     * @return true if the addition was successful, false otherwise.
     */
    public boolean addResource(Long projectId, Long resourceId, int quantity) {
        ProjectEntity projectEntity = projectDao.findProjectById(projectId);
        if (projectEntity == null) {
            logger.error("Project does not exist: " + projectId);
            return false;
        }

        ResourceEntity resourceEntity = resourceDao.findResourceById(resourceId);
        if (resourceEntity == null) {
            logger.error("Resource does not exist: " + resourceId);
            return false;
        }

        if (projectResourceBean.exists(projectEntity, resourceEntity)) {
            projectResourceBean.mergeProjectResourceConnection(projectEntity, resourceEntity, quantity);
            logger.info("Resource already exists in project.");
        }else{
            projectResourceBean.persistProjectResourceConnection(projectEntity, resourceEntity, quantity);
            logger.info("Resource does not exist in project.");
        }

        projectEntity.setUpdatedAt(LocalDateTime.now());
        projectDao.merge(projectEntity);
        return true;
    }

    /**
     * Adds a skill to a project.
     *
     * This method first retrieves the ProjectEntity and SkillEntity from the database using the provided IDs.
     * If either entity does not exist, it logs an error and returns false.
     * If both entities exist, it adds the skill to the project using the ProjectSkillBean and returns true.
     *
     * @param projectId The ID of the project to which the skill will be added.
     * @param skillId The ID of the skill to be added to the project.
     * @return true if the skill was successfully added to the project, false otherwise.
     */
    public boolean addSkill(Long projectId, Long skillId) {
        ProjectEntity projectEntity = projectDao.findProjectById(projectId);
        if (projectEntity == null) {
            logger.error("Project does not exist: " + projectId);
            return false;
        }

        SkillEntity skillEntity = skillDao.findSkillById(skillId);
        if (skillEntity == null) {
            logger.error("Skill does not exist: " + skillId);
            return false;
        }

        projectSkillBean.addSkillToProject(projectEntity, skillEntity);
        return true;
    }

    /**
     * Removes a skill from a project.
     *
     * This method first retrieves the ProjectEntity and SkillEntity from the database using the provided IDs.
     * If either entity does not exist, it logs an error and returns false.
     * If both entities exist, it removes the skill from the project using the ProjectSkillBean and returns true.
     *
     * @param projectId The ID of the project from which the skill will be removed.
     * @param skillId The ID of the skill to be removed from the project.
     * @return true if the skill was successfully removed from the project, false otherwise.
     */
    public boolean removeSkill(Long projectId, Long skillId) {
        ProjectEntity projectEntity = projectDao.findProjectById(projectId);
        if (projectEntity == null) {
            logger.error("Project does not exist: " + projectId);
            return false;
        }

        SkillEntity skillEntity = skillDao.findSkillById(skillId);
        if (skillEntity == null) {
            logger.error("Skill does not exist: " + skillId);
            return false;
        }

        projectSkillBean.updateProjectSkillStatus(projectEntity, skillEntity, false);
        return true;
    }

    public boolean addKeyword(Long projectId, String keyword) {
        ProjectEntity projectEntity = projectDao.findProjectById(projectId);
        if (projectEntity == null) {
            logger.error("Project does not exist: " + projectId);
            return false;
        }

        if (projectEntity.keywordExists(keyword)) {
            logger.error("Keyword already exists in project: " + keyword);
            return false;
        }

        projectEntity.addKeyword(keyword);
        projectEntity.setUpdatedAt(LocalDateTime.now());
        projectDao.merge(projectEntity);

        return true;
    }

    public boolean removeKeyword(Long projectId, String keyword) {
        ProjectEntity projectEntity = projectDao.findProjectById(projectId);
        if (projectEntity == null) {
            logger.error("Project does not exist: " + projectId);
            return false;
        }

        if (!projectEntity.keywordExists(keyword)) {
            logger.error("Keyword does not exist in project: " + keyword);
            return false;
        }

        projectEntity.removeKeyword(keyword);
        projectEntity.setUpdatedAt(LocalDateTime.now());
        projectDao.merge(projectEntity);

        return true;
    }

    /**
     * Checks if a state id is valid.
     * @param stateId The state id to be checked.
     * @return true if the state id is valid, false otherwise.
     */
    private boolean isValidStateId(int stateId) {
        return stateId == 100 || stateId == 200 || stateId == 300 || stateId == 400 || stateId == 500 || stateId == 600;
    }

    /**
     * Finds a project by its id.
     * @param id The id of the project to be found.
     * @return The found project entity, or null if no project with the given id exists.
     */
    public ProjectEntity findProjectById(long id) {
        return projectDao.findProjectById(id);
    }

    /**
     * Gets all projects ordered from latest to oldest.
     * @return A list of all projects ordered from latest to oldest.
     */
    public List<Project> getAllProjectsLatestToOldest() {
        return projectDao.findAllProjectsOrderedDESC().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a project DTO to a project entity.
     * @param project The project DTO to be converted.
     * @return The converted project entity.
     */
    private ProjectEntity convertToEntity(Project project) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setName(project.getName());
        projectEntity.setDescription(project.getDescription());
        projectEntity.setStateId(project.getStateId());
        projectEntity.setKeywords(project.getKeywords());
        projectEntity.setMaxMembers(project.getMaxMembers());
        projectEntity.setLab(labBean.convertToEntity(project.getLab()));
        projectEntity.setNeeds(project.getNeeds());

        return projectEntity;
    }

    /**
     * Converts a project entity to a project DTO.
     * @param projectEntity The project entity to be converted.
     * @return The converted project DTO.
     */
    private Project convertToDTO(ProjectEntity projectEntity) {
        Project project = new Project();
        project.setId(projectEntity.getId());
        project.setName(projectEntity.getName());
        project.setDescription(projectEntity.getDescription());
        project.setStateId(projectEntity.getStateId());
        project.setKeywords(projectEntity.getKeywords());
        project.setMaxMembers(projectEntity.getMaxMembers());
        project.setLab(labBean.convertToDTO(projectEntity.getLab()));
        project.setNeeds(projectEntity.getNeeds());
        project.setCreatedAt(projectEntity.getCreatedAt());
        project.setUpdatedAt(projectEntity.getUpdatedAt());
        project.setConclusionDate(projectEntity.getConclusionDate());
        project.setInitialDate(projectEntity.getInitialDate());

        return project;
    }
}