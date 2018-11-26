package com.saimir.gasa.releasevitals.web.rest;

import com.saimir.gasa.releasevitals.ReleaseVitalsApp;

import com.saimir.gasa.releasevitals.domain.Sprint;
import com.saimir.gasa.releasevitals.repository.SprintRepository;
import com.saimir.gasa.releasevitals.repository.search.SprintSearchRepository;
import com.saimir.gasa.releasevitals.service.SprintService;
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
 * Test class for the SprintResource REST controller.
 *
 * @see SprintResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReleaseVitalsApp.class)
public class SprintResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_VELOCITY = 1L;
    private static final Long UPDATED_VELOCITY = 2L;

    @Autowired
    private SprintRepository sprintRepository;
    
    @Autowired
    private SprintService sprintService;

    /**
     * This repository is mocked in the com.saimir.gasa.releasevitals.repository.search test package.
     *
     * @see com.saimir.gasa.releasevitals.repository.search.SprintSearchRepositoryMockConfiguration
     */
    @Autowired
    private SprintSearchRepository mockSprintSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSprintMockMvc;

    private Sprint sprint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SprintResource sprintResource = new SprintResource(sprintService);
        this.restSprintMockMvc = MockMvcBuilders.standaloneSetup(sprintResource)
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
    public static Sprint createEntity(EntityManager em) {
        Sprint sprint = new Sprint()
            .name(DEFAULT_NAME)
            .startDatetime(DEFAULT_START_DATETIME)
            .endDatetime(DEFAULT_END_DATETIME)
            .velocity(DEFAULT_VELOCITY);
        return sprint;
    }

    @Before
    public void initTest() {
        sprint = createEntity(em);
    }

    @Test
    @Transactional
    public void createSprint() throws Exception {
        int databaseSizeBeforeCreate = sprintRepository.findAll().size();

        // Create the Sprint
        restSprintMockMvc.perform(post("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprint)))
            .andExpect(status().isCreated());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeCreate + 1);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSprint.getStartDatetime()).isEqualTo(DEFAULT_START_DATETIME);
        assertThat(testSprint.getEndDatetime()).isEqualTo(DEFAULT_END_DATETIME);
        assertThat(testSprint.getVelocity()).isEqualTo(DEFAULT_VELOCITY);

        // Validate the Sprint in Elasticsearch
        verify(mockSprintSearchRepository, times(1)).save(testSprint);
    }

    @Test
    @Transactional
    public void createSprintWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sprintRepository.findAll().size();

        // Create the Sprint with an existing ID
        sprint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSprintMockMvc.perform(post("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprint)))
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeCreate);

        // Validate the Sprint in Elasticsearch
        verify(mockSprintSearchRepository, times(0)).save(sprint);
    }

    @Test
    @Transactional
    public void getAllSprints() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList
        restSprintMockMvc.perform(get("/api/sprints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDatetime").value(hasItem(DEFAULT_START_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].endDatetime").value(hasItem(DEFAULT_END_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].velocity").value(hasItem(DEFAULT_VELOCITY.intValue())));
    }
    
    @Test
    @Transactional
    public void getSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get the sprint
        restSprintMockMvc.perform(get("/api/sprints/{id}", sprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sprint.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startDatetime").value(DEFAULT_START_DATETIME.toString()))
            .andExpect(jsonPath("$.endDatetime").value(DEFAULT_END_DATETIME.toString()))
            .andExpect(jsonPath("$.velocity").value(DEFAULT_VELOCITY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSprint() throws Exception {
        // Get the sprint
        restSprintMockMvc.perform(get("/api/sprints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSprint() throws Exception {
        // Initialize the database
        sprintService.save(sprint);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSprintSearchRepository);

        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Update the sprint
        Sprint updatedSprint = sprintRepository.findById(sprint.getId()).get();
        // Disconnect from session so that the updates on updatedSprint are not directly saved in db
        em.detach(updatedSprint);
        updatedSprint
            .name(UPDATED_NAME)
            .startDatetime(UPDATED_START_DATETIME)
            .endDatetime(UPDATED_END_DATETIME)
            .velocity(UPDATED_VELOCITY);

        restSprintMockMvc.perform(put("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSprint)))
            .andExpect(status().isOk());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSprint.getStartDatetime()).isEqualTo(UPDATED_START_DATETIME);
        assertThat(testSprint.getEndDatetime()).isEqualTo(UPDATED_END_DATETIME);
        assertThat(testSprint.getVelocity()).isEqualTo(UPDATED_VELOCITY);

        // Validate the Sprint in Elasticsearch
        verify(mockSprintSearchRepository, times(1)).save(testSprint);
    }

    @Test
    @Transactional
    public void updateNonExistingSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Create the Sprint

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSprintMockMvc.perform(put("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprint)))
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Sprint in Elasticsearch
        verify(mockSprintSearchRepository, times(0)).save(sprint);
    }

    @Test
    @Transactional
    public void deleteSprint() throws Exception {
        // Initialize the database
        sprintService.save(sprint);

        int databaseSizeBeforeDelete = sprintRepository.findAll().size();

        // Get the sprint
        restSprintMockMvc.perform(delete("/api/sprints/{id}", sprint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Sprint in Elasticsearch
        verify(mockSprintSearchRepository, times(1)).deleteById(sprint.getId());
    }

    @Test
    @Transactional
    public void searchSprint() throws Exception {
        // Initialize the database
        sprintService.save(sprint);
        when(mockSprintSearchRepository.search(queryStringQuery("id:" + sprint.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(sprint), PageRequest.of(0, 1), 1));
        // Search the sprint
        restSprintMockMvc.perform(get("/api/_search/sprints?query=id:" + sprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDatetime").value(hasItem(DEFAULT_START_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].endDatetime").value(hasItem(DEFAULT_END_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].velocity").value(hasItem(DEFAULT_VELOCITY.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sprint.class);
        Sprint sprint1 = new Sprint();
        sprint1.setId(1L);
        Sprint sprint2 = new Sprint();
        sprint2.setId(sprint1.getId());
        assertThat(sprint1).isEqualTo(sprint2);
        sprint2.setId(2L);
        assertThat(sprint1).isNotEqualTo(sprint2);
        sprint1.setId(null);
        assertThat(sprint1).isNotEqualTo(sprint2);
    }
}
