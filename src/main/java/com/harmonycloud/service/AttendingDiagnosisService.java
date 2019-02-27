package com.harmonycloud.service;

import com.harmonycloud.entity.AttendingDiagnosis;
import com.harmonycloud.repository.AttendingDiagnosisRepository;
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
public class AttendingDiagnosisService {

    @Resource
    AttendingDiagnosisRepository attendingDiagnosisRepository;

    public Result setPatientDiagnosis(AttendingDiagnosis patientDiagnosis) {
        try {
            attendingDiagnosisRepository.save(patientDiagnosis);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
        }
        return Result.buildSuccess("success");
    }

    public Result getPatientDiagnosisList(Integer encounterId) {
        List<AttendingDiagnosis> attendingDiagnosisList = null;
        try {
            attendingDiagnosisList = attendingDiagnosisRepository.findByEncounterId(encounterId);
        } catch(Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.QUERY_DATA_ERROR);
        }
        return Result.buildSuccess(attendingDiagnosisList);
    }
}