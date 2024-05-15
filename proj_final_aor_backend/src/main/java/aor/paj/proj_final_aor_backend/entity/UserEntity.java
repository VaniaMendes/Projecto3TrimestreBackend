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

    /**
     * Messages received by the user.
     */
    @OneToMany(mappedBy = "receiver")
    private Set<MessageEntity> messagesReceived;

    /**
     * Sessions of the user.
     */
    @OneToMany(mappedBy = "user")
    private Set<SessionEntity> sessions;

    // Set of tasks associated with the user
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TaskEntity> tasks = new HashSet<>();

    // Lab associated with the user
    @ManyToOne
    @JoinColumn(name = "lab_id", referencedColumnName = "id")
    private LabEntity lab;


    @OneToMany(mappedBy = "user")
    private Set<UserProjectEntity> projects;


    /**
     * Default constructor for the UserEntity class.
     */
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

    /**
     * Getter for the last name.
     * @return last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for the last name.
     * @param lastName new last name of the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for the visibility state.
     * @return visibility state of the user.
     */
    public boolean isVisibilityState() {
        return visibilityState;
    }

    /**
     * Setter for the visibility state.
     * @param visibilityState new visibility state of the user.
     */
    public void setVisibilityState(boolean visibilityState) {
        this.visibilityState = visibilityState;
    }

    /**
     * Getter for the authentication state.
     * @return authentication state of the user.
     */
    public boolean isAuthState() {
        return authState;
    }

    /**
     * Setter for the authentication state.
     * @param authState new authentication state of the user.
     */
    public void setAuthState(boolean authState) {
        this.authState = authState;
    }

    /**
     * Getter for the active state.
     * @return active state of the user.
     */
    public boolean isActiveState() {
        return activeState;
    }

    /**
     * Setter for the active state.
     * @param activeState new active state of the user.
     */
    public void setActiveState(boolean activeState) {
        this.activeState = activeState;
    }

    /**
     * Getter for the reset password token.
     * @return reset password token of the user.
     */
    public String getResetPassToken() {
        return resetPassToken;
    }

    /**
     * Setter for the reset password token.
     * @param resetPassToken new reset password token of the user.
     */
    public void setResetPassToken(String resetPassToken) {
        this.resetPassToken = resetPassToken;
    }

    /**
     * Getter for the reset password token expiricy.
     * @return reset password token expiricy of the user.
     */
    public String getResetPassTokenExpiricy() {
        return resetPassTokenExpiricy;
    }

    /**
     * Setter for the reset password token expiricy.
     * @param resetPassTokenExpiricy new reset password token expiricy of the user.
     */
    public void setResetPassTokenExpiricy(String resetPassTokenExpiricy) {
        this.resetPassTokenExpiricy = resetPassTokenExpiricy;
    }

    /**
     * Getter for the biography.
     * @return biography of the user.
     */

    public String getBiography() {
        return biography;
    }

    /**
     * Setter for the biography.
     * @param biography new biography of the user.
     */
    public void setBiography(String biography) {
        this.biography = biography;
    }

    /**
     * Getter for the register date.
     * @return register date of the user.
     */
    public String getRegisterDate() {
        return registerDate;
    }

    /**
     * Setter for the register date.
     * @param registerDate new register date of the user.
     */
    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * Getter for the user type.
     * @return user type of the user.
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Setter for the user type.
     * @param userType new user type of the user.
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * Getter for the interests.
     * @return interests of the user.
     */
    public Set<InterestEntity> getInterests() {
        return interests;
    }

    /**
     * Setter for the interests.
     * @param interests new interests of the user.
     */
    public void setInterests(Set<InterestEntity> interests) {
        this.interests = interests;
    }

    /**
     * Getter for the skills.
     * @return skills of the user.
     */
    public Set<SkillEntity> getSkills() {
        return skills;
    }

    /**
     * Setter for the skills.
     * @param skills new skills of the user.
     */
    public void setSkills(Set<SkillEntity> skills) {
        this.skills = skills;
    }

    /**
     * Getter for the messages sent.
     * @return messages sent by the user.
     */
    public Set<NotificationEntity> getNotifications() {
        return notifications;
    }

    /**
     * Setter for the messages sent.
     * @param notifications new messages sent by the user.
     */
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
