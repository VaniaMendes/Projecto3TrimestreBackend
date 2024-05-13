package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="user")
@NamedQuery(name = "User.findAllUsers", query = "SELECT u FROM UserEntity u WHERE u.username <> 'ADMIN' AND u.confirmDate IS NOT NULL")
public class UserEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    //user unique username has ID - not updatable, unique, not null
    @Id
    @Column(name="username", nullable=false, unique = true, updatable = false)
    private String username;

    @Column(name="password", nullable=true, unique = false, updatable = true)
    private String password;

    @Column(name="type_of_user", nullable=false, unique = false, updatable = true)
    private int typeOfUser;

    @Column(name="email", nullable=false, unique = true, updatable = true)
    private String email;

    @Column(name="first_name", nullable=false, unique = false, updatable = true)
    private String firstName;

    @Column(name="last_name", nullable=false, unique = false, updatable = true)
    private String lastName;

    @Column(name="phone", nullable=false, unique = true, updatable = true)
    private String phone;

    @Column(name="photo_url", nullable=true, unique = false, updatable = true)
    private String photoURL;

    @Column(name="session_token", nullable=true, unique = true, updatable = true)
    private String token;

    @Column(name="reset_token", nullable=true, unique = true, updatable = true)
    private String resetToken;

    @Column(name="visible", nullable = false, unique = false, updatable = true)
    private boolean visible;

    @Column(name = "reset_token_expiry", nullable = true)
    private Date resetTokenExpiry;

    @Column(name = "confirm_date", nullable = true)
    private Date confirmDate;



    //default empty constructor
    public UserEntity() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(int typeOfUser) {
        this.typeOfUser = typeOfUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isVisible() {return visible;}

    public void setVisible(boolean visivel) {this.visible = visivel;}

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Date getResetTokenExpiry() {
        return resetTokenExpiry;
    }

    public void setResetTokenExpiry(Date resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }
}