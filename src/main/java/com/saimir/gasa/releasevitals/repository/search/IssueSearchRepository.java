package com.saimir.gasa.releasevitals.repository.search;

import com.saimir.gasa.releasevitals.domain.Issue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Issue entity.
 */
public interface IssueSearchRepository extends ElasticsearchRepository<Issue, Long> {
}
