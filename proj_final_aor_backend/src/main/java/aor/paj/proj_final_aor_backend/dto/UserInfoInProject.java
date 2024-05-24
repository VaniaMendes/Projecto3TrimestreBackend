package aor.paj.proj_final_aor_backend.dto;

import aor.paj.proj_final_aor_backend.util.enums.UserTypeInProject;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * This class represents the information of a user in a project.
 * It is annotated with @XmlRootElement to indicate that it can be the root element in an XML document.
 * Each field in the class is annotated with @XmlElement to indicate that it will be represented as an XML element in an XML document.
 */
@XmlRootElement
public class UserInfoInProject {

    // The unique identifier of the user
    @XmlElement
    private Long userId;

    // The first name of the user
    @XmlElement
    private String firstName;

    // The last name of the user
    @XmlElement
    private String lastName;

    // The photo of the user
    @XmlElement
    private String photo;

    // The type of the user in the project
    @XmlElement
    private UserTypeInProject userType;

    // Default constructor
    public UserInfoInProject() {
    }

    /**
     * Returns the unique identifier of the user.
     * @return the user's id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the unique identifier of the user.
     * @param userId the user's id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the first name of the user.
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     * @param firstName the user's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     * @param lastName the user's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the photo of the user.
     * @return the user's photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets the photo of the user.
     * @param photo the user's photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Returns the type of the user in the project.
     * @return the user's type in the project
     */
    public UserTypeInProject getUserType() {
        return userType;
    }

    /**
     * Sets the type of the user in the project.
     * @param userType the user's type in the project
     */
    public void setUserType(UserTypeInProject userType) {
        this.userType = userType;
    }
}