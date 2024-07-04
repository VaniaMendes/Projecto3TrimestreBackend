package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * The UserSkillEntity class represents the relationship between a user and their skills.
 * It is mapped to the "user_skill" table in the database.
 * This class implements Serializable, allowing instances of this class to be serialized.
 */
@Entity
@Table(name = "user_skill")
@IdClass(UserSkillId.class)

// Named query to find all users with a specific skill and active status
@NamedQuery(name = "UserSkillEntity.findAllUsersWithSkillAndActive", query = "SELECT u FROM UserSkillEntity u WHERE u.skill = :skill AND u.active = :active")

// Named query to get all skills of a user by their ID
@NamedQuery(name= "UserSkillEntity.findAllSkillsForUser", query = "SELECT u.skill FROM UserSkillEntity u WHERE u.user.id = :id AND u.active = true")

// Named query to find a user's skill by user and skill IDs
@NamedQuery(name="UserSkillEntity.findUserSkill", query = "SELECT u FROM UserSkillEntity u WHERE u.user.id = :user AND u.skill.id = :skill")


public class UserSkillEntity implements Serializable {

    // Serial version UID for serialization and deserialization
    private static final long serialVersionUID = 1L;

    /**
     * User ID part of the composite key.
     * It is a foreign key that references the id column in the user table.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /**
     * Skill ID part of the composite key.
     * It is a foreign key that references the id column in the skill table.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private SkillEntity skill;

    /**
     * Active status of the user's skill.
     */
    @Column(name = "active", nullable = false)
    private boolean active;


    /**
     * Default constructor for the UserSkillEntity class.
     */
    public UserSkillEntity() {
    }


    /**
     * Getter for the user ID part of the composite key.
     * @return user ID part of the composite key.
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * Setter for the user ID part of the composite key.
     * @param user the new user ID part of the composite key.
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * Getter for the skill ID part of the composite key.
     * @return skill ID part of the composite key.
     */
    public SkillEntity getSkill() {
        return skill;
    }

    /**
     * Setter for the skill ID part of the composite key.
     * @param skill the new skill ID part of the composite key.
     */
    public void setSkill(SkillEntity skill) {
        this.skill = skill;
    }

    /**
     * Getter for the active status of the user's skill.
     * @return active status of the user's skill.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Setter for the active status of the user's skill.
     * @param active the new active status of the user's skill.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
