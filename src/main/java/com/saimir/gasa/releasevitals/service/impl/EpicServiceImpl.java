package com.saimir.gasa.releasevitals.service.impl;

import com.saimir.gasa.releasevitals.service.EpicService;
import com.saimir.gasa.releasevitals.domain.Epic;
import com.saimir.gasa.releasevitals.repository.EpicRepository;
import com.saimir.gasa.releasevitals.repository.search.EpicSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Epic.
 */
@Service
@Transactional
public class EpicServiceImpl implements EpicService {

    private final Logger log = LoggerFactory.getLogger(EpicServiceImpl.class);

    private final EpicRepository epicRepository;

    private final EpicSearchRepository epicSearchRepository;

    public EpicServiceImpl(EpicRepository epicRepository, EpicSearchRepository epicSearchRepository) {
        this.epicRepository = epicRepository;
        this.epicSearchRepository = epicSearchRepository;
    }

    /**
     * Save a epic.
     *
     * @param epic the entity to save
     * @return the persisted entity
     */
    @Override
    public Epic save(Epic epic) {
        log.debug("Request to save Epic : {}", epic);
        Epic result = epicRepository.save(epic);
        epicSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the epics.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Epic> findAll(@PageableDefault(size = 99)Pageable pageable) {
        log.debug("Request to get all Epics");
        return epicRepository.findAll(pageable);
    }

    /**
     * Get all the Epic with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Epic> findAllWithEagerRelationships(@PageableDefault(size = 99)Pageable pageable) {
        return epicRepository.findAllWithEagerRelationships(pageable);
    }


    /**
     * Get one epic by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Epic> findOne(Long id) {
        log.debug("Request to get Epic : {}", id);
        return epicRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the epic by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Epic : {}", id);
        epicRepository.deleteById(id);
        epicSearchRepository.deleteById(id);
    }

    /**
     * Search for the epic corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Epic> search(String query, @PageableDefault(size = 99)Pageable pageable) {
        log.debug("Request to search for a page of Epics for query {}", query);
        return epicSearchRepository.search(queryStringQuery(query), pageable);    }
}
