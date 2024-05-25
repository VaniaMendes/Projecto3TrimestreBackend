package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * This class is responsible for handling the business logic for the MessageInfoUser entity.
 * It is responsible for creating, deleting and associating MessageInfoUser to users.
 * It is used to pass information about a user in a message.
 */
@XmlRootElement
public class MessageInfoUser {
    /**
     * Unique identifier for the user.
     */
    @XmlElement
    private long id;
    /**
     * First name of the user.
     */
    @XmlElement
    private String firstName;
    /**
     * Last name of the user.
     */
    @XmlElement
    private String lastName;

    /**
     * Default constructor for MessageInfoUser.
     */

    public MessageInfoUser() {
    }

    /**
     * Getter for the 'id' field.
     * @return long - The id of the MessageInfoUser.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the 'id' field.
     * @param id - The new id of the MessageInfoUser.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the 'firstName' field.
     * @return String - The first name of the MessageInfoUser.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for the 'firstName' field.
     * @param firstName - The new first name of the MessageInfoUser.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for the 'lastName' field.
     * @return String - The last name of the MessageInfoUser.
     */

    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for the 'lastName' field.
     * @param lastName - The new last name of the MessageInfoUser.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
