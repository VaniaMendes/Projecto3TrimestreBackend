package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.AppSettingsDao;
import aor.paj.proj_final_aor_backend.entity.AppSettingsEntity;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
public class StartupBean {

    @EJB
    LabBean labBean;
    @EJB
    UserBean userBean;
    @EJB
    SettingsBean settingsBean;

    @PostConstruct
    public void init() {
        labBean.createAllLabs();
        userBean.createAdminUser();
        settingsBean.createDefaultSettings();
    }



}
