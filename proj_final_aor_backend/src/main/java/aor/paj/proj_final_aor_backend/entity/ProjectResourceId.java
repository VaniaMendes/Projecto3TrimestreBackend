package aor.paj.proj_final_aor_backend.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a composite key for the ProjectResource entity.
 * It implements Serializable as it's used as a key in a Map.
 */
public class ProjectResourceId implements Serializable {

    // Project ID part of the composite key
    private Long project;

    // Resource ID part of the composite key
    private Long resource;

    /**
     * Getter for the project ID part of the composite key.
     * @return project ID part of the composite key.
     */
    public Long getProject() {
        return project;
    }

    /**
     * Setter for the project ID part of the composite key.
     * @param project the new project ID part of the composite key.
     */
    public void setProject(Long project) {
        this.project = project;
    }

    /**
     * Getter for the resource ID part of the composite key.
     * @return resource ID part of the composite key.
     */
    public Long getResource() {
        return resource;
    }

    /**
     * Setter for the resource ID part of the composite key.
     * @param resource the new resource ID part of the composite key.
     */
    public void setResource(Long resource) {
        this.resource = resource;
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
        ProjectResourceId that = (ProjectResourceId) o;
        return Objects.equals(getProject(), that.getProject()) && Objects.equals(getResource(), that.getResource());
    }

    /**
     * Overridden hashCode method for the composite key.
     * @return the hash code of the composite key.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getProject(), getResource());
    }
}