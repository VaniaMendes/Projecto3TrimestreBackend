package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ProjectDao;
import aor.paj.proj_final_aor_backend.dto.Project;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

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

    private Project convertToDto(ProjectEntity projectEntity) {
        Project project = new Project();
        project.setId(projectEntity.getId());
        project.setName(projectEntity.getName());
        project.setDescription(projectEntity.getDescription());
        project.setStateId(projectEntity.getStateId());
        project.setKeywords(projectEntity.getKeywords());
        project.setMaxMembers(projectEntity.getMaxMembers());
        project.setLab(labBean.convertToDto(projectEntity.getLab()));
        project.setNeeds(projectEntity.getNeeds());
        project.setCreatedAt(projectEntity.getCreatedAt());
        project.setUpdatedAt(projectEntity.getUpdatedAt());
        project.setConclusionDate(projectEntity.getConclusionDate());
        project.setInitialDate(projectEntity.getInitialDate());
        project.setDeadline(projectEntity.getDeadline());

        return project;
    }


}
