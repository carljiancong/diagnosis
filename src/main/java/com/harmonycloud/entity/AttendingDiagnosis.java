package com.harmonycloud.entity;

import com.harmonycloud.result.Result;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author qidong
 * @date 2019/2/13
 */

@Document(collection="attending_diagnosis")
public class AttendingDiagnosis {

    @Id
    private Integer attendingDiagnosisId;
    private Integer diagnosisId;
    private Integer patientId;
    private Integer encounterId;


    public AttendingDiagnosis() {
    }

    public AttendingDiagnosis(Integer attendingDiagnosisId,Integer patientId, Integer diagnosisId, Integer encounterId) {
        this.attendingDiagnosisId = attendingDiagnosisId;
        this.patientId=patientId;
        this.diagnosisId = diagnosisId;
        this.encounterId = encounterId;
    }

    public Integer getId() {
        return attendingDiagnosisId;
    }

    public void setId(Integer attendingDiagnosisId) {
        this.attendingDiagnosisId = attendingDiagnosisId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
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
