package com.saimir.gasa.releasevitals.service.impl;

import com.saimir.gasa.releasevitals.service.StatusService;
import com.saimir.gasa.releasevitals.domain.Status;
import com.saimir.gasa.releasevitals.repository.StatusRepository;
import com.saimir.gasa.releasevitals.repository.search.StatusSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Status.
 */
@Service
@Transactional
public class StatusServiceImpl implements StatusService {

    private final Logger log = LoggerFactory.getLogger(StatusServiceImpl.class);

    private final StatusRepository statusRepository;

    private final StatusSearchRepository statusSearchRepository;

    public StatusServiceImpl(StatusRepository statusRepository, StatusSearchRepository statusSearchRepository) {
        this.statusRepository = statusRepository;
        this.statusSearchRepository = statusSearchRepository;
    }

    /**
     * Save a status.
     *
     * @param status the entity to save
     * @return the persisted entity
     */
    @Override
    public Status save(Status status) {
        log.debug("Request to save Status : {}", status);
        Status result = statusRepository.save(status);
        statusSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the statuses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Status> findAll(Pageable pageable) {
        log.debug("Request to get all Statuses");
        return statusRepository.findAll(pageable);
    }


    /**
     * Get one status by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Status> findOne(Long id) {
        log.debug("Request to get Status : {}", id);
        return statusRepository.findById(id);
    }

    /**
     * Delete the status by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Status : {}", id);
        statusRepository.deleteById(id);
        statusSearchRepository.deleteById(id);
    }

    /**
     * Search for the status corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Status> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Statuses for query {}", query);
        return statusSearchRepository.search(queryStringQuery(query), pageable);    }
}
