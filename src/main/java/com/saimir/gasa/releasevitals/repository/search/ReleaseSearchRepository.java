package com.saimir.gasa.releasevitals.repository.search;

import com.saimir.gasa.releasevitals.domain.Release;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Release entity.
 */
public interface ReleaseSearchRepository extends ElasticsearchRepository<Release, Long> {
}
