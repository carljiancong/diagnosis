package com.harmonycloud.dto;

import com.harmonycloud.entity.ChronicDiagnosis;

/**
 * @date 2019/3/7
 */
public class ChronicDiagnosisDto {
    private Integer chronicDiagnosisId;
    private Integer diagnosisId;
    private String diagnosisDescription;
    private Integer patientId;
    private Integer encounterId;
    private String status;

    public ChronicDiagnosisDto() {
    }

    public ChronicDiagnosisDto(Integer chronicDiagnosisId, Integer diagnosisId,
                               String diagnosisDescription, Integer patientId, Integer encounterId, String status) {
        this.chronicDiagnosisId = chronicDiagnosisId;
        this.diagnosisId = diagnosisId;
        this.diagnosisDescription = diagnosisDescription;
        this.patientId = patientId;
        this.encounterId = encounterId;
        this.status = status;
    }

    public void setChronicDiagnosis(ChronicDiagnosis chronicDiagnosis) {
        this.chronicDiagnosisId = chronicDiagnosis.getId();
        this.diagnosisId = chronicDiagnosis.getDiagnosisId();
        this.patientId = chronicDiagnosis.getPatientId();
        this.encounterId = chronicDiagnosis.getEncounterId();
        this.status = chronicDiagnosis.getStatus();
    }

    public Integer getChronicDiagnosisId() {
        return chronicDiagnosisId;
    }

    public void setChronicDiagnosisId(Integer chronicDiagnosisId) {
        this.chronicDiagnosisId = chronicDiagnosisId;
    }

    public Integer getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Integer diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getDiagnosisDescription() {
        return diagnosisDescription;
    }

    public void setDiagnosisDescription(String diagnosisDescription) {
        this.diagnosisDescription = diagnosisDescription;
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
