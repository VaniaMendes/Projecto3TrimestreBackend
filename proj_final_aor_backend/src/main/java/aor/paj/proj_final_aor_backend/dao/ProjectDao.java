package aor.paj.proj_final_aor_backend.dao;

import aor.paj.proj_final_aor_backend.entity.LabEntity;
import aor.paj.proj_final_aor_backend.entity.ProjectEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
     * Retrieves all projects that contain a given keyword in ascending order.
     * This method uses a named query "Project.findProjectsByKeywordOrderedASC" defined in the ProjectEntity class.
     * The query is expected to select projects from the database where the keyword matches the provided parameter.
     * The keyword is set as a parameter in the query using the setParameter method.
     * The '%' wildcard character is used to ensure that the keyword can be found anywhere within the project's keywords.
     *
     * If the query returns results, a list of ProjectEntity objects is returned.
     * If the query does not return any results (i.e., there are no projects containing the given keyword),
     * a NoResultException is caught and an empty list is returned.
     *
     * @param keyword the keyword to search for in project keywords.
     * @return a list of projects that contain the given keyword in ascending order, or an empty list if no such projects are found.
     */
    public List<ProjectEntity> findProjectsByKeywordOrSkillOrderedASC(String keyword) {
        try {
            return em.createNamedQuery("Project.findProjectsByKeywordOrSkillOrderedASC", ProjectEntity.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all projects that contain a given keyword in descending order.
     * This method uses a named query "Project.findProjectsByKeywordOrderedDESC" defined in the ProjectEntity class.
     * The query is expected to select projects from the database where the keyword matches the provided parameter.
     * The keyword is set as a parameter in the query using the setParameter method.
     * The '%' wildcard character is used to ensure that the keyword can be found anywhere within the project's keywords.
     *
     * If the query returns results, a list of ProjectEntity objects is returned.
     * If the query does not return any results (i.e., there are no projects containing the given keyword),
     * a NoResultException is caught and an empty list is returned.
     *
     * @param keyword the keyword to search for in project keywords.
     * @return a list of projects that contain the given keyword in descending order, or an empty list if no such projects are found.
     */
    public List<ProjectEntity> findProjectsByKeywordOrSkillOrderedDESC(String keyword) {
        try {
            return em.createNamedQuery("Project.findProjectsByKeywordOrSkillOrderedDESC", ProjectEntity.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Finds and returns a list of ProjectEntity objects that match a given keyword and state, ordered in ascending order.
     * This method performs a database query using a named query "Project.findProjectsByKeywordAndStateOrderedASC".
     * The query selects projects that contain the specified keyword in any part of their details and match the given state.
     * The '%' wildcard is used around the keyword to allow for partial matches.
     *
     * @param keyword The keyword to search for within project details.
     * @param state The state that the projects must match.
     * @return A list of ProjectEntity objects that match the criteria, ordered in ascending order.
     *         Returns an empty list if no matching projects are found.
     */
    public List<ProjectEntity> findProjectsByKeywordOrSkillAndStateOrderedASC(String keyword, int state) {
        try {
            return em.createNamedQuery("Project.findProjectsByKeywordOrSkillAndStateOrderedASC", ProjectEntity.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .setParameter("stateId", state)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Finds and returns a list of ProjectEntity objects that match a given keyword and state, ordered in descending order.
     * This method performs a database query using a named query "Project.findProjectsByKeywordAndStateOrderedDESC".
     * Similar to the ascending order method, this query selects projects that contain the specified keyword in any part
     * of their details and match the given state, but the results are ordered in descending order.
     * The '%' wildcard is used around the keyword to allow for partial matches.
     *
     * @param keyword The keyword to search for within project details.
     * @param state The state that the projects must match.
     * @return A list of ProjectEntity objects that match the criteria, ordered in descending order.
     *         Returns an empty list if no matching projects are found.
     */
    public List<ProjectEntity> findProjectsByKeywordOrSkillAndStateOrderedDESC(String keyword, int state) {
        try {
            return em.createNamedQuery("Project.findProjectsByKeywordOrSkillAndStateOrderedDESC", ProjectEntity.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .setParameter("stateId", state)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a list of projects filtered by a keyword and ordered by vacancies in ascending order.
     * This method performs a database query using a named query "Project.findProjectsByKeywordOrderedByVacanciesASC".
     * The query is designed to find projects where the project's details contain the specified keyword,
     * and the results are ordered by the number of vacancies in ascending order. The '%' wildcard is used
     * around the keyword parameter to allow for partial matches.
     *
     * @param keyword The keyword to search for within project details.
     * @return A list of {@link ProjectEntity} instances that match the search criteria, ordered by vacancies in ascending order.
     *         Returns an empty list if no matching projects are found.
     */
    public List<ProjectEntity> findProjectsByKeywordOrSkillOrderedByVacanciesASC(String keyword) {
        try {
            return em.createNamedQuery("Project.findProjectsByKeywordOrSkillOrderedByVacanciesASC", ProjectEntity.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a list of projects filtered by a keyword and ordered by vacancies in descending order.
     * This method performs a database query using a named query "Project.findProjectsByKeywordOrderedByVacanciesDESC".
     * The query is designed to find projects where the project's details contain the specified keyword,
     * and the results are ordered by the number of vacancies in descending order. The '%' wildcard is used
     * around the keyword parameter to allow for partial matches.
     *
     * @param keyword The keyword to search for within project details.
     * @return A list of {@link ProjectEntity} instances that match the search criteria, ordered by vacancies in descending order.
     *         Returns an empty list if no matching projects are found.
     */
    public List<ProjectEntity> findProjectsByKeywordOrSkillOrderedByVacanciesDESC(String keyword) {
        try {
            return em.createNamedQuery("Project.findProjectsByKeywordOrSkillOrderedByVacanciesDESC", ProjectEntity.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all unique keywords from the projects in the database.
     *
     * This method executes a named query to get a list of keyword strings from the database,
     * then uses Java 8's Stream API to split each string into individual keywords, trim any whitespace,
     * and collect the keywords into a set, which automatically removes duplicates.
     *
     * @return a set of all unique keywords from the projects in the database.
     */
    public List<String> findAllUniqueKeywords() {
        // Execute the named query to get the list of keyword strings
        List<String> keywordStrings = em.createNamedQuery("Project.findAllKeywords", String.class).getResultList();

        Set<String> uniqueKeywords = keywordStrings.stream()
                // Split each keyword string into individual keywords
                .flatMap(keywords -> Arrays.stream(keywords.split(",")))
                // Trim whitespace from each keyword
                .map(String::trim)
                // Collect the keywords into a set, automatically removing duplicates
                .collect(Collectors.toSet());

        // Convert the set to a list
        return new ArrayList<>(uniqueKeywords);
    }

    /**
     * Searches for individual keywords within the projects' keywords that contain a specified piece of text.
     * This method performs a database query to fetch projects whose keywords contain the specified search term.
     * The keywords in the database are stored as comma-separated strings. This method splits these strings
     * into individual keywords, filters them to include only those that contain the specified piece of text,
     * and returns a list of unique keywords that match the criteria.
     *
     * @param keyword The piece of text to search for within the project keywords.
     * @return A list of unique keywords that contain the specified piece of text. If no keywords match,
     *         or an exception occurs (e.g., {@link NoResultException}), an empty list is returned.
     */
    public List<String> searchKeywords(String keyword) {
        try {
            // Fetch the comma-separated keywords string from the database
            List<String> keywordStrings = em.createNamedQuery("Project.searchKeywords", String.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();

            // Split the strings into individual keywords and filter them
            List<String> filteredKeywords = keywordStrings.stream()
                    .flatMap(keywords -> Arrays.stream(keywords.split(",")))
                    .filter(k -> k.trim().toLowerCase().contains(keyword.toLowerCase()))
                    .distinct()
                    .collect(Collectors.toList());

            return filteredKeywords;
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Searches for projects by name in descending order.
     * This method performs a database query using a named query "Project.searchProjectsByNameOrderedDESC".
     * The query is designed to find projects where the project name contains the specified name parameter,
     * allowing for partial matches. The '%' wildcard is used before and after the name parameter to achieve this.
     * If projects are found, a list of {@link ProjectEntity} instances is returned, ordered by name in descending order.
     * If no projects are found, an empty list is returned.
     *
     * @param name The name or partial name of the project to search for.
     * @return A list of {@link ProjectEntity} instances that match the search criteria, ordered by name in descending order.
     *         Returns an empty list if no projects are found.
     */
    public List<ProjectEntity> searchProjectsByNameOrderedDESC(String name) {
        try {
            return em.createNamedQuery("Project.searchProjectsByNameOrderedDESC", ProjectEntity.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Searches for projects by name in ascending order.
     * This method performs a database query using a named query "Project.searchProjectsByNameOrderedASC".
     * Similar to the descending order method, this query finds projects where the project name contains the specified name parameter,
     * using the '%' wildcard for partial matches. The results are ordered by name in ascending order.
     * If projects are found, a list of {@link ProjectEntity} instances is returned.
     * If no projects are found, an empty list is returned.
     *
     * @param name The name or partial name of the project to search for.
     * @return A list of {@link ProjectEntity} instances that match the search criteria, ordered by name in ascending order.
     *         Returns an empty list if no projects are found.
     */
    public List<ProjectEntity> searchProjectsByNameOrderedASC(String name) {
        try {
            return em.createNamedQuery("Project.searchProjectsByNameOrderedASC", ProjectEntity.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Searches for projects by name and state in ascending order.
     * This method uses a named query "Project.searchProjectsByNameAndStateOrderedASC" to perform a database query.
     * It finds projects where the project name contains the specified name parameter and matches the specified state.
     * The '%' wildcard is used for partial name matches. Results are ordered by name in ascending order.
     * If matching projects are found, a list of {@link ProjectEntity} instances is returned.
     * If no projects are found, an empty list is returned.
     *
     * @param name The name or partial name of the project to search for.
     * @param state The state of the projects to be matched.
     * @return A list of {@link ProjectEntity} instances that match the search criteria, ordered by name in ascending order.
     *         Returns an empty list if no projects are found.
     */
    public List<ProjectEntity> searchProjectsByNameAndStateOrderedASC(String name, int state) {
        try {
            return em.createNamedQuery("Project.searchProjectsByNameAndStateOrderedASC", ProjectEntity.class)
                    .setParameter("name", "%" + name + "%")
                    .setParameter("stateId", state)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Searches for projects by name and state in descending order.
     * This method performs a database query using a named query "Project.searchProjectsByNameAndStateOrderedDESC".
     * It finds projects where the project name contains the specified name parameter and matches the specified state,
     * using the '%' wildcard for partial name matches. The results are ordered by name in descending order.
     * If matching projects are found, a list of {@link ProjectEntity} instances is returned.
     * If no projects are found, an empty list is returned.
     *
     * @param name The name or partial name of the project to search for.
     * @param state The state of the projects to be matched.
     * @return A list of {@link ProjectEntity} instances that match the search criteria, ordered by name in descending order.
     *         Returns an empty list if no projects are found.
     */
    public List<ProjectEntity> searchProjectsByNameAndStateOrderedDESC(String name, int state) {
        try {
            return em.createNamedQuery("Project.searchProjectsByNameAndStateOrderedDESC", ProjectEntity.class)
                    .setParameter("name", "%" + name + "%")
                    .setParameter("stateId", state)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Searches for projects by name ordered by vacancies in ascending order.
     * This method performs a database query using a named query "Project.searchProjectsByNameOrderedByVacanciesASC".
     * It finds projects where the project name contains the specified name parameter, using the '%' wildcard for partial matches.
     * The results are ordered by the number of vacancies in ascending order.
     * If projects are found, a list of {@link ProjectEntity} instances is returned.
     * If no projects are found, an empty list is returned.
     *
     * @param name The name or partial name of the project to search for.
     * @return A list of {@link ProjectEntity} instances that match the search criteria, ordered by vacancies in ascending order.
     *         Returns an empty list if no projects are found.
     */
    public List<ProjectEntity> searchProjectsByNameOrderedByVacanciesASC(String name) {
        try {
            return em.createNamedQuery("Project.searchProjectsByNameOrderedByVacanciesASC", ProjectEntity.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Searches for projects by name ordered by vacancies in descending order.
     * This method performs a database query using a named query "Project.searchProjectsByNameOrderedByVacanciesDESC".
     * It finds projects where the project name contains the specified name parameter, using the '%' wildcard for partial matches.
     * The results are ordered by the number of vacancies in descending order.
     * If projects are found, a list of {@link ProjectEntity} instances is returned.
     * If no projects are found, an empty list is returned.
     *
     * @param name The name or partial name of the project to search for.
     * @return A list of {@link ProjectEntity} instances that match the search criteria, ordered by vacancies in descending order.
     *         Returns an empty list if no projects are found.
     */
    public List<ProjectEntity> searchProjectsByNameOrderedByVacanciesDESC(String name) {
        try {
            return em.createNamedQuery("Project.searchProjectsByNameOrderedByVacanciesDESC", ProjectEntity.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
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
     * Counts the number of projects in the database that contain a specific keyword in their name.
     * This method uses a named query "Project.countProjectsByKeyword" to count the projects.
     *
     * @param keyword The keyword to be found in the names of the projects to be counted.
     * @return The number of projects that contain the specified keyword in their name. Returns null if an exception occurs.
     */
    public Integer countProjectsByKeywordOrSkill(String keyword) {
        try {
            return ((Number) em.createNamedQuery("Project.countProjectsByKeywordOrSkill").setParameter("keyword", "%" + keyword + "%").getSingleResult()).intValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Counts the number of projects in the database that match both a specified keyword and state.
     * This method performs a database query using a named query "Project.countProjectsByKeywordAndState".
     * The query is designed to count projects where the project's details contain the specified keyword
     * and match the specified state. The '%' wildcard is used before and after the keyword parameter
     * to allow for partial matches. The state is matched exactly.
     *
     * @param keyword The keyword to be found in the projects' details.
     * @param state The state of the projects to be counted.
     * @return The number of projects that match both the specified keyword and state. Returns null if an exception occurs.
     */
    public Integer countProjectsByKeywordOrSkillAndState(String keyword, int state) {
        try {
            return ((Number) em.createNamedQuery("Project.countProjectsByKeywordOrSkillAndState")
                    .setParameter("keyword", "%" + keyword + "%")
                    .setParameter("stateId", state)
                    .getSingleResult()).intValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Counts the number of projects in the database that match a specified name or part of it.
     * This method performs a database query using a named query "Project.countSearchedProjectsByName".
     * The query is designed to count projects where the project name contains the specified search term,
     * allowing for partial matches. The '%' wildcard is used before and after the name parameter to achieve this.
     *
     * @param name The name or partial name of the project to search for.
     * @return The total number of projects matching the search criteria. Returns null if an exception occurs during query execution.
     */
    public Integer countSearchProjectsByName(String name) {
        try {
            return ((Number) em.createNamedQuery("Project.countSearchedProjectsByName").setParameter("name", "%" + name + "%").getSingleResult()).intValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Counts the number of projects in the database that match both a specified name (or part of it) and state.
     * This method performs a database query using a named query "Project.countSearchedProjectsByNameAndState".
     * The query is designed to count projects where the project name contains the specified name parameter,
     * allowing for partial matches by using the '%' wildcard before and after the name parameter. The state is matched exactly.
     *
     * @param name The name or partial name of the project to search for.
     * @param state The state of the projects to be counted.
     * @return The total number of projects matching the search criteria. Returns null if an exception occurs during query execution.
     */
    public Integer countSearchProjectsByNameAndState(String name, int state) {
        try {
            return ((Number) em.createNamedQuery("Project.countSearchedProjectsByNameAndState")
                    .setParameter("name", "%" + name + "%")
                    .setParameter("stateId", state)
                    .getSingleResult()).intValue();
        } catch (Exception e) {
            return null;
        }
    }
}