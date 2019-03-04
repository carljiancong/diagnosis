package com.harmonycloud.service;

import com.harmonycloud.entity.AttendingDiagnosis;
import com.harmonycloud.monRepository.AttendingDiagnosisMonRepository;
import com.harmonycloud.oraRepository.AttendingDiagnosisOraRepository;
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
public class AttendingDiagnosisService {

    /**
     *
     */
    @Autowired
    AttendingDiagnosisMonRepository attendingDiagnosisMonRepository;

    @Autowired
    AttendingDiagnosisOraRepository attendingDiagnosisOraRepository;


    public Result setAttendingProblem(List<AttendingDiagnosis> attendingDiagnosisList) {
        try {
            for (int i = 0; i < attendingDiagnosisList.size(); i++) {
                AttendingDiagnosis attendingDiagnosis = attendingDiagnosisList.get(i);
                attendingDiagnosisOraRepository.save(attendingDiagnosis);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
        }
        return Result.buildSuccess("save success");
    }

    public Result getPatientDiagnosisList(Integer patientId) {
        List<AttendingDiagnosis> attendingDiagnosisList = null;
        try {
            attendingDiagnosisList = attendingDiagnosisMonRepository.findByEncounterId(patientId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.QUERY_DATA_ERROR);
        }
        return Result.buildSuccess(attendingDiagnosisList);
    }


}
