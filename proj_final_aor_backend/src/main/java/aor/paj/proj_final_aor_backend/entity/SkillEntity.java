package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * SkillEntity is a JPA entity that represents the skills of the users.
 */
@Entity
@Table(name="skill")
public class SkillEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for skill.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;

    /**
     * Name of the skill. It is unique and cannot be updated.
     */
    @Column(name = "name", nullable = false, unique = true, updatable = false)
    private String name;

    /**
     * Type of the skill. It is unique and cannot be updated.
     */
    @Column(name = "type", nullable = false, unique = true, updatable = false)
    private String type;

    // Set of projects associated with the skill
    @OneToMany(mappedBy = "skill")
    private Set<ProjectSkillEntity> projectSkill = new HashSet<>();


    // Set of users associated with the skill
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSkillEntity> usersSkills = new HashSet<>();


    /**
     * Default constructor of the SkillEntity class.
     */
    public SkillEntity() {
    }

    /**
     * Getter for the unique identifier of the skill.
     * @return id of the skill.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the unique identifier of the skill.
     * @param id the new id of the skill.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the name of the skill.
     * @return name of the skill.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the skill.
     * @param name the new name of the skill.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the type of the skill.
     * @return type of the skill.
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for the type of the skill.
     * @param type the new type of the skill.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * This method is a getter for the projectSkill field.
     * It returns a Set of ProjectSkillEntity objects that are associated with the skill.
     * Each ProjectSkillEntity object represents a project that requires this skill.
     *
     * @return a Set of ProjectSkillEntity objects that are associated with the skill
     */
    public Set<ProjectSkillEntity> getProjectSkill() {
        return projectSkill;
    }

    /**
     * This method is a setter for the projectSkill field.
     * It takes a Set of ProjectSkillEntity objects and assigns it to the projectSkill field.
     * Each ProjectSkillEntity object in the Set represents a project that requires this skill.
     *
     * @param projectSkill a Set of ProjectSkillEntity objects to be associated with the skill
     */
    public void setProjectSkill(Set<ProjectSkillEntity> projectSkill) {
        this.projectSkill = projectSkill;
    }

    /**
     * This method is a getter for the usersSkills field.
     * It returns a Set of UserSkillEntity objects that are associated with the skill.
     * Each UserSkillEntity object represents a user that possesses this skill.
     *
     * @return a Set of UserSkillEntity objects that are associated with the skill
     */
    public Set<UserSkillEntity> getUsersSkills() {
        return usersSkills;
    }

    /**
     * This method is a setter for the usersSkills field.
     * It takes a Set of UserSkillEntity objects and assigns it to the usersSkills field.
     * Each UserSkillEntity object in the Set represents a user that possesses this skill.
     *
     * @param usersSkills a Set of UserSkillEntity objects to be associated with the skill
     */
    public void setUsersSkills(Set<UserSkillEntity> usersSkills) {
        this.usersSkills = usersSkills;
    }
}