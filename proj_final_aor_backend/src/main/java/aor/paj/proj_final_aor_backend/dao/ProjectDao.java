package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.LabEntity;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

/**
 * Stateless session bean that provides CRUD operations for ProjectEntity.
 */
@Stateless
public class ProjectDao extends AbstractDao<ProjectEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ProjectDao() {
        super(ProjectEntity.class);
    }

    /**
     * Retrieves all projects from the database.
     * @return a list of all projects, or an empty list if no projects are found.
     */
    public List<ProjectEntity> findAllProjects() {
        try {
            return em.createNamedQuery("Project.findAllProjects", ProjectEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all projects from the database in descending order.
     * @return a list of all projects in descending order, or an empty list if no projects are found.
     */
    public List<ProjectEntity> findAllProjectsOrderedDESC() {
        try {
            return em.createNamedQuery("Project.findAllProjectsOrderedDESC", ProjectEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a project by its ID.
     * @param id the ID of the project to retrieve.
     * @return the project with the given ID, or null if no such project is found.
     */
    public ProjectEntity findProjectById(Long id) {
        try {
            return (ProjectEntity) em.createNamedQuery("Project.findProjectById").setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Retrieves a project by its name.
     * @param name the name of the project to retrieve.
     * @return the project with the given name, or null if no such project is found.
     */
    public ProjectEntity findProjectByName(String name) {
        try {
            return (ProjectEntity) em.createNamedQuery("Project.findProjectByName").setParameter("name", name)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ProjectEntity> findActiveProjectsByUserIdOrderedASC(Long id) {
        try {
            return em.createNamedQuery("Project.findActiveProjectsByUserIdOrderedASC", ProjectEntity.class).setParameter("id", id).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ProjectEntity> findActiveProjectsByUserIdOrderedDESC(Long id) {
        try {
            return em.createNamedQuery("Project.findActiveProjectsByUserIdOrderedDESC", ProjectEntity.class).setParameter("id", id).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ProjectEntity> findActiveProjectsByUserIdAndStateOrderedASC(Long id, int state) {
        try {
            return em.createNamedQuery("Project.findActiveProjectsByUserIdAndStateOrderedASC", ProjectEntity.class).setParameter("id", id).setParameter("stateId", state).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ProjectEntity> findActiveProjectsByUserIdAndStateOrderedDESC(Long id, int state) {
        try {
            return em.createNamedQuery("Project.findActiveProjectsByUserIdAndStateOrderedDESC", ProjectEntity.class).setParameter("id", id).setParameter("stateId", state).getResultList();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Retrieves all projects associated with a given lab.
     * @param lab the lab to retrieve projects for.
     * @return a list of projects associated with the given lab, or an empty list if no such projects are found.
     */
    public List<ProjectEntity> findProjectsByLab(LabEntity lab) {
        try {
            return em.createNamedQuery("Project.findProjectsByLab", ProjectEntity.class).setParameter("lab", lab)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all projects with a given state in ascending order.
     * @param state the state to retrieve projects for.
     * @return a list of projects with the given state in ascending order, or an empty list if no such projects are found.
     */
    public List<ProjectEntity> findProjectsByStateOrderedASC(int state) {
        try {
            return em.createNamedQuery("Project.findProjectsByStateOrderedASC", ProjectEntity.class).setParameter("stateId", state)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all projects with a given state in descending order.
     * @param state the state to retrieve projects for.
     * @return a list of projects with the given state in descending order, or an empty list if no such projects are found.
     */
    public List<ProjectEntity> findProjectsByStateOrderedDESC(int state) {
        try {
            return em.createNamedQuery("Project.findProjectsByStateOrderedDESC", ProjectEntity.class).setParameter("stateId", state)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all projects that contain a given keyword.
     * @param keyword the keyword to search for in project names.
     * @return a list of projects that contain the given keyword, or an empty list if no such projects are found.
     */
    public List<ProjectEntity> findProjectsByKeyword(String keyword) {
        try {
            return em.createNamedQuery("Project.findProjectsByKeyword", ProjectEntity.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all projects associated with a given skill.
     *
     * This method uses a named query "Project.findProjectsBySkill" defined in the ProjectEntity class.
     * The query is expected to select projects from the database where the skill name matches the provided parameter.
     * The skill name is set as a parameter in the query using the setParameter method.
     *
     * If the query returns results, a list of ProjectEntity objects is returned.
     * If the query does not return any results (i.e., there are no projects associated with the given skill),
     * a NoResultException is caught and an empty list is returned.
     *
     * @param skill the name of the skill to retrieve projects for.
     * @return a list of projects associated with the given skill, or an empty list if no such projects are found.
     */
    public List<ProjectEntity> findProjectsBySkill(String skill) {
        try {
            return em.createNamedQuery("Project.findProjectsBySkill", ProjectEntity.class)
                    .setParameter("skillName", skill)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all projects from the database ordered by vacancies in ascending order.
     * This method uses a named query "Project.orderByVacanciesASC" to retrieve the projects.
     *
     * @return A list of ProjectEntity instances ordered by vacancies in ascending order. Returns an empty list if no projects are found.
     */
    public List<ProjectEntity> findProjectsByVacanciesOrderedASC() {
        try {
            return em.createNamedQuery("Project.orderByVacanciesASC", ProjectEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all projects from the database ordered by vacancies in descending order.
     * This method uses a named query "Project.orderByVacanciesDESC" to retrieve the projects.
     *
     * @return A list of ProjectEntity instances ordered by vacancies in descending order. Returns an empty list if no projects are found.
     */
    public List<ProjectEntity> findProjectsByVacanciesOrderedDESC() {
        try {
            return em.createNamedQuery("Project.orderByVacanciesDESC", ProjectEntity.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all projects associated with a specific user ordered by vacancies in ascending order.
     * This method uses a named query "Project.orderByUserByVacanciesASC" to retrieve the projects.
     *
     * @param userId The ID of the user for whom to retrieve the projects.
     * @return A list of ProjectEntity instances associated with the specified user and ordered by vacancies in ascending order. Returns an empty list if no projects are found.
     */
    public List<ProjectEntity> findProjectsByUserByVacanciesASC(Long userId) {
        try {
            return em.createNamedQuery("Project.orderByUserByVacanciesASC", ProjectEntity.class).setParameter("userId", userId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all projects associated with a specific user ordered by vacancies in descending order.
     * This method uses a named query "Project.orderByUserByVacanciesDESC" to retrieve the projects.
     *
     * @param userId The ID of the user for whom to retrieve the projects.
     * @return A list of ProjectEntity instances associated with the specified user and ordered by vacancies in descending order. Returns an empty list if no projects are found.
     */
    public List<ProjectEntity> findProjectsByUserByVacanciesDESC(Long userId) {
        try {
            return em.createNamedQuery("Project.orderByUserByVacanciesDESC", ProjectEntity.class).setParameter("userId", userId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all projects in a specific state ordered by vacancies in ascending order.
     * This method uses a named query "Project.orderByVacanciesAndStateASC" to retrieve the projects.
     *
     * @param state The state of the projects to be retrieved.
     * @return A list of ProjectEntity instances in the specified state and ordered by vacancies in ascending order. Returns an empty list if no projects are found.
     */
    public List<ProjectEntity> findProjectsByVacanciesAndStateOrderedASC(int state) {
        try {
            return em.createNamedQuery("Project.orderByVacanciesAndStateASC", ProjectEntity.class).setParameter("stateId", state).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all projects in a specific state ordered by vacancies in descending order.
     * This method uses a named query "Project.orderByVacanciesAndStateDESC" to retrieve the projects.
     *
     * @param state The state of the projects to be retrieved.
     * @return A list of ProjectEntity instances in the specified state and ordered by vacancies in descending order. Returns an empty list if no projects are found.
     */
    public List<ProjectEntity> findProjectsByVacanciesAndStateOrderedDESC(int state) {
        try {
            return em.createNamedQuery("Project.orderByVacanciesAndStateDESC", ProjectEntity.class).setParameter("stateId", state).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Counts the total number of projects in the database.
     * This method uses a named query "Project.countAllProjects" to count the projects.
     *
     * @return The total number of projects in the database. Returns null if an exception occurs.
     */
    public Integer countAllProjects() {
        try {
            return ((Number) em.createNamedQuery("Project.countAllProjects").getSingleResult()).intValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Counts the number of projects in the database that are in a specific state.
     * This method uses a named query "Project.countProjectsByState" to count the projects.
     *
     * @param state The state of the projects to be counted.
     * @return The number of projects in the specified state. Returns null if an exception occurs.
     */
    public Integer countProjectsByState(int state) {
        try {
            return ((Number) em.createNamedQuery("Project.countProjectsByState").setParameter("stateId", state).getSingleResult()).intValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Counts the number of projects in the database that require a specific skill.
     * This method uses a named query "Project.countProjectsBySkill" to count the projects.
     *
     * @param skill The skill required by the projects to be counted.
     * @return The number of projects that require the specified skill. Returns null if an exception occurs.
     */
    public Integer countProjectsBySkill(String skill) {
        try {
            return ((Number) em.createNamedQuery("Project.countProjectsBySkill").setParameter("skillName", skill).getSingleResult()).intValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Counts the number of projects in the database that contain a specific keyword in their name.
     * This method uses a named query "Project.countProjectsByKeyword" to count the projects.
     *
     * @param keyword The keyword to be found in the names of the projects to be counted.
     * @return The number of projects that contain the specified keyword in their name. Returns null if an exception occurs.
     */
    public Integer countProjectsByKeyword(String keyword) {
        try {
            return ((Number) em.createNamedQuery("Project.countProjectsByKeyword").setParameter("keyword", "%" + keyword + "%").getSingleResult()).intValue();
        } catch (Exception e) {
            return null;
        }
    }
}