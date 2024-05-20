package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

/**
 * This class represents a Project in the system.
 * It contains various properties related to a project and their getter and setter methods.
 */
@XmlRootElement
public class Project {

    // Unique identifier for the project
    @XmlElement
    private Long id;

    // Name of the project
    @XmlElement
    private String name;

    // Description of the project
    @XmlElement
    private String description;

    // Date and time when the project was created
    @XmlElement
    private LocalDateTime createdAt;

    // Date and time when the project was last updated
    @XmlElement
    private LocalDateTime updatedAt;

    // Initial date of the project
    @XmlElement
    private LocalDateTime initialDate;

    // Final date of the project
    @XmlElement
    private LocalDateTime deadline;

    // Date and time when the project was concluded
    @XmlElement
    private LocalDateTime conclusionDate;

    // Keywords related to the project
    @XmlElement
    private String keywords;

    // Maximum number of members in the project
    @XmlElement
    private int maxMembers;

    // Lab associated with the project
    @XmlElement
    private Lab lab;

    @XmlElement
    private String needs;

    @XmlElement
    private Integer vacancyNumber;

    // Current state of the project
    @XmlElement
    private int stateId;

    // Constants representing different states of the project
    @XmlElement
    public static final int PLANNING = 100;
    @XmlElement
    public static final int READY = 200;
    @XmlElement
    public static final int APPROVED = 300;
    @XmlElement
    public static final int IN_PROGRESS = 400;
    @XmlElement
    public static final int FINISHED = 500;
    @XmlElement
    public static final int CANCELLED = 600;

    /**
     * Default constructor for the Project class.
     */
    public Project() {
    }

    /**
     * Getter for the unique identifier of the project.
     * @return id of the project.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the unique identifier of the project.
     * @param id the new id of the project.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the name of the project.
     * @return name of the project.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the project.
     * @param name the new name of the project.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the description of the project.
     * @return description of the project.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description of the project.
     * @param description the new description of the project.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the creation date and time of the project.
     * @return creation date and time of the project.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Setter for the creation date and time of the project.
     * @param createdAt the new creation date and time of the project.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Getter for the last updated date and time of the project.
     * @return last updated date and time of the project.
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Setter for the last updated date and time of the project.
     * @param updatedAt the new last updated date and time of the project.
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Getter for the initial date of the project.
     * @return initial date of the project.
     */
    public LocalDateTime getInitialDate() {
        return initialDate;
    }

    /**
     * Setter for the initial date of the project.
     * @param initialDate the new initial date of the project.
     */
    public void setInitialDate(LocalDateTime initialDate) {
        this.initialDate = initialDate;
    }

    /**
     * Getter for the final date of the project.
     * @return final date of the project.
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /**
     * Setter for the final date of the project.
     * @param deadline the new final date of the project.
     */
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    /**
     * Getter for the conclusion date of the project.
     * @return conclusion date of the project.
     */
    public LocalDateTime getConclusionDate() {
        return conclusionDate;
    }

    /**
     * Setter for the conclusion date of the project.
     * @param conclusionDate the new conclusion date of the project.
     */
    public void setConclusionDate(LocalDateTime conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    /**
     * Getter for the keywords related to the project.
     * @return keywords related to the project.
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * Setter for the keywords related to the project.
     * @param keywords the new keywords related to the project.
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * Getter for the maximum number of members in the project.
     * @return maximum number of members in the project.
     */
    public int getMaxMembers() {
        return maxMembers;
    }

    /**
     * Setter for the maximum number of members in the project.
     * @param maxMembers the new maximum number of members in the project.
     */
    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    /**
     * Getter for the lab associated with the project.
     * @return lab associated with the project.
     */
    public Lab getLab() {
        return lab;
    }

    /**
     * Setter for the lab associated with the project.
     * @param lab the new lab associated with the project.
     */
    public void setLab(Lab lab) {
        this.lab = lab;
    }

    /**
     * Getter for the current state of the project.
     * @return current state of the project.
     */
    public int getStateId() {
        return stateId;
    }

    /**
     * Setter for the current state of the project.
     * @param stateId the new current state of the project.
     */
    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    /**
     * Gets the needs of the project.
     *
     * @return A string representing the specific needs of the project.
     */
    public String getNeeds() {
        return needs;
    }

    /**
     * Sets the needs of the project.
     *
     * @param needs A string representing the specific needs to be set for the project.
     */
    public void setNeeds(String needs) {
        this.needs = needs;
    }

    /**
     * Gets the number of vacancies available in the project.
     *
     * @return An Integer representing the number of vacancies available in the project.
     */
    public Integer getVacancyNumber() {
        return vacancyNumber;
    }

    /**
     * Sets the number of vacancies available in the project.
     *
     * @param vacancyNumber An Integer representing the number of vacancies to be set for the project.
     */
    public void setVacancyNumber(Integer vacancyNumber) {
        this.vacancyNumber = vacancyNumber;
    }
}