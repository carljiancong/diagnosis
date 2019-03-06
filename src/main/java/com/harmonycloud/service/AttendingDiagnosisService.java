package com.harmonycloud.service;

import com.alibaba.fastjson.JSON;
import com.harmonycloud.entity.AttendingDiagnosis;
import com.harmonycloud.monRepository.AttendingDiagnosisMonRepository;
import com.harmonycloud.oraRepository.AttendingDiagnosisOraRepository;
import com.harmonycloud.result.CodeMsg;
import com.harmonycloud.result.Result;
import com.harmonycloud.rocketmq.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    Producer producer;

    /**
     *  先将数据保存到oracle中，保存成功后，将数据通过rocketmq消费到mongodb中
     * @param attendingDiagnosisList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result setAttendingProblem(List<AttendingDiagnosis> attendingDiagnosisList) {
        try {
            for (int i = 0; i < attendingDiagnosisList.size(); i++) {
                AttendingDiagnosis attendingDiagnosis = attendingDiagnosisList.get(i);
                Integer adId = attendingDiagnosisOraRepository.save(attendingDiagnosis).getAttendingDiagnosisId();
                attendingDiagnosis.setAttendingDiagnosisId(adId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
        }
        try {
            for (int i = 0; i < attendingDiagnosisList.size(); i++) {
                producer.send("AttendingTopic", "attendingPush", JSON.toJSONString(attendingDiagnosisList.get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
        }
        return Result.buildSuccess("save success");
    }

    /**
     * 通过patient id 查找病人的最近的encounter id，然后在通过
     * encounter id查到到最近一次到医院的就诊记录
     * @param patientId
     * @return
     */
    public Result getPatientDiagnosisList(Integer patientId) {
        List<AttendingDiagnosis> attendingDiagnosisList = null;
        try {
            attendingDiagnosisList = attendingDiagnosisMonRepository.findByPatientIdOrderByEncounterId(patientId);
            if (attendingDiagnosisList != null || attendingDiagnosisList.size() != 0) {
                Integer encounterId = attendingDiagnosisList.get(attendingDiagnosisList.size()-1).getEncounterId();
                attendingDiagnosisList = attendingDiagnosisMonRepository.findByEncounterId(encounterId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.QUERY_DATA_ERROR);
        }
        return Result.buildSuccess(attendingDiagnosisList);
    }


}
