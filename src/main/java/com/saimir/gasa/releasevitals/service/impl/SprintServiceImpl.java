package com.saimir.gasa.releasevitals.service.impl;

import com.saimir.gasa.releasevitals.service.SprintService;
import com.saimir.gasa.releasevitals.domain.Sprint;
import com.saimir.gasa.releasevitals.repository.SprintRepository;
import com.saimir.gasa.releasevitals.repository.search.SprintSearchRepository;
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
 * Service Implementation for managing Sprint.
 */
@Service
@Transactional
public class SprintServiceImpl implements SprintService {

    private final Logger log = LoggerFactory.getLogger(SprintServiceImpl.class);

    private final SprintRepository sprintRepository;

    private final SprintSearchRepository sprintSearchRepository;

    public SprintServiceImpl(SprintRepository sprintRepository, SprintSearchRepository sprintSearchRepository) {
        this.sprintRepository = sprintRepository;
        this.sprintSearchRepository = sprintSearchRepository;
    }

    /**
     * Save a sprint.
     *
     * @param sprint the entity to save
     * @return the persisted entity
     */
    @Override
    public Sprint save(Sprint sprint) {
        log.debug("Request to save Sprint : {}", sprint);
        Sprint result = sprintRepository.save(sprint);
        sprintSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the sprints.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Sprint> findAll(@PageableDefault(size = 99)Pageable pageable) {
        log.debug("Request to get all Sprints");
        return sprintRepository.findAll(pageable);
    }


    /**
     * Get one sprint by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Sprint> findOne(Long id) {
        log.debug("Request to get Sprint : {}", id);
        return sprintRepository.findById(id);
    }

    /**
     * Delete the sprint by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sprint : {}", id);
        sprintRepository.deleteById(id);
        sprintSearchRepository.deleteById(id);
    }

    /**
     * Search for the sprint corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Sprint> search(String query, @PageableDefault(size = 99)Pageable pageable) {
        log.debug("Request to search for a page of Sprints for query {}", query);
        return sprintSearchRepository.search(queryStringQuery(query), pageable);    }
}
