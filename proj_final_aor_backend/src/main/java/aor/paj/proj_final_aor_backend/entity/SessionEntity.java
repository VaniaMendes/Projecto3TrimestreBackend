package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="sessions")
public class SessionEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;
    @Column(name = "initSession", nullable = false)
    private LocalDateTime initSession;

    @Column(name = "endSession", nullable = false)
    private LocalDateTime endSession;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

    public SessionEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getInitSession() {
        return initSession;
    }

    public void setInitSession(LocalDateTime initSession) {
        this.initSession = initSession;
    }

    public LocalDateTime getEndSession() {
        return endSession;
    }

    public void setEndSession(LocalDateTime endSession) {
        this.endSession = endSession;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
