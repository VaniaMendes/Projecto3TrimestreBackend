package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "user_interest")
@IdClass(UserInterestId.class)

@NamedQuery(name = "UserInterestEntity.findUserInterest", query = "SELECT u FROM UserInterestEntity u WHERE u.user.id = :user AND u.interest.id = :interest")
@NamedQuery(name = "UserInterestEntity.getAllInterestsByUserId", query = "SELECT u.interest FROM UserInterestEntity u WHERE u.user.id = :user")
public class UserInterestEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @ManyToOne
    @JoinColumn(name = "user_id",  nullable = false)
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "interest_id", nullable = false)
    private InterestEntity interest;

    @Column(name = "active", nullable = false)
    private boolean active;

    public UserInterestEntity() {
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public InterestEntity getInterest() {
        return interest;
    }

    public void setInterest(InterestEntity interest) {
        this.interest = interest;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
