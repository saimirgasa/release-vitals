package com.saimir.gasa.releasevitals.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.saimir.gasa.releasevitals.domain.Version;
import com.saimir.gasa.releasevitals.service.VersionService;
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
 * REST controller for managing Version.
 */
@RestController
@RequestMapping("/api")
public class VersionResource {

    private final Logger log = LoggerFactory.getLogger(VersionResource.class);

    private static final String ENTITY_NAME = "version";

    private final VersionService versionService;

    public VersionResource(VersionService versionService) {
        this.versionService = versionService;
    }

    /**
     * POST  /versions : Create a new version.
     *
     * @param version the version to create
     * @return the ResponseEntity with status 201 (Created) and with body the new version, or with status 400 (Bad Request) if the version has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/versions")
    @Timed
    public ResponseEntity<Version> createVersion(@RequestBody Version version) throws URISyntaxException {
        log.debug("REST request to save Version : {}", version);
        if (version.getId() != null) {
            throw new BadRequestAlertException("A new version cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Version result = versionService.save(version);
        return ResponseEntity.created(new URI("/api/versions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /versions : Updates an existing version.
     *
     * @param version the version to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated version,
     * or with status 400 (Bad Request) if the version is not valid,
     * or with status 500 (Internal Server Error) if the version couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/versions")
    @Timed
    public ResponseEntity<Version> updateVersion(@RequestBody Version version) throws URISyntaxException {
        log.debug("REST request to update Version : {}", version);
        if (version.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Version result = versionService.save(version);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, version.getId().toString()))
            .body(result);
    }

    /**
     * GET  /versions : get all the versions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of versions in body
     */
    @GetMapping("/versions")
    @Timed
    public ResponseEntity<List<Version>> getAllVersions(Pageable pageable) {
        log.debug("REST request to get a page of Versions");
        Page<Version> page = versionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/versions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /versions/:id : get the "id" version.
     *
     * @param id the id of the version to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the version, or with status 404 (Not Found)
     */
    @GetMapping("/versions/{id}")
    @Timed
    public ResponseEntity<Version> getVersion(@PathVariable Long id) {
        log.debug("REST request to get Version : {}", id);
        Optional<Version> version = versionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(version);
    }

    /**
     * DELETE  /versions/:id : delete the "id" version.
     *
     * @param id the id of the version to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/versions/{id}")
    @Timed
    public ResponseEntity<Void> deleteVersion(@PathVariable Long id) {
        log.debug("REST request to delete Version : {}", id);
        versionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/versions?query=:query : search for the version corresponding
     * to the query.
     *
     * @param query the query of the version search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/versions")
    @Timed
    public ResponseEntity<List<Version>> searchVersions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Versions for query {}", query);
        Page<Version> page = versionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/versions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
