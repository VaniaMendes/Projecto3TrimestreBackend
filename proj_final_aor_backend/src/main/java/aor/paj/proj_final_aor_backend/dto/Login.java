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
private String username;

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
     * Returns the username of the user trying to log in.
     * @return the username of the user trying to log in.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user trying to log in.
     * @param username the username of the user trying to log in.
     */
    public void setUsername(String username) {
        this.username = username;
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
