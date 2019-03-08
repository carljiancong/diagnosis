package com.harmonycloud.bo;

import com.harmonycloud.entity.ChronicDiagnosis;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author qidong
 * @date 2019/3/6
 */
public class ChronicDiagnosisNewAndOldList {
    List<ChronicDiagnosis> newChronicDiagnosisList;
    List<ChronicDiagnosis> oldChronicDiagnosisList;

    public ChronicDiagnosisNewAndOldList() {
    }

    public ChronicDiagnosisNewAndOldList(List<ChronicDiagnosis> newChronicDiagnosisList,
                                         List<ChronicDiagnosis> oldChronicDiagnosisList) {
        this.newChronicDiagnosisList = newChronicDiagnosisList;
        this.oldChronicDiagnosisList = oldChronicDiagnosisList;
    }

    public List<ChronicDiagnosis> getNewChronicDiagnosisList() {
        return newChronicDiagnosisList;
    }

    public void setNewChronicDiagnosisList(List<ChronicDiagnosis> newChronicDiagnosisList) {
        this.newChronicDiagnosisList = newChronicDiagnosisList;
    }

    public List<ChronicDiagnosis> getOldChronicDiagnosisList() {
        return oldChronicDiagnosisList;
    }

    public void setOldChronicDiagnosisList(List<ChronicDiagnosis> oldChronicDiagnosisList) {
        this.oldChronicDiagnosisList = oldChronicDiagnosisList;
    }
}
