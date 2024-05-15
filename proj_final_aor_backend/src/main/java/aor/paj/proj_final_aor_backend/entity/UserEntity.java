package aor.paj.proj_final_aor_backend.entity;

import aor.paj.proj_final_aor_backend.util.enums.UserType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * UserEntity is a JPA entity that represents a user in the database.
 * It contains details about a user such as id, email, password, first name, last name, visibility state,
 * authentication state, active state, reset password token, reset password token expiricy, biography, register date,
 * user type, interests, skills, notifications, messages sent and messages received.
 *
 */
@Entity
@Table(name="user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for user.
     */
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;

    /**
     * Email of the user. It is unique and cannot be updated.
     */
    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String email;

    /**
     * Password of the user. It cannot be null.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * First name of the user.
     */
    @Column(name = "firstName")
    private String firstName;

    /**
     * Last name of the user.
     */
    @Column(name = "lastName")
    private String lastName;

    /**
     * Visibility state of the user. Cannot be null.
     * Represents the visibility of the user profile.
     */
    @Column(name = "visibilityState", nullable = false)
    private boolean visibilityState;

    /**
     * Authentication state of the user. Cannot be null.
     */
    @Column(name = "authState", nullable = false)
    private boolean authState;

    /**
     * Active state of the user. Cannot be null.
     */
    @Column(name = "activeState", nullable = false)
    private boolean activeState;

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
     * Biography of the user.
     */
    @Column(name = "biography")
    private String biography;

    /**
     * Register date of the user. Cannot be null.
     */
    @Column(name = "registerDate", nullable = false)
    private String registerDate;

    /**
     * Type of the user. Cannot be null.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "userType", nullable = false)
    private UserType userType;

    /**
     * Interests of the user.
     */
    @ManyToMany
    @JoinTable(
            name = "user_interest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id"))
    private Set<InterestEntity> interests;

    /**
     * Skills of the user.
     */
    @ManyToMany
    @JoinTable(
            name = "user_skill",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<SkillEntity> skills;

    /**
     * Notifications of the user.
     */
    @ManyToMany
    @JoinTable(
            name = "user_notification",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id"))
    private Set<NotificationEntity> notifications;

    /**
     * Messages sent by the user.
     */

    @OneToMany(mappedBy = "sender")
    private Set<MessageEntity> messagesSent;

    @OneToMany(mappedBy = "receiver")
    private Set<MessageEntity> messagesReceived;

    @OneToMany(mappedBy = "user")
    private Set<SessionEntity> sessions;

    // Set of tasks associated with the user
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TaskEntity> tasks = new HashSet<>();

    // Lab associated with the user
    @ManyToOne
    @JoinColumn(name = "lab_id", referencedColumnName = "id")
    private LabEntity lab;

    public UserEntity() {

    }

    /**
     * Getter for the id.
     * @return id of the interest.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the id.
     * @param id new id of the interest
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the email.
     * @return email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for the email.
     * @param email new email of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for the password.
     * @return password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password.
     * @param password new password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for the first name.
     * @return first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }


    /**
     * Setter for the first name.
     * @param firstName new first name of the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isVisibilityState() {
        return visibilityState;
    }

    public void setVisibilityState(boolean visibilityState) {
        this.visibilityState = visibilityState;
    }

    public boolean isAuthState() {
        return authState;
    }

    public void setAuthState(boolean authState) {
        this.authState = authState;
    }

    public boolean isActiveState() {
        return activeState;
    }

    public void setActiveState(boolean activeState) {
        this.activeState = activeState;
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

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Set<InterestEntity> getInterests() {
        return interests;
    }

    public void setInterests(Set<InterestEntity> interests) {
        this.interests = interests;
    }

    public Set<SkillEntity> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillEntity> skills) {
        this.skills = skills;
    }

    public Set<NotificationEntity> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<NotificationEntity> notifications) {
        this.notifications = notifications;
    }

    public Set<MessageEntity> getMessagesSent() {
        return messagesSent;
    }

    public void setMessagesSent(Set<MessageEntity> messagesSent) {
        this.messagesSent = messagesSent;
    }

    public Set<MessageEntity> getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(Set<MessageEntity> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    public Set<SessionEntity> getSessions() {
        return sessions;
    }

    public void setSessions(Set<SessionEntity> sessions) {
        this.sessions = sessions;
    }

    public Set<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    public LabEntity getLab() {
        return lab;
    }

    public void setLab(LabEntity lab) {
        this.lab = lab;
    }
}
