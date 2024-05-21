package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user_project")
@IdClass(UserProjectId.class)

@NamedQuery(name = "UserProject.findUserProjectByProjectId", query = "SELECT up FROM UserProjectEntity up WHERE up.project.id = :id")
@NamedQuery(name = "UserProject.findUserProjectByUserId", query = "SELECT up FROM UserProjectEntity up WHERE up.user.id = :id")
public class UserProjectEntity implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name="userType", nullable = false)
    private String userType;

    @Column(name="approved", nullable = false)
    private boolean approved;

    @Column(name="exited", nullable = false)
    private boolean exited;

    @OneToMany(mappedBy = "receiverGroup")
    private Set<MessageEntity> messagesReceived;

    public UserProjectEntity() {
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Set<MessageEntity> getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(Set<MessageEntity> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    public boolean isExited() {
        return exited;
    }

    public void setExited(boolean exited) {
        this.exited = exited;
    }
}
