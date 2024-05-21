package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;

    /**
     * Name of the interest. It is unique and cannot be updated.
     */
    @Column(name = "name", nullable = false, unique = true, updatable = false)
    private String name;

    /**
     * Default constructor for InterestEntity.
     * It is required by JPA for entity instantiation.
     */
    public InterestEntity() {
    }

    /**
     * Getter for the 'id' field.
     *
     * @return long - The id of the interest.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the 'id' field.
     *
     * @param id - The id to set for the interest.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the 'name' field.
     *
     * @return String - The name of the interest.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the 'name' field.
     *
     * @param name - The name to set for the interest.
     */
    public void setName(String name) {
        this.name = name;
    }
}
