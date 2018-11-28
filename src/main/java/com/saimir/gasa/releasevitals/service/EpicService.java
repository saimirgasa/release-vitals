package com.saimir.gasa.releasevitals.service;

import com.saimir.gasa.releasevitals.domain.Epic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Epic.
 */
public interface EpicService {

    /**
     * Save a epic.
     *
     * @param epic the entity to save
     * @return the persisted entity
     */
    Epic save(Epic epic);

    /**
     * Get all the epics.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Epic> findAll(Pageable pageable);

    /**
     * Get all the Epic with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<Epic> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" epic.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Epic> findOne(Long id);

    /**
     * Delete the "id" epic.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the epic corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Epic> search(String query, Pageable pageable);
}
