package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * This class represents a ProjectEntity in the system.
 * It contains various properties related to a project and their getter and setter methods.
 */
@Entity
@Table(name = "project")
@NamedQuery(name = "Project.findAllProjects", query = "SELECT p FROM ProjectEntity p")
@NamedQuery(name = "Project.findAllProjectsOrderedDESC", query = "SELECT p FROM ProjectEntity p ORDER BY p.createdAt DESC")
@NamedQuery(name = "Project.findProjectById", query = "SELECT p FROM ProjectEntity p WHERE p.id = :id")
@NamedQuery(name = "Project.findActiveProjectsByUserIdOrderedASC", query = "SELECT up.project FROM UserProjectEntity up WHERE up.user.id = :id AND up.approved = true AND up.exited = false")
@NamedQuery(name = "Project.findActiveProjectsByUserIdOrderedDESC", query = "SELECT up.project FROM UserProjectEntity up WHERE up.user.id = :id AND up.approved = true AND up.exited = false ORDER BY up.project.createdAt DESC")
@NamedQuery(name = "Project.findActiveProjectsByUserIdAndStateOrderedASC", query = "SELECT up.project FROM UserProjectEntity up WHERE up.user.id = :id AND up.approved = true AND up.exited = false AND up.project.stateId = :stateId")
@NamedQuery(name = "Project.findActiveProjectsByUserIdAndStateOrderedDESC", query = "SELECT up.project FROM UserProjectEntity up WHERE up.user.id = :id AND up.approved = true AND up.exited = false AND up.project.stateId = :stateId ORDER BY up.project.createdAt DESC")
@NamedQuery(name = "Project.findProjectByName", query = "SELECT p FROM ProjectEntity p WHERE p.name = :name ORDER BY p.createdAt DESC")
@NamedQuery(name = "Project.findProjectsByLab", query = "SELECT p FROM ProjectEntity p WHERE p.lab = :lab")
@NamedQuery(name = "Project.findProjectsByKeywordOrderedASC", query = "SELECT p FROM ProjectEntity p WHERE CONCAT(',', p.keywords, ',') LIKE CONCAT('%,', :keyword, ',%')")
@NamedQuery(name = "Project.findProjectsByKeywordOrderedDESC", query = "SELECT p FROM ProjectEntity p WHERE CONCAT(',', p.keywords, ',') LIKE CONCAT('%,', :keyword, ',%') ORDER BY p.createdAt DESC")
@NamedQuery(name = "Project.findProjectsByStateOrderedASC", query = "SELECT p FROM ProjectEntity p WHERE p.stateId = :stateId")
@NamedQuery(name = "Project.findProjectsByStateOrderedDESC", query = "SELECT p FROM ProjectEntity p WHERE p.stateId = :stateId ORDER BY p.createdAt DESC")
@NamedQuery(name = "Project.findProjectsBySkillOrderedASC", query = "SELECT p FROM ProjectEntity p JOIN p.projectSkill ps JOIN ps.skill s WHERE s.name = :skillName")
@NamedQuery(name = "Project.findProjectsBySkill", query = "SELECT p FROM ProjectEntity p JOIN p.projectSkill ps JOIN ps.skill s WHERE s.name = :skillName")
@NamedQuery(name = "Project.findAllKeywords", query = "SELECT p.keywords FROM ProjectEntity p")
@NamedQuery(name = "Project.orderByVacanciesASC", query = "SELECT p FROM ProjectEntity p " +
                "LEFT JOIN UserProjectEntity up ON p.id = up.project.id AND up.approved = true AND up.exited = false " +
                "GROUP BY p.id " +
                "ORDER BY (p.maxMembers - COUNT(up)) ASC"
)
@NamedQuery(name = "Project.orderByVacanciesDESC",
        query = "SELECT p FROM ProjectEntity p " +
                "LEFT JOIN UserProjectEntity up ON p.id = up.project.id AND up.approved = true AND up.exited = false " +
                "GROUP BY p.id " +
                "ORDER BY (p.maxMembers - COUNT(up)) DESC"
)
@NamedQuery(name = "Project.orderByVacanciesAndStateASC", query = "SELECT p FROM ProjectEntity p " +
        "LEFT JOIN UserProjectEntity up ON p.id = up.project.id AND up.approved = true AND up.exited = false " +
        "WHERE p.stateId = :stateId " +
        "GROUP BY p.id " +
        "ORDER BY (p.maxMembers - COUNT(up)) ASC"
)
@NamedQuery(name = "Project.orderByUserByVacanciesASC", query = "SELECT p FROM ProjectEntity p " +
        "LEFT JOIN UserProjectEntity up ON p.id = up.project.id AND up.approved = true AND up.exited = false " +
        "WHERE up.user.id = :userId " +
        "GROUP BY p.id " +
        "ORDER BY (p.maxMembers - COUNT(up)) ASC"
)
@NamedQuery(name = "Project.orderByUserByVacanciesDESC", query = "SELECT p FROM ProjectEntity p " +
        "LEFT JOIN UserProjectEntity up ON p.id = up.project.id AND up.approved = true AND up.exited = false " +
        "WHERE up.user.id = :userId " +
        "GROUP BY p.id " +
        "ORDER BY (p.maxMembers - COUNT(up)) DESC"
)
@NamedQuery(name = "Project.orderByVacanciesAndStateDESC", query = "SELECT p FROM ProjectEntity p " +
        "LEFT JOIN UserProjectEntity up ON p.id = up.project.id AND up.approved = true AND up.exited = false " +
        "WHERE p.stateId = :stateId " +
        "GROUP BY p.id " +
        "ORDER BY (p.maxMembers - COUNT(up)) DESC"
)

