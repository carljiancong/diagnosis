package com.harmonycloud.entity;

/**
 * @author qidong
 * @date 2019/2/13
 */
public class ChronicDiagnosis {
    private Integer chronicDiagnosisId;
    private Integer diagnosisId;
    private Integer patientId;
    private Integer encounterId;
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
}
