package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.AuthenticationDao;
import aor.paj.proj_final_aor_backend.dao.UserProjectDao;
import aor.paj.proj_final_aor_backend.dto.UserProject;
import aor.paj.proj_final_aor_backend.entity.*;
import aor.paj.proj_final_aor_backend.util.enums.UserTypeInProject;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserProjectBean implements Serializable {

    // Logger instance for logging events, info, errors etc.
    private static final Logger logger = LogManager.getLogger(UserProjectBean.class);

    @EJB
    private UserProjectDao userProjectDao;

    @EJB
    private UserBean userBean;

    @EJB
    private ProjectBean projectBean;

    public UserProjectBean() {
    }

    public UserProjectBean(UserProjectDao userProjectDao) {
        this.userProjectDao = userProjectDao;
    }

    public boolean addUserToProject(UserEntity userEntity, ProjectEntity projectEntity, UserTypeInProject userType) {

        if (userType == UserTypeInProject.EXITED) {
            return false;
        }

        if (userProjectExists(userEntity.getId(), projectEntity.getId())) {
            return false;
        }

        UserProjectEntity userProjectEntity = new UserProjectEntity();
        userProjectEntity.setProject(projectEntity);
        userProjectEntity.setUser(userEntity);
        userProjectEntity.setUserType(userType);
        userProjectEntity.setExited(false);

        if (userType == UserTypeInProject.CANDIDATE) {
            userProjectEntity.setApproved(false);
        } else {
            userProjectEntity.setApproved(true);
            userProjectEntity.setJoinedAt(LocalDateTime.now());
        }

        userProjectDao.persist(userProjectEntity);
        logger.info("User added to Project");
        return true;
    }

    public boolean removeUserFromProject(Long userId, Long projectId) {
        UserProjectEntity userProjectEntity = userProjectDao.findUserInProject(projectId, userId);

        if (!userProjectExists(userId, projectId) || isCreator(userId, projectId)) {
            return false;
        }

        userProjectEntity.setExited(true);
        userProjectDao.merge(userProjectEntity);
        logger.info("User removed from Project");
        return true;
    }

    private boolean userProjectExists(Long userId, Long projectId) {
        UserProjectEntity userProjectEntity = userProjectDao.findUserInProject(projectId, userId);

        if(userProjectEntity != null){
            logger.info("User '" + userId + "' already exists in project: " + projectId);
            return true;
        }

        return false;
    }

    public boolean isCreator(Long userId, Long projectId) {
        UserProjectEntity userProjectEntity = userProjectDao.findProjectCreator(projectId);

        if(userProjectEntity != null && userProjectEntity.getUser().getId() == userId){
            logger.info("User '" + userId + "' is the creator of project: " + projectId);
            return true;
        }

        return false;
    }

    public List<UserProject> getUsersAssociatedWithAProject(Long projectId) {
        List<UserProjectEntity> userProjectEntities = userProjectDao.findUserProjectByProjectId(projectId);
        List<UserProject> userProjects = new ArrayList<>();
        for (UserProjectEntity userProjectEntity : userProjectEntities) {
            userProjects.add(convertToDTO(userProjectEntity));
        }
        return userProjects;
    }

    public List<UserProject> getUsersInAProject(Long projectId){
        List<UserProjectEntity> userProjectEntities = userProjectDao.findActiveUsersByProjectId(projectId);
        List<UserProject> userProjects = new ArrayList<>();
        for (UserProjectEntity userProjectEntity : userProjectEntities) {
            userProjects.add(convertToDTO(userProjectEntity));
        }
        return userProjects;
    }

    public List<UserProject> getProjectsAssociatedWithAUser(Long userId) {
        List<UserProjectEntity> userProjectEntities = userProjectDao.findUserProjectByUserId(userId);
        List<UserProject> userProjects = new ArrayList<>();
        for (UserProjectEntity userProjectEntity : userProjectEntities) {
            userProjects.add(convertToDTO(userProjectEntity));
        }
        return userProjects;
    }

    public List<UserProject> getActiveProjectsOfAUser(Long userId){
        List<UserProjectEntity> userProjectEntities = userProjectDao.findActiveProjectsFromAUserByUserId(userId);
        List<UserProject> userProjects = new ArrayList<>();
        for (UserProjectEntity userProjectEntity : userProjectEntities) {
            userProjects.add(convertToDTO(userProjectEntity));
        }
        return userProjects;
    }

    private UserProjectEntity convertToEntity(UserProject userProject) {
        UserProjectEntity userProjectEntity = new UserProjectEntity();
        userProjectEntity.setProject(projectBean.findProject(userProject.getProjectId()));
        userProjectEntity.setUser(userBean.findUserById(userProject.getUserId()));
        userProjectEntity.setUserType(userProject.getUserType());
        userProjectEntity.setApproved(userProject.isApproved());
        userProjectEntity.setExited(userProject.isExited());
        return userProjectEntity;
    }

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
