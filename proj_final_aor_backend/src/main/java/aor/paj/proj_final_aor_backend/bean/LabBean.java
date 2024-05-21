package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.LabDao;
import aor.paj.proj_final_aor_backend.dto.Lab;
import aor.paj.proj_final_aor_backend.entity.LabEntity;
import aor.paj.proj_final_aor_backend.util.enums.Workplace;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

/**
 * This class represents a Stateless Session Bean for Lab operations.
 * It implements Serializable interface for the bean to be passed around in the network.
 */
@Stateless
public class LabBean implements Serializable {

    // Logger for this class
    private static final Logger logger = LogManager.getLogger(LabBean.class);

    // Injecting the Lab Data Access Object
    @EJB
    private LabDao labDao;

    /**
     * Default constructor
     */
    public LabBean() {
    }

    /**
     * Constructor with LabDao parameter
     * @param labDao Lab Data Access Object
     */
    public LabBean(LabDao labDao) {
        this.labDao = labDao;
    }

    /**
     * This method creates labs for all workplaces.
     * It checks if a lab already exists for a workplace, if not it creates a new one.
     */
    public void createAllLabs() {
        for (Workplace workplace : Workplace.values()) {
            if (labDao.findLabByName(workplace.name()) != null) {
                synchronized (this) {
                    Lab lab = new Lab();
                    lab.setName(workplace);
                    createLab(lab);
                }
            } else {
                logger.info("Lab already exists: " + workplace.name());
            }
        }
    }

    /**
     * This method creates a new LabEntity from a Lab DTO and persists it using the LabDao.
     * @param lab Lab DTO
     * @return The created LabEntity
     */
    private LabEntity createLab(Lab lab) {
        LabEntity labEntity = convertToEntity(lab);

        labDao.persist(labEntity);

        return labEntity;
    }

    public LabEntity findLabByName(String name) {
        return labDao.findLabByName(name);
    }

    /**
     * This method converts a Lab DTO to a LabEntity.
     * @param lab Lab DTO
     * @return The converted LabEntity
     */
    public LabEntity convertToEntity(Lab lab) {
        LabEntity labEntity = new LabEntity();

        labEntity.setName(lab.getName());

        return labEntity;
    }

    /**
     * This method converts a LabEntity to a Lab DTO.
     * @param labEntity LabEntity
     * @return The converted Lab DTO
     */
    public Lab convertToDto(LabEntity labEntity) {
        Lab lab = new Lab();

        lab.setId(labEntity.getId());
        lab.setName(labEntity.getName());

        return lab;
    }
}