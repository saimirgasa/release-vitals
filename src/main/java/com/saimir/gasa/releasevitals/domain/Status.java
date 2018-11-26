package com.saimir.gasa.releasevitals.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Status.
 */
@Entity
@Table(name = "status")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "status")
public class Status implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "from_status")
    private String fromStatus;

    @Column(name = "to_status")
    private String toStatus;

    @Column(name = "time_chaged")
    private Instant timeChaged;

    @ManyToOne
    @JsonIgnoreProperties("statuses")
    private Issue issue;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromStatus() {
        return fromStatus;
    }

    public Status fromStatus(String fromStatus) {
        this.fromStatus = fromStatus;
        return this;
    }

    public void setFromStatus(String fromStatus) {
        this.fromStatus = fromStatus;
    }

    public String getToStatus() {
        return toStatus;
    }

    public Status toStatus(String toStatus) {
        this.toStatus = toStatus;
        return this;
    }

    public void setToStatus(String toStatus) {
        this.toStatus = toStatus;
    }

    public Instant getTimeChaged() {
        return timeChaged;
    }

    public Status timeChaged(Instant timeChaged) {
        this.timeChaged = timeChaged;
        return this;
    }

    public void setTimeChaged(Instant timeChaged) {
        this.timeChaged = timeChaged;
    }

    public Issue getIssue() {
        return issue;
    }

    public Status issue(Issue issue) {
        this.issue = issue;
        return this;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
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
        Status status = (Status) o;
        if (status.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), status.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Status{" +
            "id=" + getId() +
            ", fromStatus='" + getFromStatus() + "'" +
            ", toStatus='" + getToStatus() + "'" +
            ", timeChaged='" + getTimeChaged() + "'" +
            "}";
    }
}
