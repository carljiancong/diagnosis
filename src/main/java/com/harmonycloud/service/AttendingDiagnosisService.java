package com.harmonycloud.service;

import com.harmonycloud.entity.AttendingDiagnosis;
import com.harmonycloud.monRepository.AttendingDiagnosisMonRepository;
import com.harmonycloud.oraRepository.AttendingDiagnosisOraRepository;
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

    /**
     *
     */
    @Resource
    AttendingDiagnosisMonRepository attendingDiagnosisMonRepository;

    @Resource
    AttendingDiagnosisOraRepository attendingDiagnosisOraRepository;

//    public Result setPatientDiagnosis(List<AttendingDiagnosis> patientDiagnosisList) {
//        try {
//            Integer tmp = (int)System.currentTimeMillis();
//            patientDiagnosis.setId(tmp);
//            attendingDiagnosisRepository.save(patientDiagnosis);
//            return Result.buildSuccess(tmp);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
//        }
//    }

    public Result setPatientDiagnosis(AttendingDiagnosis patientDiagnosis) {
        try {
//            List<AttendingDiagnosis> adl = patientDiagnosisList.getAttendingDiagnosesList();
//            for (AttendingDiagnosis ad : adl) {
                attendingDiagnosisOraRepository.save(patientDiagnosis);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
        }
        return Result.buildSuccess("success");
    }

    public Result getPatientDiagnosisList(Integer patientId) {
        List<AttendingDiagnosis> attendingDiagnosisList = null;
        try {
            attendingDiagnosisList = attendingDiagnosisMonRepository.findByEncounterId(patientId);
        } catch(Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.QUERY_DATA_ERROR);
        }
        return Result.buildSuccess(attendingDiagnosisList);
    }

    public Result deletePatientDiagnosis(AttendingDiagnosis patientDiagnosis) {
        try {
            attendingDiagnosisMonRepository.delete(patientDiagnosis);
        } catch(Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.DELETE_DATA_ERROR);
        }
        return Result.buildSuccess("success");
    }

}
