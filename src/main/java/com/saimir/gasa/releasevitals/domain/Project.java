package com.saimir.gasa.releasevitals.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * entity Theme {
 * name String,
 * totalIssueCount Integer
 * }
 * 
 * entity Feature {
 * name String,
 * key String
 * }
 */
@ApiModel(description = "entity Theme { name String, totalIssueCount Integer } entity Feature { name String, key String }")
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_key")
    private String key;

    @ManyToOne
    @JsonIgnoreProperties("projects")
    private Release release;

    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Version> versions = new HashSet<>();
    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Epic> epics = new HashSet<>();
    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Issue> issues = new HashSet<>();
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

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public Project key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Release getRelease() {
        return release;
    }

    public Project release(Release release) {
        this.release = release;
        return this;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public Set<Version> getVersions() {
        return versions;
    }

    public Project versions(Set<Version> versions) {
        this.versions = versions;
        return this;
    }

    public Project addVersion(Version version) {
        this.versions.add(version);
        version.setProject(this);
        return this;
    }

    public Project removeVersion(Version version) {
        this.versions.remove(version);
        version.setProject(null);
        return this;
    }

    public void setVersions(Set<Version> versions) {
        this.versions = versions;
    }

    public Set<Epic> getEpics() {
        return epics;
    }

    public Project epics(Set<Epic> epics) {
        this.epics = epics;
        return this;
    }

    public Project addEpic(Epic epic) {
        this.epics.add(epic);
        epic.setProject(this);
        return this;
    }

    public Project removeEpic(Epic epic) {
        this.epics.remove(epic);
        epic.setProject(null);
        return this;
    }

    public void setEpics(Set<Epic> epics) {
        this.epics = epics;
    }

    public Set<Issue> getIssues() {
        return issues;
    }

    public Project issues(Set<Issue> issues) {
        this.issues = issues;
        return this;
    }

    public Project addIssue(Issue issue) {
        this.issues.add(issue);
        issue.setProject(this);
        return this;
    }

    public Project removeIssue(Issue issue) {
        this.issues.remove(issue);
        issue.setProject(null);
        return this;
    }

    public void setIssues(Set<Issue> issues) {
        this.issues = issues;
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
        Project project = (Project) o;
        if (project.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", key='" + getKey() + "'" +
            "}";
    }
}
