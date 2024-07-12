package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;
import jdk.jfr.Name;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "authentication")
//Querys to table authentication
@NamedQuery(name= "Authentication.findUserByToken", query = "SELECT a.user FROM AuthenticationEntity a WHERE a.authenticationToken = :authenticationToken")
@NamedQuery(name= "Authentication.findByToken", query = "SELECT a FROM AuthenticationEntity a WHERE a.authenticationToken = :authenticationToken")
@NamedQuery(name= "Authentication.findByUser", query = "SELECT a FROM AuthenticationEntity a WHERE a.user = :user")
@NamedQuery(name= "Authentication.findByresetPassToken", query = "SELECT a FROM AuthenticationEntity a WHERE a.resetPassToken = :resetPassToken")
@NamedQuery(name= "Authentication.findUserByresetPassToken", query = "SELECT a.user FROM AuthenticationEntity a WHERE a.resetPassToken = :resetPassToken")
public class AuthenticationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;

    /**
     * Authentication state of the user. Cannot be null.
     */
    @Column(name = "authState", nullable = false)
    private boolean authState;
    /**
     * Reset password token of the user. It is unique.
     */
    @Column(name = "resetPassToken", unique = true)
    private String resetPassToken;

    /**
     * Authentication token of the user.
     */
    @Column(name = "authenticationToken")
    private String authenticationToken;

    /**
     * Register date of the user. Cannot be null.
     */
    @Column(name = "registerDate", nullable = false)
    private String registerDate;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    /**
     * Default constructor for the AuthenticationEntity class.
     */
    public AuthenticationEntity() {
    }

    /**
     * Getter for the id field.
     * @return the current id field.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the id field.
     * @param id the new value for the id field.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the authentication state field.
     * @return the current authentication state field.
     */
    public boolean isAuthState() {
        return authState;
    }

    /**
     * Setter for the authentication state field.
     * @param authState the new value for the authentication state field.
     */
    public void setAuthState(boolean authState) {
        this.authState = authState;
    }

    /**
     * Getter for the reset password token field.
     * @return the current reset password token field.
     */
    public String getResetPassToken() {
        return resetPassToken;
    }

    /**
     * Setter for the reset password token field.
     * @param resetPassToken the new value for the reset password token field.
     */

    public void setResetPassToken(String resetPassToken) {
        this.resetPassToken = resetPassToken;
    }



    /**
     * Getter for the register date field.
     * @return the current register date field.
     */

    public String getRegisterDate() {
        return registerDate;
    }

    /**
     * Setter for the register date field.
     * @param registerDate the new value for the register date field.
     */

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * Getter for the user field.
     * @return the current user field.
     */

    public UserEntity getUser() {
        return user;
    }

    /**
     * Setter for the user field.
     * @param user the new value for the user field.
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * Getter for the authentication token field.
     * @return the current authentication token field.
     */
    public String getAuthenticationToken() {
        return authenticationToken;
    }

    /**
     * Setter for the authentication token field.
     * @param authenticationToken the new value for the authentication token field.
     */
    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }
}
