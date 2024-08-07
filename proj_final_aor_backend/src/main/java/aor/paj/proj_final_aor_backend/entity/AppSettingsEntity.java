package aor.paj.proj_final_aor_backend.entity;

import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.io.Serializable;

/**
 * The AppSettingsEntity class represents the settings of the application.
 * It is mapped to the "settings" table in the database.
 * This class implements Serializable, allowing instances of this class to be serialized.
 */
@Entity
@Table(name = "settings")
@NamedQuery(name = "AppSettingsEntity.findCurrentMaxUsers", query = "SELECT s.maxUsersPerProject FROM AppSettingsEntity s WHERE s.id = 1")
public class AppSettingsEntity implements Serializable {

    // Serial version UID for serialization and deserialization
    private static final long serialVersionUID = 1L;



    /**
     * The ID of the settings. It is the primary key of the "settings" table.
     * It is generated automatically and is unique and not updatable.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    /**
     * The session timeout setting. It is mapped to the "timeout" column in the "settings" table.
     */
    @Column(name = "timeout")
    private int sessionTimeout;


    /**
     * The maximum number of users per project setting. It is mapped to the "max_users_per_project" column in the "settings" table.
     */
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
