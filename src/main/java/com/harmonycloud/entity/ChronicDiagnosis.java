package com.harmonycloud.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;


/**
 * @author qidong
 * @date 2019/2/13
 */
@Document(collection = "chronic_diagnosis")
@Entity
@Table(name = "chronic_diagnosis")
public class ChronicDiagnosis {
    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer chronicDiagnosisId;
    @Column(name = "diagnosis_id")
    private Integer diagnosisId;
    @Column(name = "patient_id")
    private Integer patientId;
    @Column(name = "encounter_id")
    private Integer encounterId;
    @Column(name = "status")
    private String status;

    public ChronicDiagnosis() {

    }

    public ChronicDiagnosis(Integer chronicDiagnosisId, Integer diagnosisId, Integer patientId, Integer encounterId, String status) {
        this.chronicDiagnosisId = chronicDiagnosisId;
        this.diagnosisId = diagnosisId;
        this.patientId = patientId;
        this.encounterId = encounterId;
        this.status = status;
    }

    public Integer getId() {
        return chronicDiagnosisId;
    }

    public void setId(Integer chronicDiagnosisId) {
        this.chronicDiagnosisId = chronicDiagnosisId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ChronicDiagnosis{" +
                ", diagnosisId=" + diagnosisId +
                ", patientId=" + patientId +
                ", encounterId=" + encounterId +
                '}';
    }
}
