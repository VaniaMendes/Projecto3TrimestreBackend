package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.LabEntity;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProjectDao extends AbstractDao<ProjectEntity> {

    private static final long serialVersionUID = 1L;

    public ProjectDao() {
        super(ProjectEntity.class);
    }

    public List<ProjectEntity> findAllProjectsOrderedASC() {
        try {
            return em.createNamedQuery("Project.findAllProjects", ProjectEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public List<ProjectEntity> findAllProjectsOrderedDESC() {
        try {
            return em.createNamedQuery("Project.findAllProjectsOrderedDESC", ProjectEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public ProjectEntity findProjectById(String id) {
        try {
            return (ProjectEntity) em.createNamedQuery("Project.findProjectById").setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public ProjectEntity findProjectByName(String name) {
        try {
            return (ProjectEntity) em.createNamedQuery("Project.findProjectByName").setParameter("name", name)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ProjectEntity> findProjectsByLab(LabEntity lab) {
        try {
            return em.createNamedQuery("Project.findProjectsByLab", ProjectEntity.class).setParameter("lab", lab)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public List<ProjectEntity> findProjectsByStateOrderedASC(int state) {
        try {
            return em.createNamedQuery("Project.findProjectsByStateOrderedASC", ProjectEntity.class).setParameter("stateId", state)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public List<ProjectEntity> findProjectsByStateOrderedDESC(int state) {
        try {
            return em.createNamedQuery("Project.findProjectsByStateOrderedDESC", ProjectEntity.class).setParameter("stateId", state)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public List<ProjectEntity> findProjectsByKeyword(String keyword) {
        try {
            return em.createNamedQuery("Project.findProjectsByKeyword", ProjectEntity.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }
}
