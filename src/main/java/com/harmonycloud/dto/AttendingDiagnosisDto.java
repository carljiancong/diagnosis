package com.harmonycloud.dto;

import com.harmonycloud.entity.AttendingDiagnosis;

/**
 * @author qidong
 * @date 2019/3/7
 */
public class AttendingDiagnosisDto {
    private Integer attendingDiagnosisId;
    private Integer diagnosisId;
    private String diagnosisDescription;
    private Integer patientId;
    private Integer encounterId;

    public AttendingDiagnosisDto(Integer attendingDiagnosisId, Integer diagnosisId, String diagnosisDescription,
                                 Integer patientId, Integer encounterId) {
        this.attendingDiagnosisId = attendingDiagnosisId;
        this.diagnosisId = diagnosisId;
        this.diagnosisDescription = diagnosisDescription;
        this.patientId = patientId;
        this.encounterId = encounterId;
    }

    public AttendingDiagnosisDto() {
    }

    public void setAttendingDiagnosis(AttendingDiagnosis attendingDiagnosis){
        this.attendingDiagnosisId = attendingDiagnosis.getAttendingDiagnosisId();
        this.diagnosisId = attendingDiagnosis.getDiagnosisId();
        this.patientId = attendingDiagnosis.getPatientId();
        this.encounterId = attendingDiagnosis.getEncounterId();
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
}
