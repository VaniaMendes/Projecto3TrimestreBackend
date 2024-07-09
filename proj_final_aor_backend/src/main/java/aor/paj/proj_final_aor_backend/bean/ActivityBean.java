package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ActivityDao;
import aor.paj.proj_final_aor_backend.dto.Activity;
import aor.paj.proj_final_aor_backend.dto.IdAndNameDTO;
import aor.paj.proj_final_aor_backend.dto.User;
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

    /**
     * Default constructor.
     */
    public ActivityBean() {
    }

    /**
     * Constructs an ActivityBean with a specific ActivityDao.
     *
     * @param activityDao The ActivityDao to be used by this bean.
     */
    public ActivityBean(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    /**
     * Registers a new activity for a given project.
     *
     * @param projectEntity The project entity to which the activity belongs.
     * @param type The type of the activity.
     * @param author The user entity who authored the activity.
     * @param observation Additional observations about the activity.
     */
    public void registerActivity(ProjectEntity projectEntity, ProjectActivityType type, UserEntity author, String observation) {
        ActivityEntity activityEntity = new ActivityEntity();

        activityEntity.setType(type);
        activityEntity.setAuthor(author);
        activityEntity.setProject(projectEntity);
        activityEntity.setObservation(observation);

        activityDao.persist(activityEntity);
    }

    /**
     * Registers a new activity of type MEMBER_COMMENT for a specific project.
     * This method first retrieves the project entity by its ID. If the project is found,
     * it proceeds to register a new activity with the MEMBER_COMMENT type, associating it
     * with the provided author and observation. If the project is not found, it logs an error.
     *
     * @param projectId The ID of the project for which the comment activity is to be registered.
     * @param author The user entity who authored the comment.
     * @param observation The content or observation of the comment.
     */
    public void registerActivityTypeMemberComment(Long projectId, User author, String observation) {
        ProjectEntity projectEntity = projectBean.findProjectById(projectId);

        if (projectEntity == null) {
            logger.error("Project not found");
            return;
        }

        UserEntity userEntity = userBean.findUserById(author.getId());

        if (userEntity == null) {
            logger.error("User not found");
            return;
        }

        registerActivity(projectEntity, ProjectActivityType.MEMBER_COMMENT, userEntity, observation);
    }

    /**
     * Retrieves all activities associated with a given project.
     *
     * @param projectId The ID of the project for which activities are being retrieved.
     * @return A list of Activity DTOs representing all activities for the specified project.
     */
    public List<Activity> getActivitiesFromProject(Long projectId) {
        List<ActivityEntity> activityEntities = activityDao.findAllFromProject(projectId);
        List<Activity> activities = new ArrayList<>();
        for (ActivityEntity activityEntity : activityEntities) {
            activities.add(convertToDTO(activityEntity));
        }
        return activities;
    }

    /**
     * Retrieves the last specified number of activities for a given project.
     *
     * @param projectId The ID of the project for which to retrieve the activities.
     * @param maxResults The maximum number of activities to retrieve.
     * @return A list of {@link Activity} DTOs representing the last activities for the specified project, up to the specified maxResults.
     */
    public List<Activity> getLastXActivitiesFromProject(Long projectId, Integer maxResults) {
        List<ActivityEntity> activityEntities = activityDao.getLastXActivitiesFromProject(projectId, maxResults);
        List<Activity> activities = new ArrayList<>();
        for (ActivityEntity activityEntity : activityEntities) {
            activities.add(convertToDTO(activityEntity));
        }
        return activities;
    }

    /**
     * Converts an {@link Activity} DTO to an {@link ActivityEntity}.
     *
     * @param activity The Activity DTO to convert.
     * @return An ActivityEntity that represents the same data as the provided Activity DTO.
     */
    private ActivityEntity convertToEntity(Activity activity) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(activity.getId());
        activityEntity.setCreatedAt(activity.getCreatedAt());
        activityEntity.setType(activity.getType());
        activityEntity.setObservation(activity.getObservation());
        return activityEntity;
    }

    /**
     * Converts an {@link ActivityEntity} to an {@link Activity} DTO.
     *
     * @param activityEntity The ActivityEntity to convert.
     * @return An Activity DTO that represents the same data as the provided ActivityEntity.
     */
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

    /**
     * Converts a {@link ProjectEntity} to an {@link IdAndNameDTO}.
     * This method is used to extract and encapsulate the ID and name information
     * from a ProjectEntity into a more simplified DTO form for easier data handling and transfer.
     *
     * @param projectEntity The ProjectEntity to convert.
     * @return An {@link IdAndNameDTO} containing the ID and name of the project.
     */
    private IdAndNameDTO convertToDTOProjectInfo(ProjectEntity projectEntity) {
        IdAndNameDTO project = new IdAndNameDTO();
        project.setId(projectEntity.getId());
        project.setName(projectEntity.getName());
        return project;
    }

    /**
     * Converts a {@link UserEntity} to an {@link IdAndNameDTO}.
     * This method is utilized to compile and encapsulate the ID and full name (first and last name)
     * from a UserEntity into a simplified DTO format, facilitating easier data management and transfer.
     *
     * @param userEntity The UserEntity to convert.
     * @return An {@link IdAndNameDTO} containing the ID and full name of the user.
     */
    private IdAndNameDTO convertToDTOAuthorInfo(UserEntity userEntity) {
        IdAndNameDTO user = new IdAndNameDTO();
        user.setId(userEntity.getId());
        String fullName = userEntity.getFirstName() + " " + userEntity.getLastName();
        user.setName(fullName);
        return user;
    }

}