package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * This class represents when the user do the log in.
 */
@XmlRootElement
public class Login {

/**
 * The username of the user trying to log in.
 */
@XmlElement
private String email;

    /**
     * The password of the user trying to log in.
     */
    @XmlElement
private String password;

    /**
     * Default constructor.
     */
    public Login(){}

    /**
     * Returns the email of the user trying to log in.
     * @return the e mail of the user trying to log in.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user trying to log in.
     * @param email the email of the user trying to log in.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password of the user trying to log in.
     * @return the password of the user trying to log in.
     */

    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user trying to log in.
     * @param password the password of the user trying to log in.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
