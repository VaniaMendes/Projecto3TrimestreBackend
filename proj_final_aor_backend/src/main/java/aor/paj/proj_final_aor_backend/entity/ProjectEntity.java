package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a ProjectEntity in the system.
 * It contains various properties related to a project and their getter and setter methods.
 */
@Entity
@Table(name = "project")
public class ProjectEntity implements Serializable {

    // Unique identifier for serialization
    private static final long serialVersionUID = 1L;

    // Unique identifier for the project
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    // Name of the project
    @Column(name = "name", nullable = false, unique = true, updatable = true)
    private String name;

    // Description of the project
    @Column(name = "description", nullable = true, unique = false, updatable = true)
    private String description;

    // Date and time when the project was created
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, unique = false, updatable = false)
    private LocalDateTime createdAt;

    // Initial date of the project
    @Column(name = "initial_date", nullable = false, unique = false, updatable = true)
    private LocalDateTime initialDate;

    // Deadline of the project
    @Column(name = "deadline", nullable = false, unique = false, updatable = true)
    private LocalDateTime deadline;

    // Conclusion date of the project
    @Column(name = "conclusion_date", nullable = true, unique = false, updatable = true)
    private LocalDateTime conclusionDate;

    // State ID of the project
    @Column(name = "stateId", nullable = false, unique = false, updatable = true)
    private int stateId;

    // Keywords related to the project
    @Column(name = "keywords", nullable = false, unique = false, updatable = true)
    private String keywords;

    // Maximum number of members in the project
    @Column(name = "max_members", nullable = false, unique = false, updatable = true)
    private int maxMembers;

    // Set of activities associated with the project
    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ActivityEntity> activities = new HashSet<>();

    // Lab associated with the project
    @ManyToOne
    @JoinColumn(name = "lab_id", referencedColumnName = "id")
    private LabEntity lab;

    // Set of resources associated with the project
    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ProjectResource> resources = new HashSet<>();

    // Set of tasks associated with the user
    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TaskEntity> tasks = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<UserProject> userProjects = new HashSet<>();


    /**
     * The skills associated with the projects. It is a many-to-many relationship with the SkillEntity.
     */
    @ManyToMany
    @JoinTable(
            name = "project_skill",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<SkillEntity> skills = new HashSet<>();


    /**
     * Default constructor for the ProjectEntity class.
     */
    public ProjectEntity() {
    }

    // Getter and setter methods for all the properties

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
     * Getter for the deadline of the project.
     * @return deadline of the project.
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /**
     * Setter for the deadline of the project.
     * @param deadline the new deadline of the project.
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
     * Getter for the state ID of the project.
     * @return state ID of the project.
     */
    public int getStateId() {
        return stateId;
    }

    /**
     * Setter for the state ID of the project.
     * @param stateId the new state ID of the project.
     */
    public void setStateId(int stateId) {
        this.stateId = stateId;
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
     * Getter for the activities associated with the project.
     * @return activities associated with the project.
     */
    public Set<ActivityEntity> getActivities() {
        return activities;
    }

    /**
     * Setter for the activities associated with the project.
     * @param activities the new activities associated with the project.
     */
    public void setActivities(Set<ActivityEntity> activities) {
        this.activities = activities;
    }

    /**
     * Getter for the lab associated with the project.
     * @return lab associated with the project.
     */
    public LabEntity getLab() {
        return lab;
    }

    /**
     * Setter for the lab associated with the project.
     * @param lab the new lab associated with the project.
     */
    public void setLab(LabEntity lab) {
        this.lab = lab;
    }

    /**
     * Getter for the resources associated with the project.
     * @return resources associated with the project.
     */
    public Set<ProjectResource> getResources() {
        return resources;
    }

    /**
     * Setter for the resources associated with the project.
     * @param resources the new resources associated with the project.
     */
    public void setResources(Set<ProjectResource> resources) {
        this.resources = resources;
    }

    /**
     * Getter for the tasks associated with the project.
     * @return tasks associated with the project.
     */
    public Set<TaskEntity> getTasks() {
        return tasks;
    }

    /**
     * Setter for the tasks associated with the project.
     * @param tasks the new tasks associated with the project.
     */
    public void setTasks(Set<TaskEntity> tasks) {
        this.tasks = tasks;
    }
}