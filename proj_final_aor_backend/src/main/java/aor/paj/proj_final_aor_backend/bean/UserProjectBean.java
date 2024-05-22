package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.UserProjectDao;
import aor.paj.proj_final_aor_backend.dto.UserProject;
import aor.paj.proj_final_aor_backend.entity.UserProjectEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserProjectBean implements Serializable {

    private static final long serialVersionUID = 1L;

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
        userProjectEntity.setProject(projectBean.findProjectById(userProject.getProjectId()));
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
