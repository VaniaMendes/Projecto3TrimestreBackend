package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.AppSettingsDao;
import aor.paj.proj_final_aor_backend.dto.AppSettings;
import aor.paj.proj_final_aor_backend.entity.AppSettingsEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.io.Serializable;

@Stateless
public class SettingsBean implements Serializable {

    @EJB
    AppSettingsDao appSettingsDao;


    public AppSettings getSettings(){
        AppSettingsEntity appSettingsEntity = appSettingsDao.findSettingsById(1);
        if(appSettingsEntity == null) {
            return null;
        }
        AppSettings appSettings = new AppSettings();
        appSettings.setSessionTimeout(appSettingsEntity.getSessionTimeout());
        appSettings.setMaxUsersPerProject(appSettingsEntity.getMaxUsersPerProject());
        return appSettings;
    }

    public boolean updateSettings(AppSettings settings) {
        AppSettingsEntity appSettingsEntity = appSettingsDao.findSettingsById(1);
        if(appSettingsEntity == null) {
            return false;
        }
        if (settings.getSessionTimeout() != 0) {
            appSettingsEntity.setSessionTimeout(settings.getSessionTimeout());

        }
        if (settings.getMaxUsersPerProject() != 0) {
            appSettingsEntity.setMaxUsersPerProject(settings.getMaxUsersPerProject());
        }


        appSettingsDao.updateSettings(appSettingsEntity);
        return true;
    }
}
