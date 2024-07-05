package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

/**
 * This class represents the authentication information of a user.
 */
@XmlRootElement
public class Authentication {

    /**
     * The unique identifier for the authentication information.
     */
    @XmlElement
    private long id;
    /**
     * The authentication state of the user.
     */
    @XmlElement
    private boolean authState;
    /**
     * The reset password token.
     */
    @XmlElement
    private String resetPassToken;
    /**
     * The expiration time of the reset password token.
     */
    @XmlElement
    private String resetPassTokenExpiricy;

    /**
     * The authentication token.
     */
    @XmlElement
    private String authenticationToken;
    /**
     * The register date of the user.
     */
    @XmlElement
    private LocalDateTime registerDate;

    /**
     * Default constructor for the Authentication class.
     */
    public Authentication() {
    }

    /**
     * Getter for the authState field.
     *
     * @return the current authState field.
     */
    public boolean isAuthState() {
        return authState;
    }

    /**
     * Setter for the authState field.
     *
     * @param authState the new value for the authState field.
     */
    public void setAuthState(boolean authState) {
        this.authState = authState;
    }

    /**
     * Getter for the resetPassToken field.
     *
     * @return the current reset password token.
     */

    public String getResetPassToken() {
        return resetPassToken;
    }

    /**
     * Setter for the resetPassToken field.
     *
     * @param resetPassToken the new value for the reset password token.
     */
    public void setResetPassToken(String resetPassToken) {
        this.resetPassToken = resetPassToken;
    }

    /**
     * Getter for the resetPassTokenExpiricy field.
     *
     * @return the current reset password token expiricy.
     */
    public String getResetPassTokenExpiricy() {
        return resetPassTokenExpiricy;
    }

    /**
     * Setter for the resetPassTokenExpiricy field.
     *
     * @param resetPassTokenExpiricy the new value for the reset password token expiricy.
     */
    public void setResetPassTokenExpiricy(String resetPassTokenExpiricy) {
        this.resetPassTokenExpiricy = resetPassTokenExpiricy;
    }

    /**
     * Getter for the register date of the user.
     *
     * @return The current register date.
     */
    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    /**
     * Setter for the refister date
     *
     * @param registerDate The new register date.
     */
    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * Getter for the id field.
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the id field.
     *
     * @param id the new value for the id field.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the authentication token.
     *
     * @return the current authentication token.
     */
    public String getAuthenticationToken() {
        return authenticationToken;
    }

    /**
     * Setter for the authentication token.
     *
     * @param authenticationToken the new value for the authentication token.
     */
    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }
}
