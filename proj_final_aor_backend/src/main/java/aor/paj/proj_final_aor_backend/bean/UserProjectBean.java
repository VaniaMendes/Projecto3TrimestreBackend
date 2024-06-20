package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.SessionDao;
import aor.paj.proj_final_aor_backend.dao.UserProjectDao;
import aor.paj.proj_final_aor_backend.dto.*;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.entity.UserProjectEntity;
import aor.paj.proj_final_aor_backend.util.enums.ProjectActivityType;
import aor.paj.proj_final_aor_backend.util.enums.UserTypeInProject;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Stateless EJB bean for managing user-project associations.
 */
@Stateless
public class UserProjectBean implements Serializable {

    // Logger instance for logging events, info, errors etc.
    private static final Logger logger = LogManager.getLogger(UserProjectBean.class);

    // EJB injection for UserProjectDao
    @EJB
    private UserProjectDao userProjectDao;

    @EJB
    private SessionDao sessionDao;

    // EJB injection for UserBean
    @EJB
    private UserBean userBean;

    @EJB
    private ActivityBean activityBean;

    // EJB injection for ProjectBean
    @EJB
    private ProjectBean projectBean;

    /**
     * Default constructor for UserProjectBean.
     */
    public UserProjectBean() {
    }

    /**
     * Overloaded constructor for UserProjectBean.
     * @param userProjectDao UserProjectDao instance
     */
    public UserProjectBean(UserProjectDao userProjectDao) {
        this.userProjectDao = userProjectDao;
    }

    /**
     * Method to add a user to a project.
     * @param userEntity UserEntity instance representing the user to be added
     * @param projectEntity ProjectEntity instance representing the project to which the user is to be added
     * @param userType UserTypeInProject enum value representing the type of user in the project
     * @return boolean value indicating whether the user was successfully added to the project
     */
    public boolean addUserToProject(UserEntity userEntity, ProjectEntity projectEntity, UserTypeInProject userType) {

        // If the user type is EXITED, return false
        if (userType == UserTypeInProject.EXITED) {
            return false;
        }

        // If the user already exists in the project, return false
        if (userProjectExists(userEntity.getId(), projectEntity.getId())) {
            return false;
        }

        // Create a new UserProjectEntity instance
        UserProjectEntity userProjectEntity = new UserProjectEntity();
        userProjectEntity.setProject(projectEntity);
        userProjectEntity.setUser(userEntity);
        userProjectEntity.setUserType(userType);
        userProjectEntity.setExited(false);

        // If the user type is CANDIDATE, set approved to false, else set it to true and set the joinedAt time
        if (userType == UserTypeInProject.CANDIDATE) {
            userProjectEntity.setApproved(false);
        } else {
            userProjectEntity.setApproved(true);
            userProjectEntity.setJoinedAt(LocalDateTime.now());
        }

        // Persist the UserProjectEntity instance
        userProjectDao.persist(userProjectEntity);
        logger.info("User with ID '" + userEntity.getId() + "' added to Project with ID '" + projectEntity.getId() + "'");
        return true;
    }

    /**
     * Method to remove a user from a project.
     * @param userId The ID of the user to be removed.
     * @param projectId The ID of the project from which the user is to be removed.
     * @return boolean value indicating whether the user was successfully removed from the project.
     */
    public boolean removeUserFromProject(Long userId, Long projectId) {
        UserProjectEntity userProjectEntity = userProjectDao.findUserInProject(projectId, userId);

        // If the user does not exist in the project or is the creator of the project, return false
        if (!userProjectExists(userId, projectId) || isCreator(userId, projectId)) {
            return false;
        }

        // Set the user's status to exited and update the time they left the project
        userProjectEntity.setExited(true);
        userProjectEntity.setUserType(UserTypeInProject.EXITED);
        userProjectEntity.setLeftAt(LocalDateTime.now());

        userProjectDao.merge(userProjectEntity);

        logger.info("User with ID '" + userId + "' removed from Project with ID '" + projectId + "' by User with ID '");
        return true;
    }

