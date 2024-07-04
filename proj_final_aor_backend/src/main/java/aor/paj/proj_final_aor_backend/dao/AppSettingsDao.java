package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.ActivityEntity;
import aor.paj.proj_final_aor_backend.entity.AppSettingsEntity;
import jakarta.ejb.Stateless;

/**
 * The AppSettingsDao class provides data access operations for the AppSettingsEntity.
 * It extends the AbstractDao class, inheriting common data access operations.
 * This class is marked as Stateless, meaning it does not hold any conversational state.
 */
@Stateless
public class AppSettingsDao extends AbstractDao<AppSettingsEntity> {

    // Default serial version UID for serialization.
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     * Initializes the superclass with the AppSettingsEntity class.
     */
    public AppSettingsDao() {
        super(AppSettingsEntity.class);
    }


    /**
     * Finds an AppSettingsEntity by its ID.
     *
     * @param id The ID of the AppSettingsEntity to find.
     * @return The found AppSettingsEntity, or null if no entity with the given ID exists.
     */
    public AppSettingsEntity findSettingsById(long id) {
        return em.find(AppSettingsEntity.class, id);
    }

    /**
     * Creates or updates an AppSettingsEntity in the database.
     * If the entity does not exist, it will be created. If it already exists, it will be updated.
     *
     * @param settings The AppSettingsEntity to create or update.
     */
    public void createSettings(AppSettingsEntity settings) {
        em.merge(settings);
    }

    public Integer findCurrentMaxUsers() {
        try {
            return em.createNamedQuery("AppSettingsEntity.findCurrentMaxUsers", Integer.class).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * Updates an existing AppSettingsEntity in the database.
     * If the entity does not exist, it will not be created.
     *
     * @param settings The AppSettingsEntity to update.
     */
    public void updateSettings(AppSettingsEntity settings) {
        em.merge(settings);
    }


}
