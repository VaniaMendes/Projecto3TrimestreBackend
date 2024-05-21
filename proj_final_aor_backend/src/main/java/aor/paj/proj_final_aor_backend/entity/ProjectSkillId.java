package aor.paj.proj_final_aor_backend.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents the composite key for the many-to-many relationship between Project and Skill entities.
 * It implements Serializable, which is required for composite keys in JPA entities.
 */
public class ProjectSkillId implements Serializable {

    /**
     * The ID of the Project entity. This is one part of the composite key.
     */
    private Long project;

    /**
     * The ID of the Skill entity. This is the other part of the composite key.
     */
    private Long skill;

    /**
     * Returns the ID of the Project entity.
     *
     * @return the ID of the Project entity
     */
    public Long getProject() {
        return project;
    }

    /**
     * Sets the ID of the Project entity.
     *
     * @param project the ID of the Project entity
     */
    public void setProject(Long project) {
        this.project = project;
    }

    /**
     * Returns the ID of the Skill entity.
     *
     * @return the ID of the Skill entity
     */
    public Long getSkill() {
        return skill;
    }

    /**
     * Sets the ID of the Skill entity.
     *
     * @param skill the ID of the Skill entity
     */
    public void setSkill(Long skill) {
        this.skill = skill;
    }

    /**
     * Determines whether the provided object is equal to the current ProjectSkillId.
     * Two ProjectSkillId objects are considered equal if their project and skill IDs are equal.
     *
     * @param o the object to compare with the current ProjectSkillId
     * @return true if the provided object is equal to the current ProjectSkillId, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectSkillId that = (ProjectSkillId) o;
        return Objects.equals(getProject(), that.getProject()) && Objects.equals(getSkill(), that.getSkill());
    }

    /**
     * Returns a hash code value for the ProjectSkillId object.
     * This method is supported for the benefit of hash tables such as those provided by HashMap.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getProject(), getSkill());
    }
}