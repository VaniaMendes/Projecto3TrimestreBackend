package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * UserRegistration is a DTO (Data Transfer Object) class used to encapsulate
 * the data of a user during the registration process. It includes the user's
 * email, password, and a confirmation of the password.
 */
@XmlRootElement
public class UserRegistration {

    @XmlElement
    private String email;
    @XmlElement
    private String password;
    @XmlElement
    private String confirmPassword;


    /**
     * Default constructor.

     */
    public UserRegistration() {
    }

    /**
     * Gets the email of the user.
     * @return the email of the user.
     */
    public String getEmail() {
        return email;
    }


    /**
     * Gets the password of the user.
     * @return the password of the user.
     */
    public String getPassword() {
        return password;
    }


    /**
     * Gets the confirmation of the password of the user.
     * @return the confirmation of the password of the user.
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }


}
