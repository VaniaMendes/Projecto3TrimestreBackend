package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

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
}