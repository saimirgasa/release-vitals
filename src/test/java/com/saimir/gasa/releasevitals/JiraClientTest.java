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

            // set the release
            Release release = createRelease();

            // set the projects
            Set<Project> projects = new HashSet<>();

            projects.add(createProject("Magnolia UI", "MGNLUI", release, createVersions("6.0")));
            projects.add(createProject("Magnolia Enterprise Edition", "MGNLEE", release, createVersions("6.0")));
            projects.add(createProject("Magnolia Community Edition", "MGNLCE", release, createVersions("6.0")));
            projects.add(createProject("Content Tags", "CONTTAGS", release, createVersions("1.2")));
            projects.add(createProject("Magnolia", "MAGNOLIA", release, createVersions("6.0")));
            projects.add(createProject("Magnolia", "MAGNOLIA", release, createVersions("5.7.1")));
            projects.add(createProject("Magnolia Categorization Module", "MGNLCAT", release, createVersions("2.6.1")));
            projects.add(createProject("Magnolia Content Types Module", "MGNLCT", release, createVersions("1.0 Developer Preview")));
            projects.add(createProject("Magnolia Demo Projects", "MGNLDEMO", release, createVersions("1.4")));
            projects.add(createProject("Magnolia Google Sitemap Module", "MGNLGS", release, createVersions("2.5.1")));
            projects.add(createProject("Magnolia Google Sitemap Module", "MGNLGS", release, createVersions("2.6")));
            projects.add(createProject("Magnolia Icons", "MGNLICONS", release, createVersions("20")));
            projects.add(createProject("Magnolia License", "MGNLLIC", release, createVersions("1.6.2")));
            projects.add(createProject("Magnolia License", "MGNLLIC", release, createVersions("1.7")));
            projects.add(createProject("Periscope", "MGNLPER", release, createVersions("1.0")));
            projects.add(createProject("Magnolia Public User Registration", "MGNLPUR", release, createVersions("2.7.1")));
            projects.add(createProject("Magnolia Resources Module", "MGNLRES", release, createVersions("2.6.3")));
            projects.add(createProject("Magnolia Resources Module", "MGNLRES", release, createVersions("2.7")));
            projects.add(createProject("Magnolia RSS Aggregator Module", "MGNLRSSAGG", release, createVersions("2.6.1")));
            projects.add(createProject("Magnolia RSS Aggregator Module", "MGNLRSSAGG", release, createVersions("2.6.2")));
            projects.add(createProject("Marketing Tags Manager", "MGNLTAGS", release, createVersions("1.4.1")));
            projects.add(createProject("Magnolia Templating Samples", "MGNLTPLSMPL", release, createVersions("6.0")));
            projects.add(createProject("Magnolia CLI npm Module", "NPMCLI", release, createVersions("3.0.4")));
            projects.add(createProject("Magnolia CLI npm Module", "NPMCLI", release, createVersions("3.0.5")));
            projects.add(createProject("Magnolia Content Editor", "CONTEDIT", release, createVersions("1.3")));
            projects.add(createProject("Magnolia DAM Module", "MGNLDAM", release, createVersions("2.5")));
            projects.add(createProject("Magnolia DAM Module", "MGNLDAM", release, createVersions("2.3.1")));
            projects.add(createProject("Magnolia Pages Module", "PAGES", release, createVersions("6.0")));
            projects.add(createProject("Magnolia Personalization", "MGNLPN", release, createVersions("1.7")));
            projects.add(createProject("Magnolia Personalization", "MGNLPN", release, createVersions("1.6.1")));
            projects.add(createProject("Machine Learning", "MLEARN", release, createVersions("1.0")));
            projects.add(createProject("Image Recognition", "IMGREC", release, createVersions("1.0")));
            projects.add(createProject("Magnolia License Management", "LICMGMT", release, createVersions("1.0")));
            projects.add(createProject("Magnolia License Management", "LICMGMT", release, createVersions("2.0")));

            Epic epic = new Epic();
            epic.setName("Resurface theme");
            epic.setProjects(projects);

            returnedEpic = printEpicSummary(client, epic, 0);

            returnedEpic.setEpicBrowserURL(buildBrowserURI(returnedEpic.getKey()));
            System.out.println("Epic URL: " + returnedEpic.getEpicBrowserURL());
        }

        System.out.println("Total Issue Count is: " + returnedEpic.getTotalIssueCount());
        System.out.println("Total Story Points is: " + returnedEpic.getTotalStoryPoints());
        System.out.println("The remaining story points are: " + returnedEpic.getRemainingStoryPoints());
        System.out.println("Total story points completed are: " + returnedEpic.getStoryPointsCompleted());
        System.out.println("Total non-estimated issues is: " + returnedEpic.getUnestimatedIssues().size());
        for (Issue issue : returnedEpic.getUnestimatedIssues()) {
            System.out.println(issue.getBrowserURL());
        }

        // Done
        System.out.println("Example complete. Now exiting.");
        System.exit(0);
    }

    private static Epic printEpicSummary(JiraRestClient client, Epic epic, int startIndex) throws URISyntaxException, IOException, JSONException, ParseException {
        IssueRestClient issueRestClient = client.getIssueClient();

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
                                            jIssue.setBrowserURL(buildBrowserURI(issue.getString("key")));
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
            epic = printEpicSummary(client, epic, startIndex);
        }

        return epic;
    }

    private static String buildBrowserURI(String issueKey) throws URISyntaxException {
        return JIRA_URL + "/browse/" + issueKey;
    }

    private static Set<Version> createVersions(String name) {
        Set<Version> versions = new HashSet<>();
        Version version = new Version();
        version.setName(name);
        versions.add(version);

        return versions;
    }

    private static Release createRelease() throws ParseException {
        Release release = new Release();
        release.setName("Magnolia 6.0 Release");
        // 2018-11-15T11:51:43.000+0100
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2018-06-25T00:00:00.000+0100");
        Instant instant = date.toInstant();
        release.setStartDate(instant);
        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2018-11-30T23:59:59.999+0100");
        instant = date.toInstant();
        release.setEndDate(instant);

        return release;
    }

    private static Project createProject(String name, String key, Release release, Set<Version> versions) {
        Project project = new Project();
        project.setName(name);
        project.setKey(key);
        project.setRelease(release);
        project.setVersions(versions);

        return project;
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
