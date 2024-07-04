package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.AppSettingsDao;
import aor.paj.proj_final_aor_backend.dto.AppSettings;
import aor.paj.proj_final_aor_backend.entity.AppSettingsEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.io.Serializable;

/**
 * This class is responsible for handling the business logic for the AppSettings entity.
 * It is responsible for getting and updating the application settings.
 */
@Stateless
public class SettingsBean implements Serializable {

    /**
     * AppSettingsDao instance to interact with the database.
     */
    @EJB
    AppSettingsDao appSettingsDao;


    /**
     * Default constructor for the SettingsBean class.
     */
    public SettingsBean() {
    }

    /**
     * This method gets the application settings from the database.
     *
     * @return The application settings.
     */
    public AppSettings getSettings() {
        // Find the settings by ID, which is always 1
        AppSettingsEntity appSettingsEntity = appSettingsDao.findSettingsById(1);
        if (appSettingsEntity == null) {
            return null;
        }
        // Create a new AppSettings object and set the session timeout and max users per project
        AppSettings appSettings = new AppSettings();
        appSettings.setSessionTimeout(appSettingsEntity.getSessionTimeout());
        appSettings.setMaxUsersPerProject(appSettingsEntity.getMaxUsersPerProject());
        // Return the AppSettings object
        return appSettings;
    }

    /**
     * This method updates the application settings in the database.
     *
     * @param settings The new application settings.
     * @return True if the settings were updated successfully, false otherwise.
     */
    public boolean updateSettings(AppSettings settings) {
        // Find the settings by ID, which is always 1
        AppSettingsEntity appSettingsEntity = appSettingsDao.findSettingsById(1);
        // If the settings are null, return false
        if (appSettingsEntity == null) {
            return false;
        }
        // If the session timeout is not 0, set the session timeout
        if (settings.getSessionTimeout() != 0) {
            appSettingsEntity.setSessionTimeout(settings.getSessionTimeout());

        }
        // If the max users per project is not 0, set the max users per project
        if (settings.getMaxUsersPerProject() != 0) {
            appSettingsEntity.setMaxUsersPerProject(settings.getMaxUsersPerProject());
        }
        // Update the settings in the database
        appSettingsDao.updateSettings(appSettingsEntity);
        return true;
    }


    /**
     * This method creates the default application settings in the database when the application is started.
     */
    public void createDefaultSettings() {
        // Find the settings by ID, which is always 1
        AppSettingsEntity appSettingsEntity = appSettingsDao.findSettingsById(1);

        // If the settings are null, create the default settings
        if (appSettingsEntity == null) {
            appSettingsEntity = new AppSettingsEntity();
            // Set the ID to 1, the session timeout to 30, and the max users per project to 5
            appSettingsEntity.setId(Long.valueOf(1));
            appSettingsEntity.setSessionTimeout(30);
            appSettingsEntity.setMaxUsersPerProject(5);
            // Create the settings in the database
            appSettingsDao.createSettings(appSettingsEntity);
        }
    }

    /**
     * This method gets the session time out for project from the database.
     *
     * @return The session time out for project.
     */
    public int getSessionTimeout() {
        // Find the settings by ID, which is always 1
        AppSettingsEntity appSettingsEntity = appSettingsDao.findSettingsById(1);
        //  If the settings are null, return 0
        if (appSettingsEntity == null) {
            return 0;
        }
        // Return the session timeout
        return appSettingsEntity.getSessionTimeout();
    }
}
