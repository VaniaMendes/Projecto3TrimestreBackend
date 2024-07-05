package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.AppSettingsDao;
import aor.paj.proj_final_aor_backend.entity.AppSettingsEntity;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

/**
 * This class is responsible for creating the default data in the database when the application starts.
 */
@Singleton
@Startup
public class StartupBean {

    /**
     * AppSettingsDao instance to interact with the database.
     */
    @EJB
    LabBean labBean;
    /**
     * UserDao instance to interact with the database.
     */
    @EJB
    UserBean userBean;
    /**
     * UserInterestDao instance to interact with the database.
     */
    @EJB
    SettingsBean settingsBean;


    /**
     * Default constructor for the StartupBean class.
     */
    @PostConstruct
    public void init() {

        labBean.createAllLabs();
        userBean.createAdminUser();
        settingsBean.createDefaultSettings();
    }


}
