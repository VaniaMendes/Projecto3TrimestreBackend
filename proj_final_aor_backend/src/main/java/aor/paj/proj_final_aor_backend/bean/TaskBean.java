package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.TaskDao;
import aor.paj.proj_final_aor_backend.dao.TaskDependencyDao;
import aor.paj.proj_final_aor_backend.dto.Project;
import aor.paj.proj_final_aor_backend.dto.Task;
import aor.paj.proj_final_aor_backend.dto.TaskInfo;
import aor.paj.proj_final_aor_backend.dto.User;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import aor.paj.proj_final_aor_backend.entity.TaskDependencyEntity;
import aor.paj.proj_final_aor_backend.entity.TaskEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
import aor.paj.proj_final_aor_backend.websocket.WebsocketTask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for handling the business logic for the Task entity.
 * It is responsible for creating, updating, deleting and associating tasks to projects.
 */
@Stateless
public class TaskBean implements Serializable {

    /**
     * Logger for the TaskBean class.
     */
    private static Logger logger = LogManager.getLogger(TaskBean.class);
    /**
     * ObjectMapper instance to serialize and deserialize objects.
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * TaskDao instance to interact with the database.
     */
    @EJB
    TaskDao taskDao;

    /**
     * UserBean instance to interact with the database.
     */
    @EJB
    UserBean userBean;

    /**
     * ProjectBean instance to interact with the database.
     */
    @EJB
    ProjectBean projectBean;
    /**
     * TaskDependencyDao instance to interact with the database.
     */
    @EJB
    TaskDependencyDao taskDependencyDao;
    /**
     * WebsocketTask instance to interact with the websocket.
     */
    @EJB
    WebsocketTask websocketTask;

    /**
     * Default constructor for the TaskBean class.
     */
    public TaskBean() {
    }

