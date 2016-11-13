package com.pawsey.prorata.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

public class ManualSessionEntity {

    @Id
    @Column(name = "manual_session_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manual_session_manual_session_id_pk_seq")
    @SequenceGenerator(name = "employment_session_employment_session_id_pk_seq", sequenceName = "employment_session_employment_session_id_pk_seq")
    protected Integer manualSessionId;

    @JsonManagedReference("LocationEntity_EmploymentSessionEntity")
    @OneToOne(mappedBy = "location", targetEntity = EmploymentSessionEntity.class)
    protected EmploymentSessionEntity employmentSession;

    @JsonBackReference("EmploymentEntity_LocationEntity")
    @ManyToOne
    @JoinColumn(name = "employment_id", referencedColumnName = "employment_id")
    protected EmploymentEntity employment;

    public ManualSessionEntity() {
    }

    public void setManualSessionId(Integer manualSessionId) {
        this.manualSessionId = manualSessionId;
    }

    public Integer getManualSessionId() {
        return this.manualSessionId;
    }

    public void setEmploymentSession(EmploymentSessionEntity employmentSession) {
        this.employmentSession = employmentSession;
    }

    public EmploymentSessionEntity getEmploymentSession() {
        return this.employmentSession;
    }

    public void setEmployment(EmploymentEntity employment) {
        this.employment = employment;
    }

    public EmploymentEntity getEmployment() {
        return this.employment;
    }
}
