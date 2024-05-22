package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "user_skill")
@IdClass(UserSkillId.class)
@NamedQuery(name = "UserSkillEntity.findAllSkillsFromAUser", query = "SELECT u FROM UserSkillEntity u WHERE u.user = :user")
@NamedQuery(name = "UserSkillEntity.findUserSkill", query = "SELECT u FROM UserSkillEntity u WHERE u.user = :user AND u.skill = :skill")
@NamedQuery(name= "UserSkillEntity.findAllUsersWithSkill", query = "SELECT u FROM UserSkillEntity u WHERE u.skill = :skill")


public class UserSkillEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private SkillEntity skill;

    @Column(name = "active", nullable = false)
    private boolean active;





    public UserSkillEntity() {
    }


    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public SkillEntity getSkill() {
        return skill;
    }

    public void setSkill(SkillEntity skill) {
        this.skill = skill;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
