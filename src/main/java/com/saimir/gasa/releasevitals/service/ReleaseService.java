package com.saimir.gasa.releasevitals.service;

import com.saimir.gasa.releasevitals.domain.Release;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Release.
 */
public interface ReleaseService {

    /**
     * Save a release.
     *
     * @param release the entity to save
     * @return the persisted entity
     */
    Release save(Release release);

    /**
     * Get all the releases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Release> findAll(Pageable pageable);


    /**
     * Get the "id" release.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Release> findOne(Long id);

    /**
     * Delete the "id" release.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the release corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Release> search(String query, Pageable pageable);
}
