package com.harmonycloud.bo;

import com.harmonycloud.entity.ChronicDiagnosis;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author qidong
 * @date 2019/3/6
 */
public class ChronicDiagnosisNewAndOldList {
    List<ChronicDiagnosis> chronicDiagnosisNewList;
    List<ChronicDiagnosis> chronicDiagnosisOldList;

    public ChronicDiagnosisNewAndOldList() {
    }

    public ChronicDiagnosisNewAndOldList(List<ChronicDiagnosis> chronicDiagnosisNewList,
                                         List<ChronicDiagnosis> chronicDiagnosisOldList) {
        this.chronicDiagnosisNewList = chronicDiagnosisNewList;
        this.chronicDiagnosisOldList = chronicDiagnosisOldList;
    }

    public List<ChronicDiagnosis> getChronicDiagnosisNewList() {
        return chronicDiagnosisNewList;
    }

    public void setChronicDiagnosisNewList(List<ChronicDiagnosis> chronicDiagnosisNewList) {
        this.chronicDiagnosisNewList = chronicDiagnosisNewList;
    }

    public List<ChronicDiagnosis> getChronicDiagnosisOldList() {
        return chronicDiagnosisOldList;
    }

    public void setChronicDiagnosisOldList(List<ChronicDiagnosis> chronicDiagnosisOldList) {
        this.chronicDiagnosisOldList = chronicDiagnosisOldList;
    }
}
