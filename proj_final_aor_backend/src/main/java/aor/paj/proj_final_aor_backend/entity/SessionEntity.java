package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class represents a SessionEntity in the system.
 */
@Entity
@Table(name="sessions")
@NamedQuery(name = "Session.findUserIDbyToken", query = "SELECT s FROM SessionEntity s WHERE s.token = :token")
@NamedQuery(name = "Session.findSessionByUserId", query = "SELECT s FROM SessionEntity s WHERE s.user.id = :userId")
public class SessionEntity implements Serializable {

    /**
     * Unique identifier for session
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;

    /**
     * Initial timestamp of the session
     */
    @Column(name = "initSession", nullable = false)
    private LocalDateTime initSession;

    /**
     * End timestamp of the session
     */
    @Column(name = "endSession", nullable = true)
    private LocalDateTime endSession;

    /**
     * Token of the session
     */
    @Column(name = "token", unique = true)
    private String token;

    /**
     * User of the session
     */
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;


    /**
     * Default constructor of the SessionEntity class.
     */
    public SessionEntity() {
    }


    /**
     * Getter for the unique identifier of the session.
     * @return id of the session.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the unique identifier of the session.
     * @param id the new id of the session.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the initial timestamp of the session.
     * @return initial timestamp of the session.
     */
    public LocalDateTime getInitSession() {
        return initSession;
    }

    /**
     * Setter for the initial timestamp of the session.
     * @param initSession the new initial timestamp of the session.
     */
    public void setInitSession(LocalDateTime initSession) {
        this.initSession = initSession;
    }

    /**
     * Getter for the end timestamp of the session.
     * @return end timestamp of the session.
     */
    public LocalDateTime getEndSession() {
        return endSession;
    }

    /**
     * Setter for the end timestamp of the session.
     * @param endSession the new end timestamp of the session.
     */
    public void setEndSession(LocalDateTime endSession) {
        this.endSession = endSession;
    }

    /**
     * Getter for the token of the session.
     * @return token of the session.
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * Setter for the token of the session.
     * @param user the new token of the session.
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * Getter for the token of the session.
     * @return token of the session.
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter for the token of the session.
     * @param token the new token of the session.
     */
    public void setToken(String token) {
        this.token = token;
    }


}
