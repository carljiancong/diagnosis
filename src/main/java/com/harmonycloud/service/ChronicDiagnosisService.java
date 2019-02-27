package com.harmonycloud.service;

import com.harmonycloud.entity.ChronicDiagnosis;
import com.harmonycloud.repository.ChronicDiagnosisRepository;
import com.harmonycloud.result.CodeMsg;
import com.harmonycloud.result.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qidong
 * @date 2019/2/27
 */
@Service
public class ChronicDiagnosisService {

    @Resource
    ChronicDiagnosisRepository chronicDiagnosisRepository;

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

    public Result setPatientChronicProblem(ChronicDiagnosis chronicDiagnosis) {
        try {
            chronicDiagnosisRepository.save(chronicDiagnosis);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
        }
        return Result.buildSuccess("success");
    }
}
