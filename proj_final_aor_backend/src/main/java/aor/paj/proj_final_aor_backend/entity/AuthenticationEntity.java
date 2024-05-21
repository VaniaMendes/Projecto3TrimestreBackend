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
     * Reset password token expiricy of the user.
     */
    @Column(name = "resetPassTokenExpiricy")
    private String resetPassTokenExpiricy;

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

    public AuthenticationEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAuthState() {
        return authState;
    }

    public void setAuthState(boolean authState) {
        this.authState = authState;
    }

    public String getResetPassToken() {
        return resetPassToken;
    }

    public void setResetPassToken(String resetPassToken) {
        this.resetPassToken = resetPassToken;
    }

    public String getResetPassTokenExpiricy() {
        return resetPassTokenExpiricy;
    }

    public void setResetPassTokenExpiricy(String resetPassTokenExpiricy) {
        this.resetPassTokenExpiricy = resetPassTokenExpiricy;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }
}