@NamedQuery(name = "Project.countAllProjects", query = "SELECT COUNT(p) FROM ProjectEntity p")
@NamedQuery(name = "Project.countProjectsByState", query = "SELECT COUNT(p) FROM ProjectEntity p WHERE p.stateId = :stateId")
@NamedQuery(name = "Project.countProjectsByKeyword", query = "SELECT COUNT(p) FROM ProjectEntity p WHERE CONCAT(',', p.keywords, ',') LIKE CONCAT('%,', :keyword, ',%')")
@NamedQuery(name = "Project.countProjectsBySkill", query = "SELECT COUNT(p) FROM ProjectEntity p JOIN p.projectSkill ps JOIN ps.skill s WHERE s.name = :skillName")

public class ProjectEntity implements Serializable {

    // Unique identifier for serialization
    private static final long serialVersionUID = 1L;

    // Unique identifier for the project
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    // Name of the project
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    // Description of the project
    @Column(name = "description", length = 1000)
    private String description;

    // Date and time when the project was created
    @CreationTimestamp
    @Column(name = "created_at", nullable = true, updatable = false)
    private LocalDateTime createdAt;

    // Date and time when the project was last updated
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Initial date of the project
    @Column(name = "initial_date")
    private LocalDateTime initialDate;

    // Conclusion date of the project
    @Column(name = "conclusion_date")
    private LocalDateTime conclusionDate;

    // State ID of the project
    @Column(name = "stateId", nullable = false)
    private int stateId;

    // Keywords related to the project
    @Column(name = "keywords", nullable = false)
    private String keywords;

    @Column(name = "max_members", nullable = false)
    private int maxMembers;

    // Needs of the project
    @Column(name = "needs")
    private String needs;

    // Set of activities associated with the project
    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ActivityEntity> activities = new HashSet<>();

    // Lab associated with the project
    @ManyToOne
    @JoinColumn(name = "lab_id", referencedColumnName = "id")
    private LabEntity lab;

    // Set of resources associated with the project
    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ProjectResourceEntity> resources = new HashSet<>();

    // Set of tasks associated with the user
    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TaskEntity> tasks = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<UserProjectEntity> userProjects = new HashSet<>();


    /**
     * The skills associated with the projects. It is a many-to-many relationship with the SkillEntity.
     */
    @OneToMany(mappedBy = "project")
    private Set<ProjectSkillEntity> projectSkill = new HashSet<>();


    /**
     * Default constructor for the ProjectEntity class.
     */
    public ProjectEntity() {
    }

