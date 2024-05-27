package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.ActivityEntity;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.List;

/**
 * The ActivityDao class provides data access operations for the ActivityEntity.
 * It extends the AbstractDao class, inheriting common data access operations.
 * This class is marked as Stateless, meaning it does not hold any conversational state.
 * It can be safely used by multiple clients concurrently.
 */
@Stateless
public class ActivityDao extends AbstractDao<ActivityEntity>{

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     * Calls the superclass constructor with ActivityEntity class as the parameter.
     */
    public ActivityDao() {
        super(ActivityEntity.class);
    }

    /**
     * Finds all activities associated with a project.
     * @param projectId the id of the project
     * @return a list of activities associated with the project
     */
    public List<ActivityEntity> findAllFromProject(Long projectId) {
        try {
            return em.createNamedQuery("ActivityEntity.findAllFromProject", ActivityEntity.class)
                    .setParameter("projectId", projectId)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
