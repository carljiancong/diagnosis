package com.harmonycloud.entity;

import com.harmonycloud.result.Result;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.List;

/**
 * @author qidong
 * @date 2019/2/13
 */

@Document(collection = "attending_diagosis")
@Entity
@Table(name = "attending_diagosis")
public class AttendingDiagnosis {

    @Id
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

    public AttendingDiagnosis(Integer attendingDiagnosisId, Integer diagnosisId, Integer patientId, Integer encounterId) {
        this.attendingDiagnosisId = attendingDiagnosisId;
        this.diagnosisId = diagnosisId;
        this.patientId = patientId;
        this.encounterId = encounterId;
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
}
