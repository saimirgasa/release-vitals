package com.saimir.gasa.releasevitals.web.rest;

import com.saimir.gasa.releasevitals.ReleaseVitalsApp;

import com.saimir.gasa.releasevitals.domain.Epic;
import com.saimir.gasa.releasevitals.repository.EpicRepository;
import com.saimir.gasa.releasevitals.repository.search.EpicSearchRepository;
import com.saimir.gasa.releasevitals.service.EpicService;
import com.saimir.gasa.releasevitals.service.IssueService;
import com.saimir.gasa.releasevitals.service.JiraService;
import com.saimir.gasa.releasevitals.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.ArrayList;
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
 * Test class for the EpicResource REST controller.
 *
 * @see EpicResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReleaseVitalsApp.class)
public class EpicResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_STORY_POINTS = 1D;
    private static final Double UPDATED_TOTAL_STORY_POINTS = 2D;

    private static final Double DEFAULT_STORY_POINTS_COMPLETED = 1D;
    private static final Double UPDATED_STORY_POINTS_COMPLETED = 2D;

    private static final Double DEFAULT_REMAINING_STORY_POINTS = 1D;
    private static final Double UPDATED_REMAINING_STORY_POINTS = 2D;

    private static final Integer DEFAULT_TOTAL_ISSUE_COUNT = 1;
    private static final Integer UPDATED_TOTAL_ISSUE_COUNT = 2;

    private static final Double DEFAULT_PERCENTAGE_COMPLETED = 1D;
    private static final Double UPDATED_PERCENTAGE_COMPLETED = 2D;

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_EPIC_BROWSER_URL = "AAAAAAAAAA";
    private static final String UPDATED_EPIC_BROWSER_URL = "BBBBBBBBBB";

    @Autowired
    private EpicRepository epicRepository;

    @Mock
    private EpicRepository epicRepositoryMock;


    @Mock
    private EpicService epicServiceMock;

    @Autowired
    private EpicService epicService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private JiraService jiraService;

    /**
     * This repository is mocked in the com.saimir.gasa.releasevitals.repository.search test package.
     *
     * @see com.saimir.gasa.releasevitals.repository.search.EpicSearchRepositoryMockConfiguration
     */
    @Autowired
    private EpicSearchRepository mockEpicSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEpicMockMvc;

    private Epic epic;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EpicResource epicResource = new EpicResource(epicService, jiraService, issueService);
        this.restEpicMockMvc = MockMvcBuilders.standaloneSetup(epicResource)
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
    public static Epic createEntity(EntityManager em) {
        Epic epic = new Epic()
            .name(DEFAULT_NAME)
            .totalStoryPoints(DEFAULT_TOTAL_STORY_POINTS)
            .storyPointsCompleted(DEFAULT_STORY_POINTS_COMPLETED)
            .remainingStoryPoints(DEFAULT_REMAINING_STORY_POINTS)
            .totalIssueCount(DEFAULT_TOTAL_ISSUE_COUNT)
            .percentageCompleted(DEFAULT_PERCENTAGE_COMPLETED)
            .key(DEFAULT_KEY)
            .epicBrowserURL(DEFAULT_EPIC_BROWSER_URL);
        return epic;
    }

    @Before
    public void initTest() {
        epic = createEntity(em);
    }

