package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ActivityDao;
import aor.paj.proj_final_aor_backend.dto.Activity;
import aor.paj.proj_final_aor_backend.dto.IdAndNameDTO;
import aor.paj.proj_final_aor_backend.entity.ActivityEntity;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.util.enums.ProjectActivityType;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ActivityBean implements Serializable {

    private static Logger logger = LogManager.getLogger(ActivityBean.class);

    @EJB
    private ActivityDao activityDao;

    @EJB
    private UserBean userBean;

    @EJB
    private ProjectBean projectBean;

    public ActivityBean() {
    }

    public ActivityBean (ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public void registerActivity(ProjectEntity projectEntity, ProjectActivityType type, UserEntity author, String observation) {
        ActivityEntity activityEntity = new ActivityEntity();

        activityEntity.setType(type);
        activityEntity.setAuthor(author);
        activityEntity.setProject(projectEntity);
        activityEntity.setObservation(observation);

        activityDao.persist(activityEntity);
    }

    public List<Activity> getActivitiesFromProject(Long projectId) {
        List<ActivityEntity> activityEntities = activityDao.findAllFromProject(projectId);
        List<Activity> activities = new ArrayList<>();
        for (ActivityEntity activityEntity : activityEntities) {
            activities.add(convertToDTO(activityEntity));
        }
        return activities;
    }

    private ActivityEntity convertToEntity(Activity activity) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(activity.getId());
        activityEntity.setCreatedAt(activity.getCreatedAt());
        activityEntity.setType(activity.getType());
        activityEntity.setObservation(activity.getObservation());
        return activityEntity;
    }

    private Activity convertToDTO(ActivityEntity activityEntity) {
        Activity activity = new Activity();
        activity.setId(activityEntity.getId());
        activity.setCreatedAt(activityEntity.getCreatedAt());
        activity.setType(activityEntity.getType());
        activity.setAuthor(convertToDTOAuthorInfo(userBean.findUserById(activityEntity.getAuthor().getId())));
        activity.setProject(convertToDTOProjectInfo(activityEntity.getProject()));
        activity.setObservation(activityEntity.getObservation());
        return activity;
    }

    private IdAndNameDTO convertToDTOProjectInfo(ProjectEntity projectEntity) {
        IdAndNameDTO project = new IdAndNameDTO();
        project.setId(projectEntity.getId());
        project.setName(projectEntity.getName());
        return project;
    }

    private IdAndNameDTO convertToDTOAuthorInfo(UserEntity userEntity) {
        IdAndNameDTO user = new IdAndNameDTO();
        user.setId(userEntity.getId());
        String fullName = userEntity.getFirstName() + " " + userEntity.getLastName();
        user.setName(fullName);
        return user;
    }

}