    public ProjectEntity(ProjectEntity other) {
        this.id = other.id;
        this.name = other.name;
        this.description = other.description;
        this.createdAt = other.createdAt;
        this.updatedAt = other.updatedAt;
        this.initialDate = other.initialDate;
        this.conclusionDate = other.conclusionDate;
        this.stateId = other.stateId;
        this.keywords = other.keywords;
        this.maxMembers = other.maxMembers;
        this.needs = other.needs;
        this.activities = new HashSet<>(other.activities);
        this.lab = other.lab;
        this.resources = new HashSet<>(other.resources);
        this.tasks = new HashSet<>(other.tasks);
        this.userProjects = new HashSet<>(other.userProjects);
        this.projectSkill = new HashSet<>(other.projectSkill);
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
     * Sets the keywords for the project.
     * The input is a string of keywords separated by commas.
     * Each keyword is transformed so that it begins with an uppercase letter and the rest of the letters are in lowercase.
     * The transformed keywords are then joined back into a single string with commas and spaces in between.
     *
     * @param keywords A string of keywords separated by commas.
     */
    public void setKeywords(String keywords) {
        // Split the input string into individual keywords based on comma
        String[] keywordArray = keywords.split(",");

        for (int i = 0; i < keywordArray.length; i++) {
            // Split the keyword into individual words
            String[] words = keywordArray[i].trim().split(" ");

            // Loop through each word
            for (int j = 0; j < words.length; j++) {
                // Capitalize the first letter and make the rest of the letters lowercase
                words[j] = words[j].substring(0, 1).toUpperCase() + words[j].substring(1).toLowerCase();
            }

            // Join the words back into a single keyword and store it back in the array
            keywordArray[i] = String.join(" ", words);
        }

        // Join the transformed keywords back into a single string with comma and space in between
        this.keywords = String.join(",", keywordArray);
    }

    /**
     * Adds a keyword to the project's keywords.
     * The input keyword can be one or more words long.
     * Each word in the keyword is transformed so that it begins with an uppercase letter and the rest of the letters are in lowercase.
     * The transformed keyword is then added to the project's keywords if it doesn't already exist.
     *
     * @param keyword A string representing the keyword to be added.
     */
    public void addKeyword(String keyword) {
        // Split the keyword into individual words
        String[] words = keyword.split(" ");

        // Loop through each word
        for (int i = 0; i < words.length; i++) {
            // Capitalize the first letter and make the rest of the letters lowercase
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        // Join the words back into a single keyword
        keyword = String.join(" ", words);

        // Split the keywords string into individual keywords based on comma
        List<String> keywordsList = new ArrayList<>(Arrays.asList(this.keywords.split(",")));

        // Add the keyword to the list if it doesn't already exist
        if (!keywordsList.contains(keyword)) {
            keywordsList.add(keyword);
            this.keywords = String.join(",", keywordsList);
        }
    }

    /**
     * Removes a keyword from the project's keywords.
     * The input keyword can be one or more words long.
     * Each word in the keyword is transformed so that it begins with an uppercase letter and the rest of the letters are in lowercase.
     * The transformed keyword is then removed from the project's keywords if it exists and there's more than one keyword.
     *
     * @param keyword A string representing the keyword to be removed.
     */
    public void removeKeyword(String keyword) {
        // Split the keyword into individual words
        String[] words = keyword.split(" ");

        // Loop through each word
        for (int i = 0; i < words.length; i++) {
            // Capitalize the first letter and make the rest of the letters lowercase
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        // Join the words back into a single keyword
        keyword = String.join(" ", words);

        // Split the keywords string into individual keywords based on comma
        List<String> keywordsList = new ArrayList<>(Arrays.asList(this.keywords.split(",")));

        // Only remove the keyword if there's more than one keyword
        if (keywordsList.size() > 1 && keywordsList.contains(keyword)) {
            keywordsList.remove(keyword);
            this.keywords = String.join(",", keywordsList);
        }
    }

    /**
     * Checks if a keyword exists in the project's keywords.
     * The input keyword can be one or more words long.
     * Each word in the keyword is transformed so that it begins with an uppercase letter and the rest of the letters are in lowercase.
     * The transformed keyword is then checked against the project's keywords.
     *
     * @param keyword A string representing the keyword to be checked.
     * @return true if the keyword exists in the project's keywords, false otherwise.
     */
    public boolean keywordExists(String keyword) {
        // Split the keyword into individual words
        String[] words = keyword.split(" ");

        // Loop through each word
        for (int i = 0; i < words.length; i++) {
            // Capitalize the first letter and make the rest of the letters lowercase
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        // Join the words back into a single keyword
        keyword = String.join(" ", words);

        // Split the keywords string into individual keywords based on comma
        List<String> keywordsList = new ArrayList<>(Arrays.asList(this.keywords.split(",")));

        // Check if the keyword exists in the list
        return keywordsList.contains(keyword);
    }


    /**
     * This method checks if the provided keyword is the only keyword associated with the project.
     *
     * @param keyword A string representing the keyword to be checked.
     * @return true if the provided keyword is the only keyword associated with the project, false otherwise.
     */
    public boolean isKeywordOnly(String keyword) {
        // Split the keywords string into individual keywords based on comma
        List<String> keywordsList = new ArrayList<>(Arrays.asList(this.keywords.split(",")));

        // Check if the keyword is the only one in the list and if it matches the provided keyword
        return keywordsList.size() == 1 && keywordsList.get(0).equals(keyword);
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
    public Set<ProjectResourceEntity> getResources() {
        return resources;
    }

    /**
     * Setter for the resources associated with the project.
     * @param resources the new resources associated with the project.
     */
    public void setResources(Set<ProjectResourceEntity> resources) {
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
    /**
     * Getter for the needs of the project.
     * @return needs of the project.
     */
    public String getNeeds() {
        return needs;
    }

    /**
     * Setter for the needs of the project.
     * @param needs the new needs of the project.
     */
    public void setNeeds(String needs) {
        this.needs = needs;
    }

    /**
     * Getter for the user projects associated with the project.
     * This is a many-to-many relationship with the UserProjectEntity.
     * @return user projects associated with the project.
     */
    public Set<UserProjectEntity> getUserProjects() {
        return userProjects;
    }

    /**
     * Setter for the user projects associated with the project.
     * This is a many-to-many relationship with the UserProjectEntity.
     * @param userProjects the new user projects associated with the project.
     */
    public void setUserProjects(Set<UserProjectEntity> userProjects) {
        this.userProjects = userProjects;
    }

    /**
     * Setter for the tasks associated with the project.
     * It takes a Set of TaskEntity objects and assigns it to the tasks field.
     * Each TaskEntity object in the Set represents a task that is part of this project.
     *
     * @param tasks a Set of TaskEntity objects to be associated with the project
     */
    public void setTasks(Set<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    /**
     * Getter for the project skills associated with the project.
     * It returns a Set of ProjectSkillEntity objects that are associated with the project.
     * Each ProjectSkillEntity object represents a skill that is required for this project.
     *
     * @return a Set of ProjectSkillEntity objects that are associated with the project
     */
    public Set<ProjectSkillEntity> getProjectSkill() {
        return projectSkill;
    }

    /**
     * Setter for the project skills associated with the project.
     * It takes a Set of ProjectSkillEntity objects and assigns it to the projectSkill field.
     * Each ProjectSkillEntity object in the Set represents a skill that is required for this project.
     *
     * @param projectSkill a Set of ProjectSkillEntity objects to be associated with the project
     */
    public void setProjectSkill(Set<ProjectSkillEntity> projectSkill) {
        this.projectSkill = projectSkill;
    }

    /**
     * Getter for the updated at timestamp of the project.
     * It returns the LocalDateTime object that represents the last time the project was updated.
     *
     * @return the LocalDateTime object that represents the last time the project was updated
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * This method is a getter for the maxMembers property of the ProjectEntity class.
     * The maxMembers property represents the maximum number of members that a project can have.
     *
     * @return the maximum number of members that the project can have.
     */
    public int getMaxMembers() {
        return maxMembers;
    }

    /**
     * This method is a setter for the maxMembers property of the ProjectEntity class.
     * It updates the maximum number of members that a project can have.
     *
     * @param maxMembers the new maximum number of members that the project can have.
     */
    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    /**
     * Setter for the updated at timestamp of the project.
     * It takes a LocalDateTime object and assigns it to the updatedAt field.
     * The LocalDateTime object represents the new last update time of the project.
     *
     * @param updatedAt the LocalDateTime object that represents the new last update time of the project
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}