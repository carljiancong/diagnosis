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
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
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

    public Result getPatientChronicProblemList(Integer patientId) {
        List<ChronicDiagnosis> chronicDiagnosisList = null;
        try {
            chronicDiagnosisList = chronicDiagnosisMonRepository.findByPatientIdOrderByEncounterId(patientId);
            if ((chronicDiagnosisList != null) && (chronicDiagnosisList.size() != 0) ) {
                Integer encounterId = chronicDiagnosisList.get(chronicDiagnosisList.size()-1).getEncounterId();
                chronicDiagnosisList = chronicDiagnosisMonRepository.findByEncounterId(encounterId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.QUERY_DATA_ERROR);
        }
        List<ChronicDiagnosisDto> chronicDiagnosisDtoList = new ArrayList<>();
        if ((chronicDiagnosisList != null) && (chronicDiagnosisList.size() != 0)) {
            for (ChronicDiagnosis cd: chronicDiagnosisList) {
                ChronicDiagnosisDto cdd = new ChronicDiagnosisDto();
                Diagnosis diagnosis = diagnosisMonRepository.findByDiagnosisId(cd.getDiagnosisId());
                cdd.setChronicDiagnosis(cd);
                cdd.setDiagnosisDescription(diagnosis.getDiagnosisDescription());
                chronicDiagnosisDtoList.add(cdd);
            }
        }
        return Result.buildSuccess(chronicDiagnosisDtoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result setChronicProblem(List<ChronicDiagnosis> chronicDiagnosisList) {
        try {
            for (int i = 0; i < chronicDiagnosisList.size(); i++) {
                ChronicDiagnosis chronicDiagnosis = chronicDiagnosisList.get(i);
                Integer cdId = chronicDiagnosisOraRepository.save(chronicDiagnosis).getId();
                chronicDiagnosis.setId(cdId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
        }
        try {
            for (int i = 0; i < chronicDiagnosisList.size(); i++) {
                producer.send("ChronicTopic", "chronicPush", JSON.toJSONString(chronicDiagnosisList.get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
        }
        return Result.buildSuccess("save success");
    }

    /**
     * 与updateAttendingDiagnosis 的原理相同
     * @param chronicDiagnosisNewList
     * @param chronicDiagnosisOldList
     * @return
     */
    public Result updateChronicProblemList(List<ChronicDiagnosis> chronicDiagnosisNewList, List<ChronicDiagnosis> chronicDiagnosisOldList) {
        Integer encounterId = chronicDiagnosisOldList.get(0).getEncounterId();
        List<ChronicDiagnosis> ChronicDiagnosisList = chronicDiagnosisMonRepository.findByEncounterId(encounterId);
        Set<String> cdlSet = new HashSet<>();
        for (ChronicDiagnosis cd: ChronicDiagnosisList) {
            cdlSet.add(cd.toString());
        }

        for (ChronicDiagnosis cd: ChronicDiagnosisList) {
            if (cdlSet.contains(cd.toString()) == false) {
                return Result.buildError(CodeMsg.SAVE_DATA_FAIL);
            }
        }
        return setChronicProblem(chronicDiagnosisNewList);
    }
}
