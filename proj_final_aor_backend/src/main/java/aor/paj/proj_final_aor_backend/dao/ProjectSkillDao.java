package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.ProjectSkillEntity;
import aor.paj.proj_final_aor_backend.entity.SkillEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProjectSkillDao extends AbstractDao<ProjectSkillEntity>{

    private static final long serialVersionUID = 1L;

    public ProjectSkillDao() {
        super(ProjectSkillEntity.class);
    }

    /**
     * Finds all skills associated with a given project.
     * This method queries the database using a named query "ProjectSkill.findAllSkillsFromProject"
     * to retrieve all skills linked to a specific project identified by its ID.
     *
     * @param projectId The ID of the project for which skills are to be found.
     * @return A list of ProjectSkillEntity objects representing the skills associated with the project.
     *         Returns an empty list if no skills are found.
     */
    public List<ProjectSkillEntity> findAllSkillsFromProject(Long projectId) {
        try {
            return em.createNamedQuery("ProjectSkill.findAllSkillsFromProject", ProjectSkillEntity.class)
                    .setParameter("projectId", projectId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Finds all projects associated with a given skill, ordered by descending order.
     * This method queries the database using a named query "ProjectSkill.findAllProjectsFromSkillOrderedDESC"
     * to retrieve all projects linked to a specific skill identified by its ID, with the results
     * ordered in descending order based on a predefined criteria.
     *
     * @param skillId The ID of the skill for which projects are to be found.
     * @return A list of ProjectSkillEntity objects representing the projects associated with the skill,
     *         ordered by descending order. Returns an empty list if no projects are found.
     */
    public List<ProjectSkillEntity> findAllProjectsFromSkillOrderedDESC(Long skillId) {
        try {
            return em.createNamedQuery("ProjectSkill.findAllProjectsFromSkillOrderedDESC", ProjectSkillEntity.class)
                    .setParameter("skillId", skillId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Finds all projects associated with a given skill, ordered by ascending order.
     * This method queries the database using a named query "ProjectSkill.findAllProjectsFromSkillOrderedASC"
     * to retrieve all projects linked to a specific skill identified by its ID, with the results
     * ordered in ascending order based on a predefined criteria.
     *
     * @param skillId The ID of the skill for which projects are to be found.
     * @return A list of ProjectSkillEntity objects representing the projects associated with the skill,
     *         ordered by ascending order. Returns an empty list if no projects are found.
     */
    public List<ProjectSkillEntity> findAllProjectsFromSkillOrderedASC(Long skillId) {
        try {
            return em.createNamedQuery("ProjectSkill.findAllProjectsFromSkillOrderedASC", ProjectSkillEntity.class)
                    .setParameter("skillId", skillId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Finds all projects associated with a given skill and state, ordered by descending order.
     * This method queries the database using a named query "ProjectSkill.findAllProjectsFromSkillByStateOrderedDESC"
     * to retrieve all projects linked to a specific skill identified by its ID and a specific state,
     * with the results ordered in descending order based on a predefined criteria.
     *
     * @param skillId The ID of the skill for which projects are to be found.
     * @param state The state of the projects to be filtered by.
     * @return A list of ProjectSkillEntity objects representing the projects associated with the skill and state,
     *         ordered by descending order. Returns an empty list if no projects are found.
     */
    public List<ProjectSkillEntity> findAllProjectsFromSkillByStateOrderedDESC(Long skillId, Integer state) {
        try {
            return em.createNamedQuery("ProjectSkill.findAllProjectsFromSkillByStateOrderedDESC", ProjectSkillEntity.class)
                    .setParameter("skillId", skillId)
                    .setParameter("state", state).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Finds all projects associated with a given skill and state, ordered by ascending order.
     * This method queries the database using a named query "ProjectSkill.findAllProjectsFromSkillByStateOrderedASC"
     * to retrieve all projects linked to a specific skill identified by its ID and a specific state,
     * with the results ordered in ascending order based on a predefined criteria.
     *
     * @param skillId The ID of the skill for which projects are to be found.
     * @param state The state of the projects to be filtered by.
     * @return A list of ProjectSkillEntity objects representing the projects associated with the skill and state,
     *         ordered by ascending order. Returns an empty list if no projects are found.
     */
    public List<ProjectSkillEntity> findAllProjectsFromSkillByStateOrderedASC(Long skillId, Integer state) {
        try {
            return em.createNamedQuery("ProjectSkill.findAllProjectsFromSkillByStateOrderedASC", ProjectSkillEntity.class)
                    .setParameter("skillId", skillId)
                    .setParameter("state", state).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Finds a specific skill associated with a given project.
     * This method queries the database using a named query "ProjectSkill.findSkillFromProject"
     * to retrieve a single skill linked to a specific project identified by its ID and the skill's ID.
     *
     * @param projectId The ID of the project for which the skill is to be found.
     * @param skillId The ID of the skill to be found.
     * @return A ProjectSkillEntity object representing the skill associated with the project.
     *         Returns null if the skill is not found.
     */
    public ProjectSkillEntity findSkillFromProject(Long projectId, Long skillId) {
        try {
            return em.createNamedQuery("ProjectSkill.findSkillFromProject", ProjectSkillEntity.class)
                    .setParameter("projectId", projectId)
                    .setParameter("skillId", skillId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Finds all skills not associated with a given project.
     * This method queries the database using a named query "ProjectSkill.findAllSkillsNotInProject"
     * to retrieve all skills not linked to a specific project identified by its ID.
     *
     * @param projectId The ID of the project for which non-associated skills are to be found.
     * @return A list of SkillEntity objects representing the skills not associated with the project.
     *         Returns an empty list if no skills are found.
     */
    public List<SkillEntity> findAllSkillsNotInProject(Long projectId) {
        try {
            return em.createNamedQuery("ProjectSkill.findAllSkillsNotInProject", SkillEntity.class)
                    .setParameter("projectId", projectId).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

}
