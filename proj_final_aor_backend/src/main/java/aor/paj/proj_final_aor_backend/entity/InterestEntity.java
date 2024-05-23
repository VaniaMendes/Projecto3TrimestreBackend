package aor.paj.proj_final_aor_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * InterestEntity is a JPA entity that represents the interests of the users.
 * it contains details about the interest such as id and name.

 */
@Entity
@Table(name="interest")
@NamedQuery(name= "InterestEntity.findInterestByName", query ="SELECT i FROM InterestEntity i WHERE i.name=:name")
@NamedQuery(name= "InterestEntity.findAllInterests", query ="SELECT i FROM InterestEntity i ORDER BY i.name ASC")
@JsonIgnoreProperties({"interests"})
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
     * Set of users associated with the interest.
     */
    @OneToMany(mappedBy = "interest", fetch = FetchType.EAGER)
    private Set<UserInterestEntity> interests = new HashSet<>();


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

    /**
     * Getter for the 'interests' field.
     *
     * @return Set<UserInterestEntity> - The set of users associated with the interest.
     */
    public Set<UserInterestEntity> getInterests() {
        return interests;
    }

    /**
     * Setter for the 'interests' field.
     * @param interests - The set of users to set for the interest.
     */
    public void setInterests(Set<UserInterestEntity> interests) {
        this.interests = interests;
    }
}
