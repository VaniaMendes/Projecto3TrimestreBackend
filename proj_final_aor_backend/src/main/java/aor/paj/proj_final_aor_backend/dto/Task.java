package aor.paj.proj_final_aor_backend.dto;

import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class represents a Task in the system.
 * It contains various properties related to a task and their getter and setter methods.
 */
@XmlRootElement
public class Task {

    // Unique identifier for the task
    @XmlElement
    private Long id;

    // Title of the task
    @XmlElement
    private String title;

    // Description of the task
    @XmlElement
    private String description;

    // Date and time when the task was created
    @XmlElement
    private LocalDateTime createdAt;

    // Date and time when the task was last updated
    @XmlElement
    private LocalDateTime updatedAt;

    // Start date of the task
    @XmlElement
    private LocalDateTime startDate;

    // Deadline of the task
    @XmlElement
    private LocalDateTime deadline;

    // Date and time when the task was concluded
    @XmlElement
    private LocalDateTime conclusionDate;

    // Current state of the task
    @XmlElement
    private Integer stateId;

    // Constants representing different states of the task
    @XmlElement
    public static final int PLANNED = 10;
    @XmlElement
    public static final int IN_PROGRESS = 20;
    @XmlElement
    public static final int FINISHED = 30;

    // Priority of the task
    @XmlElement
    private Integer priorityId;

    // Constants representing different priorities of the task
    @XmlElement
    public static final int LOW = 10;
    @XmlElement
    public static final int MEDIUM = 20;
    @XmlElement
    public static final int HIGH = 30;

    // Owner of the task
    @XmlElement
    private User owner;

    // Additional executors of the task
    @XmlElement
    private String additionalExecutors;

    // Project associated with the task
    @XmlElement
    private Project project;

    // Flag indicating if the task is erased
    @XmlElement
    private Boolean erased;
    @XmlElement
    private List<Task> dependencies;

    /**
     * Default constructor for the Task class.
     */
    public Task() {
    }

    // Getter and setter methods for all the properties

    /**
     * Getter for the unique identifier of the task.
     * @return id of the task.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the unique identifier of the task.
     * @param id the new id of the task.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the title of the task.
     * @return title of the task.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the title of the task.
     * @param title the new title of the task.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the description of the task.
     * @return description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description of the task.
     * @param description the new description of the task.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the creation date and time of the task.
     * @return creation date and time of the task.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Setter for the creation date and time of the task.
     * @param createdAt the new creation date and time of the task.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Getter for the last updated date and time of the task.
     * @return last updated date and time of the task.
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Setter for the last updated date and time of the task.
     * @param updatedAt the new last updated date and time of the task.
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Getter for the start date of the task.
     * @return start date of the task.
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Setter for the start date of the task.
     * @param startDate the new start date of the task.
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for the deadline of the task.
     * @return deadline of the task.
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /**
     * Setter for the deadline of the task.
     * @param deadline the new deadline of the task.
     */
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    /**
     * Getter for the conclusion date of the task.
     * @return conclusion date of the task.
     */
    public LocalDateTime getConclusionDate() {
        return conclusionDate;
    }

    /**
     * Setter for the conclusion date of the task.
     * @param conclusionDate the new conclusion date of the task.
     */
    public void setConclusionDate(LocalDateTime conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    /**
     * Getter for the current state of the task.
     * @return current state of the task.
     */
    public Integer getStateId() {
        return stateId;
    }

    /**
     * Setter for the current state of the task.
     * @param stateId the new current state of the task.
     */
    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    /**
     * Getter for the priority of the task.
     * @return priority of the task.
     */
    public Integer getPriorityId() {
        return priorityId;
    }

    /**
     * Setter for the priority of the task.
     * @param priorityId the new priority of the task.
     */
    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    /**
     * Getter for the owner of the task.
     * @return owner of the task.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Setter for the owner of the task.
     * @param owner the new owner of the task.
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Getter for the additional executors of the task.
     * @return additional executors of the task.
     */
    public String getAdditionalExecutors() {
        return additionalExecutors;
    }

    /**
     * Setter for the additional executors of the task.
     * @param additionalExecutors the new additional executors of the task.
     */
    public void setAdditionalExecutors(String additionalExecutors) {
        this.additionalExecutors = additionalExecutors;
    }

    /**
     * Getter for the project associated with the task.
     *
     * @return project associated with the task.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Setter for the project associated with the task.
     * @param project the new project associated with the task.
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Getter for the erased flag of the task.
     * @return erased flag of the task.
     */
    public Boolean getErased() {
        return erased;
    }

    /**
     * Setter for the erased flag of the task.
     * @param erased the new erased flag of the task.
     */
    public void setErased(Boolean erased) {
        this.erased = erased;
    }

    public List<Task> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Task> dependencies) {
        this.dependencies = dependencies;
    }
}