    /**
     * Method to approve a user in a project.
     * @param userId The ID of the user to be approved.
     * @param projectId The ID of the project in which the user is to be approved.
     * @return boolean value indicating whether the user was successfully approved in the project.
     */
    public boolean approveUserInProject(Long userId, Long projectId, UserTypeInProject userType) {
        if (userType == UserTypeInProject.EXITED || userType == UserTypeInProject.CREATOR || userType == UserTypeInProject.CANDIDATE) {
            logger.warn("Cannot approve user type EXITED, CREATOR or CANDIDATE");
            return false;
        }

        UserProjectEntity userProjectEntity = userProjectDao.findUserInProject(projectId, userId);

        // If the user does not exist in the project or has already been approved, return false
        if (userProjectEntity == null || userProjectEntity.isApproved()) {
            logger.warn("User not found or approved in Project at some point");
            return false;
        }

        // Set the user's status to approved and update the time they joined the project
        userProjectEntity.setApproved(true);
        userProjectEntity.setUserType(userType);
        userProjectEntity.setJoinedAt(LocalDateTime.now());
        userProjectDao.merge(userProjectEntity);
        logger.info("User approved in Project");
        return true;
    }

    public boolean updateUserTypeInProject(Long userId, Long projectId, UserTypeInProject userType) {
        if (userType == UserTypeInProject.EXITED || userType == UserTypeInProject.CREATOR || userType == UserTypeInProject.CANDIDATE) {
            logger.warn("Cannot update user type to EXITED, CREATOR or CANDIDATE");
            return false;
        }

        UserProjectEntity userProjectEntity = userProjectDao.findUserInProject(projectId, userId);

        // If the user does not exist in the project, return false
        if (userProjectEntity == null || userProjectEntity.isExited() || isCreator(userId, projectId)) {
            logger.warn("Cannot update user type in Project as user does not exist in Project, has exited the Project or is the Creator");
            return false;
        }

        // Set the user's type to the new type
        userProjectEntity.setUserType(userType);
        userProjectDao.merge(userProjectEntity);
        logger.info("User type updated in Project");
        return true;
    }

    /**
     * Method to check if a user exists in a project.
     * @param userId The ID of the user to be checked.
     * @param projectId The ID of the project to be checked.
     * @return boolean value indicating whether the user exists in the project.
     */
    public boolean userProjectExists(Long userId, Long projectId) {
        UserProjectEntity userProjectEntity = userProjectDao.findUserInProject(projectId, userId);

        // If the user exists in the project, return true
        if(userProjectEntity != null){
            logger.info("User '" + userId + "' exists in project: " + projectId);
            return true;
        }
        return false;
    }

    public UserProject getUserProject(Long userId, Long projectId) {
        UserProjectEntity userProjectEntity = userProjectDao.findUserInProject(projectId, userId);

        if(userProjectEntity != null){
            return convertToDTO(userProjectEntity);
        }
        return null;
    }

    /**
     * Method to check if a user is the creator of a project.
     * @param userId The ID of the user to be checked.
     * @param projectId The ID of the project to be checked.
     * @return boolean value indicating whether the user is the creator of the project.
     */
    public boolean isCreator(Long userId, Long projectId) {
        UserProjectEntity userProjectEntity = userProjectDao.findProjectCreator(projectId);

        // If the user is the creator of the project, return true
        if(userProjectEntity != null && userProjectEntity.getUser().getId() == userId){
            logger.info("User '" + userId + "' is the creator of project: " + projectId);
            return true;
        }

        return false;
    }

