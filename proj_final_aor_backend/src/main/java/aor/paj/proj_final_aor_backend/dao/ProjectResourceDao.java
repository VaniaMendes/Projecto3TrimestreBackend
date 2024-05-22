package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.ProjectResourceEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProjectResourceDao extends AbstractDao<ProjectResourceEntity>{

    private static final long serialVersionUID = 1L;

    public ProjectResourceDao() {
        super(ProjectResourceEntity.class);
    }

    public List<ProjectResourceEntity> findAllResourcesFromProject(Long projectId) {
        try {
            return em.createNamedQuery("ProjectResource.findAllResourcesFromProject", ProjectResourceEntity.class)
                    .setParameter("projectId", projectId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public List<ProjectResourceEntity> findAllProjectsFromResource(Long resourceId) {
        try {
            return em.createNamedQuery("ProjectResource.findAllProjectsFromResource", ProjectResourceEntity.class)
                    .setParameter("resourceId", resourceId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public ProjectResourceEntity findResourceFromProject(Long projectId, Long resourceId) {
        try {
            return em.createNamedQuery("ProjectResource.findResourceFromProject", ProjectResourceEntity.class)
                    .setParameter("projectId", projectId)
                    .setParameter("resourceId", resourceId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
