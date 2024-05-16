package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.LabEntity;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import aor.paj.proj_final_aor_backend.entity.UserEntity;
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
    public ProjectEntity findProjectById(String id) {
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
     * Retrieves all users associated with a given project.
     *
     * This method uses a named query "Project.findUsersByProjectId" defined in the UserEntity class.
     * The query is expected to select users from the database where the project ID matches the provided parameter.
     * The project ID is set as a parameter in the query using the setParameter method.
     *
     * If the query returns results, a list of UserEntity objects is returned.
     * If the query does not return any results (i.e., there are no users associated with the given project),
     * a NoResultException is caught and an empty list is returned.
     *
     * @param projectId the ID of the project to retrieve users for.
     * @return a list of users associated with the given project, or an empty list if no such users are found.
     */
    public List<UserEntity> findUsersByProjectId(Long projectId) {
        try {
            return em.createNamedQuery("Project.findUsersByProjectId", UserEntity.class)
                    .setParameter("projectId", projectId)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }
}