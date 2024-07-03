package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.TaskDependencyEntity;
import aor.paj.proj_final_aor_backend.entity.TaskEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.List;

@Stateless
public class TaskDependencyDao extends AbstractDao<TaskDependencyEntity> {
    private static final long serialVersionUID = 1L;
    public TaskDependencyDao() {
        super(TaskDependencyEntity.class);
    }

    public void createTaskDependency(TaskDependencyEntity taskDependencyEntity) {
        em.persist(taskDependencyEntity);
    }

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
