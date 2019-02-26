package com.harmonycloud.entity;

/**
 * @author qidong
 * @date 2019/2/13
 */
public class AttendingDiagnosis {
    private Integer attendingDiagnosisId;
    private Integer diagnosisId;
    private Integer encounterId;


    public AttendingDiagnosis() {
    }

    public AttendingDiagnosis(Integer attendingDiagnosisId, Integer diagnosisId, Integer encounterId) {
        this.attendingDiagnosisId = attendingDiagnosisId;
        this.diagnosisId = diagnosisId;
        this.encounterId = encounterId;
    }

    public Integer getId() {
        return attendingDiagnosisId;
    }

    public void setId(Integer attendingDiagnosisId) {
        this.attendingDiagnosisId = attendingDiagnosisId;
    }

    public Integer getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Integer diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public Integer getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Integer encounterId) {
        this.encounterId = encounterId;
    }
}
