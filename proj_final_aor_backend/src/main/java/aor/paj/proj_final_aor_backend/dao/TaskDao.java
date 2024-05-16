package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.TaskEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

/**
 * Stateless session bean that provides CRUD operations for TaskEntity.
 */
@Stateless
public class TaskDao extends AbstractDao<TaskEntity>{

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public TaskDao() {
        super(TaskEntity.class);
    }

    /**
     * Retrieves all tasks from the database.
     * @return a list of all tasks, or an empty list if no tasks are found.
     */
    public List<TaskEntity> findAllTasks() {
        try {
            return em.createNamedQuery("Task.findAllTasks", TaskEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a task by its ID.
     * @param id the ID of the task to retrieve.
     * @return the task with the given ID, or null if no such task is found.
     */
    public TaskEntity findTaskById(Long id) {
        try {
            return (TaskEntity) em.createNamedQuery("Task.findTaskById").setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Retrieves all tasks associated with a given project.
     * @param projectId the ID of the project.
     * @return a list of tasks associated with the project, or an empty list if no tasks are found.
     */
    public List<TaskEntity> findTasksByProject(Long projectId) {
        try {
            return em.createNamedQuery("Task.findTasksByProject", TaskEntity.class).setParameter("project", projectId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all tasks assigned to a given user.
     * @param responsibleUserId the ID of the user.
     * @return a list of tasks assigned to the user, or an empty list if no tasks are found.
     */
    public List<TaskEntity> findTasksByUser(Long responsibleUserId) {
        try {
            return em.createNamedQuery("Task.findTasksByUser", TaskEntity.class).setParameter("responsibleUser", responsibleUserId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all tasks with a given state.
     * @param stateId the ID of the state.
     * @return a list of tasks with the given state, or an empty list if no tasks are found.
     */
    public List<TaskEntity> findTasksByState(Integer stateId) {
        try {
            return em.createNamedQuery("Task.findTasksByState", TaskEntity.class).setParameter("stateId", stateId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all tasks with a given priority.
     * @param priorityId the ID of the priority.
     * @return a list of tasks with the given priority, or an empty list if no tasks are found.
     */
    public List<TaskEntity> findTasksByPriority(Integer priorityId) {
        try {
            return em.createNamedQuery("Task.findTasksByPriority", TaskEntity.class).setParameter("priorityId", priorityId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

}
