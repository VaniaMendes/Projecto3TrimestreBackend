package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "user_project")
public class UserProjectEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @Column(name="userType", nullable = false)
    private String userType;

    @Column(name="approved", nullable = false)
    private boolean approved;

    @OneToMany(mappedBy = "receiverGroup")
    private Set<MessageEntity> messagesReceived;

    public UserProjectEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