    /**
     * Constructor for the TaskBean class.
     *
     * @param taskDao The TaskDao instance to interact with the database.
     */
    public TaskBean(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    /**
     * This method registers a new task in the database.
     *
     * @param task       The task to be registered.
     * @param projectId  The id of the project to associate the task with.
     * @param user       The user responsible for the task.
     * @param taskIdList The list of task ids that the task depends on.
     * @return True if the task was registered successfully, false otherwise.
     */
    public boolean registerTask(Task task, long projectId, User user, List<Long> taskIdList) {

        //Check if the task is valid
        if (!validateTask(task)) {
            logger.debug("Task validation failed");
            return false;
        }
        //Check if the project exists
        ProjectEntity projectEntity = projectBean.findProjectById(projectId);

        if (projectEntity == null) {
            logger.debug("Project with ID " + projectId + " not found");
            return false;
        }
        // If task owner is not specified, set the current user as owner
        if (task.getOwner() == null || task.getOwner().getId() == 0) {
            task.setOwner(user); // Set the current user as the owner of the task
        } else {
            // Ensure the owner entity is properly set in the task
            UserEntity userEntity = userBean.convertUserDtoToEntity(task.getOwner());
            task.setOwner(userBean.convertUserEntityToDto(userEntity));
        }


        // Create the task entity
        TaskEntity taskEntity = convertToEntity(task);
        taskEntity.setResponsibleUser(userBean.convertUserDtoToEntity(task.getOwner()));
        taskEntity.setErased(false);
        taskEntity.setUpdatedAt(LocalDateTime.now());
        taskEntity.setCreatedAt(LocalDateTime.now());
        taskEntity.setStateId(10);
        taskEntity.setProject(projectEntity);

        // Persist the task in the database
        taskDao.persist(taskEntity);

        //Check if the task has dependencies
        if (taskIdList != null) {
            //If the task has dependencies, add them
            addDependentTask(taskEntity.getId(), taskIdList);
        }

        logger.info("Task registered successfully: " + taskEntity.getId());


        // Send the task to the project by websocket
        try {
            String jsonTask = mapper.writeValueAsString(convertToDTO(taskEntity));
            websocketTask.sendTaskToProject(jsonTask, projectId);
            logger.debug("Task sent to project with id: " + projectId);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing the task: " + e.getMessage());
        }
        return true;
    }


    /**
     * This method creates a final task for a project, when this one is created.
     * @param project The project to create the final task.
     * @param user The user responsible for the task.
     */
    public void createFinalTaskOfProject (ProjectEntity project, User user) {

        // Create the final task
        Task task = new Task();
        task.setTitle("Final Presentation");
        task.setDescription("This is the final presentation of the project.");
        // Set the conclusion date of the project as the conclusion date of the task
        LocalDateTime conclusionDateTime = project.getConclusionDate();

        // Set start and end times for the task
        LocalDateTime startDateTime = conclusionDateTime.toLocalDate().atStartOfDay(); //00:00:00.000000000
        LocalDateTime endDateTime = conclusionDateTime.toLocalDate().atTime(LocalTime.MAX); // 23:59:59.999999999

        // Set the task dates
        task.setStartDate(startDateTime);
        task.setDeadline(endDateTime);
        task.setPriorityId(10);
        task.setErased(false);

        // Register the task in the database
        registerTask(task, project.getId(), user, null);
    }


    /**
     * This method updates a task in the database.
     *
     * @param taskId     The id of the task to be updated.
     * @param task       The task with the new information.
     * @param taskIdList The list of task ids that the task depends on.
     * @param projectId  The id of the project to associate the task with.
     * @return True if the task was updated successfully, false otherwise.
     */
    public boolean updateTask(long taskId, Task task, List<Long> taskIdList, long projectId) {
        //Check if the project exists
        ProjectEntity projectEntity = projectBean.findProjectById(projectId);


        if (projectEntity == null) {
            logger.debug("Project with ID " + projectId + " not found");
            return false;
        }

        //Check if the task exists
        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        if (taskEntity == null || taskEntity.isErased()) {
            logger.debug("Task with ID " + taskId + " not found or is erased");
            return false;
        }

        boolean updated = false;

        // Update the task fields
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
        if(task.getOwner().getId() != 0){
            User userEntity = userBean.getUserById(task.getOwner().getId());
            taskEntity.setResponsibleUser(userBean.convertUserDtoToEntity(userEntity));
            updated = true;
        }


        if (task.getAdditionalExecutors() != null) {
            taskEntity.setAdditionalExecutors(task.getAdditionalExecutors());
            updated = true;
        }

        // Update the task in the database
        if (updated) {
            taskDao.merge(taskEntity);
            logger.info("Task updated successfully: " + taskEntity.getId());
        } else {
            logger.debug("No fields to update for task ID: " + taskId);
        }

        // Update the dependencies
        if (taskIdList != null) {
            updateDependenciesTask(taskEntity.getId(), taskIdList);
        }

        // Send the task to the project by websocket
        try {
            String jsonTask = mapper.writeValueAsString(convertToDTO(taskEntity));
            websocketTask.sendTaskToProject(jsonTask, projectId);
            logger.debug("Task sent to project with id: " + projectId);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing the task: " + e.getMessage());
        }
        return updated;
    }


    /**
     * This method updates the dependencies of a task in the database when the task is updated.
     *
     * @param taskId     The id of the task to update the dependencies.
     * @param taskIdList The list of task ids that the task depends on.
     * @return True if the dependencies were updated successfully, false otherwise.
     */
    public boolean updateDependenciesTask(long taskId, List<Long> taskIdList) {
        //Check if the principal tasks exists
        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        if (taskEntity == null) {
            return false;
        }

        // Put the existing dependencies inactive
        List<TaskDependencyEntity> existingDependencies = taskDependencyDao.findTaskDependenciesEntitesByTaskId(taskId);
        for (TaskDependencyEntity dependency : existingDependencies) {
            dependency.setAtive(false);

        }

        // Update the dependencies
        for (Long dependentTaskId : taskIdList) {
            // Check if the dependent task exists, if not continue to the next dependent task
            TaskEntity dependentTaskEntity = taskDao.findTaskById(dependentTaskId);
            if (dependentTaskEntity == null || dependentTaskEntity.getId() == taskId) {
                logger.error("Dependent task not found: " + dependentTaskId);
                continue; // Skip to the next dependent task if not found
            }

            // Check if the dependency already exists. If it does, set it active and continue to the next dependent task
            TaskDependencyEntity dependencyAlreadyExists = taskDependencyDao.dependencyExists(taskId, dependentTaskId);
            if (dependencyAlreadyExists != null) {
                logger.debug("Dependency already exists between tasks: " + taskId + " and " + dependentTaskId);
                // Set the dependency active
                dependencyAlreadyExists.setAtive(true);
                continue;
            }

            // If the dependency does not exist, create a new one
            TaskDependencyEntity taskDependencyEntity = new TaskDependencyEntity();
            taskDependencyEntity.setTask(taskEntity);
            taskDependencyEntity.setDependentTask(dependentTaskEntity);
            taskDependencyEntity.setAtive(true);

            // Persist the dependency in the database
            taskDependencyDao.createTaskDependency(taskDependencyEntity);
            logger.debug("Dependent task added successfully: " + taskDependencyEntity.getId());
        }

        return true;
    }



    /**
     * This method updates the status of a task in the database.
     *
     * @param taskId    The id of the task to update the status.
     * @param newStatus The new status of the task.
     * @param projectId The id of the project to associate the task with.
     * @return True if the status was updated successfully, false otherwise.
     */
    public boolean updateTaskStatus(Long taskId, int newStatus, Long projectId) {
        //Check if the new status is valid
        if (newStatus != 10 && newStatus != 20 && newStatus != 30) {
            return false;
        }

        //Check if the task exists
        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        if (taskEntity == null || taskEntity.isErased()) {
            return false;
        } else {
            //Update the task status
            taskEntity.setStateId(newStatus);
            if (newStatus == 30) {
                // If the task is concluded, set the conclusion date
                taskEntity.setConclusionDate(LocalDateTime.now());
                if (taskEntity.getStartDate().isAfter(taskEntity.getConclusionDate())) {
                    taskEntity.setStartDate(taskEntity.getConclusionDate());
                }
            }
            //Persist the task in the database
            taskDao.merge(taskEntity);
            logger.info("Task status updated: " + taskEntity.getId());
            //Send the task to the project by websocket
            try {
                String jsonTask = mapper.writeValueAsString(convertToDTO(taskEntity));
                websocketTask.sendTaskToProject(jsonTask, projectId);
                logger.debug("Task sent to project with id: " + projectId);
            } catch (JsonProcessingException e) {
                logger.error("Error serializing the task: " + e.getMessage());
            }
            return true;
        }
    }

    /**
     * This method gets all the tasks from a project with minimal information, just id and title.
     *
     * @param projectId The id of the project to get the tasks from.
     * @return List of all the tasks from the project.
     */
    public List<TaskInfo> getTasksFromProjectMinimalInfo(Long projectId) {
        //Fetch the tasks from the database
        List<TaskEntity> taskEntities = taskDao.findTasksByProject(projectId);
        ArrayList<TaskInfo> tasks = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            //Convert the task entity to a task DTO
            TaskInfo task = convertTaskMinimalInfoToDTO(taskEntity);
            tasks.add(task);
        }
        //Return the list of tasks
        return tasks;
    }


