package com.saimir.gasa.releasevitals;

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
import java.util.Set;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import com.google.common.io.CharStreams;
import com.saimir.gasa.releasevitals.domain.Epic;
import com.saimir.gasa.releasevitals.domain.Issue;
import com.saimir.gasa.releasevitals.domain.Project;
import com.saimir.gasa.releasevitals.domain.Release;
import com.saimir.gasa.releasevitals.domain.Version;

public class JiraClientTest {
    private static final String JIRA_URL = "https://jira.magnolia-cms.com/";
    private static final String JIRA_ADMIN_USERNAME = "sgasa";
    private static final String JIRA_ADMIN_PASSWORD = "!M@tG@s@1587";

    public static void main(String[] args) throws Exception
    {
        // Print usage instructions
        StringBuilder intro = new StringBuilder();
        intro.append("**********************************************************************************************\r\n");
        intro.append("* JIRA Java REST Client ('JRJC') example.                                                    *\r\n");
        intro.append("* NOTE: Start JIRA using the Atlassian Plugin SDK before running this example.               *\r\n");
        intro.append("* (for example, use 'atlas-run-standalone --product jira --version 6.0 --data-version 6.0'.) *\r\n");
        intro.append("**********************************************************************************************\r\n");
        System.out.println(intro.toString());

        // Construct the JRJC client
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        URI uri = new URI(JIRA_URL);
        Epic returnedEpic;
        try (JiraRestClient client = factory.createWithBasicHttpAuthentication(uri, JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD)) {

            // print transition times for given epic
//        printIssueTransitionTimes(client);

            // print Epic summary
            Set<Version> versions = new HashSet<>();
            Version version = new Version();
            version.setName("6.0");
            versions.add(version);

            Release release = new Release();
            release.setName("Magnolia 6.0 Release");
            // 2018-11-15T11:51:43.000+0100
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2018-06-25T00:00:00.000+0100");
            Instant instant = date.toInstant();
            release.setStartDate(instant);
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2018-11-30T23:59:59.999+0100");
            instant = date.toInstant();
            release.setEndDate(instant);

            Project project = new Project();
            project.setName("Magnolia UI");
            project.setKey("MGNLUI");
            project.setRelease(release);
            project.setVersions(versions);

            Epic epic = new Epic();
            epic.setName("Resurface theme");
            epic.setProject(project);

            returnedEpic = printEpicSummary(client, epic, 0);

            returnedEpic.setEpicBrowserURL(uri + "/browse/" + returnedEpic.getKey());
            System.out.println("Epic URL: " + returnedEpic.getEpicBrowserURL());
        }

        System.out.println("Total Issue Count is: " + returnedEpic.getTotalIssueCount());
        System.out.println("Total Story Points is: " + returnedEpic.getTotalStoryPoints());
        System.out.println("The remaining story points are: " + returnedEpic.getRemainingStoryPoints());
        System.out.println("Total story points completed are: " + returnedEpic.getStoryPointsCompleted());
        System.out.println("Total non-estimated issues is: " + returnedEpic.getUnestimatedIssues().size());
        for (Issue issue : returnedEpic.getUnestimatedIssues()) {
            System.out.println(issue.getKey());
        }

        // Done
        System.out.println("Example complete. Now exiting.");
        System.exit(0);
    }

