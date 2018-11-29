package com.saimir.gasa.releasevitals.service.impl;

import com.saimir.gasa.releasevitals.service.IssueService;
import com.saimir.gasa.releasevitals.domain.Issue;
import com.saimir.gasa.releasevitals.repository.IssueRepository;
import com.saimir.gasa.releasevitals.repository.search.IssueSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Issue.
 */
@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private final Logger log = LoggerFactory.getLogger(IssueServiceImpl.class);

    private final IssueRepository issueRepository;

    private final IssueSearchRepository issueSearchRepository;

    public IssueServiceImpl(IssueRepository issueRepository, IssueSearchRepository issueSearchRepository) {
        this.issueRepository = issueRepository;
        this.issueSearchRepository = issueSearchRepository;
    }

    /**
     * Save a issue.
     *
     * @param issue the entity to save
     * @return the persisted entity
     */
    @Override
    public Issue save(Issue issue) {
        log.debug("Request to save Issue : {}", issue);
        Issue result = issueRepository.save(issue);
        issueSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the issues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Issue> findAll(Pageable pageable) {
        log.debug("Request to get all Issues");
        return issueRepository.findAll(pageable);
    }


    /**
     * Get one issue by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Issue> findOne(Long id) {
        log.debug("Request to get Issue : {}", id);
        return issueRepository.findById(id);
    }

    /**
     * Delete the issue by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Issue : {}", id);
        issueRepository.deleteById(id);
        issueSearchRepository.deleteById(id);
    }

    /**
     * Search for the issue corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Issue> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Issues for query {}", query);
        return issueSearchRepository.search(queryStringQuery(query), pageable);    }
}
