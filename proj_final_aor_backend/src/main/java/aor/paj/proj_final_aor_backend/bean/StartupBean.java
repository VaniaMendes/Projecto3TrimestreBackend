package aor.paj.proj_final_aor_backend.bean;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
public class StartupBean {

    @EJB
    LabBean labBean;

    @PostConstruct
    public void init() {
        labBean.createAllLabs();
    }


}
