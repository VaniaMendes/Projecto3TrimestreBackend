package aor.paj.proj_final_aor_backend.dao;

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

    public List<ProjectEntity> findAllProjects() {
        try {
            return em.createNamedQuery("Project.findAllProjects", ProjectEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
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
}
