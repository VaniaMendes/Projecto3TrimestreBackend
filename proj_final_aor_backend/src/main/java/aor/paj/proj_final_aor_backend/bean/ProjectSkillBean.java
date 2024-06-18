package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ProjectSkillDao;
import aor.paj.proj_final_aor_backend.dto.Skill;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import aor.paj.proj_final_aor_backend.entity.ProjectSkillEntity;
import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProjectSkillBean implements Serializable {

    private static final Logger logger = LogManager.getLogger(ProjectSkillBean.class);

    @EJB
    private ProjectSkillDao projectSkillDao;

    @EJB
    private SkillBean skillBean;

    public ProjectSkillBean() {
    }

    public ProjectSkillBean(ProjectSkillDao projectSkillDao) {
        this.projectSkillDao = projectSkillDao;
    }

    /**
     * Checks if a skill is already associated with a project.
     *
     * @param projectEntity The project to check.
     * @param skillEntity The skill to check.
     * @return true if the skill is already associated with the project, false otherwise.
     */
    public boolean exists(ProjectEntity projectEntity, SkillEntity skillEntity) {
        return projectSkillDao.findSkillFromProject(projectEntity.getId(), skillEntity.getId()) != null;
    }

    /**
     * Associates a skill with a project.
     *
     * @param projectEntity The project to which the skill will be associated.
     * @param skillEntity The skill to be associated with the project.
     * @return true if the skill was successfully associated with the project, false otherwise.
     */
    public boolean associateSkillToProject(ProjectEntity projectEntity, SkillEntity skillEntity) {

        if (exists(projectEntity, skillEntity)) {
            logger.error("Skill already exists in project");
            return false;
        }

        ProjectSkillEntity projectSkillEntity = new ProjectSkillEntity();
        projectSkillEntity.setProject(projectEntity);
        projectSkillEntity.setSkill(skillEntity);
        projectSkillEntity.setActiveStatus(false);

        projectSkillDao.persist(projectSkillEntity);
        logger.info("Skill added to project");
        return true;
    }

    /**
     * Updates the status of a skill in a project.
     *
     * @param projectEntity The project in which the skill status will be updated.
     * @param skillEntity The skill whose status will be updated.
     * @param activeStatus The new status of the skill.
     * @return true if the skill status was successfully updated, false otherwise.
     */
    public boolean updateProjectSkillStatus(ProjectEntity projectEntity, SkillEntity skillEntity, boolean activeStatus) {

        ProjectSkillEntity projectSkillEntity = projectSkillDao.findSkillFromProject(projectEntity.getId(), skillEntity.getId());

        if (projectSkillEntity == null) {
            logger.error("Project and Skill connection not found");
            return false;
        }

        projectSkillEntity.setActiveStatus(activeStatus);
        projectSkillDao.merge(projectSkillEntity);
        logger.info("Project and Skill connection updated");
        return true;
    }

    /**
     * Retrieves all skills associated with a project.
     *
     * @param projectId The ID of the project whose skills will be retrieved.
     * @return A list of skills associated with the project.
     */
    public List<Skill> getSkillsOfProject(Long projectId) {
        List<ProjectSkillEntity> projectSkillEntities = projectSkillDao.findAllSkillsFromProject(projectId);
        ArrayList<Skill> skills = new ArrayList<>();
        for (ProjectSkillEntity projectSkillEntity : projectSkillEntities) {
            SkillEntity skillEntity = projectSkillEntity.getSkill();
            skills.add(skillBean.convertToDTO(skillEntity));
        }
        return skills;
    }

    /**
     * Retrieves all skills not associated with a project.
     *
     * @param projectId The ID of the project whose non-associated skills will be retrieved.
     * @return A list of skills not associated with the project.
     */
    public List<Skill> getSkillsNotInProject(Long projectId) {
        List<SkillEntity> skillEntities = projectSkillDao.findAllSkillsNotInProject(projectId);

        ArrayList<Skill> skills = new ArrayList<>();
        for (SkillEntity skillEntity : skillEntities) {
            skills.add(skillBean.convertToDTO(skillEntity));
        }
        return skills;
    }
}