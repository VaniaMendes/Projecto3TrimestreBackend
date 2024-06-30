package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.TaskDao;
import aor.paj.proj_final_aor_backend.dao.TaskDependencyDao;
import aor.paj.proj_final_aor_backend.dto.Task;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import aor.paj.proj_final_aor_backend.entity.TaskDependencyEntity;
import aor.paj.proj_final_aor_backend.entity.TaskEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class TaskBean implements Serializable {

    private static Logger logger = LogManager.getLogger(TaskBean.class);

    @EJB
   TaskDao taskDao;

    @EJB
    UserBean userBean;

    @EJB
    ProjectBean projectBean;
    @EJB
    TaskDependencyDao taskDependencyDao;


    public TaskBean() {
    }

    public TaskBean (TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public boolean registerTask(Task task, long projectId, User user) {

        if (!validateTask(task)) {
            logger.debug("Task validation failed");
            return false;
        }

        ProjectEntity projectEntity = projectBean.findProjectById(projectId);

        if(projectEntity == null) {
            logger.debug("Project with ID " + projectId + " not found");
            return false;
        }
        // Criar a entidade da tarefa e associar ao projeto existente
        TaskEntity taskEntity = convertToEntity(task);
        taskEntity.setResponsibleUser(userBean.convertUserDtoToEntity(user));
        taskEntity.setErased(false);
        taskEntity.setUpdatedAt(LocalDateTime.now());
        taskEntity.setCreatedAt(LocalDateTime.now());
        taskEntity.setStateId(10);
        taskEntity.setProject(projectEntity);

        // Persistir a entidade da tarefa
        taskDao.persist(taskEntity);

        logger.info("Task registered successfully: " + taskEntity.getId());
        return true;
    }

    public boolean updateTaskStatus(Long taskId, int newStatus) {

        if (newStatus != 10 && newStatus != 20 && newStatus != 30) {
            return false;
        }

        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        if (taskEntity == null) {
            return false;
        }else {
            taskEntity.setStateId(newStatus);
            if (newStatus == 30) {
                taskEntity.setConclusionDate(LocalDateTime.now());
                if (taskEntity.getStartDate().isAfter(taskEntity.getConclusionDate())) {
                    taskEntity.setStartDate(taskEntity.getConclusionDate());
                }
            }
            taskDao.merge(taskEntity);
            logger.info("Task status updated: " + taskEntity.getId());
            return true;
        }
    }

    public List<Task> getTasksFromProject(Long projectId) {
        List<TaskEntity> taskEntities = taskDao.findTasksByProject(projectId);
        ArrayList<Task> tasks = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            tasks.add(convertToDTO(taskEntity));
        }
        return tasks;
    }

    public List<Task> getTasksByUser(Long userId) {
        List<TaskEntity> taskEntities = taskDao.findTasksByUser(userId);
        ArrayList<Task> tasks = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            tasks.add(convertToDTO(taskEntity));
        }
        return tasks;
    }
    public boolean addDependentTask(Task dependency, long taskId) {
        //Check if the principal tasks exists
        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        if (taskEntity == null) {
            return false;
        }

        // Verifica se a tarefa dependente existe
        TaskEntity dependentTaskEntity = taskDao.findTaskById(dependency.getId());


        TaskDependencyEntity dependencyAlreadyExists = taskDependencyDao.dependencyExists(taskId, dependency.getId());

        if(dependencyAlreadyExists != null) {
            return false;
        }

        // Cria a entidade de dependência de tarefas
        TaskDependencyEntity taskDependencyEntity = new TaskDependencyEntity();
        taskDependencyEntity.setTask(taskEntity);
        taskDependencyEntity.setDependentTask(dependentTaskEntity);

        // Persiste a dependência
        taskDependencyDao.createTaskDependency(taskDependencyEntity);
        logger.debug("Dependent task added successfully: " + taskDependencyEntity.getId());
        return true;
    }



    private boolean validateTask(Task task) {
        return task.getTitle() != null
                && task.getDescription() != null
                && task.getStartDate() != null
                && task.getDeadline() != null
                && !task.getStartDate().isAfter(task.getDeadline());
    }

    public TaskEntity convertToEntity(Task task) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(task.getTitle());
        taskEntity.setDescription(task.getDescription());
        taskEntity.setPriorityId(task.getPriorityId());
        taskEntity.setStartDate(task.getStartDate());
        taskEntity.setDeadline(task.getDeadline());
        taskEntity.setAdditionalExecutors(task.getAdditionalExecutors());
        taskEntity.setErased(task.getErased());
        taskEntity.setUpdatedAt(task.getUpdatedAt());
        


        return taskEntity;
    }

    private Task convertToDTO(TaskEntity taskEntity) {
        Task task = new Task();
        task.setId(taskEntity.getId());
        task.setTitle(taskEntity.getTitle());
        task.setDescription(taskEntity.getDescription());
        task.setStartDate(taskEntity.getStartDate());
        task.setDeadline(taskEntity.getDeadline());
        task.setStateId(taskEntity.getStateId());
        task.setPriorityId(taskEntity.getPriorityId());
        task.setOwner(userBean.convertUserEntityToDto(taskEntity.getResponsibleUser()));
        task.setAdditionalExecutors(taskEntity.getAdditionalExecutors());
        task.setErased(taskEntity.isErased());
        task.setUpdatedAt(taskEntity.getUpdatedAt());
        return task;
    }

}
