package com.harmonycloud.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

/**
 * @author qidong
 * @date 2019/2/13
 */

@Document(collection = "attending_diagnosis")
@Entity
@Table(name = "attending_diagosis")
public class AttendingDiagnosis {

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer attendingDiagnosisId;
    @Column(name = "diagnosis_id")
    private Integer diagnosisId;
    @Column(name = "patient_id")
    private Integer patientId;
    @Column(name = "encounter_id")
    private Integer encounterId;

    public AttendingDiagnosis() {
    }


    public AttendingDiagnosis(Integer diagnosisId, Integer patientId, Integer encounterId) {
        this.diagnosisId = diagnosisId;
        this.patientId = patientId;
        this.encounterId = encounterId;
    }

    public Integer getAttendingDiagnosisId() {
        return attendingDiagnosisId;
    }

    public void setAttendingDiagnosisId(Integer attendingDiagnosisId) {
        this.attendingDiagnosisId = attendingDiagnosisId;
    }

    public Integer getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Integer diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Integer encounterId) {
        this.encounterId = encounterId;
    }

    @Override
    public String toString() {
        return "AttendingDiagnosis{" +
                ", diagnosisId=" + diagnosisId +
                ", patientId=" + patientId +
                ", encounterId=" + encounterId +
                '}';
    }
}
