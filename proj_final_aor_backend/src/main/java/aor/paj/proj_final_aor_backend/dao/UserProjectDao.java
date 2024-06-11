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

    /**
     * Finds all UserProjectEntity instances associated with a specific project ID.
     * This method uses a named query "UserProject.findUserProjectByProjectId" to find the UserProjectEntity instances.
     *
     * @param id The ID of the project.
     * @return A list of UserProjectEntity instances associated with the specified project ID. Returns null if no match is found or an exception occurs.
     */
    public List<UserProjectEntity> findUserProjectByProjectId(Long id) {
        try {
            return em.createNamedQuery("UserProject.findUserProjectByProjectId", UserProjectEntity.class).setParameter("id", id).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds all UserProjectEntity instances associated with a specific user ID.
     * This method uses a named query "UserProject.findUserProjectByUserId" to find the UserProjectEntity instances.
     *
     * @param id The ID of the user.
     * @return A list of UserProjectEntity instances associated with the specified user ID. Returns null if no match is found or an exception occurs.
     */
    public List<UserProjectEntity> findUserProjectByUserId(Long id) {
        try {
            return em.createNamedQuery("UserProject.findUserProjectByUserId", UserProjectEntity.class).setParameter("id", id).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds all active UserProjectEntity instances associated with a specific project ID.
     * This method uses a named query "UserProject.findActiveUsersByProjectId" to find the UserProjectEntity instances.
     *
     * @param id The ID of the project.
     * @return A list of active UserProjectEntity instances associated with the specified project ID. Returns null if no match is found or an exception occurs.
     */
    public List<UserProjectEntity> findActiveUsersByProjectId(Long id) {
        try {
            return em.createNamedQuery("UserProject.findActiveUsersByProjectId", UserProjectEntity.class).setParameter("id", id).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds all active UserProjectEntity instances associated with a specific user ID.
     * This method uses a named query "UserProject.findActiveProjectsFromAUserByUserId" to find the UserProjectEntity instances.
     *
     * @param id The ID of the user.
     * @return A list of active UserProjectEntity instances associated with the specified user ID. Returns null if no match is found or an exception occurs.
     */
    public List<UserProjectEntity> findActiveProjectsFromAUserByUserId(Long id) {
        try {
            return em.createNamedQuery("UserProject.findActiveProjectsFromAUserByUserId", UserProjectEntity.class).setParameter("id", id).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds the UserProjectEntity instance associated with a specific project ID that represents the project creator.
     * This method uses a named query "UserProject.findProjectCreator" to find the UserProjectEntity instance.
     *
     * @param id The ID of the project.
     * @return The UserProjectEntity instance that represents the project creator. Returns null if no match is found or an exception occurs.
     */
    public UserProjectEntity findProjectCreator(Long id) {
        try {
            return em.createNamedQuery("UserProject.findProjectCreator", UserProjectEntity.class).setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a UserProjectEntity by the given project ID and user ID.
     * This method uses a named query "UserProject.findUserInProject" to find the UserProjectEntity.
     *
     * @param projectId The ID of the project.
     * @param userId The ID of the user.
     * @return The UserProjectEntity that matches the given project ID and user ID. Returns null if no match is found or an exception occurs.
     */
    public UserProjectEntity findUserInProject(Long projectId, Long userId) {
        try {
            return em.createNamedQuery("UserProject.findUserInProject", UserProjectEntity.class).setParameter("projectId", projectId).setParameter("userId", userId).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Counts the number of active users by the given project ID.
     * This method uses a named query "UserProject.countActiveUsersByProjectId" to count the active users.
     *
     * @param id The ID of the project.
     * @return The number of active users in the project. Returns null if an exception occurs.
     */
    public Integer countActiveUsersByProjectId(Long id) {
        try {
            return em.createNamedQuery("UserProject.countActiveUsersByProjectId", Long.class).setParameter("id", id).getSingleResult().intValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Counts the number of projects by the given user ID.
     * This method uses a named query "UserProject.countProjectsByUserId" to count the projects.
     *
     * @param id The ID of the user.
     * @return The number of projects associated with the user. Returns null if an exception occurs.
     */
    public Integer countProjectsByUserId(Long id) {
        try {
            return em.createNamedQuery("UserProject.countProjectsByUserId", Long.class).setParameter("id", id).getSingleResult().intValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Counts the number of projects by the given user ID and state ID.
     * This method uses a named query "UserProject.countProjectsByUserIdAndState" to count the projects.
     *
     * @param id The ID of the user.
     * @param stateId The ID of the state.
     * @return The number of projects associated with the user and in the given state. Returns null if an exception occurs.
     */
    public Integer countProjectsByUserIdAndState(Long id, Integer stateId) {
        try {
            return em.createNamedQuery("UserProject.countProjectsByUserIdAndState", Long.class).setParameter("id", id).setParameter("stateId", stateId).getSingleResult().intValue();
        } catch (Exception e) {
            return null;
        }
    }

}
