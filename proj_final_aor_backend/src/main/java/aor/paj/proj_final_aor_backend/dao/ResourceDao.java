package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.ResourceEntity;
import jakarta.ejb.Stateless;

@Stateless
public class ResourceDao extends AbstractDao<ResourceEntity>{
    private static final long serialVersionUID = 1L;

    public ResourceDao() {
        super(ResourceEntity.class);
    }


}
