package com.saimir.gasa.releasevitals.service.impl;

import com.saimir.gasa.releasevitals.service.VersionService;
import com.saimir.gasa.releasevitals.domain.Version;
import com.saimir.gasa.releasevitals.repository.VersionRepository;
import com.saimir.gasa.releasevitals.repository.search.VersionSearchRepository;
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
 * Service Implementation for managing Version.
 */
@Service
@Transactional
public class VersionServiceImpl implements VersionService {

    private final Logger log = LoggerFactory.getLogger(VersionServiceImpl.class);

    private final VersionRepository versionRepository;

    private final VersionSearchRepository versionSearchRepository;

    public VersionServiceImpl(VersionRepository versionRepository, VersionSearchRepository versionSearchRepository) {
        this.versionRepository = versionRepository;
        this.versionSearchRepository = versionSearchRepository;
    }

    /**
     * Save a version.
     *
     * @param version the entity to save
     * @return the persisted entity
     */
    @Override
    public Version save(Version version) {
        log.debug("Request to save Version : {}", version);
        Version result = versionRepository.save(version);
        versionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the versions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Version> findAll(@PageableDefault(size = 99)Pageable pageable) {
        log.debug("Request to get all Versions");
        return versionRepository.findAll(pageable);
    }


    /**
     * Get one version by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Version> findOne(Long id) {
        log.debug("Request to get Version : {}", id);
        return versionRepository.findById(id);
    }

    /**
     * Delete the version by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Version : {}", id);
        versionRepository.deleteById(id);
        versionSearchRepository.deleteById(id);
    }

    /**
     * Search for the version corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Version> search(String query, @PageableDefault(size = 99)Pageable pageable) {
        log.debug("Request to search for a page of Versions for query {}", query);
        return versionSearchRepository.search(queryStringQuery(query), pageable);    }
}
