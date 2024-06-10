package aor.paj.proj_final_aor_backend.entity;

import aor.paj.proj_final_aor_backend.util.enums.UserTypeInProject;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * The UserProjectEntity class represents the "user_project" table in the database.
 * It contains information about the association between a user and a project.
 *
 * It includes NamedQueries for common database operations related to this entity.
 */
@Entity
@Table(name = "user_project")
@IdClass(UserProjectId.class)

// NamedQuery to find UserProjectEntity by project id
@NamedQuery(name = "UserProject.findUserProjectByProjectId", query = "SELECT up FROM UserProjectEntity up WHERE up.project.id = :id")

// NamedQuery to find UserProjectEntity by user id
@NamedQuery(name = "UserProject.findUserProjectByUserId", query = "SELECT up FROM UserProjectEntity up WHERE up.user.id = :id")

// NamedQuery to find active users by project id
@NamedQuery(name = "UserProject.findActiveUsersByProjectId", query = "SELECT up FROM UserProjectEntity up WHERE up.project.id = :id AND up.approved = true AND up.exited = false")

// NamedQuery to find active projects of a user by user id
@NamedQuery(name = "UserProject.findActiveProjectsFromAUserByUserId", query = "SELECT up FROM UserProjectEntity up WHERE up.user.id = :id AND up.approved = true AND up.exited = false")

@NamedQuery(name = "UserProject.findProjectCreator", query = "SELECT up FROM UserProjectEntity up WHERE up.project.id = :id AND up.userType = aor.paj.proj_final_aor_backend.util.enums.UserTypeInProject.CREATOR")

@NamedQuery(name = "UserProject.findUserInProject", query = "SELECT up FROM UserProjectEntity up WHERE up.project.id = :projectId AND up.user.id = :userId")


@NamedQuery(name = "UserProject.countActiveUsersByProjectId", query = "SELECT COUNT(up) FROM UserProjectEntity up WHERE up.project.id = :id AND up.approved = true AND up.exited = false")
@NamedQuery(name = "UserProject.countProjectsByUserId", query = "SELECT COUNT(up) FROM UserProjectEntity up WHERE up.user.id = :id AND up.approved = true AND up.exited = false")
public class UserProjectEntity implements Serializable {

    // Represents the project associated with this UserProjectEntity
    @Id
    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    // Represents the user associated with this UserProjectEntity
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // Represents the type of the user in the project
    @Column(name="userType", nullable = false)
    private UserTypeInProject userType;

    // Represents whether the user is approved in the project
    @Column(name="approved", nullable = false)
    private boolean approved;

    // Represents whether the user has exited the project
    @Column(name="exited", nullable = false)
    private boolean exited;

    // Represents the date and time when the user joined the project
    // This field is automatically set to the current date and time when the entity is persisted
    // It is not updatable, meaning that once set, it cannot be changed
    @Column(name="joinedAt")
    private LocalDateTime joinedAt;

    // Represents the date and time when the user left the project
    // This field is nullable, meaning that it can be null if the user has not yet left the project
    @Column(name="leftAt", nullable = true)
    private LocalDateTime leftAt;

    // Represents the messages received by the user in the project
    @OneToMany(mappedBy = "receiverGroup")
    private Set<MessageEntity> messagesReceived = new HashSet<>();

    // Default constructor
    public UserProjectEntity() {
    }

    /**
     * Returns the project associated with this UserProjectEntity.
     *
     * @return the project associated with this UserProjectEntity
     */
    public ProjectEntity getProject() {
        return project;
    }

    /**
     * Sets the project associated with this UserProjectEntity.
     *
     * @param project the project to associate with this UserProjectEntity
     */
    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    /**
     * Returns the user associated with this UserProjectEntity.
     *
     * @return the user associated with this UserProjectEntity
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * Sets the user associated with this UserProjectEntity.
     *
     * @param user the user to associate with this UserProjectEntity
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * Returns the type of the user in the project.
     *
     * @return the type of the user in the project
     */
    public UserTypeInProject getUserType() {
        return userType;
    }

    /**
     * Sets the type of the user in the project.
     *
     * @param userType the type of the user in the project
     */
    public void setUserType(UserTypeInProject userType) {
        this.userType = userType;
    }

    /**
     * Returns whether the user is approved in the project.
     *
     * @return true if the user is approved in the project, false otherwise
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * Sets whether the user is approved in the project.
     *
     * @param approved true if the user is approved in the project, false otherwise
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * Returns the messages received by the user in the project.
     *
     * @return the messages received by the user in the project
     */
    public Set<MessageEntity> getMessagesReceived() {
        return messagesReceived;
    }

    /**
     * Sets the messages received by the user in the project.
     *
     * @param messagesReceived the messages received by the user in the project
     */
    public void setMessagesReceived(Set<MessageEntity> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    /**
     * Returns whether the user has exited the project.
     *
     * @return true if the user has exited the project, false otherwise
     */
    public boolean isExited() {
        return exited;
    }

    /**
     * Sets whether the user has exited the project.
     *
     * @param exited true if the user has exited the project, false otherwise
     */
    public void setExited(boolean exited) {
        this.exited = exited;
    }

    /**
     * Returns the date and time when the user joined the project.
     *
     * @return the date and time when the user joined the project
     */
    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    /**
     * Sets the date and time when the user joined the project.
     *
     * @param joinedAt the date and time when the user joined the project
     */
    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    /**
     * Returns the date and time when the user left the project.
     *
     * @return the date and time when the user left the project
     */
    public LocalDateTime getLeftAt() {
        return leftAt;
    }

    /**
     * Sets the date and time when the user left the project.
     *
     * @param leftAt the date and time when the user left the project
     */
    public void setLeftAt(LocalDateTime leftAt) {
        this.leftAt = leftAt;
    }
}