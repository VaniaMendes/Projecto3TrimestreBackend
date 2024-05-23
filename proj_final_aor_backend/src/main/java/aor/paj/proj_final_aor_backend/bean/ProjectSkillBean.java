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

    public boolean exists(ProjectEntity projectEntity, SkillEntity skillEntity) {
        return projectSkillDao.findSkillFromProject(projectEntity.getId(), skillEntity.getId()) != null;
    }

    public boolean addSkillToProject(ProjectEntity projectEntity, SkillEntity skillEntity) {

        if (exists(projectEntity, skillEntity)) {
            logger.error("Skill already exists in project");
            return false;
        }

        ProjectSkillEntity projectSkillEntity = new ProjectSkillEntity();
        projectSkillEntity.setProject(projectEntity);
        projectSkillEntity.setSkill(skillEntity);
        projectSkillEntity.setActiveStatus(true);

        projectSkillDao.persist(projectSkillEntity);
        logger.info("Skill added to project");
        return true;
    }

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

    public ArrayList<Skill> getSkillsOfProject(Long projectId) {
        List<ProjectSkillEntity> projectSkillEntities = projectSkillDao.findAllSkillsFromProject(projectId);
        ArrayList<Skill> skills = new ArrayList<>();
        for (ProjectSkillEntity projectSkillEntity : projectSkillEntities) {
            SkillEntity skillEntity = projectSkillEntity.getSkill();
            skills.add(skillBean.convertToDTO(skillEntity));
        }
        return skills;
    }


}
