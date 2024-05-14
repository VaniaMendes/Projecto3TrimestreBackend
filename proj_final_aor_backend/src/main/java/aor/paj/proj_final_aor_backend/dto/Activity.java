package aor.paj.proj_final_aor_backend.dto;

import aor.paj.proj_final_aor_backend.util.enums.ProjectActivityType;
import jakarta.xml.bind.annotation.XmlElement;

import java.time.LocalDateTime;

/**
 * This class represents an Activity made in the project.
 * Each activity has an id, creation time, type, author, and associated project.
 */
public class Activity {

    // The unique identifier for the activity
    @XmlElement
    private Long id;

    // The time when the activity was created
    @XmlElement
    private LocalDateTime createdAt;

    // The type of the activity
    @XmlElement
    private ProjectActivityType type;

    // The author of the activity
    @XmlElement
    private User author;

    // The project associated with the activity
    @XmlElement
    private Project project;

    // Default constructor
    public Activity() {
    }

    /**
     * Returns the id of the activity.
     * @return the id of the activity
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of the activity.
     * @param id the new id of the activity
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the creation time of the activity.
     * @return the creation time of the activity
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation time of the activity.
     * @param createdAt the new creation time of the activity
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the type of the activity.
     * @return the type of the activity
     */
    public ProjectActivityType getType() {
        return type;
    }

    /**
     * Sets the type of the activity.
     * @param type the new type of the activity
     */
    public void setType(ProjectActivityType type) {
        this.type = type;
    }

    /**
     * Returns the author of the activity.
     * @return the author of the activity
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Sets the author of the activity.
     * @param author the new author of the activity
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Returns the project associated with the activity.
     * @return the project associated with the activity
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the project associated with the activity.
     * @param project the new project associated with the activity
     */
    public void setProject(Project project) {
        this.project = project;
    }
}
