package com.saimir.gasa.releasevitals.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import com.google.common.io.CharStreams;
import com.saimir.gasa.releasevitals.domain.Epic;
import com.saimir.gasa.releasevitals.domain.Issue;
import com.saimir.gasa.releasevitals.domain.Project;
import com.saimir.gasa.releasevitals.domain.Version;
import com.saimir.gasa.releasevitals.repository.EpicRepository;
import com.saimir.gasa.releasevitals.repository.IssueRepository;
import com.saimir.gasa.releasevitals.repository.search.EpicSearchRepository;
import com.saimir.gasa.releasevitals.service.JiraService;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JiraServiceImpl implements JiraService {

    private final Logger log = LoggerFactory.getLogger(JiraServiceImpl.class);

    private static final String JIRA_URL = "https://jira.magnolia-cms.com/";
    private static final String JIRA_ADMIN_USERNAME = "sgasa";
    private static final String JIRA_ADMIN_PASSWORD = "!M@tG@s@1587";

    private final EpicRepository epicRepository;

    private final IssueRepository issueRepository;

    private final EpicSearchRepository epicSearchRepository;

    private final JiraRestClient jiraRestClient;

    public JiraServiceImpl(EpicRepository epicRepository, IssueRepository issueRepository, EpicSearchRepository epicSearchRepository) {
        this.epicRepository = epicRepository;
        this.issueRepository = issueRepository;
        this.epicSearchRepository = epicSearchRepository;
        this.jiraRestClient = new AsynchronousJiraRestClientFactory()
            .createWithBasicHttpAuthentication(URI.create(JIRA_URL), JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD);
    }

    /**
     * Update Jira details for the given epic
     */
    @Override
    public Epic updateEpicDetails(Long id, boolean update) {
        log.debug("Request to update Jira details for Epic : {}", id);
        Optional<Epic> optionalEpic = epicRepository.findById(id);
        Epic epic = null;
        if (optionalEpic.isPresent()) {
            epic = optionalEpic.get();
        }

        if (update) {
            epic.totalIssueCount(0);
            epic.totalStoryPoints(0d);
            epic.unestimatedIssues(new HashSet<>());
            epic.remainingStoryPoints(0d);
            epic.storyPointsCompleted(0d);
        }

        try {
            epic = epicSummary(epic, 0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("Total Issue Count is: " + epic.getTotalIssueCount());
        System.out.println("Total Story Points is: " + epic.getTotalStoryPoints());
        System.out.println("The remaining story points are: " + epic.getRemainingStoryPoints());
        System.out.println("Total story points completed are: " + epic.getStoryPointsCompleted());
        System.out.println("Total non-estimated issues is: " + epic.getUnestimatedIssues().size());
        for (Issue issue : epic.getUnestimatedIssues()) {
            System.out.println(issue.getKey());
        }
        epic.setEpicBrowserURL(JIRA_URL + "/browse/" + epic.getKey());
        System.out.println("Epic URL: " + epic.getEpicBrowserURL());
        epic.setPercentageCompleted(epic.getStoryPointsCompleted() / epic.getTotalStoryPoints());
        return epic;
    }

    private Epic epicSummary(Epic epic, int startIndex) throws URISyntaxException, IOException, JSONException, ParseException {
        IssueRestClient issueRestClient = this.jiraRestClient.getIssueClient();

        String encodedEpicLink = URLEncoder.encode("\"Epic Link\"=" + "\"" + epic.getName() + "\"", "UTF-8");

        URI epicURI = new URI(JIRA_URL + "rest/api/2/search?startAt=" + startIndex + "&jql=" + encodedEpicLink + "&fields=customfield_10242,fixVersions,project,customfield_10246,status,resolutiondate,resolution");
        System.out.println(epicURI);

        Promise<InputStream> attachmentPromise = issueRestClient.getAttachment(epicURI);
        InputStream attachments = attachmentPromise.claim();

        String text;
        boolean getNextPageOfResults = false;
        try (final Reader reader = new InputStreamReader(attachments)) {
            text = CharStreams.toString(reader);
            JSONObject object = new JSONObject(text);
            int total = object.getInt("total") - startIndex;
            if (total > 0) {
                getNextPageOfResults = true;
                startIndex += 50;
            }
            JSONArray issues = object.getJSONArray("issues");
            // iterate over the issues array
            for (int i = 0; i < issues.length(); i++) {
                JSONObject issue = issues.getJSONObject(i);
                JSONObject fields = issue.getJSONObject("fields");
                if (!fields.isNull("fixVersions")) {
                    JSONArray fixVersions = fields.getJSONArray("fixVersions");
                    // only the issues that are tagged with the release provided should be inspected
                    for (int j = 0; j < fixVersions.length(); j++) {
                        JSONObject statusObj = fields.getJSONObject("status");
                        String status = statusObj.getString("name");
                        for (Project project : epic.getProjects()) {
                            for (Version version : project.getVersions()) {
                                // check if the issue has the fix version set to the same value as the project one
                                if (version.getName().equalsIgnoreCase(fixVersions.getJSONObject(j).getString("name"))) {
                                    JSONObject projectObj = fields.getJSONObject("project");
                                    String projectKey = projectObj.getString("key");
                                    if (project.getKey().equalsIgnoreCase(projectKey)) {
                                        double estimate = 0;
                                        if (fields.isNull("customfield_10242")) {
                                            Issue jIssue = new Issue();
                                            jIssue.setKey(issue.getString("key"));
                                            jIssue.setProject(project);
                                            jIssue.setEpic(epic);
                                            epic.addUnestimatedIssue(jIssue);
                                        } else {
                                            estimate = Double.valueOf(fields.getString("customfield_10242"));
                                        }

                                        System.out.println(issue.getString("key"));
                                        // add to total story points
                                        epic.addToTotalStoryPoints(estimate);
                                        // add to total issue count
                                        epic.addToTotalIssueCount(1);

                                        if (!fields.isNull("resolution")) {
                                            JSONObject resolutionObj = fields.getJSONObject("resolution");
                                            String resolution = resolutionObj.getString("name");

                                            if (!"Duplicate".equalsIgnoreCase(resolution) && !"Obsolete".equalsIgnoreCase(resolution) &&
                                                !"Not an issue".equalsIgnoreCase(resolution) && !"Won't Do".equalsIgnoreCase(resolution)) {

                                                // 2018-11-15T11:51:43.000+0100
                                                String resolutionDateString = fields.getString("resolutiondate");
                                                // yyyy-MM-dd'T'HH:mm:ss.SSSZ
                                                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(resolutionDateString);
                                                Instant resolutionInstant = date.toInstant();

                                                // check if the issue is resolved after the release start date and before the end date
                                                if (project.getRelease().getEndDate().isAfter(resolutionInstant) &&
                                                    project.getRelease().getStartDate().isBefore(resolutionInstant)) {

                                                    if (("Closed".equalsIgnoreCase(status) || "Resolved".equalsIgnoreCase(status)) &&
                                                        ("Done".equalsIgnoreCase(resolution) || "Fixed".equalsIgnoreCase(resolution))) {

                                                        epic.addToStoryPointsCompleted(estimate);
                                                    } else {
                                                        epic.addToRemainingStoryPoints(estimate);
                                                    }
                                                }
                                            }
                                        } else {
                                            epic.addToRemainingStoryPoints(estimate);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                epic.setKey(fields.getString("customfield_10246"));
            }
        }

        if (getNextPageOfResults) {
            epic = epicSummary(epic, startIndex);
        }

        return epic;
    }

    private static URI buildBrowserURI(String issueKey) throws URISyntaxException {
        return new URI(JIRA_URL + "/browse/" + issueKey);
    }
}
