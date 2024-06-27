package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "settings")
public class AppSettingsEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "timeout")
    private int sessionTimeout;

    @Column(name = "max_users_per_project")
    private int maxUsersPerProject;

    /**
     * Default constructor for the AppSettingsEntity class.
     */
    public AppSettingsEntity() {
    }

    /**
     * Getter for the id field.
     * @return  the current id field.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the id field.
     * @param id the new value for the id field.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the sessionTimeout field.
     * @return the current sessionTimeout field.
     */
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * Setter for the sessionTimeout field.
     * @param sessionTimeout the new value for the sessionTimeout field.
     */
    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    /**
     * Getter for the maxUsersPerProject field.
     * @return the current maxUsersPerProject field.
     */
    public int getMaxUsersPerProject() {
        return maxUsersPerProject;
    }

    /**
     * Setter for the maxUsersPerProject field.
     * @param maxUsersPerProject the new value for the maxUsersPerProject field.
     */
    public void setMaxUsersPerProject(int maxUsersPerProject) {
        this.maxUsersPerProject = maxUsersPerProject;
    }
}
