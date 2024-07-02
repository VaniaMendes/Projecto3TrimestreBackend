package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.dto.AppSettings;
import aor.paj.proj_final_aor_backend.entity.ActivityEntity;
import aor.paj.proj_final_aor_backend.entity.AppSettingsEntity;
import aor.paj.proj_final_aor_backend.entity.InterestEntity;
import jakarta.ejb.Stateless;

@Stateless
public class AppSettingsDao extends AbstractDao<ActivityEntity>{
    private static final long serialVersionUID = 1L;
    public AppSettingsDao() {
        super(ActivityEntity.class);
    }


    public AppSettingsEntity findSettingsById(long id) {
        return em.find(AppSettingsEntity.class, id);
    }
    public void createSettings(AppSettingsEntity settings) {
        em.merge(settings);
    }

   public void updateSettings (AppSettingsEntity settings) {
        em.merge(settings);
    }




}
