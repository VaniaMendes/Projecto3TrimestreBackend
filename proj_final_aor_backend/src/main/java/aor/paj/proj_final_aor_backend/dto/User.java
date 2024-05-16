package aor.paj.proj_final_aor_backend.dto;

import aor.paj.proj_final_aor_backend.util.enums.UserType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

/**
 * This class represents a user.
 * It contains fields for the user´s id, nickname, password, email, first name, last name, visibility state, active state, biography and user type.
 * The user type is an enum that can be either ADMIN, MODERATOR or USER.
 */
@XmlRootElement
public class User {

    @XmlElement
    private long id;
    @XmlElement
    private String nickname;

    @XmlElement
    private String password;
    @XmlElement
    private String email;
    @XmlElement
    private String firstName;
    @XmlElement
    private String lastName;

    @XmlElement
    private boolean visibilityState;

    @XmlElement
    private boolean activeState;

    @XmlElement
    private String biography;

    @XmlElement
    private UserType userType;


    /**
     * Default constructor for the User class.
     */
    public User(){}

    /**
     * Gets the id of the user.
     * @return the id of the user
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of the user.
     * @param id the id of the user
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the nickname of the user.
     * @return the nickname of the user
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname of the user.
     * @param nickname the nickname of the user.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    /**
     * Gets the password of the user.
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     * @param password the password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email of the user.
     * @return the email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     * @param email the email of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the first name of the user.
     * @return the first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     * @param firstName the first name of the user.
     */

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     * @return the last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     * @param lastName the last name of the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the visibility state of the user.
     * @return the visibility state of the user.
     */
    public boolean isVisibilityState() {
        return visibilityState;
    }

    /**
     * Sets the visibility state of the user.
     * @param visibilityState the visibility state of the user.
     */
    public void setVisibilityState(boolean visibilityState) {
        this.visibilityState = visibilityState;
    }

    /**
     * Gets the active state of the user.
     * @return the active state of the user.
     */

    public boolean isActiveState() {
        return activeState;
    }

    /**
     * Sets the active state of the user.
     * @param activeState the active state of the user.
     */
    public void setActiveState(boolean activeState) {
        this.activeState = activeState;
    }

    /**
     * Gets the biography of the user.
     * @return the biography of the user.
     */
    public String getBiography() {
        return biography;
    }

    /**
     * Sets the biography of the user.
     * @param biography the biography of the user.
     */
    public void setBiography(String biography) {
        this.biography = biography;
    }


    /**
     * Gets the user type of the user.
     * @return the user type of the user.
     */

    public UserType getUserType() {
        return userType;
    }

    /**
     * Sets the user type of the user.
     * @param userType the user type of the user.
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }


}
