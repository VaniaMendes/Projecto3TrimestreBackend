package aor.paj.proj_final_aor_backend.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents the composite key for the many-to-many relationship between User and Project entities.
 * It implements Serializable, which is required for composite keys in JPA entities.
 */
public class UserProjectId implements Serializable {

    /**
     * The ID of the Project entity. This is one part of the composite key.
     */
    private Long project;

    /**
     * The ID of the User entity. This is the other part of the composite key.
     */
    private Long user;

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
     * Returns the ID of the User entity.
     *
     * @return the ID of the User entity
     */
    public Long getUser() {
        return user;
    }

    /**
     * Sets the ID of the User entity.
     *
     * @param user the ID of the User entity
     */
    public void setUser(Long user) {
        this.user = user;
    }

    /**
     * Determines whether the provided object is equal to the current UserProjectId.
     * Two UserProjectId objects are considered equal if their project and user IDs are equal.
     *
     * @param o the object to compare with the current UserProjectId
     * @return true if the provided object is equal to the current UserProjectId, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProjectId that = (UserProjectId) o;
        return Objects.equals(getProject(), that.getProject()) && Objects.equals(getUser(), that.getUser());
    }

    /**
     * Returns a hash code value for the UserProjectId object.
     * This method is supported for the benefit of hash tables such as those provided by HashMap.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getProject(), getUser());
    }
}