    /**
     * This method gets all the tasks from a project.
     *
     * @param projectId The id of the project to get the tasks from.
     * @return List of all the tasks from the project.
     */
    public List<Task> getTasksFromProject(Long projectId) {
        //Fetch the tasks from the database
        List<TaskEntity> taskEntities = taskDao.findTasksByProject(projectId);
        ArrayList<Task> tasks = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            //Get the dependencies for the task
            List<Task> getDependencies = getDependenciesForTask(taskEntity.getId());
            //Convert the task entity to a task DTO
            Task task = convertToDTO(taskEntity);
            if (getDependencies != null) {
                //Set the dependencies for the task
                task.setDependencies(getDependencies);
            }

            tasks.add(task);
        }
        //Return the list of tasks
        return tasks;
    }

    /**
     * This method gets all the tasks from a project.
     *
     * @param projectId The id of the project to get the tasks from.
     * @return List of all the tasks from the project.
     */
    public List<Task> getTasksFromProjectOrderByDate(Long projectId) {
        //Fetch the tasks from the database
        List<TaskEntity> taskEntities = taskDao.findTasksByProjectOrdered(projectId);
        ArrayList<Task> tasks = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            //Get the dependencies for the task
            List<Task> getDependencies = getDependenciesForTask(taskEntity.getId());
            //Convert the task entity to a task DTO
            Task task = convertToDTO(taskEntity);
            if (getDependencies != null) {
                //Set the dependencies for the task
                task.setDependencies(getDependencies);
            }

            tasks.add(task);
        }
        //Return the list of tasks
        return tasks;
    }

    /**
     * This method adds a dependent task to a principal task.
     *
     * @param taskId     The id of the principal task.
     * @param taskIdList The list of task ids that the task depends on.
     * @return True if the dependent task was added successfully, false otherwise.
     */
    public boolean addDependentTask(long taskId, List<Long> taskIdList) {
        //Check if the principal tasks exists
        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        if (taskEntity == null) {
            return false;
        }


        for (Long dependentTaskId : taskIdList) {
            // Check if the dependent task exists
            TaskEntity dependentTaskEntity = taskDao.findTaskById(dependentTaskId);
            if (dependentTaskEntity == null || dependentTaskEntity.getId() == taskId) {
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

    /**
     * This method gets the information of a task.
     *
     * @param taskId The id of the task to get the information.
     * @return The task information.
     */
    public Task getTaskInfo(Long taskId) {
        //Fetch the task from the database
        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        //Get the dependencies for the task
        List<Task> getDependencies = getDependenciesForTask(taskEntity.getId());
        if (taskEntity == null) {
            return null;
        } else {
            //Convert the task entity to a task DTO
            Task task = convertToDTO(taskEntity);
            if (getDependencies != null) {
                //Set the dependencies for the task
                task.setDependencies(getDependencies);
            }
            return task;
        }
    }


    /**
     * This method put the task erased true in the database.
     *
     * @param taskId    The id of the task to erased.
     * @param projectId The id of the project to associate the task with.
     * @return True if the task was deleted successfully, false otherwise.
     */
    public boolean softDeleteTask(Long taskId, Long projectId) {
        //Check if the task exists
        TaskEntity taskEntity = taskDao.findTaskById(taskId);
        if (taskEntity == null) {
            return false;
        } else {
            //Set the task erased true
            taskEntity.setErased(true);
            //Persist the task in the database
            taskDao.merge(taskEntity);
            logger.info("Task erased: " + taskEntity.getId());

            //Send the task to the project by websocket
            try {
                String jsonTask = mapper.writeValueAsString(convertToDTO(taskEntity));
                websocketTask.sendTaskToProject(jsonTask, projectId);
                logger.debug("Task sent to project with id: " + projectId);
            } catch (JsonProcessingException e) {
                logger.error("Error serializing the task: " + e.getMessage());
            }
            return true;
        }
    }

    /**
     * This method validates the task information to create a new one.
     *
     * @param task The task to be validated.
     * @return True if the task is valid, false otherwise.
     */
    private boolean validateTask(Task task) {
        return task.getTitle() != null
                && task.getDescription() != null
                && task.getStartDate() != null
                && task.getDeadline() != null
                && !task.getStartDate().isAfter(task.getDeadline());
    }

    /**
     * This method converts a task DTO to a task entity.
     *
     * @param task The task DTO to be converted.
     * @return The task entity.
     */
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

    /**
     * This method gets the dependencies for a task.
     *
     * @param taskId The id of the task to get the dependencies.
     * @return The list of dependent tasks.
     */
    public List<Task> getDependenciesForTask(Long taskId) {
        //Fetch the dependencies from the database
        List<TaskEntity> dependencies = taskDependencyDao.findDependenciesByTaskId(taskId);
        List<Task> dependentTasks = new ArrayList<>();
        for (TaskEntity dependency : dependencies) {
            //Convert the task entity to a task DTO
            dependentTasks.add(convertToDTO(dependency));
        }
        return dependentTasks;
    }


    /**
     * This method converts a task entity to a task DTO with minimal information.
     *
     * @param taskEntity The task entity to be converted.
     * @return The task DTO with minimal information.
     */
    private TaskInfo convertTaskMinimalInfoToDTO(TaskEntity taskEntity) {
        TaskInfo task = new TaskInfo();
        task.setId(taskEntity.getId());
        task.setTitle(taskEntity.getTitle());

        return task;
    }

    /**
     * This method converts a task entity to a task DTO.
     *
     * @param taskEntity The task entity to be converted.
     * @return The task DTO.
     */
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
