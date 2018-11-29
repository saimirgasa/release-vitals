package com.saimir.gasa.releasevitals.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Issue.
 */
@Entity
@Table(name = "issue")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "issue")
public class Issue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "jhi_key")
    private String key;

    @Column(name = "browser_url")
    private String browserURL;

    @ManyToOne
    @JsonIgnoreProperties("issues")
    private Sprint sprint;

    @ManyToOne
    @JsonIgnoreProperties("issues")
    private Project project;

    @ManyToOne
    @JsonIgnoreProperties("unestimatedIssues")
    private Epic epic;

    @OneToMany(mappedBy = "issue")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Status> statuses = new HashSet<>();
    @OneToMany(mappedBy = "issue")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Version> fixVersions = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Issue title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Issue description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public Issue key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBrowserURL() {
        return browserURL;
    }

    public Issue browserURL(String browserURL) {
        this.browserURL = browserURL;
        return this;
    }

    public void setBrowserURL(String browserURL) {
        this.browserURL = browserURL;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public Issue sprint(Sprint sprint) {
        this.sprint = sprint;
        return this;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public Project getProject() {
        return project;
    }

    public Issue project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Epic getEpic() {
        return epic;
    }

    public Issue epic(Epic epic) {
        this.epic = epic;
        return this;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public Set<Status> getStatuses() {
        return statuses;
    }

    public Issue statuses(Set<Status> statuses) {
        this.statuses = statuses;
        return this;
    }

    public Issue addStatus(Status status) {
        this.statuses.add(status);
        status.setIssue(this);
        return this;
    }

    public Issue removeStatus(Status status) {
        this.statuses.remove(status);
        status.setIssue(null);
        return this;
    }

    public void setStatuses(Set<Status> statuses) {
        this.statuses = statuses;
    }

    public Set<Version> getFixVersions() {
        return fixVersions;
    }

    public Issue fixVersions(Set<Version> versions) {
        this.fixVersions = versions;
        return this;
    }

    public Issue addFixVersion(Version version) {
        this.fixVersions.add(version);
        version.setIssue(this);
        return this;
    }

    public Issue removeFixVersion(Version version) {
        this.fixVersions.remove(version);
        version.setIssue(null);
        return this;
    }

    public void setFixVersions(Set<Version> versions) {
        this.fixVersions = versions;
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
        Issue issue = (Issue) o;
        if (issue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Issue{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", key='" + getKey() + "'" +
            ", browserURL='" + getBrowserURL() + "'" +
            "}";
    }
}
