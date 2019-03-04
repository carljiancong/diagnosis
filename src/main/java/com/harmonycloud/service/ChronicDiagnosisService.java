package com.harmonycloud.service;

import com.harmonycloud.entity.ChronicDiagnosis;
import com.harmonycloud.monRepository.ChronicDiagnosisRepository;
import com.harmonycloud.oraRepository.ChronicDiagnosisOraRepository;
import com.harmonycloud.result.CodeMsg;
import com.harmonycloud.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qidong
 * @date 2019/2/27
 */
@Service
public class ChronicDiagnosisService {

    @Autowired
    ChronicDiagnosisRepository chronicDiagnosisRepository;

    @Autowired
    ChronicDiagnosisOraRepository chronicDiagnosisOraRepository;

    public Result getPatientChronicProblemList(Integer patientId) {
        List<ChronicDiagnosis> chronicDiagnosisList = null;
        try {
            chronicDiagnosisList = chronicDiagnosisRepository.findByPatientId(patientId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.QUERY_DATA_ERROR);
        }
        return Result.buildSuccess(chronicDiagnosisList);
    }

    public Result setChronicProblem(List<ChronicDiagnosis> chronicDiagnosisList) {
        try {
            for (int i = 0; i < chronicDiagnosisList.size(); i++) {
                ChronicDiagnosis chronicDiagnosis = chronicDiagnosisList.get(i);
                chronicDiagnosisOraRepository.save(chronicDiagnosis);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
        }
        return Result.buildSuccess("save success");
    }
}
