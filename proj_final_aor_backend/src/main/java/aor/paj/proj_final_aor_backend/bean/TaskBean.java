package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.TaskDao;
import aor.paj.proj_final_aor_backend.dao.TaskDependencyDao;
import aor.paj.proj_final_aor_backend.dto.Task;
import aor.paj.proj_final_aor_backend.dto.TaskInfo;
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

    public boolean registerTask(Task task, long projectId, User user, List<Long> taskIdList) {

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
        if(taskIdList != null) {
            addDependentTask(taskEntity.getId(), taskIdList);
        }

        logger.info("Task registered successfully: " + taskEntity.getId());
        return true;
    }


    public boolean updateTask(long taskId, Task task, List<Long> taskIdList, long projectId) {
        ProjectEntity projectEntity = projectBean.findProjectById(projectId);

        if (projectEntity == null) {
            logger.debug("Project with ID " + projectId + " not found");
            return false;
        }

        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        if (taskEntity == null || taskEntity.isErased()) {
            logger.debug("Task with ID " + taskId + " not found or is erased");
            return false;
        }

        boolean updated = false;

        if (task.getDescription() != null) {
            taskEntity.setDescription(task.getDescription());
            updated = true;
        }
        if (task.getTitle() != null) {
            taskEntity.setTitle(task.getTitle());
            updated = true;
        }
        if (task.getStartDate() != null) {
            taskEntity.setStartDate(task.getStartDate());
            updated = true;
        }
        if (task.getDeadline() != null) {
            taskEntity.setDeadline(task.getDeadline());
            updated = true;
        }
        if (task.getPriorityId() != 0) {
            taskEntity.setPriorityId(task.getPriorityId());
            updated = true;
        }
        if (task.getAdditionalExecutors() != null) {
            taskEntity.setAdditionalExecutors(task.getAdditionalExecutors());
            updated = true;
        }

        if (updated) {
            taskDao.merge(taskEntity);
            logger.info("Task updated successfully: " + taskEntity.getId());
        } else {
            logger.debug("No fields to update for task ID: " + taskId);
        }

        if (taskIdList != null) {
            updateDependenciesTask(taskEntity.getId(), taskIdList);
        }

        return updated;
    }

    public boolean updateDependenciesTask( long taskId, List<Long> taskIdList) {
        //Check if the principal tasks exists
        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        if (taskEntity == null) {
            return false;
        }

        // Remover todas as dependências existentes para a tarefa
        List<TaskDependencyEntity> existingDependencies = taskDependencyDao.findTaskDependenciesEntitesByTaskId(taskId);
        for (TaskDependencyEntity dependency : existingDependencies) {
            dependency.setAtive(false);

        }

        for (Long dependentTaskId : taskIdList) {
            // Check if the dependent task exists
            TaskEntity dependentTaskEntity = taskDao.findTaskById(dependentTaskId);
            if (dependentTaskEntity == null || dependentTaskEntity.getId() == taskId){
                logger.error("Dependent task not found: " + dependentTaskId);
                continue; // Skip to the next dependent task if not found
            }

            // Check if the dependency already exists
            TaskDependencyEntity dependencyAlreadyExists = taskDependencyDao.dependencyExists(taskId, dependentTaskId);
            if (dependencyAlreadyExists != null) {
                logger.debug("Dependency already exists between tasks: " + taskId + " and " + dependentTaskId);
                dependencyAlreadyExists.setAtive(true);
                continue; // Skip to the next dependent task if dependency already exists
            }

            // Create the task dependency entity
            TaskDependencyEntity taskDependencyEntity = new TaskDependencyEntity();
            taskDependencyEntity.setTask(taskEntity);
            taskDependencyEntity.setDependentTask(dependentTaskEntity);
            taskDependencyEntity.setAtive(true);

            // Persist the dependency
            taskDependencyDao.createTaskDependency(taskDependencyEntity);
            logger.debug("Dependent task added successfully: " + taskDependencyEntity.getId());
        }

        return true;
    }

    public boolean updateTaskStatus(Long taskId, int newStatus) {

        if (newStatus != 10 && newStatus != 20 && newStatus != 30) {
            return false;
        }

        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        if (taskEntity == null || taskEntity.isErased()) {
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

    public List<TaskInfo> getTasksFromProjectMinimalInfo(Long projectId) {
        List<TaskEntity> taskEntities = taskDao.findTasksByProject(projectId);
        ArrayList<TaskInfo> tasks = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            TaskInfo task = convertTaskMinimalInfoToDTO(taskEntity);


            tasks.add(task);
        }
        return tasks;
    }


    public List<Task> getTasksFromProject(Long projectId) {
        List<TaskEntity> taskEntities = taskDao.findTasksByProject(projectId);
        ArrayList<Task> tasks = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            List<Task> getDependencies = getDependenciesForTask(taskEntity.getId());
            Task task = convertToDTO(taskEntity);
            if(getDependencies != null) {
                task.setDependencies(getDependencies);
            }

            tasks.add(task);
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
    public boolean addDependentTask( long taskId, List<Long> taskIdList) {
        //Check if the principal tasks exists
        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        if (taskEntity == null) {
            return false;
        }

        for (Long dependentTaskId : taskIdList) {
            // Check if the dependent task exists
            TaskEntity dependentTaskEntity = taskDao.findTaskById(dependentTaskId);
            if (dependentTaskEntity == null || dependentTaskEntity.getId() == taskId){
                logger.error("Dependent task not found: " + dependentTaskId);
                continue; // Skip to the next dependent task if not found
            }

            // Check if the dependency already exists
            TaskDependencyEntity dependencyAlreadyExists = taskDependencyDao.dependencyExists(taskId, dependentTaskId);
            if (dependencyAlreadyExists != null) {
                logger.warn("Dependency already exists between tasks: " + taskId + " and " + dependentTaskId);
                continue; // Skip to the next dependent task if dependency already exists
            }

            // Create the task dependency entity
            TaskDependencyEntity taskDependencyEntity = new TaskDependencyEntity();
            taskDependencyEntity.setTask(taskEntity);
            taskDependencyEntity.setDependentTask(dependentTaskEntity);
            taskDependencyEntity.setAtive(true);

            // Persist the dependency
            taskDependencyDao.createTaskDependency(taskDependencyEntity);
            logger.debug("Dependent task added successfully: " + taskDependencyEntity.getId());
        }
        return true;
    }

    public Task getTaskInfo(Long taskId) {
        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        List<Task> getDependencies = getDependenciesForTask(taskEntity.getId());
        if (taskEntity == null) {
            return null;
        } else {
            Task task = convertToDTO(taskEntity);
            if(getDependencies != null) {
                task.setDependencies(getDependencies);
            }
            return task;
        }
    }


    public boolean softDeleteTask(Long taskId) {
        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        if (taskEntity == null) {
            return false;
        } else {
            taskEntity.setErased(true);
            taskDao.merge(taskEntity);
            logger.info("Task erased: " + taskEntity.getId());
            return true;
        }
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

    public List<Task> getDependenciesForTask(Long taskId) {
        List<TaskEntity> dependencies = taskDependencyDao.findDependenciesByTaskId(taskId);
        List<Task> dependentTasks = new ArrayList<>();
        for (TaskEntity dependency : dependencies) {
            dependentTasks.add(convertToDTO(dependency));
        }
        return dependentTasks;
    }
    private TaskInfo convertTaskMinimalInfoToDTO(TaskEntity taskEntity) {
        TaskInfo task = new TaskInfo();
        task.setId(taskEntity.getId());
        task.setTitle(taskEntity.getTitle());

        return task;
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
