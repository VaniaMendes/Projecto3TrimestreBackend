package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

/**
 * This class is responsible for handling the business logic for the ProjectInfoUser entity.
 * It is responsible for creating, deleting and associating ProjectInfoUser to users.
 */
@XmlRootElement
public class ProjectInfoUser {

    /**
     * Unique identifier for ProjectInfoUser.
     */
    @XmlElement
    private long id;

    /**
     * Name of the ProjectInfoUser. It is unique and cannot be updated.
     */
    @XmlElement
    private String name;
    /**
     * Date when the user joined the project.
     */
    @XmlElement
    private LocalDateTime joinedAt;
    /**
     * Date when the user left the project.
     */
    @XmlElement
    private LocalDateTime leftAt;
    /**
     * Lab associated with the project.
     */
    @XmlElement
    private Lab lab;

    @XmlElement
    private String stateId;


    /**
     * Default constructor for ProjectInfoUser.
     */
    public ProjectInfoUser() {
    }


    /**
     * Getter for the 'id' field.
     * @return long - The id of the ProjectInfoUser.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the 'id' field.
     * @param id - The new id of the ProjectInfoUser.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the 'name' field.
     * @return String - The name of the ProjectInfoUser.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the 'name' field.
     * @param name - The new name of the ProjectInfoUser.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the 'joinedAt' field.
     * @return LocalDateTime - The date when the user joined the project.
     */

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    /**
     * Setter for the 'joinedAt' field.
     * @param joinedAt - The new date when the user joined the project.
     */
    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    /**
     * Getter for the 'leftAt' field.
     * @return LocalDateTime - The date when the user left the project.
     */
    public LocalDateTime getLeftAt() {
        return leftAt;
    }

    /**
     * Setter for the 'leftAt' field.
     * @param leftAt - The new date when the user left the project.
     */
    public void setLeftAt(LocalDateTime leftAt) {
        this.leftAt = leftAt;
    }

    /**
     * Getter for the 'lab' field.
     * @return Lab - The lab associated with the project.
     */
    public Lab getLab() {
        return lab;
    }

    /**
     * Setter for the 'lab' field.
     * @param lab - The new lab associated with the project.
     */
    public void setLab(Lab lab) {
        this.lab = lab;
    }

    /**
     * Getter for the 'stateId' field.
     * @return String - The stateId of the ProjectInfoUser.
     */

    public String getStateId() {
        return stateId;
    }

    /**
     * Setter for the 'stateId' field.
     * @param stateId - The new stateId of the ProjectInfoUser.
     */
    public void setStateId(String stateId) {
        this.stateId = stateId;
    }
}
