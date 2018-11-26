package com.saimir.gasa.releasevitals.service;

import com.saimir.gasa.releasevitals.domain.Issue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Issue.
 */
public interface IssueService {

    /**
     * Save a issue.
     *
     * @param issue the entity to save
     * @return the persisted entity
     */
    Issue save(Issue issue);

    /**
     * Get all the issues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Issue> findAll(Pageable pageable);


    /**
     * Get the "id" issue.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Issue> findOne(Long id);

    /**
     * Delete the "id" issue.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the issue corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Issue> search(String query, Pageable pageable);
}
