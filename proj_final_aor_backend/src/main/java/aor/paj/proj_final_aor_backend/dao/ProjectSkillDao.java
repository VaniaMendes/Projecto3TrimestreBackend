package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.ProjectSkillEntity;
import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProjectSkillDao extends AbstractDao<ProjectSkillEntity>{

    private static final long serialVersionUID = 1L;

    public ProjectSkillDao() {
        super(ProjectSkillEntity.class);
    }

    public List<ProjectSkillEntity> findAllSkillsFromProject(Long projectId) {
        try {
            return em.createNamedQuery("ProjectSkill.findAllSkillsFromProject", ProjectSkillEntity.class)
                    .setParameter("projectId", projectId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public List<ProjectSkillEntity> findAllProjectsFromSkill(Long skillId) {
        try {
            return em.createNamedQuery("ProjectSkill.findAllProjectsFromSkill", ProjectSkillEntity.class)
                    .setParameter("skillId", skillId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public ProjectSkillEntity findSkillFromProject(Long projectId, Long skillId) {
        try {
            return em.createNamedQuery("ProjectSkill.findSkillFromProject", ProjectSkillEntity.class)
                    .setParameter("projectId", projectId)
                    .setParameter("skillId", skillId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<SkillEntity> findAllSkillsNotInProject(Long projectId) {
        try {
            return em.createNamedQuery("ProjectSkill.findAllSkillsNotInProject", SkillEntity.class)
                    .setParameter("projectId", projectId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }
}
