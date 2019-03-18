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
        List<ChronicDiagnosis> newChronicDiagnosisList = null;
        try {
            newChronicDiagnosisList = chronicDiagnosisOraRepository.findByEncounterId(chronicDiagnosisList.get(0).getEncounterId());
            chronicDiagnosisOraRepository.deleteAll(newChronicDiagnosisList);
            rocketmqService.deleteChronic(newChronicDiagnosisList);
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
    public Result updateChronicProblemList(List<ChronicDiagnosis> chronicDiagnosisNewList, List<ChronicDiagnosis> chronicDiagnosisOldList) throws Exception {
        if (chronicDiagnosisNewList != null && chronicDiagnosisNewList.size() != 0) {
            Set<String> cdlNewSet = new HashSet<>();
            for (int i = 0; i < chronicDiagnosisNewList.size(); i++) {
                if (cdlNewSet.contains(chronicDiagnosisNewList.get(i).toString())) {
                    throw new Exception("update data fail");
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
                chronicDiagnosisOraRepository.deleteAll(chronicDiagnosisList);
                rocketmqService.deleteChronic(chronicDiagnosisList);
            }
            chronicDiagnosisOraRepository.saveAll(chronicDiagnosisOldList);
            rocketmqService.saveChronic(chronicDiagnosisOldList);
        } catch (
                Exception e)

        {
            e.printStackTrace();

        }
    }
}
