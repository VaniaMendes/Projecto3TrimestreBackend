package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.UserProjectEntity;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class UserProjectDao extends AbstractDao<UserProjectEntity>{

    private static final long serialVersionUID = 1L;

    public UserProjectDao() {
        super(UserProjectEntity.class);
    }

    public List<UserProjectEntity> findUserProjectByProjectId(Long id) {
        try {
            return em.createNamedQuery("UserProject.findUserProjectByProjectId", UserProjectEntity.class).setParameter("id", id).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<UserProjectEntity> findUserProjectByUserId(Long id) {
        try {
            return em.createNamedQuery("UserProject.findUserProjectByUserId", UserProjectEntity.class).setParameter("id", id).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<UserProjectEntity> findActiveUsersByProjectId(Long id) {
        try {
            return em.createNamedQuery("UserProject.findActiveUsersByProjectId", UserProjectEntity.class).setParameter("id", id).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<UserProjectEntity> findActiveProjectsFromAUserByUserId(Long id) {
        try {
            return em.createNamedQuery("UserProject.findActiveProjectsFromAUserByUserId", UserProjectEntity.class).setParameter("id", id).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
