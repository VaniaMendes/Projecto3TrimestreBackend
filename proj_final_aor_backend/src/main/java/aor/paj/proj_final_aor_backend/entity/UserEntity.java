package aor.paj.proj_final_aor_backend.entity;

import aor.paj.proj_final_aor_backend.util.enums.UserType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * UserEntity is a JPA entity that represents a user in the database.
 * It contains details about a user such as id, email, password, first name, last name, visibility state,
 * authentication state, active state, reset password token, reset password token expiricy, biography, register date,
 * user type, interests, skills, notifications, messages sent and messages received.
 */
@Entity
@Table(name="user")
//Queries for the UserEntity
@NamedQuery(name="User.findUserById", query = "SELECT u FROM UserEntity u WHERE u.id = :id")
@NamedQuery(name = "User.findUserByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = :email")
@NamedQuery(name = "User.findAllActiveUsers", query = "SELECT u FROM UserEntity u WHERE u.activeState = true ORDER BY u.firstName")
@NamedQuery(name="User.findUserByNameStartingWith", query = "SELECT u FROM UserEntity u WHERE u.id != :userId AND u.activeState=true AND (LOWER (u.firstName) LIKE LOWER (:prefix) OR LOWER (u.lastName) LIKE LOWER (:prefix)OR LOWER (u.nickname) LIKE LOWER (:prefix))")
@NamedQuery(name = "User.findSkillsByUserId", query = "SELECT u.skills FROM UserEntity u WHERE u.id = :id")
@NamedQuery(name = "User.findInterestsByUserId", query = "SELECT u.interests FROM UserEntity u WHERE u.id = :id")
@NamedQuery(name = "User.findUserByNickname", query = "SELECT u FROM UserEntity u WHERE u.nickname = :nickname")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * Active state of the user. Cannot be null.
     */
    @Column(name = "activeState", nullable = false)
    private boolean activeState;

    /**
     * Biography of the user.
     */
    @Column(name = "biography", length = 1000)
    private String biography;

    /**
     * Photo of the user.
     */
    @Column(name = "photo")
    private String photo;

    /**
     * Nickname of the user.
     */
    @Column(name = "nickname")
    private String nickname;


    /**
     * Type of the user. Cannot be null.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "userType", nullable = false)
    private UserType userType;

    /**
     * Interests of the user.
     */
    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL)
    private Set<UserInterestEntity> interests = new HashSet<>();

    /**
     * Skills of the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserSkillEntity> skills = new HashSet<>();

    /**
     * Notifications of the user.
     */

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_notification",
            joinColumns = @JoinColumn(name = "receiver_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id"))
    private Set<NotificationEntity> notifications = new HashSet<>();

    /**
     * Messages sent by the user.
     */

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Set<MessageEntity> messagesSent = new HashSet<>();

    /**
     * Messages received by the user.
     */
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<MessageEntity> messagesReceived = new HashSet<>();

    /**
     * Sessions of the user.
     */
    @OneToMany(mappedBy = "user")
    private Set<SessionEntity> sessions;

    // Set of tasks associated with the user
    @OneToMany(mappedBy = "responsibleUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TaskEntity> tasks = new HashSet<>();

    // Lab associated with the user
    @ManyToOne
    @JoinColumn(name = "lab_id", referencedColumnName = "id")
    private LabEntity lab;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserProjectEntity> projects;

    @OneToOne(mappedBy = "user")
    private AuthenticationEntity authentication;

    @OneToMany(mappedBy = "author")
    private Set<ActivityEntity> activities;


    /**
     * Default constructor for the UserEntity class.
     */
    public UserEntity() {
        this.interests = new HashSet<>();
        this.skills = new HashSet<>();
        this.notifications = new HashSet<>();
        this.messagesSent = new HashSet<>();
        this.messagesReceived = new HashSet<>();
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


    /**
     * Getter for the messages sent.
     * @return messages sent by the user.
     */
    public Set<MessageEntity> getMessagesSent() {
        return messagesSent;
    }

    /**
     * Setter for the messages sent.
     * @param messagesSent new messages sent by the user.
     */
    public void setMessagesSent(Set<MessageEntity> messagesSent) {
        this.messagesSent = messagesSent;
    }

    /**
     * Getter for the messages received.
     * @return messages received by the user.
     */
    public Set<MessageEntity> getMessagesReceived() {
        return messagesReceived;
    }

    /**
     * Setter for the messages received.
     * @param messagesReceived new messages received by the user.
     */
    public void setMessagesReceived(Set<MessageEntity> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    /**
     * Getter for the sessions.
     * @return sessions of the user.
     */
    public Set<SessionEntity> getSessions() {
        return sessions;
    }

    /**
     * Setter for the sessions.
     * @param sessions new sessions of the user.
     */
    public void setSessions(Set<SessionEntity> sessions) {
        this.sessions = sessions;
    }

    /**
     * Getter for the tasks.
     * @return tasks of the user.
     */
    public Set<TaskEntity> getTasks() {
        return tasks;
    }

    /**
     * Setter for the tasks.
     * @param tasks new tasks of the user.
     */
    public void setTasks(Set<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    /**
     * Getter for the lab.
     * @return lab of the user.
     */
    public LabEntity getLab() {
        return lab;
    }

    /**
     * Setter for the lab.
     * @param lab new lab of the user.
     */
    public void setLab(LabEntity lab) {
        this.lab = lab;
    }

    /**
     * Getter for the projects.
     * @return projects of the user.
     */
    public Set<UserProjectEntity> getProjects() {
        return projects;
    }

    /**
     * Setter for the projects.
     * @param projects new projects of the user.
     */

    public void setProjects(Set<UserProjectEntity> projects) {
        this.projects = projects;
    }


    /**
     * Getter for the authentication.
     * @return authentication of the user.
     */
    public AuthenticationEntity getAuthentication() {
        return authentication;
    }


    /**
     * Setter for the authentication.
     * @param authentication new authentication of the user.
     */
    public void setAuthentication(AuthenticationEntity authentication) {
        this.authentication = authentication;
    }

    /**
     * Getter for the photo.
     * @return photo of the user.
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Setter for the photo.
     * @param photo new photo of the user.
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Getter for the nickname.
     * @return  nickname of the user.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Setter for the nickname.
     * @param nickname new nickname of the user.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Getter for the skills.
     * @return skills of the user.
     */
    public Set<UserSkillEntity> getSkills() {
        return skills;
    }

    /**
     * Setter for the skills.
     * @param skills new skills of the user.
     */
    public void setSkills(Set<UserSkillEntity> skills) {
        this.skills = skills;
    }

    /**
     * Getter for the interests.
     * @return interests of the user.
     */
    public Set<UserInterestEntity> getInterests() {
        return interests;
    }

    /**
     * Setter for the interests.
     * @param interests new interests of the user.
     */
    public void setInterests(Set<UserInterestEntity> interests) {
        this.interests = interests;
    }

    /**
     * Getter for the activities.
     * @return activities of the user.
     */
    public Set<ActivityEntity> getActivities() {
        return activities;
    }

    /**
     * Setter for the activities.
     * @param activities new activities of the user.
     */
    public void setActivities(Set<ActivityEntity> activities) {
        this.activities = activities;
    }
}
