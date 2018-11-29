package com.saimir.gasa.releasevitals.service;

import com.saimir.gasa.releasevitals.domain.Epic;

public interface JiraService {

    /**
     * Update Epic with the Jira details
     * @param id the id of the Epic entity
     * @return the updated Epic entity
     */
    Epic updateEpicDetails(Long id, boolean update);
}
