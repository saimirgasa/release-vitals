package com.saimir.gasa.releasevitals.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Sprint.
 */
@Entity
@Table(name = "sprint")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sprint")
public class Sprint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_datetime")
    private Instant startDatetime;

    @Column(name = "end_datetime")
    private Instant endDatetime;

    @Column(name = "velocity")
    private Long velocity;

    @OneToMany(mappedBy = "sprint")
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

    public Sprint name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStartDatetime() {
        return startDatetime;
    }

    public Sprint startDatetime(Instant startDatetime) {
        this.startDatetime = startDatetime;
        return this;
    }

    public void setStartDatetime(Instant startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Instant getEndDatetime() {
        return endDatetime;
    }

    public Sprint endDatetime(Instant endDatetime) {
        this.endDatetime = endDatetime;
        return this;
    }

    public void setEndDatetime(Instant endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Long getVelocity() {
        return velocity;
    }

    public Sprint velocity(Long velocity) {
        this.velocity = velocity;
        return this;
    }

    public void setVelocity(Long velocity) {
        this.velocity = velocity;
    }

    public Set<Issue> getIssues() {
        return issues;
    }

    public Sprint issues(Set<Issue> issues) {
        this.issues = issues;
        return this;
    }

    public Sprint addIssue(Issue issue) {
        this.issues.add(issue);
        issue.setSprint(this);
        return this;
    }

    public Sprint removeIssue(Issue issue) {
        this.issues.remove(issue);
        issue.setSprint(null);
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
        Sprint sprint = (Sprint) o;
        if (sprint.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sprint.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sprint{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDatetime='" + getStartDatetime() + "'" +
            ", endDatetime='" + getEndDatetime() + "'" +
            ", velocity=" + getVelocity() +
            "}";
    }
}
