package com.harmonycloud.service;

import com.alibaba.fastjson.JSON;
import com.harmonycloud.dto.AttendingDiagnosisDto;
import com.harmonycloud.entity.AttendingDiagnosis;
import com.harmonycloud.entity.Diagnosis;
import com.harmonycloud.monRepository.AttendingDiagnosisMonRepository;
import com.harmonycloud.monRepository.DiagnosisMonRepository;
import com.harmonycloud.oraRepository.AttendingDiagnosisOraRepository;
import com.harmonycloud.result.CodeMsg;
import com.harmonycloud.result.Result;
import com.harmonycloud.rocketmq.Producer;
import org.omg.CORBA.CODESET_INCOMPATIBLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    DiagnosisMonRepository diagnosisMonRepository;

    @Autowired
    Producer producer;

    /**
     *  先将数据保存到oracle中，保存成功后，将数据通过rocketmq消费到mongodb中
     * @param attendingDiagnosisList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result setAttendingProblem(List<AttendingDiagnosis> attendingDiagnosisList) throws Exception{
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
            throw e;
            //return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
        }
        return Result.buildSuccess("save success");
    }

    /**
     * 通过 encounter id查到到最近一次到医院的就诊记录
     * @param encounterId
     * @return
     */
    public Result getPatientDiagnosisList(Integer encounterId) {
        List<AttendingDiagnosis> attendingDiagnosisList = null;
        try {
//            attendingDiagnosisList = attendingDiagnosisMonRepository.findByPatientIdOrderByEncounterId(patientId);
//            if (attendingDiagnosisList != null || attendingDiagnosisList.size() != 0) {
//                Integer encounterId = attendingDiagnosisList.get(attendingDiagnosisList.size()-1).getEncounterId();
//                attendingDiagnosisList = attendingDiagnosisMonRepository.findByEncounterId(encounterId);
//            }
            attendingDiagnosisList = attendingDiagnosisMonRepository.findByEncounterId(encounterId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.QUERY_DATA_ERROR);
        }
        List<AttendingDiagnosisDto> addList = new ArrayList<>();
        if((attendingDiagnosisList != null) && (attendingDiagnosisList.size() != 0)) {
            for (AttendingDiagnosis ad: attendingDiagnosisList) {
                AttendingDiagnosisDto add = new AttendingDiagnosisDto();
                Diagnosis diagnosis = diagnosisMonRepository.findByDiagnosisId(ad.getDiagnosisId());
                add.setAttendingDiagnosis(ad);
                add.setDiagnosisDescription(diagnosis.getDiagnosisDescription());
                addList.add(add);
            }
        }
        return Result.buildSuccess(addList);
    }

    /**
     * oldList里的encounterId都是一定的，取其中encounterId。在数据库找出encounterId下的所有数据，
     * 让oldList与数据库中的值对比，若没有一处不同，则可以更新，否则更新失败。
     * @param attendingDiagnosisNewList
     * @param attendingDiagnosisOldList
     * @return
     */
    public Result updateAttendingProblemList(List<AttendingDiagnosis> attendingDiagnosisNewList,
                                             List<AttendingDiagnosis> attendingDiagnosisOldList) {
        try {
            setAttendingProblem(attendingDiagnosisNewList);
        } catch(Exception e) {
            return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
        }
        Integer encounterId = attendingDiagnosisOldList.get(0).getEncounterId();
        List<AttendingDiagnosis> attendingDiagnosisList = attendingDiagnosisMonRepository.findByEncounterId(encounterId);
//        Set<String> adlSet = new HashSet<>();
//        for (AttendingDiagnosis ad: attendingDiagnosisList) {
//            adlSet.add(ad.toString());
//        }
//
//        for (AttendingDiagnosis ad: attendingDiagnosisOldList) {
//            if (adlSet.contains(ad.toString()) == false) {
//                return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
//            }
//        }
        Set<String> adlNewSet = new HashSet<>();
        for (AttendingDiagnosis ad: attendingDiagnosisNewList) {
            adlNewSet.add(ad.toString());
        }
        try {
            for (AttendingDiagnosis ad :attendingDiagnosisList) {
                if (adlNewSet.contains(ad.toString()) == false) {
                    attendingDiagnosisOraRepository.delete(ad);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.DELETE_DATA_ERROR);
        }
        for (AttendingDiagnosis ad :attendingDiagnosisList) {
            if (adlNewSet.contains(ad.toString()) == false) {
                attendingDiagnosisMonRepository.delete(ad);
            }
        }
        return Result.buildSuccess("save success");
    }
}
