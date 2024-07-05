package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.TaskDependencyEntity;
import aor.paj.proj_final_aor_backend.entity.TaskEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.List;


/**
 * The TaskDependencyDao class provides data access operations for the TaskDependencyEntity.
 * It extends the AbstractDao class, inheriting common data access operations.
 * This class is marked as Stateless, meaning it does not hold any conversational state.
 */
@Stateless
public class TaskDependencyDao extends AbstractDao<TaskDependencyEntity> {
    private static final long serialVersionUID = 1L;


    /**
     * Default constructor.
     * Initializes the superclass with TaskDependencyEntity class type
     */
    public TaskDependencyDao() {
        super(TaskDependencyEntity.class);
    }


    /**
     * Creates a new TaskDependencyEntity in the database.
     *
     * @param taskDependencyEntity The TaskDependencyEntity to create.
     */
    public void createTaskDependency(TaskDependencyEntity taskDependencyEntity) {
        em.persist(taskDependencyEntity);
    }

    /**
     * Checks if a dependency exists between two tasks.
     *
     * @param taskId       The ID of the task.
     * @param dependencyId The ID of the dependent task.
     * @return The found TaskDependencyEntity, or null if no such dependency exists.
     */
    public TaskDependencyEntity dependencyExists(long taskId, long dependencyId) {
        try {
            return em.createNamedQuery("TaskDependency.findDependency", TaskDependencyEntity.class)
                    .setParameter("taskId", taskId)
                    .setParameter("dependentTaskId", dependencyId)
                    .getSingleResult();
        } catch (NoResultException e) {

            return null;
        } catch (Exception e) {

            throw new RuntimeException("Error checking dependency", e);
        }
    }


    /**
     * Finds all dependencies of a task by its ID.
     *
     * @param taskId The ID of the task.
     * @return A list of TaskEntity instances that are dependencies of the task, or null if an error occurs.
     */
    public List<TaskEntity> findDependenciesByTaskId(long taskId) {
        try {
            return em.createNamedQuery("TaskDependency.findDependenciesByTaskId", TaskEntity.class)
                    .setParameter("taskId", taskId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error finding dependencies", e);
        }
    }


    /**
     * Finds all TaskDependencyEntity instances of a task by its ID.
     *
     * @param taskId The ID of the task.
     * @return A list of TaskDependencyEntity instances that are associated with the task, or null if an error occurs.
     */
    public List<TaskDependencyEntity> findTaskDependenciesEntitesByTaskId(long taskId) {
        try {
            return em.createNamedQuery("TaskDependency.findTaskDependenciesByTaskId", TaskDependencyEntity.class)
                    .setParameter("taskId", taskId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error finding dependencies", e);
        }
    }


}
