package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ProjectDao;
import aor.paj.proj_final_aor_backend.dto.Project;
import aor.paj.proj_final_aor_backend.dto.Resource;
import aor.paj.proj_final_aor_backend.entity.LabEntity;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class ProjectBean implements Serializable {

    private static final Logger logger = LogManager.getLogger(ProjectBean.class);

    @EJB
    private ProjectDao projectDao;

    @EJB
    private LabBean labBean;

    public ProjectBean() {
    }

    public ProjectBean(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

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

    public ProjectEntity findProjectById(String id) {
        return projectDao.findProjectById(id);
    }

    public List<Project> getAllProjectsLatesteToOldest() {
        return projectDao.findAllProjectsOrderedDESC().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

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
        project.setDeadline(projectEntity.getDeadline());

        return project;
    }


}
