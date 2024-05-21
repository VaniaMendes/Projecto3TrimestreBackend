package aor.paj.proj_final_aor_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "user_skill")
@IdClass(UserSkillId.class)
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
}
