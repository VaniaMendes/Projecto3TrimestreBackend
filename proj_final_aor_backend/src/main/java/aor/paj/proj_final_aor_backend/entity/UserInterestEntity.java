package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * The UserInterestEntity class represents the relationship between a user and their interests.
 * It is mapped to the "user_interest" table in the database.
 * This class implements Serializable, allowing instances of this class to be serialized.
 */
@Entity
@Table(name = "user_interest")
@IdClass(UserInterestId.class)

// Named query to find a user's interest by user and interest IDs
@NamedQuery(name = "UserInterestEntity.findUserInterest", query = "SELECT u FROM UserInterestEntity u WHERE u.user.id = :user AND u.interest.id = :interest")

// Named query to get all interests of a user by their ID
@NamedQuery(name = "UserInterestEntity.getAllInterestsByUserId", query = "SELECT u.interest FROM UserInterestEntity u WHERE u.user.id = :user AND u.active = true")
public class UserInterestEntity implements Serializable {

    // Named query to get all interests of a user by their ID
    private static final long serialVersionUID = 1L;


    /**
     * User associated with the interest. It is the primary key of the user_interest table.
     * It is a foreign key that references the id column in the user table.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id",  nullable = false)
    private UserEntity user;


    /**
     * Interest associated with the user. It is the primary key of the user_interest table.
     * It is a foreign key that references the id column in the interest table.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "interest_id", nullable = false)
    private InterestEntity interest;

    /**
     * Active status of the user's interest.
     */
    @Column(name = "active", nullable = false)
    private boolean active;

    /**
     * Default constructor for the UserInterestEntity class.
     */
    public UserInterestEntity() {
    }

    /**
     * Getter for the user associated with the interest.
     * @return user associated with the interest.
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * Setter for the user associated with the interest.
     * @param user user associated with the interest.
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * Getter for the interest associated with the user.
     * @return interest associated with the user.
     */
    public InterestEntity getInterest() {
        return interest;
    }

    /**
     * Setter for the interest associated with the user.
     * @param interest interest associated with the user.
     */
    public void setInterest(InterestEntity interest) {
        this.interest = interest;
    }

    /**
     * Getter for the active status of the user's interest.
     * @return active status of the user's interest.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Setter for the active status of the user's interest.
     * @param active active status of the user's interest.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
