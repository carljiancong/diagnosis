package com.harmonycloud.service;

import com.alibaba.fastjson.JSON;
import com.harmonycloud.entity.ChronicDiagnosis;
import com.harmonycloud.monRepository.ChronicDiagnosisMonRepository;
import com.harmonycloud.oraRepository.ChronicDiagnosisOraRepository;
import com.harmonycloud.result.CodeMsg;
import com.harmonycloud.result.Result;
import com.harmonycloud.rocketmq.Producer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;

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
    Producer producer;

    public Result getPatientChronicProblemList(Integer patientId) {
        List<ChronicDiagnosis> chronicDiagnosisList = null;
        try {
            chronicDiagnosisList = chronicDiagnosisMonRepository.findByPatientIdOrderByEncounterId(patientId);
            if (chronicDiagnosisList != null || chronicDiagnosisList.size() != 0 ) {
                Integer encounterId = chronicDiagnosisList.get(chronicDiagnosisList.size()-1).getEncounterId();
                chronicDiagnosisList = chronicDiagnosisMonRepository.findByEncounterId(encounterId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.QUERY_DATA_ERROR);
        }
        return Result.buildSuccess(chronicDiagnosisList);
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
}
