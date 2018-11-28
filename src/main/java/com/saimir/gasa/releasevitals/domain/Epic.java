package com.saimir.gasa.releasevitals.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Epic.
 */
@Entity
@Table(name = "epic")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "epic")
public class Epic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "total_story_points")
    private Double totalStoryPoints = 0d;

    @Column(name = "story_points_completed")
    private Double storyPointsCompleted = 0d;

    @Column(name = "remaining_story_points")
    private Double remainingStoryPoints = 0d;

    @Column(name = "total_issue_count")
    private Integer totalIssueCount = 0;

    @Column(name = "percentage_completed")
    private Double percentageCompleted;

    @Column(name = "jhi_key")
    private String key;

    @Column(name = "epic_browser_url")
    private String epicBrowserURL;

    @OneToMany(mappedBy = "epic")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Issue> unestimatedIssues = new HashSet<>();
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "epic_project",
               joinColumns = @JoinColumn(name = "epics_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "projects_id", referencedColumnName = "id"))
    private Set<Project> projects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Epic name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalStoryPoints() {
        return totalStoryPoints;
    }

    public Epic totalStoryPoints(Double totalStoryPoints) {
        this.totalStoryPoints = totalStoryPoints;
        return this;
    }

    public void setTotalStoryPoints(Double totalStoryPoints) {
        this.totalStoryPoints = totalStoryPoints;
    }

    public Double getStoryPointsCompleted() {
        return storyPointsCompleted;
    }

    public Epic storyPointsCompleted(Double storyPointsCompleted) {
        this.storyPointsCompleted = storyPointsCompleted;
        return this;
    }

    public void setStoryPointsCompleted(Double storyPointsCompleted) {
        this.storyPointsCompleted = storyPointsCompleted;
    }

    public Double getRemainingStoryPoints() {
        return remainingStoryPoints;
    }

    public Epic remainingStoryPoints(Double remainingStoryPoints) {
        this.remainingStoryPoints = remainingStoryPoints;
        return this;
    }

    public void setRemainingStoryPoints(Double remainingStoryPoints) {
        this.remainingStoryPoints = remainingStoryPoints;
    }

    public Integer getTotalIssueCount() {
        return totalIssueCount;
    }

    public Epic totalIssueCount(Integer totalIssueCount) {
        this.totalIssueCount = totalIssueCount;
        return this;
    }

    public void setTotalIssueCount(Integer totalIssueCount) {
        this.totalIssueCount = totalIssueCount;
    }

    public Double getPercentageCompleted() {
        return percentageCompleted;
    }

    public Epic percentageCompleted(Double percentageCompleted) {
        this.percentageCompleted = percentageCompleted;
        return this;
    }

    public void setPercentageCompleted(Double percentageCompleted) {
        this.percentageCompleted = percentageCompleted;
    }

    public String getKey() {
        return key;
    }

    public Epic key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEpicBrowserURL() {
        return epicBrowserURL;
    }

    public Epic epicBrowserURL(String epicBrowserURL) {
        this.epicBrowserURL = epicBrowserURL;
        return this;
    }

    public void setEpicBrowserURL(String epicBrowserURL) {
        this.epicBrowserURL = epicBrowserURL;
    }

    public Set<Issue> getUnestimatedIssues() {
        return unestimatedIssues;
    }

    public Epic unestimatedIssues(Set<Issue> issues) {
        this.unestimatedIssues = issues;
        return this;
    }

    public Epic addUnestimatedIssue(Issue issue) {
        this.unestimatedIssues.add(issue);
        issue.setEpic(this);
        return this;
    }

    public Epic removeUnestimatedIssue(Issue issue) {
        this.unestimatedIssues.remove(issue);
        issue.setEpic(null);
        return this;
    }

    public void setUnestimatedIssues(Set<Issue> issues) {
        this.unestimatedIssues = issues;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public Epic projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public Epic addProject(Project project) {
        this.projects.add(project);
        return this;
    }

    public Epic removeProject(Project project) {
        this.projects.remove(project);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Epic epic = (Epic) o;
        if (epic.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), epic.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Epic{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", totalStoryPoints=" + getTotalStoryPoints() +
            ", storyPointsCompleted=" + getStoryPointsCompleted() +
            ", remainingStoryPoints=" + getRemainingStoryPoints() +
            ", totalIssueCount=" + getTotalIssueCount() +
            ", percentageCompleted=" + getPercentageCompleted() +
            ", key='" + getKey() + "'" +
            ", epicBrowserURL='" + getEpicBrowserURL() + "'" +
            "}";
    }

    public void addToRemainingStoryPoints(double estimate) {
        this.remainingStoryPoints += estimate;
    }

    public void addToTotalStoryPoints(double estimate) {
        this.totalStoryPoints += estimate;
    }

    public void addToTotalIssueCount(int increment) {
        this.totalIssueCount += increment;
    }

    public void addToStoryPointsCompleted(double estimate) {
        this.storyPointsCompleted += estimate;
    }
}
