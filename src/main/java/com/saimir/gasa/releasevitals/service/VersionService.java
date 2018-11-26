package com.saimir.gasa.releasevitals.service;

import com.saimir.gasa.releasevitals.domain.Version;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Version.
 */
public interface VersionService {

    /**
     * Save a version.
     *
     * @param version the entity to save
     * @return the persisted entity
     */
    Version save(Version version);

    /**
     * Get all the versions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Version> findAll(Pageable pageable);


    /**
     * Get the "id" version.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Version> findOne(Long id);

    /**
     * Delete the "id" version.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the version corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Version> search(String query, Pageable pageable);
}
