package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "project_skill")
@IdClass(ProjectSkillId.class)

@NamedQuery(name = "ProjectSkill.findAllSkillsFromProject", query = "SELECT ps FROM ProjectSkillEntity ps WHERE ps.project.id = :projectId AND ps.activeStatus = true")
@NamedQuery(name = "ProjectSkill.findAllProjectsFromSkill", query = "SELECT ps FROM ProjectSkillEntity ps WHERE ps.skill.id = :skillId AND ps.activeStatus = true")
@NamedQuery(name = "ProjectSkill.findAllSkillsNotInProject", query = "SELECT s FROM SkillEntity s WHERE s.id NOT IN (SELECT ps.skill.id FROM ProjectSkillEntity ps WHERE ps.project.id = :projectId AND ps.activeStatus = true)")
@NamedQuery(name = "ProjectSkill.findSkillFromProject", query = "SELECT ps FROM ProjectSkillEntity ps WHERE ps.project.id = :projectId AND ps.skill.id = :skillId")
public class ProjectSkillEntity implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private ProjectEntity project;

    @Id
    @ManyToOne
    @JoinColumn(name = "skill_id", referencedColumnName = "id")
    private SkillEntity skill;

    @Column(name = "active_status", nullable = false)
    private Boolean activeStatus;

    public ProjectSkillEntity() {
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public SkillEntity getSkill() {
        return skill;
    }

    public void setSkill(SkillEntity skill) {
        this.skill = skill;
    }

    public Boolean getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus;
    }
}
