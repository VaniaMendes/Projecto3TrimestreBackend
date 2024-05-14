package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

/**
 * InterestEntity is a JPA entity that represents the interests of the users.
 * it contains details about the interest such as id and name.

 */
@Entity
@Table(name="interest")
public class InterestEntity implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for interest.
     */
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;

    /**
     * Name of the interest. It is unique and cannot be updated.
     */
    @Column(name = "name", nullable = false, unique = true, updatable = false)
    private String name;

    public InterestEntity() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
