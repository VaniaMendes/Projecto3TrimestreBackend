package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * This class represents the application settings.
 * It contains various properties related to the application settings and their getter and setter methods.
 **/
@XmlRootElement
public class AppSettings {

    /**
     * The unique identifier for the application settings.
     */
    @XmlElement
    private long id;


    /**
     * The session timeout.
     */
    @XmlElement
    private int sessionTimeout;


    /**
     * The maximum number of users per project.
     */
    @XmlElement
    private int maxUsersPerProject;

    /**
     * Default constructor for the AppSettings class.
     */
    public AppSettings() {
    }

    /**
     * Getter for the id field.
     *
     * @return the current id field.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the id field.
     *
     * @param id the new value for the id field.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the sessionTimeout field.
     *
     * @return the current sessionTimeout field.
     */
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * Setter for the sessionTimeout field.
     *
     * @param sessionTimeout the new value for the sessionTimeout field.
     */
    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    /**
     * Getter for the maxUsersPerProject field.
     *
     * @return the current maxUsersPerProject field.
     */
    public int getMaxUsersPerProject() {
        return maxUsersPerProject;
    }

    /**
     * Setter for the maxUsersPerProject field.
     *
     * @param maxUsersPerProject the new value for the maxUsersPerProject field.
     */
    public void setMaxUsersPerProject(int maxUsersPerProject) {
        this.maxUsersPerProject = maxUsersPerProject;
    }
}
