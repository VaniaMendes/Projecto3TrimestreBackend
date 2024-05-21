package aor.paj.proj_final_aor_backend.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a composite key for the UserSkill entity.
 * It implements Serializable as it's used as a key in a Map.
 */
public class UserSkillId implements Serializable {

    // User ID part of the composite key
    private Long user;

    // Skill ID part of the composite key
    private Long skill;

    /**
     * Getter for the user ID part of the composite key.
     * @return user ID part of the composite key.
     */
    public Long getUser() {
        return user;
    }

    /**
     * Setter for the user ID part of the composite key.
     * @param user the new user ID part of the composite key.
     */
    public void setUser(Long user) {
        this.user = user;
    }

    /**
     * Getter for the skill ID part of the composite key.
     * @return skill ID part of the composite key.
     */
    public Long getSkill() {
        return skill;
    }

    /**
     * Setter for the skill ID part of the composite key.
     * @param skill the new skill ID part of the composite key.
     */
    public void setSkill(Long skill) {
        this.skill = skill;
    }

    /**
     * Overridden equals method for the composite key.
     * @param o the object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSkillId that = (UserSkillId) o;
        return Objects.equals(getUser(), that.getUser()) && Objects.equals(getSkill(), that.getSkill());
    }

    /**
     * Overridden hashCode method for the composite key.
     * @return the hash code of the composite key.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getSkill());
    }
}