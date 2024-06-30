package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.TaskDependencyEntity;
import aor.paj.proj_final_aor_backend.entity.TaskEntity;
import jakarta.ejb.Stateless;

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
            return (TaskDependencyEntity) em.createNamedQuery("TaskDependency.findDependency")
                    .setParameter("taskId", taskId)
                    .setParameter("dependentTaskId", dependencyId)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace(); // print the exception
            return null;
        }
    }

}
