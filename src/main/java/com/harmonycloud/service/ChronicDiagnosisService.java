package com.harmonycloud.service;

import com.alibaba.fastjson.JSON;
import com.harmonycloud.dto.ChronicDiagnosisDto;
import com.harmonycloud.entity.AttendingDiagnosis;
import com.harmonycloud.entity.ChronicDiagnosis;
import com.harmonycloud.entity.Diagnosis;
import com.harmonycloud.monRepository.ChronicDiagnosisMonRepository;
import com.harmonycloud.monRepository.DiagnosisMonRepository;
import com.harmonycloud.oraRepository.ChronicDiagnosisOraRepository;
import com.harmonycloud.result.CodeMsg;
import com.harmonycloud.result.Result;
import com.harmonycloud.rocketmq.Producer;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author qidong
 * @date 2019/2/27
 */
@Service
public class ChronicDiagnosisService {

    @Autowired
    ChronicDiagnosisMonRepository chronicDiagnosisMonRepository;

    @Autowired
    ChronicDiagnosisOraRepository chronicDiagnosisOraRepository;

    @Autowired
    DiagnosisMonRepository diagnosisMonRepository;

    @Autowired
    Producer producer;

    @Autowired
    RocketmqService rocketmqService;

    public Result getPatientChronicProblemList(Integer patientId) {
        List<ChronicDiagnosis> chronicDiagnosisList = null;
        try {
            chronicDiagnosisList = chronicDiagnosisMonRepository.findByPatientIdOrderByEncounterId(patientId);
            if ((chronicDiagnosisList != null) && (chronicDiagnosisList.size() != 0)) {
                Integer encounterId = chronicDiagnosisList.get(chronicDiagnosisList.size() - 1).getEncounterId();
                chronicDiagnosisList = chronicDiagnosisMonRepository.findByEncounterId(encounterId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.QUERY_DATA_ERROR);
        }
        List<ChronicDiagnosisDto> chronicDiagnosisDtoList = new ArrayList<>();
        if ((chronicDiagnosisList != null) && (chronicDiagnosisList.size() != 0)) {
            for (ChronicDiagnosis cd : chronicDiagnosisList) {
                ChronicDiagnosisDto cdd = new ChronicDiagnosisDto();
                Diagnosis diagnosis = diagnosisMonRepository.findByDiagnosisId(cd.getDiagnosisId());
                cdd.setChronicDiagnosis(cd);
                cdd.setDiagnosisDescription(diagnosis.getDiagnosisDescription());
                chronicDiagnosisDtoList.add(cdd);
            }
        }
        return Result.buildSuccess(chronicDiagnosisDtoList);
    }

    public Result setChronicProblem(List<ChronicDiagnosis> chronicDiagnosisList) throws Exception {
        try {
            for (int i = 0; i < chronicDiagnosisList.size(); i++) {
                ChronicDiagnosis chronicDiagnosis = chronicDiagnosisList.get(i);
                Integer cdId = chronicDiagnosisOraRepository.save(chronicDiagnosis).getId();
                chronicDiagnosis.setId(cdId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        try {
            rocketmqService.saveChronic(chronicDiagnosisList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return Result.buildSuccess("save chronic problem success");
    }

    public void setChronicProblemCancel(List<ChronicDiagnosis> chronicDiagnosisList) {
        List<ChronicDiagnosis> chronicDiagnosisList1 = null;
        try {
            chronicDiagnosisList1 = chronicDiagnosisOraRepository.findByEncounterId(chronicDiagnosisList1.get(0).getEncounterId());
            for (int i = 0; i < chronicDiagnosisList.size(); i++) {
                chronicDiagnosisOraRepository.delete(chronicDiagnosisList1.get(i));
            }
            for (int i = 0; i < chronicDiagnosisList.size(); i++) {
                producer.send("ChronicTopicDel", "chronicPushDel", JSON.toJSONString(chronicDiagnosisList1.get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 与updateAttendingDiagnosis 的原理相同
     *
     * @param chronicDiagnosisNewList
     * @param chronicDiagnosisOldList
     * @return
     */
    public Result updateChronicProblemList(List<ChronicDiagnosis> chronicDiagnosisNewList, List<ChronicDiagnosis> chronicDiagnosisOldList) throws Exception{

//        try {
//            if (chronicDiagnosisNewList != null && chronicDiagnosisNewList.size() != 0) {
//                setChronicProblem(chronicDiagnosisNewList);
//            }
//        } catch (Exception e) {
//            return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
//        }
//        Integer encounterId = null;
//        if (chronicDiagnosisOldList != null && chronicDiagnosisOldList.size() != 0) {
//            encounterId = chronicDiagnosisOldList.get(0).getEncounterId();
//        }
//        //用mongo查，不够快。mq还没消费成功，就去请求了
//        List<ChronicDiagnosis> ChronicDiagnosisList = chronicDiagnosisOraRepository.findByEncounterId(encounterId);
//
//        Set<String> cdlNewSet = new HashSet<>();
//        if (chronicDiagnosisNewList != null && chronicDiagnosisNewList.size() != 0) {
//            for (ChronicDiagnosis cd : chronicDiagnosisNewList) {
//                cdlNewSet.add(cd.toString());
//            }
//        }
//        try {
//            for (ChronicDiagnosis cd : ChronicDiagnosisList) {
//                if (cdlNewSet.contains(cd.toString()) == false) {
//                    chronicDiagnosisOraRepository.delete(cd);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.buildError(CodeMsg.DELETE_DATA_ERROR);
//        }
//
//        try {
//            for (ChronicDiagnosis cd : ChronicDiagnosisList) {
//                if (cdlNewSet.contains(cd.toString()) == false) {
//                    producer.send("ChronicTopicDel", "chronicPushDel", JSON.toJSONString(cd));
//                    //chronicDiagnosisMonRepository.delete(cd);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.buildError(CodeMsg.DELETE_DATA_ERROR);
//        }
//
//        return Result.buildSuccess("save success");

        if (chronicDiagnosisNewList != null && chronicDiagnosisNewList.size() != 0) {
            Set<String> cdlNewSet = new HashSet<>();
            for (int i = 0; i < chronicDiagnosisNewList.size(); i++) {
                if (cdlNewSet.contains(chronicDiagnosisNewList.get(i).toString())) {
                    throw new Exception("update data fail");
                    //return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
                }
                cdlNewSet.add(chronicDiagnosisNewList.get(i).toString());
            }
        }

        try {
            if (chronicDiagnosisOldList != null && chronicDiagnosisOldList.size() != 0) {
                List<ChronicDiagnosis> chronicDiagnosisList = chronicDiagnosisOraRepository.findByEncounterId(chronicDiagnosisOldList.get(0).getEncounterId());
                for (int i = 0; i < chronicDiagnosisList.size(); i++) {
                    chronicDiagnosisOraRepository.delete(chronicDiagnosisList.get(i));
                }
                rocketmqService.deleteChronic(chronicDiagnosisList);
            }
            if (chronicDiagnosisNewList != null && chronicDiagnosisNewList.size() != 0) {
                setChronicProblem(chronicDiagnosisNewList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.buildSuccess("update success");

    }

    //    目前采用了把newList全部del，oldList全部save的操作
    public void updateChronicProblemCancel(List<ChronicDiagnosis> chronicDiagnosisNewList,
                                           List<ChronicDiagnosis> chronicDiagnosisOldList) {
        try {

            if (chronicDiagnosisNewList != null) {
                List<ChronicDiagnosis> chronicDiagnosisList = chronicDiagnosisOraRepository.findByEncounterId(chronicDiagnosisNewList.get(0).getEncounterId());
                for (int i = 0; i < chronicDiagnosisList.size(); i++) {
                    chronicDiagnosisOraRepository.delete(chronicDiagnosisList.get(i));
                    producer.send("ChronicTopicDel", "chronicPushDel", JSON.toJSONString(chronicDiagnosisList.get(i)));
                }
            }
            for (int i = 0; i < chronicDiagnosisOldList.size(); i++) {
                chronicDiagnosisOraRepository.save(chronicDiagnosisOldList.get(i));
            }
            for (int i = 0; i < chronicDiagnosisOldList.size(); i++) {
                producer.send("ChronicTopic", "chronicPush", JSON.toJSONString(chronicDiagnosisOldList.get(i)));

            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
