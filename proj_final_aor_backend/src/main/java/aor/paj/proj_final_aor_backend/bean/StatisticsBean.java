package aor.paj.proj_final_aor_backend.bean;

import aor.paj.proj_final_aor_backend.dao.ProjectDao;
import aor.paj.proj_final_aor_backend.dao.UserProjectDao;
import aor.paj.proj_final_aor_backend.dto.ProjectStatsDTO;
import aor.paj.proj_final_aor_backend.util.enums.Workplace;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class StatisticsBean {

    private static final Logger logger = LogManager.getLogger(StatisticsBean.class);

    @EJB
    private ProjectDao projectDao;

    @EJB
    private UserProjectDao userProjectDao;

    public StatisticsBean() {
    }

    private List<Map<String, Long>> getNumberOfSubmittedProjects() {
        return getProjectCounts(projectDao.countProjectsByLab());
    }

    private List<Map<String, Long>> getNumberOfApprovedProjects() {
        return getProjectCounts(projectDao.countApprovedProjectsByLab());
    }

    private List<Map<String, Long>> getProjectCounts(List<Object[]> rawData) {
        Map<String, Long> defaultMap = new HashMap<>();
        for (Workplace lab : Workplace.values()) {
            defaultMap.put(lab.name(), 0L);
        }

        for (Object[] entry : rawData) {
            String labName = Workplace.getWorkplaceNameByOrdinal((Integer) entry[0] - 1);
            defaultMap.put(labName, (Long) entry[1]);
        }

        List<Map<String, Long>> result = new ArrayList<>();
        defaultMap.forEach((key, value) -> result.add(new HashMap<>() {{ put(key, value); }}));
        return result;
    }

    private List<Map<String, Long>> getNumberOfFinishedProjects() {
        return getProjectCounts(projectDao.countFinishedProjectsByLab());
    }

    private List<Map<String, Long>> getNumberOfCancelledProjects() {
        return getProjectCounts(projectDao.countCancelledProjectsByLab());
    }

    private Double averageNumberOfActiveMembers() {
        return userProjectDao.countAverageActiveUsers();
    }

    private List<Map<String, Double>> getPercentageOfSubmittedProjects() {
        List<Object[]> rawData = projectDao.countProjectsByLab();
        Map<String, Double> labPercentages = new HashMap<>();
        Integer totalProjects = projectDao.countAllProjects();

        // Initialize labPercentages with all labs and default percentage of 0.0
        for (Workplace lab : Workplace.values()) {
            labPercentages.put(lab.name(), 0.0);
        }

        // Only proceed if totalProjects is not null and not zero
        if (totalProjects != null && totalProjects != 0) {
            for (Object[] entry : rawData) {
                String labName = Workplace.getWorkplaceNameByOrdinal((Integer) entry[0] - 1);
                long count = (Long) entry[1];
                double percentage = (double) count * 100 / totalProjects;
                labPercentages.put(labName, percentage);
            }
        }

        // Convert the map to the expected List<Map<String, Double>> format
        List<Map<String, Double>> result = new ArrayList<>();
        labPercentages.forEach((labName, percentage) -> result.add(new HashMap<>() {{ put(labName, percentage); }}));
        return result;
    }

    private List<Map<String, Double>> getPercentageOfApprovedProjects() {
        List<Object[]> rawData = projectDao.countApprovedProjectsByLab();
        Map<String, Double> labPercentages = new HashMap<>();
        Integer totalProjects = projectDao.countProjectsByState(300);

        // Initialize labPercentages with all labs and default percentage of 0.0
        for (Workplace lab : Workplace.values()) {
            labPercentages.put(lab.name(), 0.0);
        }

        // Only proceed if totalProjects is not null and not zero
        if (totalProjects != null && totalProjects != 0) {
            for (Object[] entry : rawData) {
                String labName = Workplace.getWorkplaceNameByOrdinal((Integer) entry[0] - 1);
                long count = (Long) entry[1];
                double percentage = (double) count * 100 / totalProjects;
                labPercentages.put(labName, percentage);
            }
        }

        // Convert the map to the expected List<Map<String, Double>> format
        List<Map<String, Double>> result = new ArrayList<>();
        labPercentages.forEach((labName, percentage) -> result.add(new HashMap<>() {{ put(labName, percentage); }}));
        return result;
    }

    private List<Map<String, Double>> getPercentageOfFinishedProjects() {
        List<Object[]> rawData = projectDao.countFinishedProjectsByLab();
        List<Map<String, Double>> result = new ArrayList<>();

        Integer totalProjects = projectDao.countProjectsByState(500);

        for (Object[] entry : rawData) {
            Map<String, Double> map = new HashMap<>();
            int labId = (Integer) entry[0];
            String labName = Workplace.getWorkplaceNameByOrdinal(labId - 1);
            long count = (Long) entry[1];

            // Check if totalProjects is null and handle accordingly
            if (totalProjects != null && totalProjects != 0) {
                double percentage = (double) count * 100 / totalProjects;
                map.put(labName, percentage);
            } else {
                // Decide how to handle null or zero totalProjects. Here, we're setting percentage to 0.
                map.put(labName, 0.0);
            }
            result.add(map);
        }
        return result;
    }

    private List<Map<String, Double>> getPercentageOfCancelledProjects() {
        List<Object[]> rawData = projectDao.countCancelledProjectsByLab();
        Map<String, Double> labPercentages = new HashMap<>();
        Integer totalProjects = projectDao.countProjectsByState(600);

        // Initialize labPercentages with all labs and default percentage of 0.0
        for (Workplace lab : Workplace.values()) {
            labPercentages.put(lab.name(), 0.0);
        }

        // Only proceed if totalProjects is not null and not zero
        if (totalProjects != null && totalProjects != 0) {
            for (Object[] entry : rawData) {
                String labName = Workplace.getWorkplaceNameByOrdinal((Integer) entry[0] - 1);
                long count = (Long) entry[1];
                double percentage = (double) count * 100 / totalProjects;
                labPercentages.put(labName, percentage);
            }
        }

        // Convert the map to the expected List<Map<String, Double>> format
        List<Map<String, Double>> result = new ArrayList<>();
        labPercentages.forEach((labName, percentage) -> result.add(new HashMap<>() {{ put(labName, percentage); }}));
        return result;
    }

    public ProjectStatsDTO getProjectStatistics() {
        ProjectStatsDTO dto = new ProjectStatsDTO();
        dto.setNumberOfSubmittedProjects(getNumberOfSubmittedProjects());
        dto.setPercentageOfSubmittedProjects(getPercentageOfSubmittedProjects());
        dto.setNumberOfApprovedProjects(getNumberOfApprovedProjects());
        dto.setPercentageOfApprovedProjects(getPercentageOfApprovedProjects());
        dto.setNumberOfFinishedProjects(getNumberOfFinishedProjects());
        dto.setPercentageOfFinishedProjects(getPercentageOfFinishedProjects());
        dto.setNumberOfCancelledProjects(getNumberOfCancelledProjects());
        dto.setPercentageOfCancelledProjects(getPercentageOfCancelledProjects());
        dto.setAverageNumberOfActiveMembers(averageNumberOfActiveMembers());
        return dto;
    }
}