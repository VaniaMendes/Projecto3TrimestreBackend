package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.*;
import aor.paj.proj_final_aor_backend.dto.Message;
import aor.paj.proj_final_aor_backend.dto.MessageInfoUser;
import aor.paj.proj_final_aor_backend.dto.Project;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.entity.*;
import aor.paj.proj_final_aor_backend.util.enums.NotificationType;
import aor.paj.proj_final_aor_backend.util.enums.ProjectActivityType;
import aor.paj.proj_final_aor_backend.util.enums.UserType;
import aor.paj.proj_final_aor_backend.util.enums.UserTypeInProject;
import aor.paj.proj_final_aor_backend.websocket.Notifier;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private UserProjectDao userProjectDao;
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

    @EJB
    private NotificationBean notificationBean;
    @EJB
    private SettingsBean settingsBean;
    @EJB
    Notifier notifier;
    @EJB
    MessageBean messageBean;

    @EJB
    TaskBean taskBean;
    @EJB
    UserBean userBean;
    @EJB
    MessageDao messageDao;

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

        project.setStateId(getStateNameFromId(Project.PLANNING));

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
        notificationBean.sendNotificationToAllUsers(token, NotificationType.NEW_PROJECT, projectEntity.getId());

        projectDao.persist(projectEntity);


        taskBean.createFinalTaskOfProject(projectEntity, userBean.convertUserEntityToDto(creator) );

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

        if (project.getMaxMembers() <= 0 || project.getMaxMembers() > settingsBean.getMaxUsersPerProject()){
            logger.error("Project max members is invalid.");
            return true;
        }

        return false;
    }


    /**
     * Updates the state of a project based on specified conditions and rules.
     * This method first validates the project's existence and the validity of the new state ID.
     * It then checks if the user attempting the update is not the project's creator and if the state transition is valid.
     * For ADMIN users, special handling is applied when moving a project from READY to APPROVED state by setting the project's initial date to now.
     * Finally, the project's state is updated, and a notification is sent to all project users.
     *
     * @param id The ID of the project to update.
     * @param stateId The new state ID to set for the project.
     * @param token The token of the user attempting to update the project's state.
     * @return true if the project state was successfully updated, false otherwise.
     */
    public boolean updateState(long id, int stateId, String token) {
        ProjectEntity projectEntity = findProject(id);

        if (projectEntity == null || !isValidStateId(stateId)) {
            return false;
        }
        cloneMessagesEntities(projectEntity);

        UserEntity author = sessionDao.findUserByToken(token);
        if (author == null || (!(author.getUserType() == UserType.ADMIN) && !userProjectBean.userProjectExists(author.getId(), id))) {
            return false;
        }

        int currentStateId = projectEntity.getStateId();
        boolean isAdmin = author.getUserType() == UserType.ADMIN;

        // Check for valid state transitions
        if (!isValidTransition(currentStateId, stateId, isAdmin)) {
            return false;
        }

        // Special handling for ADMIN moving from READY to APPROVED
        if (isAdmin && currentStateId == Project.READY && stateId == Project.APPROVED) {
            projectEntity.setInitialDate(LocalDateTime.now());
        }

        System.out.println(5);

        projectEntity.setStateId(stateId);
        projectEntity.setUpdatedAt(LocalDateTime.now());

        String newState = getStateNameFromId(stateId);
        activityBean.registerActivity(projectEntity, ProjectActivityType.EDIT_PROJECT_STATE, author, newState);

        projectDao.merge(projectEntity);
        String type = String.valueOf(NotificationType.PROJECT_STATE_CHANGE);
        notificationBean.sendNotificationToProjectUsers(token, id, type, projectEntity.getId());

        logger.info("Project state updated to: " + stateId + " for project: " + projectEntity.getName() + " by user with id: " + author.getId());
        return true;
    }


    /**
     * Checks if a transition between project states is valid based on the current state, the desired new state, and the user's admin status.
     * This method enforces the project state transition rules, ensuring that transitions are only made between specific states
     * and under certain conditions (e.g., admin privileges).
     *
     * @param currentState The current state ID of the project.
     * @param newState The desired new state ID for the project.
     * @param isAdmin A boolean indicating if the user attempting the transition is an admin.
     * @return true if the transition is valid according to the rules; false otherwise.
     */
    private boolean isValidTransition(int currentState, int newState, boolean isAdmin) {
        if (newState == Project.CANCELLED) return true; // Can be cancelled at any time
        if (currentState == Project.PLANNING && newState == Project.READY) return true;
        if (currentState == Project.READY && (newState == Project.PLANNING || newState == Project.APPROVED) && isAdmin) return true;
        if (currentState == Project.READY && newState == Project.CANCELLED) return true;
        if (currentState == Project.APPROVED && newState == Project.IN_PROGRESS) return true;
        if (currentState == Project.IN_PROGRESS && newState == Project.FINISHED) return true;
        return false;
    }

    public boolean updateObservations(long id, String observations, String token) {
        ProjectEntity projectEntity = findProject(id);
        if (projectEntity == null || observations == null || observations.isEmpty()) {
            return false;
        }
        cloneMessagesEntities(projectEntity);

        UserEntity author = sessionDao.findUserByToken(token);
        if (author == null || author.getUserType() != UserType.ADMIN) {
            return false;
        }

        projectEntity.setObservations(observations);
        projectEntity.setUpdatedAt(LocalDateTime.now());

        activityBean.registerActivity(projectEntity, ProjectActivityType.EDIT_PROJECT_DATA, author, "Observations updated");

        projectDao.merge(projectEntity);

        logger.info("Project observations updated for project: " + projectEntity.getName() + " by user with id: " + author.getId());
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

        if (!userProjectBean.userProjectExists(author.getId(), id)) {
            return false;
        }

        projectEntity.setDescription(description);
        projectEntity.setUpdatedAt(LocalDateTime.now());

        activityBean.registerActivity(projectEntity, ProjectActivityType.EDIT_PROJECT_DATA, author, "Description updated");

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
        cloneMessagesEntities(projectEntity);

        if (userType != UserTypeInProject.CANDIDATE) {
            projectEntity.setUpdatedAt(LocalDateTime.now());
            projectDao.merge(projectEntity);
        }

        if (userType == UserTypeInProject.CANDIDATE){
            activityBean.registerActivity(projectEntity, ProjectActivityType.ADDED_CANDIDATE, author, author.getFirstName() + " " + author.getLastName() + userEntity.getFirstName() + " " + userEntity.getLastName());
        }else {
            activityBean.registerActivity(projectEntity, ProjectActivityType.ADDED_MEMBER, author, userEntity.getFirstName() + " " + userEntity.getLastName());
        }

        notificationBean.sendNotificationToProjectUsers(token, projectId, String.valueOf(NotificationType.NEW_MEMBER), projectEntity.getId());

        createWelcomeMessage(token, userId, projectEntity);
        logger.info("User with id: " + userEntity.getId() + " added to project: " + projectEntity.getName() + " by user with id: " + author.getId());

        return true;
    }

    /**
     * Creates a welcome message when a user is added to a project.
     *
     * @param token    the token of the user who is sending the message
     * @param userId   the ID of the user who is receiving the message
     * @param project  the project to which the user has been added
     * @return         the created welcome message
     * **/
    public Message createWelcomeMessage (String token, Long userId, ProjectEntity project){
        //Get the sender by token
        User sender = userBean.getUserByToken(token);
        //Get receiver by user id
        User receiver = userBean.getUserById(userId);

        //convert both for DTO MessageInfoUser
        MessageInfoUser sender1 = userBean.convertUserToDTOForMessage(userBean.convertUserDtoToEntity(sender));
        MessageInfoUser receiver1 = userBean.convertUserToDTOForMessage(userBean.convertUserDtoToEntity(receiver));

        //Create a new message
        Message message = new Message();

        //Define the sender, receiver, subject and content
        message.setSender(sender1);
        message.setSendTimestamp(LocalDateTime.now());
        message.setReceiver(receiver1);
        message.setSubject("Bem vindo ");
        message.setContent(" Foi adicionado ao projeto " + project.getName() + ". Bom trabalho");


        //Persist de message in messageEntity

        messageBean.sendMessage(token, message);
        //Return message
        return message;
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
        cloneMessagesEntities(projectEntity);

        UserEntity author = sessionDao.findUserByToken(token);
        if (author == null) {
            return false;
        }

        if (!userProjectBean.approveUserInProject(userId, projectId, userType)){
            return false;
        }
        projectEntity.setUpdatedAt(LocalDateTime.now());

        projectDao.merge(projectEntity);

        activityBean.registerActivity(projectEntity, ProjectActivityType.ADDED_MEMBER, author, userDao.findUserById(userId).getFirstName() + " " + userDao.findUserById(userId).getLastName());
        notificationBean.sendNotificationToProjectUsers(token, projectId, String.valueOf(NotificationType.NEW_MEMBER), projectEntity.getId());

        logger.info("User with id: " + userId + " approved in project: " + projectEntity.getName() + " by user with id: " + author.getId());

        return true;
    }

    /**
     * This method is used to remove a user from a project.
     *
     * It first retrieves the ProjectEntity and UserEntity (author) from the database using the provided IDs and token.
     * If either entity does not exist, it returns false.
     * If both entities exist, it attempts to remove the user from the project using the UserProjectBean.
     * If the user removal is unsuccessful, it returns false.
     * If the user removal is successful, it updates the project's update timestamp and merges the updated project entity back into the database.
     * It then registers an activity of type REMOVED_MEMBER for the project, sends a notification of type MEMBER_EXIT to all users of the project,
     * and logs the removal of the user from the project.
     *
     * @param userId The ID of the user to be removed from the project.
     * @param projectId The ID of the project from which the user will be removed.
     * @param token The token of the user removing the user from the project.
     * @return true if the user was successfully removed from the project, false otherwise.
     */
    public boolean removeUser(Long userId, Long projectId, String token) {
        ProjectEntity projectEntity = findProject(projectId);
        if (projectEntity == null) {
            return false;
        }
        cloneMessagesEntities(projectEntity);

        UserEntity author = sessionDao.findUserByToken(token);
        if (author == null) {
            return false;
        }

        if (!userProjectBean.removeUserFromProject(userId, projectId)){
            return false;
        }

        projectEntity.setUpdatedAt(LocalDateTime.now());
        projectDao.merge(projectEntity);

        activityBean.registerActivity(projectEntity, ProjectActivityType.REMOVED_MEMBER, author, userDao.findUserById(userId).getFirstName() + " " + userDao.findUserById(userId).getLastName());
        notificationBean.sendNotificationToProjectUsers(token, projectId, String.valueOf(NotificationType.MEMBER_EXIT), projectEntity.getId());

        logger.info("User with id: " + userId + " removed from project: " + projectEntity.getName() + " by user with id: " + author.getId());

        return true;
    }

    /**
     * Updates the role of a user in a project.
     * This method updates the role (type) of a user within a specific project. It first retrieves the project and user entities
     * based on the provided project and user IDs. If either the project or the user (author of the action) cannot be found,
     * the method returns false, indicating failure. If both entities are found, it attempts to update the user's role in the project.
     * If the update is successful, the project's update timestamp is refreshed, and the project entity is merged back into the database.
     * Additionally, an activity log is registered to record the role update action.
     *
     * @param userId The ID of the user whose role is to be updated.
     * @param projectId The ID of the project in which the user's role is to be updated.
     * @param userType The new role of the user in the project.
     * @param token The authentication token of the user performing the update action.
     * @return true if the role update was successful, false otherwise.
     */
    public boolean updateUserRole(Long userId, Long projectId, UserTypeInProject userType, String token) {
        ProjectEntity projectEntity = findProject(projectId);
        if (projectEntity == null) {
            return false;
        }
        cloneMessagesEntities(projectEntity);

        UserEntity author = sessionDao.findUserByToken(token);
        if (author == null) {
            return false;
        }

        if (!userProjectBean.updateUserTypeInProject(userId, projectId, userType)) {
            return false;
        }

        projectEntity.setUpdatedAt(LocalDateTime.now());
        projectDao.merge(projectEntity);

        activityBean.registerActivity(projectEntity, ProjectActivityType.UPDATED_MEMBER_ROLE, author, userDao.findUserById(userId).getFirstName() + " " + userDao.findUserById(userId).getLastName());

        return true;
    }

    /**
     * Adds a resource to a project.
     * @param projectId The id of the project to which the resource will be added.
     * @param resourceId The id of the resource to be added.
     * @param quantity The quantity of the resource to be added.
     * @return true if the addition was successful, false otherwise.
     */
    public boolean addResource(Long projectId, Long resourceId, int quantity, String token) {
        ProjectEntity projectEntity = findProject(projectId);
        if (projectEntity == null) {
            return false;
        }
        cloneMessagesEntities(projectEntity);

        UserEntity author = sessionDao.findUserByToken(token);
        if (author == null) {
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

        activityBean.registerActivity(projectEntity, ProjectActivityType.ADDED_RESOURCE, author, resourceEntity.getName());
        return true;
    }


    /**
     * This method is used to associate a skill with a project.
     * It first retrieves the ProjectEntity and SkillEntity from the database using the provided IDs.
     * If either entity does not exist, it returns false.
     * If both entities exist, it associates the skill with the project using the ProjectSkillBean and returns true.
     *
     * @param projectId The ID of the project to which the skill will be associated.
     * @param skillId The ID of the skill to be associated with the project.
     * @return true if the skill was successfully associated with the project, false otherwise.
     */
    public boolean joinSkill(Long projectId, Long skillId) {
        ProjectEntity projectEntity = findProject(projectId);
        if (projectEntity == null) {
            return false;
        }

        SkillEntity skillEntity = findSkill(skillId);
        if (skillEntity == null) {
            return false;
        }

        projectSkillBean.associateSkillToProject(projectEntity, skillEntity);
        return true;
    }


    /**
     * This method is used to update the active status of a skill associated with a project.
     * It first retrieves the ProjectEntity and SkillEntity from the database using the provided IDs.
     * If either entity does not exist, it returns false.
     * If both entities exist, it updates the active status of the skill in the project using the ProjectSkillBean and returns true.
     *
     * @param projectId The ID of the project in which the skill's active status will be updated.
     * @param skillId The ID of the skill whose active status will be updated.
     * @param activeStatus The new active status to be set for the skill in the project.
     * @return true if the active status was successfully updated, false otherwise.
     */
    public boolean editSkillActiveStatus(Long projectId, Long skillId, boolean activeStatus) {
        ProjectEntity projectEntity = findProject(projectId);
        if (projectEntity == null) {
            return false;
        }

        SkillEntity skillEntity = findSkill(skillId);
        if (skillEntity == null) {
            return false;
        }

        projectSkillBean.updateProjectSkillStatus(projectEntity, skillEntity, activeStatus);
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
     * Retrieves a project DTO by its ID.
     * This method first attempts to find a ProjectEntity using the given project ID. If the project entity is found,
     * it clones the message entities associated with the project, converts the project entity to a DTO, and returns it.
     * If no project entity is found with the given ID, it returns null.
     *
     * @param projectId The ID of the project to retrieve.
     * @return The Project DTO corresponding to the given ID, or null if no project is found.
     */
    public Project getProjectById(Long projectId) {
        ProjectEntity projectEntity = findProject(projectId);
        if (projectEntity == null) {
            return null;
        }
        cloneMessagesEntities(projectEntity);
        return convertToDTO(projectEntity);
    }

    /**
     * Finds a ProjectEntity by its ID.
     * This method delegates the search to the ProjectDao to find a ProjectEntity by its ID in the database.
     * If the ProjectEntity is found, it is returned; otherwise, this method returns null.
     *
     * @param projectId The ID of the project to find.
     * @return The found ProjectEntity, or null if no project with the given ID exists.
     */
    public ProjectEntity findProjectById(Long projectId) {
        return projectDao.findProjectById(projectId);
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

    /**
     * Finds a user entity by its unique identifier.
     * This method queries the database for a user with the specified ID. If the user is found,
     * the corresponding UserEntity object is returned. If no user is found with the given ID,
     * an error is logged, and null is returned.
     *
     * @param userId The unique identifier of the user to find.
     * @return The UserEntity object corresponding to the specified ID, or null if no such user exists.
     */
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
     * Retrieves a list of all projects, optionally filtered by order, vacancies, and state.
     * This method allows for a flexible retrieval of projects based on various criteria:
     * - Order: Determines the sorting order of the projects, either ascending ("asc") or descending ("desc").
     * - Vacancies: A boolean value indicating whether to filter projects by those that have vacancies.
     * - State: An optional filter for the state of the projects. A null value or 1 indicates no specific state filter.
     *
     * The method delegates to more specific private methods based on the combination of input parameters to fetch the projects.
     *
     * @param order The order in which to sort the projects. Can be "asc" for ascending or "desc" for descending.
     * @param vacancies A Boolean indicating whether to filter projects by those that have vacancies. True to filter by vacancies, false otherwise.
     * @param state An Integer representing the state of the projects to filter by. A null value or 1 indicates no specific state filter.
     * @return A List of Project DTOs that match the specified criteria.
     */
    public List<Project> getAllProjects(String order, Boolean vacancies, Integer state) {
        List<Project> projectsDTO = new ArrayList<>();

        if (order.equals("desc")) {
            if (vacancies && (state == null || state == 1)) {
                projectsDTO = getProjectsByVacanciesDESC();
            } else if (!vacancies && (state == null || state == 1)) {
                projectsDTO = getAllProjectsLatestToOldest();
            } else if (vacancies && state != null) {
                projectsDTO = getProjectsByStateAndVacanciesDESC(state);
            } else if (state != null) {
                projectsDTO = getProjectsByStateDESC(state);
            }
        } else if (order.equals("asc")) {
            if (vacancies && (state == null || state == 1)) {
                projectsDTO = getProjectsByVacanciesASC();
            } else if (!vacancies && (state == null || state == 1)) {
                projectsDTO = getAllProjectsOldestToLatest();
            } else if (vacancies && state != null) {
                projectsDTO = getProjectsByStateAndVacanciesASC(state);
            } else if (state != null) {
                projectsDTO = getProjectsByStateASC(state);
            }
        }

        return projectsDTO;
    }


    /**
     * Gets all projects ordered from latest to oldest.
     * @return A list of all projects ordered from latest to oldest.
     */
    private List<Project> getAllProjectsLatestToOldest() {

        // Retrieve all projects from the database in descending order
        List<ProjectEntity> projects = projectDao.findAllProjectsOrderedDESC();

        cloneMessagesEntities(projects);

        List<Project> projectsDTO = projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return projectsDTO;
    }

    /**
     * Retrieves all projects from the database in ascending order of their creation time.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     * @return A list of Project DTOs, ordered from oldest to newest.
     */
    private List<Project> getAllProjectsOldestToLatest() {
        List<ProjectEntity> projects = projectDao.findAllProjects();

        cloneMessagesEntities(projects);

        List<Project> projectsDTO = projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return projectsDTO;
    }

    /**
     * Retrieves all projects from the database with a specific state in descending order of their creation time.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     * @param state The state of the projects to be retrieved.
     * @return A list of Project DTOs with the specified state, ordered from newest to oldest.
     */
    private List<Project> getProjectsByStateDESC(int state) {
        List<ProjectEntity> projects = projectDao.findProjectsByStateOrderedDESC(state);

        cloneMessagesEntities(projects);

        List<Project> projectsDTO = projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return projectsDTO;
    }

    /**
     * Retrieves all projects from the database with a specific state in ascending order of their creation time.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     * @param state The state of the projects to be retrieved.
     * @return A list of Project DTOs with the specified state, ordered from oldest to newest.
     */
    private List<Project> getProjectsByStateASC(int state) {
        List<ProjectEntity> projects = projectDao.findProjectsByStateOrderedASC(state);

        cloneMessagesEntities(projects);

        List<Project> projectsDTO = projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return projectsDTO;
    }

    /**
     * Retrieves all projects from the database that have vacancies, in descending order of their creation time.
     * For each project, it clones the messages received by each user project and sets them back to the user project.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @return A list of Project DTOs that have vacancies, ordered from newest to oldest.
     */
    private List<Project> getProjectsByVacanciesDESC() {
        return getProjectsByVacancies("DESC");
    }

    /**
     * Retrieves all projects from the database that have vacancies, in ascending order of their creation time.
     * For each project, it clones the messages received by each user project and sets them back to the user project.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @return A list of Project DTOs that have vacancies, ordered from oldest to newest.
     */
    private List<Project> getProjectsByVacanciesASC() {
        return getProjectsByVacancies("ASC");
    }

    /**
     * Retrieves all projects from the database that have vacancies, in the order specified by the parameter.
     * For each project, it clones the messages received by each user project and sets them back to the user project.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param order The order in which to retrieve the projects. Can be "ASC" for ascending order or "DESC" for descending order.
     * @return A list of Project DTOs that have vacancies, ordered according to the specified order.
     */
    private List<Project> getProjectsByVacancies(String order) {
        List<ProjectEntity> projects = "DESC".equals(order) ? projectDao.findProjectsByVacanciesOrderedDESC() : projectDao.findProjectsByVacanciesOrderedASC();

        for (ProjectEntity project : projects) {
            for (UserProjectEntity userProject : project.getUserProjects()) {
                userProject.setMessagesReceived(new HashSet<>(userProject.getMessagesReceived()));
            }
        }

        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all projects from the database that have vacancies and match a specific state, ordered in descending order of their creation time.
     * This method first queries the database for projects that have vacancies and match the given state, ordered in descending order.
     * It then clones the messages received by each user project and sets them back to the user project.
     * Finally, it converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param state The state of the projects to be retrieved.
     * @return A list of Project DTOs with the specified state and vacancies, ordered from newest to oldest.
     */
    private List<Project> getProjectsByStateAndVacanciesDESC(int state) {
        List<ProjectEntity> projects = projectDao.findProjectsByVacanciesAndStateOrderedDESC(state);
        cloneMessagesEntities(projects);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all projects from the database that have vacancies and match a specific state, ordered in ascending order of their creation time.
     * Similar to the descending order method, this method queries the database for projects that have vacancies and match the given state, but orders them in ascending order.
     * It clones the messages received by each user project and sets them back to the user project.
     * Finally, it converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param state The state of the projects to be retrieved.
     * @return A list of Project DTOs with the specified state and vacancies, ordered from oldest to newest.
     */
    private List<Project> getProjectsByStateAndVacanciesASC(int state) {
        List<ProjectEntity> projects = projectDao.findProjectsByVacanciesAndStateOrderedASC(state);
        cloneMessagesEntities(projects);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all projects associated with a specific user, with optional filters for vacancies and state, and sorted by order.
     * This method allows for a flexible retrieval of projects based on various criteria: whether the project has vacancies,
     * the state of the project, and the order (ascending or descending) in which the projects are returned.
     *
     * The method supports filtering projects that have vacancies (if vacancies is true) and projects that match a specific state.
     * If the state parameter is null or 1, it is considered as a request to retrieve all projects regardless of their state.
     * The order of the projects in the returned list can be specified as either ascending ("asc") or descending ("desc").
     *
     * Depending on the combination of the parameters provided, different private helper methods are called to perform the actual
     * retrieval and sorting of the projects from the database.
     *
     * @param userId The ID of the user whose projects are to be retrieved.
     * @param order The order in which to sort the projects. Can be "asc" for ascending or "desc" for descending.
     * @param vacancies A Boolean indicating whether to filter projects by vacancies. True to include only projects with vacancies.
     * @param state An Integer representing the state of the projects to be retrieved. If null or 1, all projects are considered.
     * @return A list of {@link Project} DTOs that match the specified criteria.
     */
    public List<Project> getAllProjectsWithUser(Long userId, String order, Boolean vacancies, Integer state) {
        List<Project> projectsDTO = new ArrayList<>();

        if (order.equals("desc")) {
            if (vacancies && (state == null || state == 1)) {
                projectsDTO = getProjectsByUserByVacanciesDESC(userId);
            } else if (!vacancies && (state == null || state == 1)) {
                projectsDTO = getProjectsWithUserLatestToOldest(userId);
            } else if (state != null) {
                projectsDTO = getProjectsWithUserByStateDESC(userId, state);
            }
        } else if (order.equals("asc")) {
            if (vacancies && (state == null || state == 1)) {
                projectsDTO = getProjectsByUserByVacanciesASC(userId);
            } else if (!vacancies && (state == null || state == 1)) {
                projectsDTO = getProjectsWithUserOldestToLatest(userId);
            } else if (state != null) {
                projectsDTO = getProjectsWithUserByStateASC(userId, state);
            }
        }

        return projectsDTO;
    }

    /**
     * Retrieves all active projects associated with a specific user, ordered from latest to oldest.
     * For each project, it clones the messages received by each user project and sets them back to the user project.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param userId The ID of the user for whom to retrieve the projects.
     * @return A list of Project DTOs associated with the specified user, ordered from newest to oldest.
     */
    public List<Project> getProjectsWithUserLatestToOldest(Long userId) {
        List<ProjectEntity> projects = projectDao.findActiveProjectsByUserIdOrderedDESC(userId);
        cloneMessagesEntities(projects);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all active projects associated with a specific user, ordered from oldest to latest.
     * For each project, it clones the messages received by each user project and sets them back to the user project.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param userId The ID of the user for whom to retrieve the projects.
     * @return A list of Project DTOs associated with the specified user, ordered from oldest to newest.
     */
    private List<Project> getProjectsWithUserOldestToLatest(Long userId) {
        List<ProjectEntity> projects = projectDao.findActiveProjectsByUserIdOrderedASC(userId);
        cloneMessagesEntities(projects);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all active projects associated with a specific user and a specific state, ordered from newest to oldest.
     * For each project, it clones the messages received by each user project and sets them back to the user project.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param userId The ID of the user for whom to retrieve the projects.
     * @param state The state of the projects to be retrieved.
     * @return A list of Project DTOs associated with the specified user and state, ordered from newest to oldest.
     */
    private List<Project> getProjectsWithUserByStateDESC(Long userId, int state) {
        List<ProjectEntity> projects = projectDao.findActiveProjectsByUserIdAndStateOrderedDESC(userId, state);
        cloneMessagesEntities(projects);
        List<Project> projectsDTO = projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return projectsDTO;
    }

    /**
     * Retrieves all active projects associated with a specific user and a specific state, ordered from oldest to newest.
     * For each project, it clones the messages received by each user project and sets them back to the user project.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param userId The ID of the user for whom to retrieve the projects.
     * @param state The state of the projects to be retrieved.
     * @return A list of Project DTOs associated with the specified user and state, ordered from oldest to newest.
     */
    private List<Project> getProjectsWithUserByStateASC(Long userId, int state) {
        List<ProjectEntity> projects = projectDao.findActiveProjectsByUserIdAndStateOrderedASC(userId, state);
        cloneMessagesEntities(projects);
        List<Project> projectsDTO = projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return projectsDTO;
    }

    /**
     * Retrieves all active projects associated with a specific user that have vacancies, in descending order of their creation time.
     * For each project, it clones the messages received by each user project and sets them back to the user project.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param userId The ID of the user for whom to retrieve the projects.
     * @return A list of Project DTOs associated with the specified user that have vacancies, ordered from newest to oldest.
     */
    private List<Project> getProjectsByUserByVacanciesDESC(Long userId) {
        return getProjectsByUserByVacancies(userId, "DESC");
    }

    /**
     * Retrieves all active projects associated with a specific user that have vacancies, in ascending order of their creation time.
     * For each project, it clones the messages received by each user project and sets them back to the user project.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param userId The ID of the user for whom to retrieve the projects.
     * @return A list of Project DTOs associated with the specified user that have vacancies, ordered from oldest to newest.
     */
    private List<Project> getProjectsByUserByVacanciesASC(Long userId) {
        return getProjectsByUserByVacancies(userId, "ASC");
    }

    /**
     * Retrieves all active projects associated with a specific user that have vacancies, in the order specified by the parameter.
     * For each project, it clones the messages received by each user project and sets them back to the user project.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param userId The ID of the user for whom to retrieve the projects.
     * @param order The order in which to retrieve the projects. Can be "ASC" for ascending order or "DESC" for descending order.
     * @return A list of Project DTOs associated with the specified user that have vacancies, ordered according to the specified order.
     */
    private List<Project> getProjectsByUserByVacancies(Long userId, String order) {
        List<ProjectEntity> projects = "DESC".equals(order) ? projectDao.findProjectsByUserByVacanciesDESC(userId) : projectDao.findProjectsByUserByVacanciesASC(userId);

        for (ProjectEntity project : projects) {
            for (UserProjectEntity userProject : project.getUserProjects()) {
                userProject.setMessagesReceived(new HashSet<>(userProject.getMessagesReceived()));
            }
        }

        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all projects from the database that contain a specific keyword, in ascending order of their creation time.
     * For each project, it clones the messages received by each user project and sets them back to the user project.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param keyword The keyword to be used for retrieving the projects.
     * @return A list of Project DTOs that contain the specified keyword, ordered from oldest to newest.
     */
    private List<Project> getProjectsByKeywordOrSkillASC(String keyword) {
        List<ProjectEntity> projects = projectDao.findProjectsByKeywordOrSkillOrderedASC(keyword);
        cloneMessagesEntities(projects);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all projects from the database that contain a specific keyword, in descending order of their creation time.
     * For each project, it clones the messages received by each user project and sets them back to the user project.
     * Converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param keyword The keyword to be used for retrieving the projects.
     * @return A list of Project DTOs that contain the specified keyword, ordered from newest to oldest.
     */
    private List<Project> getProjectsByKeywordOrSkillDESC(String keyword) {
        List<ProjectEntity> projects = projectDao.findProjectsByKeywordOrSkillOrderedDESC(keyword);
        cloneMessagesEntities(projects);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all projects from the database that contain a specific keyword and match a given state, ordered in ascending order of their creation time.
     * This method first queries the database for projects matching the specified keyword and state using a predefined query.
     * After retrieving the projects, it clones the messages received by each user project and sets them back to the user project.
     * Finally, it converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param keyword The keyword to be used for retrieving the projects.
     * @param state The state of the projects to be retrieved.
     * @return A list of Project DTOs that contain the specified keyword and match the given state, ordered from oldest to newest.
     */
    private List<Project> getProjectsByKeywordOrSkillAndStateASC(String keyword, int state) {
        List<ProjectEntity> projects = projectDao.findProjectsByKeywordOrSkillAndStateOrderedASC(keyword, state);
        cloneMessagesEntities(projects);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all projects from the database that contain a specific keyword and match a given state, ordered in descending order of their creation time.
     * Similar to the ascending order method, this method queries the database for projects matching the specified keyword and state.
     * It then clones the messages received by each user project and sets them back to the user project.
     * Finally, it converts each ProjectEntity to a Project DTO and collects them into a list, but in this case, the order is from newest to oldest.
     *
     * @param keyword The keyword to be used for retrieving the projects.
     * @param state The state of the projects to be retrieved.
     * @return A list of Project DTOs that contain the specified keyword and match the given state, ordered from newest to oldest.
     */
    private List<Project> getProjectsByKeywordOrSkillAndStateDESC(String keyword, int state) {
        List<ProjectEntity> projects = projectDao.findProjectsByKeywordOrSkillAndStateOrderedDESC(keyword, state);
        cloneMessagesEntities(projects);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all projects from the database that contain a specific keyword and are ordered by the number of vacancies in descending order.
     * This method first queries the database for projects matching the specified keyword. The projects are then ordered by the number of vacancies
     * from highest to lowest. For each project, it clones the messages received by each user project and sets them back to the user project.
     * Finally, it converts each ProjectEntity to a Project DTO and collects them into a list.
     *
     * @param keyword The keyword to be used for retrieving the projects.
     * @return A list of Project DTOs that contain the specified keyword, ordered by the number of vacancies from highest to lowest.
     */
    private List<Project> getProjectsByKeywordOrSkillOrderedByVacanciesDESC(String keyword) {
        List<ProjectEntity> projects = projectDao.findProjectsByKeywordOrSkillOrderedByVacanciesDESC(keyword);
        cloneMessagesEntities(projects);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all projects from the database that contain a specific keyword and are ordered by the number of vacancies in ascending order.
     * Similar to the descending order method, this method queries the database for projects matching the specified keyword. However, the projects
     * are ordered by the number of vacancies from lowest to highest. It clones the messages received by each user project and sets them back to the user project.
     * Each ProjectEntity is then converted to a Project DTO and collected into a list.
     *
     * @param keyword The keyword to be used for retrieving the projects.
     * @return A list of Project DTOs that contain the specified keyword, ordered by the number of vacancies from lowest to highest.
     */
    private List<Project> getProjectsByKeywordOrSkillOrderedByVacanciesASC(String keyword) {
        List<ProjectEntity> projects = projectDao.findProjectsByKeywordOrSkillOrderedByVacanciesASC(keyword);
        cloneMessagesEntities(projects);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves projects by a specified keyword and order.
     * This method allows fetching projects that contain a given keyword. The results can be ordered
     * in ascending or descending order based on the creation time of the projects.
     *
     * @param keyword The keyword to search for in project titles, descriptions, etc.
     * @param order Specifies the order of the results. Can be "asc" for ascending or "desc" for descending.
     * @return A list of {@link Project} objects that match the keyword. Returns an empty list if no matches are found.
     */
    public List<Project> getProjectsByKeywordOrSkill(String keyword, String order, Boolean vacancies, Integer state) {
        List<Project> projectsDTO = new ArrayList<>();

        if (order.equals("desc")) {
            if (vacancies && (state == null || state == 1)) {
                projectsDTO = getProjectsByKeywordOrSkillOrderedByVacanciesDESC(keyword);
            } else if (!vacancies && (state == null || state == 1)) {
                projectsDTO = getProjectsByKeywordOrSkillDESC(keyword);
            } else if (state != null) {
                projectsDTO = getProjectsByKeywordOrSkillAndStateDESC(keyword, state);
            }
        } else if (order.equals("asc")) {
            if (vacancies && (state == null || state == 1)) {
                projectsDTO = getProjectsByKeywordOrSkillOrderedByVacanciesASC(keyword);
            } else if (!vacancies && (state == null || state == 1)) {
                projectsDTO = getProjectsByKeywordOrSkillASC(keyword);
            } else if (state != null) {
                projectsDTO = getProjectsByKeywordOrSkillAndStateASC(keyword, state);
            }
        }

        return projectsDTO;
    }


    /**
     * Retrieves all unique keywords used in projects.
     * This method fetches a list of all unique keywords that have been associated with any project.
     *
     * @return A list of strings, each representing a unique keyword. Returns an empty list if no keywords are found.
     */
    public List<String> getAllKeywords() {
        return projectDao.findAllUniqueKeywords();
    }

    /**
     * Searches for projects by a specified keyword.
     * This method performs a database search for projects that contain the given keyword in their metadata.
     * The search is case-insensitive and matches any part of the keyword.
     *
     * @param keyword The keyword to search for in project metadata.
     * @return A list of strings representing the keywords found in the search. Returns an empty list if no matches are found.
     */
    public List<String> searchKeywords(String keyword) {
        return projectDao.searchKeywords(keyword);
    }


    /**
     * Searches for projects based on the given criteria.
     * This method allows for searching projects by name, state, and whether to order by vacancies.
     * The search can be ordered in ascending or descending order.
     *
     * @param name The name or partial name of the project to search for. Uses a case-insensitive search.
     * @param state The state ID of the projects to filter by. If null or 1, all projects are considered.
     * @param orderByVacancies A Boolean indicating if the search results should be ordered by the number of vacancies.
     *                          If true, projects are ordered by vacancies; otherwise, they are ordered by their names.
     * @param order The order in which to return the search results. Can be "asc" for ascending or "desc" for descending order.
     * @return A list of {@link Project} DTOs that match the search criteria. Returns an empty list if no projects are found.
     */
    public List<Project> searchProjects(String name, Integer state, Boolean orderByVacancies, String order) {
        List<Project> projectsDTO = new ArrayList<>();

        if (order.equals("desc")) {
            if (orderByVacancies != null && orderByVacancies && (state == null || state == 1)) {
                projectsDTO = projectDao.searchProjectsByNameOrderedByVacanciesDESC(name).stream().map(this::convertToDTO).collect(Collectors.toList());
            } else if (orderByVacancies != null && !orderByVacancies && (state == null || state == 1)) {
                projectsDTO = projectDao.searchProjectsByNameOrderedDESC(name).stream().map(this::convertToDTO).collect(Collectors.toList());
            } else if (state != null) {
                projectsDTO = projectDao.searchProjectsByNameAndStateOrderedDESC(name, state).stream().map(this::convertToDTO).collect(Collectors.toList());
            }
        } else if (order.equals("asc")) {
            if (orderByVacancies != null && orderByVacancies && (state == null || state == 1)) {
                projectsDTO = projectDao.searchProjectsByNameOrderedByVacanciesASC(name).stream().map(this::convertToDTO).collect(Collectors.toList());
            } else if (orderByVacancies != null && !orderByVacancies && (state == null || state == 1)) {
                projectsDTO = projectDao.searchProjectsByNameOrderedASC(name).stream().map(this::convertToDTO).collect(Collectors.toList());
            } else if (state != null) {
                projectsDTO = projectDao.searchProjectsByNameAndStateOrderedASC(name, state).stream().map(this::convertToDTO).collect(Collectors.toList());
            }
        }

        return projectsDTO;
    }

    /**
     * This method is used to count the number of vacancies in a project.
     * It first finds the project by its ID, then calculates the number of vacancies by subtracting the number of active users from the maximum number of members.
     * If the number of vacancies is less than 0, it returns 0.
     *
     * @param projectId The ID of the project for which to count the vacancies.
     * @return The number of vacancies in the project, or null if the project does not exist.
     */
    private Integer countVacancies(Long projectId) {
        ProjectEntity p = findProject(projectId);
        if (p == null) {
            return null;
        }

        int vacancies = p.getMaxMembers() - userProjectDao.countActiveUsersByProjectId(projectId);

        if (vacancies < 0) {
            return 0;
        }

        return vacancies;
    }

    /**
     * This method is used to count the number of projects based on their state.
     * If the state is 1, it counts all projects. Otherwise, it counts projects with the specified state.
     *
     * @param state The state of the projects to be counted. If state is 1, all projects are counted.
     * @return The number of projects with the specified state, or the total number of projects if state is 1.
     */
    public Integer countProjects(Integer state) {
        int count = 0;

        if (state == 1) {
            // Count all projects
            count = projectDao.countAllProjects();
        } else {
            // Count projects with the specified state
            count = projectDao.countProjectsByState(state);
        }

        return count;
    }

    /**
     * Counts the number of projects that match a given keyword and optionally filters by state.
     * This method delegates to different DAO methods based on the state parameter. If the state is 1,
     * it counts projects by keyword regardless of their state. Otherwise, it counts projects by both keyword and state.
     *
     * @param keyword The keyword to search for within project fields.
     * @param state The state of the projects to be counted. If state is 1, all projects are counted regardless of state.
     * @return The number of projects matching the criteria.
     */
    public Integer countProjectsByKeywordOrSkill(String keyword, Integer state) {
        if (state == 1) {
            return projectDao.countProjectsByKeywordOrSkill(keyword);
        } else {
            return projectDao.countProjectsByKeywordOrSkillAndState(keyword, state);
        }
    }



    /**
     * Counts the number of projects that match a given name and optionally filters by state.
     * This method delegates to different DAO methods based on the state parameter. If the state is 1,
     * it counts projects by name regardless of their state. Otherwise, it counts projects by both name and state.
     *
     * @param name The name or partial name of the project to search for. The search is case-sensitive.
     * @param state The state of the projects to be counted. If state is 1, all projects are counted regardless of state.
     * @return The number of projects matching the criteria.
     */
    public Integer countSearchProjectsByName(String name, Integer state) {
        if (state == 1) {
            return projectDao.countSearchProjectsByName(name);
        } else {
            return projectDao.countSearchProjectsByNameAndState(name, state);
        }
    }

    /**
     * This method is used to get the name of a project state from its ID.
     * It uses a switch statement to map the state ID to its corresponding name.
     *
     * @param stateId The ID of the state for which to get the name.
     * @return The name of the state, or "UNKNOWN_STATE" if the state ID does not match any known state.
     */
    private String getStateNameFromId(int stateId) {
        switch (stateId) {
            case Project.PLANNING:
                return "PLANNING";
            case Project.READY:
                return "READY";
            case Project.APPROVED:
                return "APPROVED";
            case Project.IN_PROGRESS:
                return "IN_PROGRESS";
            case Project.FINISHED:
                return "FINISHED";
            case Project.CANCELLED:
                return "CANCELLED";
            default:
                return "UNKNOWN_STATE";
        }
    }

    /**
     * This method is used to get the ID of a project state from its name.
     * It uses a switch statement to map the state name to its corresponding ID.
     *
     * @param stateName The name of the state for which to get the ID.
     * @return The ID of the state, or -1 if the state name does not match any known state.
     */
    private int getStateNumberFromName(String stateName) {
        switch (stateName) {
            case "PLANNING":
                return Project.PLANNING;
            case "READY":
                return Project.READY;
            case "APPROVED":
                return Project.APPROVED;
            case "IN_PROGRESS":
                return Project.IN_PROGRESS;
            case "FINISHED":
                return Project.FINISHED;
            case "CANCELLED":
                return Project.CANCELLED;
            default:
                return -1;
        }
    }


    /**
     * Converts a project DTO to a project entity.
     * @param project The project DTO to be converted.
     * @return The converted project entity.
     */
    public ProjectEntity convertToEntity(Project project) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setName(project.getName());
        projectEntity.setDescription(project.getDescription());
        projectEntity.setStateId(getStateNumberFromName(project.getStateId()));
        projectEntity.setKeywords(project.getKeywords());
        projectEntity.setLab(labBean.convertToEntity(project.getLab()));
        projectEntity.setNeeds(project.getNeeds());
        projectEntity.setMaxMembers(project.getMaxMembers());
        projectEntity.setConclusionDate(project.getConclusionDate());

        return projectEntity;
    }

    /**
     * Converts a project entity to a project DTO.
     * @param projectEntity The project entity to be converted.
     * @return The converted project DTO.
     */
    public Project convertToDTO(ProjectEntity projectEntity) {
        Project project = new Project();
        project.setId(projectEntity.getId());
        project.setName(projectEntity.getName());
        project.setDescription(projectEntity.getDescription());
        project.setStateId(getStateNameFromId(projectEntity.getStateId()));
        project.setKeywords(projectEntity.getKeywords());
        project.setLab(labBean.convertToDTO(projectEntity.getLab()));
        project.setMaxMembers(projectEntity.getMaxMembers());
        project.setNeeds(projectEntity.getNeeds());
        project.setObservations(projectEntity.getObservations());
        project.setCreatedAt(projectEntity.getCreatedAt());
        project.setUpdatedAt(projectEntity.getUpdatedAt());
        project.setConclusionDate(projectEntity.getConclusionDate());
        project.setInitialDate(projectEntity.getInitialDate());
        project.setSkills(projectSkillBean.getSkillsOfProject(projectEntity.getId()));
        project.setUsersInfo(userProjectBean.getUsersInAProject(projectEntity.getId()));
        project.setVacancyNumber(countVacancies(projectEntity.getId()));

        return project;
    }

    /**
     * This method is used to clone the MessageEntity set for each UserProjectEntity in each ProjectEntity.
     * The method takes a list of ProjectEntity objects as input.
     * For each ProjectEntity in the list, it retrieves the set of UserProjectEntity objects.
     * For each UserProjectEntity, it retrieves the set of MessageEntity objects received by the user.
     * It then creates a new HashSet of MessageEntity objects, which is a clone of the original set.
     * Finally, it sets the cloned set of MessageEntity objects as the messages received by the UserProjectEntity.
     *
     * @param projects A list of ProjectEntity objects for which to clone the MessageEntity set.
     */
    private void cloneMessagesEntities(List<ProjectEntity> projects) {
        for (ProjectEntity project : projects) {
            Set<UserProjectEntity> userProjects = project.getUserProjects();
            for (UserProjectEntity userProject : userProjects) {
                Set<MessageEntity> originalMessages = userProject.getMessagesReceived();
                Set<MessageEntity> clonedMessages = new HashSet<>(originalMessages);
                userProject.setMessagesReceived(clonedMessages);
            }
        }
    }

    public void cloneMessagesEntities(ProjectEntity project) {
        Set<UserProjectEntity> userProjects = project.getUserProjects();
        for (UserProjectEntity userProject : userProjects) {
            Set<MessageEntity> originalMessages = userProject.getMessagesReceived();
            Set<MessageEntity> clonedMessages = new HashSet<>(originalMessages);
            userProject.setMessagesReceived(clonedMessages);
        }
    }

}