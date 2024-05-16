package aor.paj.proj_final_aor_backend.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

@XmlRootElement
public class Authentication {

    @XmlElement
    private long id;
    @XmlElement
    private boolean authState;

    @XmlElement
    private String resetPassToken;
    @XmlElement
    private String resetPassTokenExpiricy;

    @XmlElement
    private LocalDateTime registerDate;

    public Authentication() {
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

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }
}
