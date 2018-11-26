package com.saimir.gasa.releasevitals.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of EpicSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class EpicSearchRepositoryMockConfiguration {

    @MockBean
    private EpicSearchRepository mockEpicSearchRepository;

}
