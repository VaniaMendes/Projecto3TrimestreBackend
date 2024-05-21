package aor.paj.proj_final_aor_backend.entity;

import aor.paj.proj_final_aor_backend.util.enums.SkillType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

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
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Type of the skill. It is unique and cannot be updated.
     */
    @Column(name = "type", nullable = false, updatable = false)
    private SkillType type;

    // Set of projects associated with the skill
    @ManyToMany(mappedBy = "skills")
    private Set<ProjectEntity> projects = new HashSet<>();

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

    public SkillType getType() {
        return type;
    }

    public void setType(SkillType type) {
        this.type = type;
    }

    public Set<ProjectEntity> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectEntity> projects) {
        this.projects = projects;
    }

    public Set<UserSkillEntity> getUsersSkills() {
        return usersSkills;
    }

    public void setUsersSkills(Set<UserSkillEntity> usersSkills) {
        this.usersSkills = usersSkills;
    }
}