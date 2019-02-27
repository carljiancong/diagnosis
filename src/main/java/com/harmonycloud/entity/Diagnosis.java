package com.harmonycloud.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author qidong
 * @date 2019/2/13
 */
@Document(collection="diagnosis")
public class Diagnosis {
    @Id
    private Integer diagnosisId;
    private String diagnosisDescription;

    public Diagnosis() {
    }

    public Diagnosis(Integer diagnosisId, String diagnosisDescription) {
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
