package com.saimir.gasa.releasevitals.repository.search;

import com.saimir.gasa.releasevitals.domain.Version;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Version entity.
 */
public interface VersionSearchRepository extends ElasticsearchRepository<Version, Long> {
}
