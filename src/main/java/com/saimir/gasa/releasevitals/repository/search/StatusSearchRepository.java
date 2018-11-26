package com.saimir.gasa.releasevitals.repository.search;

import com.saimir.gasa.releasevitals.domain.Status;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Status entity.
 */
public interface StatusSearchRepository extends ElasticsearchRepository<Status, Long> {
}