//    @Test
//    @Transactional
//    public void createEpic() throws Exception {
//        int databaseSizeBeforeCreate = epicRepository.findAll().size();
//
//        // Create the Epic
//        restEpicMockMvc.perform(post("/api/epics")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(epic)))
//            .andExpect(status().isCreated());
//
//        // Validate the Epic in the database
//        List<Epic> epicList = epicRepository.findAll();
//        assertThat(epicList).hasSize(databaseSizeBeforeCreate + 1);
//        Epic testEpic = epicList.get(epicList.size() - 1);
//        assertThat(testEpic.getName()).isEqualTo(DEFAULT_NAME);
//        assertThat(testEpic.getTotalStoryPoints()).isEqualTo(DEFAULT_TOTAL_STORY_POINTS);
//        assertThat(testEpic.getStoryPointsCompleted()).isEqualTo(DEFAULT_STORY_POINTS_COMPLETED);
//        assertThat(testEpic.getRemainingStoryPoints()).isEqualTo(DEFAULT_REMAINING_STORY_POINTS);
//        assertThat(testEpic.getTotalIssueCount()).isEqualTo(DEFAULT_TOTAL_ISSUE_COUNT);
//        assertThat(testEpic.getPercentageCompleted()).isEqualTo(DEFAULT_PERCENTAGE_COMPLETED);
//        assertThat(testEpic.getKey()).isEqualTo(DEFAULT_KEY);
//        assertThat(testEpic.getEpicBrowserURL()).isEqualTo(DEFAULT_EPIC_BROWSER_URL);
//
//        // Validate the Epic in Elasticsearch
//        verify(mockEpicSearchRepository, times(1)).save(testEpic);
//    }

    @Test
    @Transactional
    public void createEpicWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = epicRepository.findAll().size();

        // Create the Epic with an existing ID
        epic.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEpicMockMvc.perform(post("/api/epics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(epic)))
            .andExpect(status().isBadRequest());

        // Validate the Epic in the database
        List<Epic> epicList = epicRepository.findAll();
        assertThat(epicList).hasSize(databaseSizeBeforeCreate);

        // Validate the Epic in Elasticsearch
        verify(mockEpicSearchRepository, times(0)).save(epic);
    }

    @Test
    @Transactional
    public void getAllEpics() throws Exception {
        // Initialize the database
        epicRepository.saveAndFlush(epic);

        // Get all the epicList
        restEpicMockMvc.perform(get("/api/epics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(epic.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].totalStoryPoints").value(hasItem(DEFAULT_TOTAL_STORY_POINTS.doubleValue())))
            .andExpect(jsonPath("$.[*].storyPointsCompleted").value(hasItem(DEFAULT_STORY_POINTS_COMPLETED.doubleValue())))
            .andExpect(jsonPath("$.[*].remainingStoryPoints").value(hasItem(DEFAULT_REMAINING_STORY_POINTS.doubleValue())))
            .andExpect(jsonPath("$.[*].totalIssueCount").value(hasItem(DEFAULT_TOTAL_ISSUE_COUNT)))
            .andExpect(jsonPath("$.[*].percentageCompleted").value(hasItem(DEFAULT_PERCENTAGE_COMPLETED.doubleValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].epicBrowserURL").value(hasItem(DEFAULT_EPIC_BROWSER_URL.toString())));
    }

    public void getAllEpicsWithEagerRelationshipsIsEnabled() throws Exception {
        EpicResource epicResource = new EpicResource(epicServiceMock, jiraService, issueService);
        when(epicServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restEpicMockMvc = MockMvcBuilders.standaloneSetup(epicResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEpicMockMvc.perform(get("/api/epics?eagerload=true"))
        .andExpect(status().isOk());

        verify(epicServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllEpicsWithEagerRelationshipsIsNotEnabled() throws Exception {
        EpicResource epicResource = new EpicResource(epicServiceMock, jiraService, issueService);
            when(epicServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restEpicMockMvc = MockMvcBuilders.standaloneSetup(epicResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEpicMockMvc.perform(get("/api/epics?eagerload=true"))
        .andExpect(status().isOk());

            verify(epicServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getEpic() throws Exception {
        // Initialize the database
        epicRepository.saveAndFlush(epic);

        // Get the epic
        restEpicMockMvc.perform(get("/api/epics/{id}", epic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(epic.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.totalStoryPoints").value(DEFAULT_TOTAL_STORY_POINTS.doubleValue()))
            .andExpect(jsonPath("$.storyPointsCompleted").value(DEFAULT_STORY_POINTS_COMPLETED.doubleValue()))
            .andExpect(jsonPath("$.remainingStoryPoints").value(DEFAULT_REMAINING_STORY_POINTS.doubleValue()))
            .andExpect(jsonPath("$.totalIssueCount").value(DEFAULT_TOTAL_ISSUE_COUNT))
            .andExpect(jsonPath("$.percentageCompleted").value(DEFAULT_PERCENTAGE_COMPLETED.doubleValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.epicBrowserURL").value(DEFAULT_EPIC_BROWSER_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEpic() throws Exception {
        // Get the epic
        restEpicMockMvc.perform(get("/api/epics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEpic() throws Exception {
        // Initialize the database
        epicService.save(epic);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockEpicSearchRepository);

        int databaseSizeBeforeUpdate = epicRepository.findAll().size();

        // Update the epic
        Epic updatedEpic = epicRepository.findById(epic.getId()).get();
        // Disconnect from session so that the updates on updatedEpic are not directly saved in db
        em.detach(updatedEpic);
        updatedEpic
            .name(UPDATED_NAME)
            .totalStoryPoints(UPDATED_TOTAL_STORY_POINTS)
            .storyPointsCompleted(UPDATED_STORY_POINTS_COMPLETED)
            .remainingStoryPoints(UPDATED_REMAINING_STORY_POINTS)
            .totalIssueCount(UPDATED_TOTAL_ISSUE_COUNT)
            .percentageCompleted(UPDATED_PERCENTAGE_COMPLETED)
            .key(UPDATED_KEY)
            .epicBrowserURL(UPDATED_EPIC_BROWSER_URL);

        restEpicMockMvc.perform(put("/api/epics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEpic)))
            .andExpect(status().isOk());

        // Validate the Epic in the database
        List<Epic> epicList = epicRepository.findAll();
        assertThat(epicList).hasSize(databaseSizeBeforeUpdate);
        Epic testEpic = epicList.get(epicList.size() - 1);
        assertThat(testEpic.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEpic.getTotalStoryPoints()).isEqualTo(UPDATED_TOTAL_STORY_POINTS);
        assertThat(testEpic.getStoryPointsCompleted()).isEqualTo(UPDATED_STORY_POINTS_COMPLETED);
        assertThat(testEpic.getRemainingStoryPoints()).isEqualTo(UPDATED_REMAINING_STORY_POINTS);
        assertThat(testEpic.getTotalIssueCount()).isEqualTo(UPDATED_TOTAL_ISSUE_COUNT);
        assertThat(testEpic.getPercentageCompleted()).isEqualTo(UPDATED_PERCENTAGE_COMPLETED);
        assertThat(testEpic.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testEpic.getEpicBrowserURL()).isEqualTo(UPDATED_EPIC_BROWSER_URL);

        // Validate the Epic in Elasticsearch
        verify(mockEpicSearchRepository, times(1)).save(testEpic);
    }

    @Test
    @Transactional
    public void updateNonExistingEpic() throws Exception {
        int databaseSizeBeforeUpdate = epicRepository.findAll().size();

        // Create the Epic

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEpicMockMvc.perform(put("/api/epics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(epic)))
            .andExpect(status().isBadRequest());

        // Validate the Epic in the database
        List<Epic> epicList = epicRepository.findAll();
        assertThat(epicList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Epic in Elasticsearch
        verify(mockEpicSearchRepository, times(0)).save(epic);
    }

    @Test
    @Transactional
    public void deleteEpic() throws Exception {
        // Initialize the database
        epicService.save(epic);

        int databaseSizeBeforeDelete = epicRepository.findAll().size();

        // Get the epic
        restEpicMockMvc.perform(delete("/api/epics/{id}", epic.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Epic> epicList = epicRepository.findAll();
        assertThat(epicList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Epic in Elasticsearch
        verify(mockEpicSearchRepository, times(1)).deleteById(epic.getId());
    }

    @Test
    @Transactional
    public void searchEpic() throws Exception {
        // Initialize the database
        epicService.save(epic);
        when(mockEpicSearchRepository.search(queryStringQuery("id:" + epic.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(epic), PageRequest.of(0, 1), 1));
        // Search the epic
        restEpicMockMvc.perform(get("/api/_search/epics?query=id:" + epic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(epic.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].totalStoryPoints").value(hasItem(DEFAULT_TOTAL_STORY_POINTS.doubleValue())))
            .andExpect(jsonPath("$.[*].storyPointsCompleted").value(hasItem(DEFAULT_STORY_POINTS_COMPLETED.doubleValue())))
            .andExpect(jsonPath("$.[*].remainingStoryPoints").value(hasItem(DEFAULT_REMAINING_STORY_POINTS.doubleValue())))
            .andExpect(jsonPath("$.[*].totalIssueCount").value(hasItem(DEFAULT_TOTAL_ISSUE_COUNT)))
            .andExpect(jsonPath("$.[*].percentageCompleted").value(hasItem(DEFAULT_PERCENTAGE_COMPLETED.doubleValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].epicBrowserURL").value(hasItem(DEFAULT_EPIC_BROWSER_URL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Epic.class);
        Epic epic1 = new Epic();
        epic1.setId(1L);
        Epic epic2 = new Epic();
        epic2.setId(epic1.getId());
        assertThat(epic1).isEqualTo(epic2);
        epic2.setId(2L);
        assertThat(epic1).isNotEqualTo(epic2);
        epic1.setId(null);
        assertThat(epic1).isNotEqualTo(epic2);
    }
}
