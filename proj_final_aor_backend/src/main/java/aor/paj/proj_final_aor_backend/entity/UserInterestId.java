package aor.paj.proj_final_aor_backend.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a composite key for the UserInterest entity.
 * It implements Serializable as it's used as a key in a Map.
 */
public class UserInterestId implements Serializable {

    // User ID part of the composite key
    private Long user;

    // Interest ID part of the composite key
    private Long interest;


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
     * Getter for the interest ID part of the composite key.
     * @return interest ID part of the composite key.
     */

    public Long getInterest() {
        return interest;
    }

    /**
     * Setter for the interest ID part of the composite key.
     * @param interest the new interest ID part of the composite key.
     */

    public void setInterest(Long interest) {
        this.interest = interest;
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
        UserInterestId that = (UserInterestId) o;
        return Objects.equals(user, that.user) && Objects.equals(interest, that.interest);
    }

    /**
     * Overridden hashCode method for the composite key.
     * @return the hash code of the composite key.
     */
     
    @Override
    public int hashCode() {
        return Objects.hash(user, interest);
    }
}
