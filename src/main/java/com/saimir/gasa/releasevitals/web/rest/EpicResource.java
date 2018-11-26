package com.saimir.gasa.releasevitals.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.saimir.gasa.releasevitals.domain.Epic;
import com.saimir.gasa.releasevitals.service.EpicService;
import com.saimir.gasa.releasevitals.service.JiraService;
import com.saimir.gasa.releasevitals.web.rest.errors.BadRequestAlertException;
import com.saimir.gasa.releasevitals.web.rest.util.HeaderUtil;
import com.saimir.gasa.releasevitals.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Epic.
 */
@RestController
@RequestMapping("/api")
public class EpicResource {

    private final Logger log = LoggerFactory.getLogger(EpicResource.class);

    private static final String ENTITY_NAME = "epic";

    private final EpicService epicService;

    private final JiraService jiraService;

    public EpicResource(EpicService epicService, JiraService jiraService) {
        this.epicService = epicService;
        this.jiraService = jiraService;
    }

    /**
     * POST  /epics : Create a new epic.
     *
     * @param epic the epic to create
     * @return the ResponseEntity with status 201 (Created) and with body the new epic, or with status 400 (Bad Request) if the epic has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/epics")
    @Timed
    public ResponseEntity<Epic> createEpic(@RequestBody Epic epic) throws URISyntaxException {
        log.debug("REST request to save Epic : {}", epic);
        if (epic.getId() != null) {
            throw new BadRequestAlertException("A new epic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Epic result = epicService.save(epic);
        result = jiraService.updateEpicDetails(result.getId());
        return ResponseEntity.created(new URI("/api/epics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /epics : Updates an existing epic.
     *
     * @param epic the epic to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated epic,
     * or with status 400 (Bad Request) if the epic is not valid,
     * or with status 500 (Internal Server Error) if the epic couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/epics")
    @Timed
    public ResponseEntity<Epic> updateEpic(@RequestBody Epic epic) throws URISyntaxException {
        log.debug("REST request to update Epic : {}", epic);
        if (epic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Epic result = epicService.save(epic);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, epic.getId().toString()))
            .body(result);
    }

    /**
     * GET  /epics : get all the epics.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of epics in body
     */
    @GetMapping("/epics")
    @Timed
    public ResponseEntity<List<Epic>> getAllEpics(Pageable pageable) {
        log.debug("REST request to get a page of Epics");
        Page<Epic> page = epicService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/epics");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /epics/:id : get the "id" epic.
     *
     * @param id the id of the epic to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the epic, or with status 404 (Not Found)
     */
    @GetMapping("/epics/{id}")
    @Timed
    public ResponseEntity<Epic> getEpic(@PathVariable Long id) {
        log.debug("REST request to get Epic : {}", id);
        Optional<Epic> epic = epicService.findOne(id);
        return ResponseUtil.wrapOrNotFound(epic);
    }

    /**
     * DELETE  /epics/:id : delete the "id" epic.
     *
     * @param id the id of the epic to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/epics/{id}")
    @Timed
    public ResponseEntity<Void> deleteEpic(@PathVariable Long id) {
        log.debug("REST request to delete Epic : {}", id);
        epicService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/epics?query=:query : search for the epic corresponding
     * to the query.
     *
     * @param query the query of the epic search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/epics")
    @Timed
    public ResponseEntity<List<Epic>> searchEpics(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Epics for query {}", query);
        Page<Epic> page = epicService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/epics");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
