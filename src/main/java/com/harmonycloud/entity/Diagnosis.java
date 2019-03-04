package com.harmonycloud.entity;


import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;


/**
 * @author qidong
 * @date 2019/2/13
 */
@Document(collection = "diagnosis")
@Entity
@Table(name = "DIAGNOSIS")
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer diagnosisId;
    @Column(name = "diagnosis_description")
    private String diagnosisDescription;

    public Diagnosis(Integer diagnosisId, String diagnosisDescription) {
        this.diagnosisId = diagnosisId;
        this.diagnosisDescription = diagnosisDescription;
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
}
