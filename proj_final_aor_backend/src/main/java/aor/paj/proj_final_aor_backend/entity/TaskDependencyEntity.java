package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * This class represents a TaskDependency in the system.
 * It contains various properties related to a task dependency and their getter and setter methods.
 */
@Entity
@Table(name = "task_dependency")
@NamedQuery(name = "TaskDependency.findDependency", query = "SELECT td FROM TaskDependencyEntity td WHERE td.task.id = :taskId AND td.dependentTask.id = :dependentTaskId")
public class TaskDependencyEntity implements Serializable {

    // Unique identifier for serialization
    private static final long serialVersionUID = 1L;

    // Unique identifier for the task dependency
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    // Task associated with the dependency
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskEntity task;

    // Dependent task associated with the dependency
    @ManyToOne
    @JoinColumn(name = "dependent_task_id", nullable = false)
    private TaskEntity dependentTask;

    /**
     * Default constructor for the TaskDependency class.
     */
    public TaskDependencyEntity() {
    }

    /**
     * Getter for the unique identifier of the task dependency.
     * @return id of the task dependency.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the unique identifier of the task dependency.
     * @param id the new id of the task dependency.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the task associated with the dependency.
     * @return task associated with the dependency.
     */
    public TaskEntity getTask() {
        return task;
    }

    /**
     * Setter for the task associated with the dependency.
     * @param task the new task associated with the dependency.
     */
    public void setTask(TaskEntity task) {
        this.task = task;
    }

    /**
     * Getter for the dependent task associated with the dependency.
     * @return dependent task associated with the dependency.
     */
    public TaskEntity getDependentTask() {
        return dependentTask;
    }

    /**
     * Setter for the dependent task associated with the dependency.
     * @param dependentTask the new dependent task associated with the dependency.
     */
    public void setDependentTask(TaskEntity dependentTask) {
        this.dependentTask = dependentTask;
    }
}