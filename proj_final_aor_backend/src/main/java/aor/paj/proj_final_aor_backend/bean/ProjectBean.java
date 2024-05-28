package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.*;
import aor.paj.proj_final_aor_backend.dto.IdAndNameDTO;
import aor.paj.proj_final_aor_backend.dto.Project;
import aor.paj.proj_final_aor_backend.entity.*;
import aor.paj.proj_final_aor_backend.util.enums.ProjectActivityType;
import aor.paj.proj_final_aor_backend.util.enums.UserTypeInProject;
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

    @EJB
    private SessionDao sessionDao;

    // EJB reference for ResourceDao to perform operations related to Resource entities.
    @EJB
    private ResourceDao resourceDao;

    // EJB reference for SkillDao to perform operations related to Skill entities.
    // This bean is used to interact with the database for CRUD operations on Skill entities.
    @EJB
    private SkillDao skillDao;

    @EJB
    private UserDao userDao;

    // EJB reference for ProjectSkillBean to perform operations related to ProjectSkill entities.
    // This bean is used to manage the relationship between Project and Skill entities,
    // such as adding a skill to a project or remove a skill from a project.
    @EJB
    private ProjectSkillBean projectSkillBean;

    // EJB reference for LabBean to perform operations related to Lab entities.
    @EJB
    private LabBean labBean;

    @EJB
    private ActivityBean activityBean;

    // EJB reference for ProjectResourceBean to perform operations related to ProjectResource entities.
    @EJB
    private ProjectResourceBean projectResourceBean;

    @EJB
    private UserProjectBean userProjectBean;

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
     * @param token The token of the user creating the project.
     * @return The persisted ProjectEntity, or null if the project is invalid or the associated Lab does not exist.
     */
    public boolean createProject(Project project, String token) {
        UserEntity creator = sessionDao.findUserByToken(token);
        if (creator == null) {
            logger.error("User does not exist");
            return false;
        }

        if (isInvalidProject(project)) {
            return false;
        }

        project.setStateId(100);

        ProjectEntity projectEntity = convertToEntity(project);

        // Fetch the existing LabEntity
        logger.info("Project lab: " + project.getLab().getName());
        LabEntity labEntity = labBean.findLabByName(String.valueOf(project.getLab().getName()));
        logger.info("LabEntity: " + labEntity);
        if (labEntity == null) {
            logger.error("Lab does not exist: " + project.getLab().getName());
            return false;
        }

        // Set the existing LabEntity to the ProjectEntity
        projectEntity.setLab(labEntity);

        userProjectBean.addUserToProject(creator, projectEntity, UserTypeInProject.CREATOR);

        projectDao.persist(projectEntity);

        return true;
    }



    /**
     * This method validates a Project.
     * It checks if the project is null, if its name, description, keywords are null or empty,
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
     * @param token The token of the user updating the project.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateState(long id, int stateId, String token) {
        ProjectEntity projectEntity = findProject(id);
        if (projectEntity == null || !isValidStateId(stateId)) {
            return false;
        }

        UserEntity author = sessionDao.findUserByToken(token);
        if (author == null) {
            return false;
        }

        projectEntity.setStateId(stateId);
        projectEntity.setUpdatedAt(LocalDateTime.now());

        activityBean.registerActivity(projectEntity, ProjectActivityType.EDIT_PROJECT_STATE, author);

        projectDao.merge(projectEntity);

        logger.info("Project state updated to: " + stateId + " for project: " + projectEntity.getName() + " by user with id: " + author.getId());
        return true;
    }

    /**
     * Updates the description of a project.
     * @param id The id of the project to be updated.
     * @param description The new description to be set.
     * @param token The token of the user updating the project.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateDescription(long id, String description, String token) {
        ProjectEntity projectEntity = findProject(id);
        if (projectEntity == null || description == null || description.isEmpty()) {
            return false;
        }

        UserEntity author = sessionDao.findUserByToken(token);
        if (author == null) {
            return false;
        }

        projectEntity.setDescription(description);
        projectEntity.setUpdatedAt(LocalDateTime.now());

        activityBean.registerActivity(projectEntity, ProjectActivityType.EDIT_PROJECT_DATA, author);

        projectDao.merge(projectEntity);

        logger.info("Project description updated for project: " + projectEntity.getName() + " by user with id: " + author.getId());
        return true;
    }

    /**
     * Adds a user to a project with a specified role.
     *
     * This method first retrieves the ProjectEntity and UserEntity from the database using the provided IDs.
     * If either entity does not exist, it returns false.
     * If both entities exist, it adds the user to the project with the specified role using the UserProjectBean and returns true.
     *
     * @param projectId The ID of the project to which the user will be added.
     * @param userId The ID of the user to be added to the project.
     * @param userType The role of the user in the project.
     * @param token The token of the user adding the user to the project.
     * @return true if the user was successfully added to the project, false otherwise.
     */
    public boolean addUser(Long projectId, Long userId, UserTypeInProject userType, String token) {
        ProjectEntity projectEntity = findProject(projectId);
        if (projectEntity == null) {
            return false;
        }

        UserEntity userEntity = findUser(userId);
        if (userEntity == null) {
            return false;
        }

        UserEntity author = sessionDao.findUserByToken(token);
        if (author == null) {
            return false;
        }

        if (userProjectBean.userProjectExists(userId, projectId) || userType == UserTypeInProject.EXITED || userType == UserTypeInProject.CREATOR) {
            return false;
        }

        userProjectBean.addUserToProject(userEntity, projectEntity, userType);

        if (userType != UserTypeInProject.CANDIDATE) {
            projectEntity.setUpdatedAt(LocalDateTime.now());
            projectDao.merge(projectEntity);
        }

        activityBean.registerActivity(projectEntity, ProjectActivityType.ADDED_MEMBER, author);

        logger.info("User with id: " + userEntity.getId() + " added to project: " + projectEntity.getName() + " by user with id: " + author.getId());

        return true;
    }

    /**
     * Approves a user in a project.
     *
     * This method first retrieves the ProjectEntity from the database using the provided project ID.
     * If the ProjectEntity does not exist, it returns false.
     * If the ProjectEntity exists, it attempts to approve the user in the project using the UserProjectBean.
     * If the user approval is unsuccessful, it returns false.
     * If the user approval is successful, it updates the project's update timestamp and merges the updated project entity back into the database.
     *
     * @param userId The ID of the user to be approved.
     * @param projectId The ID of the project in which the user is to be approved.
     * @param token The token of the user approving the user in the project.
     * @return true if the user was successfully approved in the project, false otherwise.
     */
    public boolean approveUser(Long userId, Long projectId, UserTypeInProject userType, String token) {
        ProjectEntity projectEntity = findProject(projectId);
        if (projectEntity == null) {
            return false;
        }

        UserEntity author = sessionDao.findUserByToken(token);
        if (author == null) {
            return false;
        }

        if (!userProjectBean.approveUserInProject(userId, projectId, userType)){
            return false;
        }
        projectEntity.setUpdatedAt(LocalDateTime.now());

        projectDao.merge(projectEntity);

        activityBean.registerActivity(projectEntity, ProjectActivityType.ADDED_MEMBER, author);

        logger.info("User with id: " + userId + " approved in project: " + projectEntity.getName() + " by user with id: " + author.getId());

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
        ProjectEntity projectEntity = findProject(projectId);
        if (projectEntity == null) {
            return false;
        }

        ResourceEntity resourceEntity = findResource(resourceId);
        if (resourceEntity == null) {
            return false;
        }

        if (projectResourceBean.exists(projectEntity, resourceEntity)) {
            projectResourceBean.mergeProjectResourceConnection(projectEntity, resourceEntity, quantity);
            logger.info("Resource already exists in project, added more: " + quantity + " units");
        }else{
            projectResourceBean.persistProjectResourceConnection(projectEntity, resourceEntity, quantity);
            logger.info("New resource added to the project with id: " + projectId);
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
        ProjectEntity projectEntity = findProject(projectId);
        if (projectEntity == null) {
            return false;
        }

        SkillEntity skillEntity = findSkill(skillId);
        if (skillEntity == null) {
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
        ProjectEntity projectEntity = findProject(projectId);
        if (projectEntity == null) {
            return false;
        }

        SkillEntity skillEntity = findSkill(skillId);
        if (skillEntity == null) {
            return false;
        }

        projectSkillBean.updateProjectSkillStatus(projectEntity, skillEntity, false);
        return true;
    }

    /**
     * Adds a keyword to a project.
     *
     * This method first retrieves the ProjectEntity from the database using the provided project ID.
     * If the ProjectEntity does not exist, it returns false.
     * If the ProjectEntity exists, it checks if the keyword already exists in the project.
     * If the keyword already exists in the project, it logs an error and returns false.
     * If the keyword does not exist in the project, it adds the keyword to the project, updates the project's update timestamp and merges the updated project entity back into the database.
     *
     * @param projectId The ID of the project to which the keyword will be added.
     * @param keyword The keyword to be added to the project.
     * @return true if the keyword was successfully added to the project, false otherwise.
     */
    public boolean addKeyword(Long projectId, String keyword) {
        ProjectEntity projectEntity = findProject(projectId);
        if (projectEntity == null) {
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

    /**
     * Removes a keyword from a project.
     *
     * This method first retrieves the ProjectEntity from the database using the provided project ID.
     * If the ProjectEntity does not exist, it logs an error and returns false.
     * If the ProjectEntity exists, it checks if the keyword exists in the project.
     * If the keyword does not exist in the project, it logs an error and returns false.
     * If the keyword exists in the project, it checks if the keyword is the only one in the project.
     * If the keyword is the only one in the project, it logs an error and returns false.
     * If the keyword is not the only one in the project, it removes the keyword from the project, updates the project's update timestamp and merges the updated project entity back into the database.
     *
     * @param projectId The ID of the project from which the keyword will be removed.
     * @param keyword The keyword to be removed from the project.
     * @return true if the keyword was successfully removed from the project, false otherwise.
     */
    public boolean removeKeyword(Long projectId, String keyword) {
        ProjectEntity projectEntity = findProject(projectId);
        if (projectEntity == null) {
            logger.error("Project does not exist: " + projectId);
            return false;
        }

        if (!projectEntity.keywordExists(keyword)) {
            logger.error("Keyword does not exist in project: " + keyword);
            return false;
        }

        if (projectEntity.isKeywordOnly(keyword)) {
            logger.error("Cannot remove the only keyword from project: " + keyword);
            return false;
        }

        projectEntity.removeKeyword(keyword);
        projectEntity.setUpdatedAt(LocalDateTime.now());
        projectDao.merge(projectEntity);

        return true;
    }

    /**
     * Finds a ProjectEntity by its id.
     *
     * This method uses the ProjectDao to find a ProjectEntity in the database with the given id.
     * If the ProjectEntity does not exist, it logs an error and returns null.
     *
     * @param projectId The id of the ProjectEntity to find.
     * @return The found ProjectEntity, or null if it does not exist.
     */
    public ProjectEntity findProject(Long projectId) {
        ProjectEntity projectEntity = projectDao.findProjectById(projectId);
        if (projectEntity == null) {
            logger.error("Project does not exist: " + projectId);
        }
        return projectEntity;
    }

    /**
     * Finds a SkillEntity by its id.
     *
     * This method uses the SkillDao to find a SkillEntity in the database with the given id.
     * If the SkillEntity does not exist, it logs an error and returns null.
     *
     * @param skillId The id of the SkillEntity to find.
     * @return The found SkillEntity, or null if it does not exist.
     */
    private SkillEntity findSkill(Long skillId) {
        SkillEntity skillEntity = skillDao.findSkillById(skillId);
        if (skillEntity == null) {
            logger.error("Skill does not exist: " + skillId);
        }
        return skillEntity;
    }

    /**
     * Finds a ResourceEntity by its id.
     *
     * This method uses the ResourceDao to find a ResourceEntity in the database with the given id.
     * If the ResourceEntity does not exist, it logs an error and returns null.
     *
     * @param resourceId The id of the ResourceEntity to find.
     * @return The found ResourceEntity, or null if it does not exist.
     */
    private ResourceEntity findResource(Long resourceId) {
        ResourceEntity resourceEntity = resourceDao.findResourceById(resourceId);
        if (resourceEntity == null) {
            logger.error("Resource does not exist: " + resourceId);
        }
        return resourceEntity;
    }

    private UserEntity findUser(Long userId) {
        UserEntity userEntity = userDao.findUserById(userId);
        if (userEntity == null) {
            logger.error("User does not exist: " + userId);
        }
        return userEntity;
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
        project.setLab(labBean.convertToDTO(projectEntity.getLab()));
        project.setNeeds(projectEntity.getNeeds());
        project.setCreatedAt(projectEntity.getCreatedAt());
        project.setUpdatedAt(projectEntity.getUpdatedAt());
        project.setConclusionDate(projectEntity.getConclusionDate());
        project.setInitialDate(projectEntity.getInitialDate());
        project.setSkills(projectSkillBean.getSkillsOfProject(projectEntity.getId()));
        project.setUsersInfo(userProjectBean.getUsersInAProject(projectEntity.getId()));

        return project;
    }

}