    /**
     * Retrieves a list of users who are not approved in a specific project.
     *
     * This method first retrieves a list of UserEntity objects associated with the provided project ID
     * who are not approved. It then iterates over each UserEntity, converts it to a User DTO object,
     * and adds it to a list. The list of User DTO objects is then returned.
     *
     * @param projectId The ID of the project for which to retrieve the non-approved users.
     * @return A list of User DTO objects representing the non-approved users in the specified project.
     */
    public List<UserInfoInProject> getAllUsersNotApprovedInProject(Long projectId) {
        List<UserProjectEntity> userProjectEntities = userProjectDao.findUsersNotApprovedInProject(projectId);
        List<UserInfoInProject> users = new ArrayList<>();
        for (UserProjectEntity userProjectEntity : userProjectEntities) {
            users.add(userBean.convertToDTO(userProjectEntity.getUser()));
        }
        return users;
    }
    /**
     * Retrieves a list of users who are available for a specific project.
     *
     * This method first retrieves a list of UserEntity objects who are available for the provided project ID.
     * It then iterates over each UserEntity, converts it to a User DTO object, and adds it to a list.
     * The list of User DTO objects is then returned.
     *
     * @param projectId The ID of the project for which to retrieve the available users.
     * @return A list of User DTO objects representing the available users for the specified project.
     */
    public List<UserInfoInProject> getAllUsersAvailableForProject(Long projectId) {
        List<UserEntity> userEntities = userProjectDao.findAvailableUsersForProject(projectId);
        List<UserInfoInProject> users = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            users.add(userBean.convertToDTO(userEntity));
        }
        return users;
    }

    /**
     * Method to get a list of UserProject objects associated with a specific project.
     * @param projectId The ID of the project.
     * @return List of UserProject objects associated with the project.
     */
    public List<UserProject> getUsersAssociatedWithAProject(Long projectId) {
        List<UserProjectEntity> userProjectEntities = userProjectDao.findUserProjectByProjectId(projectId);
        List<UserProject> userProjects = new ArrayList<>();
        for (UserProjectEntity userProjectEntity : userProjectEntities) {
            userProjects.add(convertToDTO(userProjectEntity));
        }
        return userProjects;
    }

    /**
     * Retrieves a list of active users in a specific project.
     *
     * This method first retrieves a list of UserProjectEntity objects associated with the provided project ID.
     * It then iterates over each UserProjectEntity, retrieves the associated UserEntity, and converts it to a User DTO object.
     * The converted User DTO objects are added to a list which is then returned.
     *
     * @param projectId The ID of the project for which to retrieve the active users.
     * @return A list of User DTO objects representing the active users in the specified project.
     */
    public List<UserInfoInProject> getUsersInAProject(Long projectId){
        List<UserProjectEntity> userProjectEntities = userProjectDao.findActiveUsersByProjectId(projectId);
        List<UserInfoInProject> users = new ArrayList<>();
        for (UserProjectEntity userProjectEntity : userProjectEntities) {
            UserEntity userEntity = userProjectEntity.getUser();
            users.add(userBean.convertToDTOWithType(userEntity, userProjectEntity.getUserType()));
        }
        return users;
    }

    /**
     * Method to get a list of UserProject objects associated with a specific user.
     * @param userId The ID of the user.
     * @return List of UserProject objects associated with the user.
     */
    public List<UserProject> getProjectsAssociatedWithAUser(Long userId) {
        List<UserProjectEntity> userProjectEntities = userProjectDao.findUserProjectByUserId(userId);
        List<UserProject> userProjects = new ArrayList<>();
        for (UserProjectEntity userProjectEntity : userProjectEntities) {
            userProjects.add(convertToDTO(userProjectEntity));
        }
        return userProjects;
    }

    /**
     * Method to get a list of active UserProject objects associated with a specific user.
     * @param userId The ID of the user.
     * @return List of active UserProject objects associated with the user.
     */
    public List<ProjectInfoUser> getActiveProjectsOfAUser(Long userId){
        List<UserProjectEntity> userProjectEntities = userProjectDao.findActiveProjectsFromAUserByUserId(userId);
        List<ProjectInfoUser> userProjects = new ArrayList<>();
        for (UserProjectEntity userProjectEntity : userProjectEntities) {
            userProjects.add(convertUserProjecttoProjectDTO(userProjectEntity));
        }
        return userProjects;
    }

    /**
     * Counts the number of projects associated with a specific user.
     * If the state parameter is 1, it counts all projects associated with the user.
     * Otherwise, it counts only the projects that are in the specified state.
     *
     * @param userId The ID of the user for whom to count the projects.
     * @param state The state of the projects to be counted. If state is 1, all projects are counted.
     * @return The number of projects associated with the specified user that are in the specified state.
     */
    public Integer countProjectsByUserId(Long userId, Integer state) {
        int count = 0;

        if (state == 1) {
            // Count all projects
            count = userProjectDao.countProjectsByUserId(userId);
        } else {
            // Count projects with the specified state
            count = userProjectDao.countProjectsByUserIdAndState(userId, state);
        }

        return count;
    }

    /**
     * Converts a UserProjectEntity object to a ProjectInfoUser DTO object.
     * The conversion involves setting the ID, name, joinedAt, and leftAt properties of the ProjectInfoUser object
     * from the corresponding properties of the UserProjectEntity object.
     * It also involves creating a new Lab object, setting its ID and name from the Lab object associated with the ProjectEntity object
     * in the UserProjectEntity object, and setting the Lab object as the lab of the ProjectInfoUser object.
     *
     * @param userProjectEntity The UserProjectEntity object to be converted.
     * @return The converted ProjectInfoUser DTO object.
     */
    public ProjectInfoUser convertUserProjecttoProjectDTO(UserProjectEntity userProjectEntity){
        ProjectInfoUser project = new ProjectInfoUser();
        project.setId(userProjectEntity.getProject().getId());
        project.setName(userProjectEntity.getProject().getName());
        project.setJoinedAt(userProjectEntity.getJoinedAt());
        project.setLeftAt(userProjectEntity.getLeftAt());
        Lab lab = new Lab();
        lab.setId(userProjectEntity.getProject().getLab().getId());
        lab.setName(userProjectEntity.getProject().getLab().getName());

        project.setLab(lab);
        return project;
    }

    /**
     * Method to convert a UserProject object to a UserProjectEntity object.
     * @param userProject The UserProject object to be converted.
     * @return The converted UserProjectEntity object.
     */
    private UserProjectEntity convertToEntity(UserProject userProject) {
        UserProjectEntity userProjectEntity = new UserProjectEntity();
        userProjectEntity.setProject(projectBean.findProject(userProject.getProjectId()));
        userProjectEntity.setUser(userBean.findUserById(userProject.getUserId()));
        userProjectEntity.setUserType(userProject.getUserType());
        userProjectEntity.setApproved(userProject.isApproved());
        userProjectEntity.setExited(userProject.isExited());
        return userProjectEntity;
    }

    /**
     * Method to get a list of active users in a specific project.
     * @param projectId The ID of the project.
     * @return List of UserEntity objects representing the active users in the project.
     */
    public List<UserEntity> getActiveUsersInProject(Long projectId) {
        List<UserProjectEntity> userProjectEntities = userProjectDao.findActiveUsersByProjectId(projectId);
        List<UserEntity> users = new ArrayList<>();
        for (UserProjectEntity userProjectEntity : userProjectEntities) {
            users.add(userProjectEntity.getUser());
        }
        return users;
    }

    /**
     * Method to convert a UserProjectEntity object to a UserProject object.
     * @param userProjectEntity The UserProjectEntity object to be converted.
     * @return The converted UserProject object.
     */
    private UserProject convertToDTO(UserProjectEntity userProjectEntity) {
        UserProject userProject = new UserProject();
        userProject.setProjectId(userProjectEntity.getProject().getId());
        userProject.setUserId(userProjectEntity.getUser().getId());
        userProject.setUserType(userProjectEntity.getUserType());
        userProject.setApproved(userProjectEntity.isApproved());
        userProject.setExited(userProjectEntity.isExited());
        return userProject;
    }

}
