package com.harmonycloud.entity;

/**
 * @author qidong
 * @date 2019/2/13
 */
public class DiagnosisType {
    private Integer diagnosisId;
    private String diagnosisDescription;

    public DiagnosisType() {
    }

    public DiagnosisType(Integer diagnosisId, String diagnosisDescription) {
        this.diagnosisId = diagnosisId;
        this.diagnosisDescription = diagnosisDescription;
    }

    public Integer getId() {
        return diagnosisId;
    }

    public void setId(Integer diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getDescription() {
        return diagnosisDescription;
    }

    public void setDescription(String diagnosisDescription) {
        this.diagnosisDescription = diagnosisDescription;
    }
}
