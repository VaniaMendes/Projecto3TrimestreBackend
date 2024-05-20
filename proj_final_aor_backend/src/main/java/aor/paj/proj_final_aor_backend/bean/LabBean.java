package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.LabDao;
import aor.paj.proj_final_aor_backend.dto.Lab;
import aor.paj.proj_final_aor_backend.entity.LabEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

@Stateless
public class LabBean implements Serializable {

    private static final Logger logger = LogManager.getLogger(LabBean.class);

    @EJB
    private LabDao labDao;

    public LabBean() {
    }

    public LabBean(LabDao labDao) {
        this.labDao = labDao;
    }

    public LabEntity convertToEntity(Lab lab) {
        LabEntity labEntity = new LabEntity();

        labEntity.setName(lab.getName());

        return labEntity;
    }

    public Lab convertToDto(LabEntity labEntity) {
        Lab lab = new Lab();

        lab.setId(labEntity.getId());
        lab.setName(labEntity.getName());

        return lab;
    }



}
