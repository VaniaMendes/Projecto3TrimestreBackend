package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.InterestDao;
import aor.paj.proj_final_aor_backend.dto.Interest;
import aor.paj.proj_final_aor_backend.entity.InterestEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class InterestBean {
    private static final Logger logger = LogManager.getLogger(UserBean.class);


    @EJB
    InterestDao interestDao;


    public InterestBean() {
    }

    public boolean createNewInterest(Interest interest){
        if(interest.getName() == null ){
            logger.error("Interest name is null or empty.");
            return false;
        }
        InterestEntity interestEntity = new InterestEntity();
        interestEntity.setName(interest.getName());

        interestDao.createInterest(interestEntity);
        return true;
    }
}
