package aor.paj.proj_final_aor_backend.entity;

import aor.paj.proj_final_aor_backend.util.enums.ProjectActivityType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * This class represents an ActivityEntity in the system.
 * It contains various properties related to an activity and their getter and setter methods.
 */
@Entity
@Table(name = "activity")
public class ActivityEntity implements Serializable {

    // Unique identifier for serialization
    private static final long serialVersionUID = 1L;

    // Unique identifier for the activity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    // Date and time when the activity was created
    @Column(name = "created_at", nullable = false, unique = false, updatable = false)
    private LocalDateTime createdAt;

    // Type of the activity
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, unique = false, updatable = false)
    private ProjectActivityType type;

    // Project associated with the activity
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private ProjectEntity project;

    /**
     * Default constructor for the ActivityEntity class.
     */
    public ActivityEntity() {
    }

    /**
     * Getter for the unique identifier of the activity.
     * @return id of the activity.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the unique identifier of the activity.
     * @param id the new id of the activity.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the creation date and time of the activity.
     * @return creation date and time of the activity.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Setter for the creation date and time of the activity.
     * @param createdAt the new creation date and time of the activity.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Getter for the type of the activity.
     * @return type of the activity.
     */
    public ProjectActivityType getType() {
        return type;
    }

    /**
     * Setter for the type of the activity.
     * @param type the new type of the activity.
     */
    public void setType(ProjectActivityType type) {
        this.type = type;
    }
}