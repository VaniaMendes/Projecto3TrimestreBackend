package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * This class represents a UserSkillEntity in the system.
 * It contains various properties related to a user's skill and their getter and setter methods. */
@Entity
@Table(name = "user_skill")
@IdClass(UserSkillId.class)
@NamedQuery(name = "UserSkillEntity.findAllUsersWithSkillAndActive", query = "SELECT u FROM UserSkillEntity u WHERE u.skill = :skill AND u.active = :active")
@NamedQuery(name= "UserSkillEntity.findAllSkillsForUser", query = "SELECT u.skill FROM UserSkillEntity u WHERE u.user.id = :id AND u.active = true")
@NamedQuery(name="UserSkillEntity.findUserSkill", query = "SELECT u FROM UserSkillEntity u WHERE u.user.id = :user AND u.skill.id = :skill")


public class UserSkillEntity implements Serializable {
    /**
     * Unique identifier for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * User ID part of the composite key.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /**
     * Skill ID part of the composite key.
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
