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


    /**
     * Retrieves a specified number of the most recent activities associated with a given project.
     * This method queries the database for activities linked to the specified project ID,
     * limiting the results to the number defined by the maxResults parameter. It utilizes
     * a named query defined in the ActivityEntity class to fetch the data. In case of any
     * exceptions (e.g., database connection issues), an empty list is returned, ensuring
     * the method's resilience.
     *
     * @param projectId The ID of the project for which activities are being queried.
     * @param maxResults The maximum number of activity records to retrieve.
     * @return A list of ActivityEntity objects representing the most recent activities
     *         associated with the given project, limited by maxResults. Returns an empty
     *         list if the query fails or if there are no activities for the project.
     */
    public List<ActivityEntity> getLastXActivitiesFromProject(Long projectId, Integer maxResults) {
        try{
            return em.createNamedQuery("ActivityEntity.findAllFromProject", ActivityEntity.class)
                    .setParameter("projectId", projectId)
                    .setMaxResults(maxResults)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
