package com.saimir.gasa.releasevitals.repository.search;

import com.saimir.gasa.releasevitals.domain.Sprint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Sprint entity.
 */
public interface SprintSearchRepository extends ElasticsearchRepository<Sprint, Long> {
}