    private static Epic printEpicSummary(JiraRestClient client, Epic epic, int startIndex) throws URISyntaxException, IOException, JSONException, ParseException {
        IssueRestClient issueRestClient = client.getIssueClient();

//        String encodedEpicLink = URLEncoder.encode("\"Epic Link\"=" + "\"" + epicName + "\" AND status not in (Closed,Resolved)", "UTF-8");

        String encodedEpicLink = URLEncoder.encode("\"Epic Link\"=" + "\"" + epic.getName() + "\"", "UTF-8");

        URI epicURI = new URI(JIRA_URL + "rest/api/2/search?startAt=" + startIndex + "&jql=" + encodedEpicLink + "&fields=customfield_10242,fixVersions,project,customfield_10246,status,resolutiondate,resolution");
        System.out.println(epicURI);

        Promise<InputStream> attachmentPromise = issueRestClient.getAttachment(epicURI);
        InputStream attachments = attachmentPromise.claim();

        String text = null;
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
            System.out.println("#########################################");
            for (int i = 0; i < issues.length(); i++) {
                JSONObject issue = issues.getJSONObject(i);
                JSONObject fields = issue.getJSONObject("fields");
                JSONArray fixVersions = fields.getJSONArray("fixVersions");
                // only the issues that are tagged with the release provided should be inspected
                for (int j = 0; j < fixVersions.length(); j++) {
                    JSONObject statusObj = fields.getJSONObject("status");
                    String status = statusObj.getString("name");
                    System.out.println("Issue " + issue.getString("key") + " has fixVersion of: " + fixVersions.getJSONObject(j).getString("name"));
                    for (Version version : epic.getProject().getVersions()) {
                        System.out.println("Issue: " + issue.getString("key") +  " has Project version of: " + version.getName());
                        // check if the issue has the fix version set to the same value as the project one
                        if (version.getName().equalsIgnoreCase(fixVersions.getJSONObject(j).getString("name"))) {
                            double estimate = 0;
                            if (fields.isNull("customfield_10242")) {
                                Issue jIssue = new Issue();
                                jIssue.setKey(issue.getString("key"));
                                epic.addUnestimatedIssue(jIssue);
                            } else {
                                estimate = Double.valueOf(fields.getString("customfield_10242"));
                            }

                            if (!fields.isNull("resolutiondate")) {
                                // 2018-11-15T11:51:43.000+0100
                                String resolutionDateString = fields.getString("resolutiondate");
                                // yyyy-MM-dd'T'HH:mm:ss.SSSZ
                                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(resolutionDateString);
                                Instant resolutionInstant = date.toInstant();

                                // check if the issue is resolved after the release start date and before the end date
                                if (epic.getProject().getRelease().getEndDate().isAfter(resolutionInstant) &&
                                    epic.getProject().getRelease().getStartDate().isBefore(resolutionInstant)) {

                                    // add to total story points
                                    epic.addToTotalStoryPoints(estimate);
                                    // add to total issue count
                                    epic.addToTotalIssueCount(1);

                                    if (!fields.isNull("resolution")) {
                                        JSONObject resolutionObj = fields.getJSONObject("resolution");
                                        String resolution = resolutionObj.getString("name");
                                        // check if the issue status and resolution is done
                                        if (("Closed".equalsIgnoreCase(status) || "Resolved".equalsIgnoreCase(status)) &&
                                            ("Done".equalsIgnoreCase(resolution) || "Fixed".equalsIgnoreCase(resolution))) {

                                            epic.addToStoryPointsCompleted(estimate);
                                            System.out.println("#########################################");
                                        } else {
                                            epic.addToRemainingStoryPoints(estimate);
                                        }
                                    }
                                }
                            }
                        } else {
                            // issues are not in the scope of the release
                            System.out.println(issue.getString("key") + " is not in the " + version.getName() + " release scope!");
                        }
                    }
                }
                epic.setKey(fields.getString("customfield_10246"));
            }
        }

        if (getNextPageOfResults) {
            epic = printEpicSummary(client, epic, startIndex);
        }

        return epic;
    }

    private static void printIssueTransitionTimes(JiraRestClient client) throws URISyntaxException, IOException, JSONException {
        String projectId = "MGNLCE";

        SearchRestClient searchRestClient = client.getSearchClient();
        IssueRestClient issueRestClient = client.getIssueClient();

        Promise<SearchResult> searchResultsPromise = searchRestClient.searchJql("project=" + projectId);
        SearchResult searchResult = searchResultsPromise.claim();

        for (com.atlassian.jira.rest.client.api.domain.Issue issue : searchResult.getIssues()) {
            URI transitionsURI = new URI(JIRA_URL + "rest/api/2/issue/" + issue.getKey() + "?expand=changelog&fields=changelog.histories.items,created");

            Promise<InputStream> attachmentPromise = issueRestClient.getAttachment(transitionsURI);
            InputStream attachments = attachmentPromise.claim();

            String text = null;
            try (final Reader reader = new InputStreamReader(attachments)) {
                text = CharStreams.toString(reader);
                JSONObject obj = new JSONObject(text);
                JSONObject fields = obj.getJSONObject("fields");
                String created = fields.getString("created");
                System.out.println("Issue with ID: " + issue.getKey() + " was created on: " + created);
                JSONObject changelog = obj.getJSONObject("changelog");
                JSONArray histories = changelog.getJSONArray("histories");
                // iterate over the histories array
                for (int i = 0; i < histories.length(); i++) {
                    JSONObject history = histories.getJSONObject(i);
                    JSONArray items = history.getJSONArray("items");
                    // iterate over the items array
                    for (int j = 0; j < items.length(); j++) {
                        JSONObject item = items.getJSONObject(j);
                        if ("status".equalsIgnoreCase(item.getString("field"))) {
                            System.out.println("Issue with ID: " + issue.getKey() + " was moved from: " + item.getString("fromString") + " to " + item.getString("toString") + " on " + history.getString("created"));
                        }
                    }
                }
            }
        }
    }
}
