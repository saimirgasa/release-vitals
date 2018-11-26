package com.saimir.gasa.releasevitals.web.rest;

import com.saimir.gasa.releasevitals.ReleaseVitalsApp;

import com.saimir.gasa.releasevitals.domain.Release;
import com.saimir.gasa.releasevitals.repository.ReleaseRepository;
import com.saimir.gasa.releasevitals.repository.search.ReleaseSearchRepository;
import com.saimir.gasa.releasevitals.service.ReleaseService;
import com.saimir.gasa.releasevitals.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static com.saimir.gasa.releasevitals.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReleaseResource REST controller.
 *
 * @see ReleaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReleaseVitalsApp.class)
public class ReleaseResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ReleaseRepository releaseRepository;
    
    @Autowired
    private ReleaseService releaseService;

    /**
     * This repository is mocked in the com.saimir.gasa.releasevitals.repository.search test package.
     *
     * @see com.saimir.gasa.releasevitals.repository.search.ReleaseSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReleaseSearchRepository mockReleaseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReleaseMockMvc;

    private Release release;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReleaseResource releaseResource = new ReleaseResource(releaseService);
        this.restReleaseMockMvc = MockMvcBuilders.standaloneSetup(releaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Release createEntity(EntityManager em) {
        Release release = new Release()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return release;
    }

    @Before
    public void initTest() {
        release = createEntity(em);
    }

    @Test
    @Transactional
    public void createRelease() throws Exception {
        int databaseSizeBeforeCreate = releaseRepository.findAll().size();

        // Create the Release
        restReleaseMockMvc.perform(post("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(release)))
            .andExpect(status().isCreated());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeCreate + 1);
        Release testRelease = releaseList.get(releaseList.size() - 1);
        assertThat(testRelease.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRelease.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testRelease.getEndDate()).isEqualTo(DEFAULT_END_DATE);

        // Validate the Release in Elasticsearch
        verify(mockReleaseSearchRepository, times(1)).save(testRelease);
    }

    @Test
    @Transactional
    public void createReleaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = releaseRepository.findAll().size();

        // Create the Release with an existing ID
        release.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReleaseMockMvc.perform(post("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(release)))
            .andExpect(status().isBadRequest());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Release in Elasticsearch
        verify(mockReleaseSearchRepository, times(0)).save(release);
    }

    @Test
    @Transactional
    public void getAllReleases() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList
        restReleaseMockMvc.perform(get("/api/releases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(release.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getRelease() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get the release
        restReleaseMockMvc.perform(get("/api/releases/{id}", release.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(release.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRelease() throws Exception {
        // Get the release
        restReleaseMockMvc.perform(get("/api/releases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRelease() throws Exception {
        // Initialize the database
        releaseService.save(release);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockReleaseSearchRepository);

        int databaseSizeBeforeUpdate = releaseRepository.findAll().size();

        // Update the release
        Release updatedRelease = releaseRepository.findById(release.getId()).get();
        // Disconnect from session so that the updates on updatedRelease are not directly saved in db
        em.detach(updatedRelease);
        updatedRelease
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restReleaseMockMvc.perform(put("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRelease)))
            .andExpect(status().isOk());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeUpdate);
        Release testRelease = releaseList.get(releaseList.size() - 1);
        assertThat(testRelease.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRelease.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testRelease.getEndDate()).isEqualTo(UPDATED_END_DATE);

        // Validate the Release in Elasticsearch
        verify(mockReleaseSearchRepository, times(1)).save(testRelease);
    }

    @Test
    @Transactional
    public void updateNonExistingRelease() throws Exception {
        int databaseSizeBeforeUpdate = releaseRepository.findAll().size();

        // Create the Release

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleaseMockMvc.perform(put("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(release)))
            .andExpect(status().isBadRequest());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Release in Elasticsearch
        verify(mockReleaseSearchRepository, times(0)).save(release);
    }

    @Test
    @Transactional
    public void deleteRelease() throws Exception {
        // Initialize the database
        releaseService.save(release);

        int databaseSizeBeforeDelete = releaseRepository.findAll().size();

        // Get the release
        restReleaseMockMvc.perform(delete("/api/releases/{id}", release.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Release in Elasticsearch
        verify(mockReleaseSearchRepository, times(1)).deleteById(release.getId());
    }

    @Test
    @Transactional
    public void searchRelease() throws Exception {
        // Initialize the database
        releaseService.save(release);
        when(mockReleaseSearchRepository.search(queryStringQuery("id:" + release.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(release), PageRequest.of(0, 1), 1));
        // Search the release
        restReleaseMockMvc.perform(get("/api/_search/releases?query=id:" + release.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(release.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Release.class);
        Release release1 = new Release();
        release1.setId(1L);
        Release release2 = new Release();
        release2.setId(release1.getId());
        assertThat(release1).isEqualTo(release2);
        release2.setId(2L);
        assertThat(release1).isNotEqualTo(release2);
        release1.setId(null);
        assertThat(release1).isNotEqualTo(release2);
    }
}
