package com.saimir.gasa.releasevitals.service.impl;

import com.saimir.gasa.releasevitals.service.ReleaseService;
import com.saimir.gasa.releasevitals.domain.Release;
import com.saimir.gasa.releasevitals.repository.ReleaseRepository;
import com.saimir.gasa.releasevitals.repository.search.ReleaseSearchRepository;
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
 * Service Implementation for managing Release.
 */
@Service
@Transactional
public class ReleaseServiceImpl implements ReleaseService {

    private final Logger log = LoggerFactory.getLogger(ReleaseServiceImpl.class);

    private final ReleaseRepository releaseRepository;

    private final ReleaseSearchRepository releaseSearchRepository;

    public ReleaseServiceImpl(ReleaseRepository releaseRepository, ReleaseSearchRepository releaseSearchRepository) {
        this.releaseRepository = releaseRepository;
        this.releaseSearchRepository = releaseSearchRepository;
    }

    /**
     * Save a release.
     *
     * @param release the entity to save
     * @return the persisted entity
     */
    @Override
    public Release save(Release release) {
        log.debug("Request to save Release : {}", release);
        Release result = releaseRepository.save(release);
        releaseSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the releases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Release> findAll(@PageableDefault(size = 99)Pageable pageable) {
        log.debug("Request to get all Releases");
        return releaseRepository.findAll(pageable);
    }


    /**
     * Get one release by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Release> findOne(Long id) {
        log.debug("Request to get Release : {}", id);
        return releaseRepository.findById(id);
    }

    /**
     * Delete the release by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Release : {}", id);
        releaseRepository.deleteById(id);
        releaseSearchRepository.deleteById(id);
    }

    /**
     * Search for the release corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Release> search(String query, @PageableDefault(size = 99)Pageable pageable) {
        log.debug("Request to search for a page of Releases for query {}", query);
        return releaseSearchRepository.search(queryStringQuery(query), pageable);    }
}
