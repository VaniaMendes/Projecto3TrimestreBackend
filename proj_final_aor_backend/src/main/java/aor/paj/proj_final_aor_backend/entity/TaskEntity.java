package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The TaskEntity class represents a task entity in the database.
 * It is annotated as an Entity, meaning it is a JPA entity.
 * The Table annotation specifies the details of the table that will be used to create the table in the database.
 * It implements Serializable, which means that an object of this class can be converted to a sequence of bytes and that sequence can be reverted back into an object.
 */
@Entity
@Table(name = "task")
@NamedQuery(name = "Task.findAllTasks", query = "SELECT t FROM TaskEntity t")
@NamedQuery(name = "Task.findTaskById", query = "SELECT t FROM TaskEntity t WHERE t.id = :id")
@NamedQuery(name = "Task.findTasksByProject", query = "SELECT t FROM TaskEntity t WHERE t.project = :project")
@NamedQuery(name = "Task.findTasksByUser", query = "SELECT t FROM TaskEntity t WHERE t.responsibleUser = :responsibleUser")
@NamedQuery(name = "Task.findTasksByState", query = "SELECT t FROM TaskEntity t WHERE t.stateId = :stateId")
@NamedQuery(name = "Task.findTasksByPriority", query = "SELECT t FROM TaskEntity t WHERE t.priorityId = :priorityId")

public class TaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The ID of the task. It is the primary key of the task table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    /**
     * The title of the task.
     */
    @Column(name = "title", nullable = false, unique = false, length = 100)
    private String title;

    /**
     * The description of the task.
     */
    @Column(name = "description", nullable = false, unique = false)
    private String description;

    /**
     * The state ID of the task.
     */
    @Column(name = "stateId", nullable = false, unique = false, updatable = true)
    private Integer stateId;

    /**
     * The priority ID of the task.
     */
    @Column(name = "priorityId", nullable = false, unique = false, updatable = true)
    private Integer priorityId;

    /**
     * The creation timestamp of the task. It is automatically generated.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, unique = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * The start date of the task.
     */
    @Column(name = "start_date", nullable = false, unique = false, updatable = true)
    private LocalDateTime startDate;

    /**
     * The deadline of the task.
     */
    @Column(name = "deadline", nullable = false, unique = false, updatable = true)
    private LocalDateTime deadline;

    /**
     * The updated timestamp of the task.
     */
    @Column(name = "updated_at", nullable = false, unique = false, updatable = true)
    private LocalDateTime updatedAt;

    /**
     * The conclusion date of the task.
     */
    @Column(name = "conclusion_date", nullable = true, unique = false, updatable = true)
    private LocalDateTime conclusionDate;

    /**
     * The erased status of the task.
     */
    @Column(name = "erased", nullable = false, unique = false, updatable = true)
    private Boolean erased;

    /**
     * The additional executors of the task.
     */
    @Column(name = "additional_executors", nullable = true, unique = false, updatable = true)
    private String additionalExecutors;

    /**
     * The project of the task. It is a many-to-one relationship with the ProjectEntity.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private ProjectEntity project;

    /**
     * The user responsible for the task. It is a many-to-one relationship with the UserEntity.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity responsibleUser;

    /**
     * Default constructor.
     */
    public TaskEntity() {
    }

    /**
     * Returns the ID of the task.
     * @return id of the task
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the task.
     * @param id the new ID of the task
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the title of the task.
     * @return title of the task
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the task.
     * @param title the new title of the task
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the description of the task.
     * @return description of the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     * @param description the new description of the task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the state ID of the task.
     * @return state ID of the task
     */
    public Integer getStateId() {
        return stateId;
    }

    /**
     * Sets the state ID of the task.
     * @param stateId the new state ID of the task
     */
    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    /**
     * Returns the priority ID of the task.
     * @return priority ID of the task
     */
    public Integer getPriorityId() {
        return priorityId;
    }

    /**
     * Sets the priority ID of the task.
     * @param priorityId the new priority ID of the task
     */
    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    /**
     * Returns the creation timestamp of the task.
     * @return creation timestamp of the task
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the task.
     * @param createdAt the new creation timestamp of the task
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the start date of the task.
     * @return start date of the task
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the task.
     * @param startDate the new start date of the task
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the deadline of the task.
     * @return deadline of the task
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /**
     * Sets the deadline of the task.
     * @param deadline the new deadline of the task
     */
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    /**
     * Returns the updated timestamp of the task.
     * @return updated timestamp of the task
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the updated timestamp of the task.
     * @param updatedAt the new updated timestamp of the task
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Returns the conclusion date of the task.
     * @return conclusion date of the task
     */
    public LocalDateTime getConclusionDate() {
        return conclusionDate;
    }

    /**
     * Sets the conclusion date of the task.
     * @param conclusionDate the new conclusion date of the task
     */
    public void setConclusionDate(LocalDateTime conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    /**
     * Returns the erased status of the task.
     * @return erased status of the task
     */
    public Boolean isErased() {
        return erased;
    }

    /**
     * Sets the erased status of the task.
     * @param erased the new erased status of the task
     */
    public void setErased(Boolean erased) {
        this.erased = erased;
    }

    /**
     * Returns the additional executors of the task.
     * @return additional executors of the task
     */
    public String getAdditionalExecutors() {
        return additionalExecutors;
    }

    /**
     * Sets the additional executors of the task.
     * @param additionalExecutors the new additional executors of the task
     */
    public void setAdditionalExecutors(String additionalExecutors) {
        this.additionalExecutors = additionalExecutors;
    }

    /**
     * Returns the project of the task.
     * @return project of the task
     */
    public ProjectEntity getProject() {
        return project;
    }

    /**
     * Sets the project of the task.
     * @param project the new project of the task
     */
    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    /**
     * Returns the user responsible for the task.
     * @return responsibleUser - the user responsible for the task
     */
    public UserEntity getResponsibleUser() {
        return responsibleUser;
    }

    /**
     * Sets the user responsible for the task.
     * @param responsibleUser - the new user responsible for the task
     */
    public void setResponsibleUser(UserEntity responsibleUser) {
        this.responsibleUser = responsibleUser;
    }

    public Boolean getErased() {
        return erased;
    }
}