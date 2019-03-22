package com.harmonycloud.service;

import com.harmonycloud.dto.ChronicDiagnosisDto;
import com.harmonycloud.entity.ChronicDiagnosis;
import com.harmonycloud.entity.Diagnosis;
import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.exception.DiagnosisException;
import com.harmonycloud.monRepository.ChronicDiagnosisMonRepository;
import com.harmonycloud.monRepository.DiagnosisMonRepository;
import com.harmonycloud.oraRepository.ChronicDiagnosisOraRepository;
import com.harmonycloud.oraRepository.DiagnosisOraRepository;
import com.harmonycloud.rocketmq.Producer;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2019/2/27
 */
@Service
public class ChronicDiagnosisService {

    private static final Logger logger = LoggerFactory.getLogger(ChronicDiagnosisService.class);

    @Autowired
    private ChronicDiagnosisMonRepository chronicDiagnosisMonRepository;

    @Autowired
    private ChronicDiagnosisOraRepository chronicDiagnosisOraRepository;

    @Autowired
    private DiagnosisMonRepository diagnosisMonRepository;

    @Autowired
    private DiagnosisOraRepository diagnosisOraRepository;

    @Autowired
    private Producer producer;

    @Autowired
    private RocketmqService rocketmqService;

    @Autowired
    private DiagnosisService diagnosisService;

    /**
     * get chronic problem list by patient id
     *
     * @param patientId
     * @return
     * @throws Exception
     */
    public List<ChronicDiagnosisDto> getPatientChronicProblemList(Integer patientId) throws Exception {
        List<ChronicDiagnosis> chronicDiagnosisList = null;

        chronicDiagnosisList = chronicDiagnosisMonRepository.findByPatientIdOrderByEncounterId(patientId);

        //oracle search
        if (CollectionUtils.isEmpty(chronicDiagnosisList)) {
            chronicDiagnosisList = chronicDiagnosisOraRepository.findByPatientIdOrderByEncounterId(patientId);
        }

        if (CollectionUtils.isNotEmpty(chronicDiagnosisList)) {
            Integer encounterId = chronicDiagnosisList.get(chronicDiagnosisList.size() - 1).getEncounterId();
            chronicDiagnosisList = chronicDiagnosisMonRepository.findByEncounterId(encounterId);

            //oracle search
            if (CollectionUtils.isEmpty(chronicDiagnosisList)) {
                chronicDiagnosisList = chronicDiagnosisOraRepository.findByEncounterId(encounterId);
            }
        }

        List<ChronicDiagnosisDto> chronicDiagnosisDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(chronicDiagnosisList)) {

            //将需要用到的diagnosis全部查出，从Map中查找diagnosis
            List<Integer> integerList = new ArrayList<>();
            for (ChronicDiagnosis cd : chronicDiagnosisList) {
                integerList.add(cd.getDiagnosisId());
            }
            Map<Integer, Diagnosis> diagnosisMap = diagnosisService.getDiagnosisByIntegerList(integerList);


            for (ChronicDiagnosis cd : chronicDiagnosisList) {
                ChronicDiagnosisDto cdd = new ChronicDiagnosisDto();
                Diagnosis diagnosis = diagnosisMap.get(cd.getDiagnosisId());

                cdd.setChronicDiagnosis(cd);
                cdd.setDiagnosisDescription(diagnosis.getDiagnosisDescription());
                chronicDiagnosisDtoList.add(cdd);
            }
        }
        return chronicDiagnosisDtoList;
    }

    /**
     * save chronicDiagnosisList
     *
     * @param chronicDiagnosisList
     * @return
     * @throws DiagnosisException
     */
    public boolean setChronicProblem(List<ChronicDiagnosis> chronicDiagnosisList) throws DiagnosisException {
        for (int i = 0; i < chronicDiagnosisList.size(); i++) {
            ChronicDiagnosis chronicDiagnosis = chronicDiagnosisList.get(i);
            Integer cdId = chronicDiagnosisOraRepository.save(chronicDiagnosis).getId();
            chronicDiagnosis.setId(cdId);
        }
        try {
            rocketmqService.saveChronic(chronicDiagnosisList);
        } catch (Exception e) {
            throw new DiagnosisException(ErrorMsgEnum.ROCKETMQ_ERROR.getMessage());
        }
        return true;
    }

    /**
     * chronic problem cancel
     *
     * @param chronicDiagnosisList
     * @throws DiagnosisException
     */
    public void setChronicProblemCancel(List<ChronicDiagnosis> chronicDiagnosisList) throws DiagnosisException {
        List<ChronicDiagnosis> newChronicDiagnosisList = null;
        try {
            newChronicDiagnosisList = chronicDiagnosisOraRepository.findByEncounterId(chronicDiagnosisList.get(0).getEncounterId());
            chronicDiagnosisOraRepository.deleteAll(newChronicDiagnosisList);
            rocketmqService.deleteChronic(newChronicDiagnosisList);
        } catch (Exception e) {
            throw new DiagnosisException(ErrorMsgEnum.SAVE_ERROR.getMessage());
        }
    }

    /**
     * update chronic problem list
     *
     * @param chronicDiagnosisNewList
     * @param chronicDiagnosisOldList
     * @return
     * @throws DiagnosisException
     */
    public boolean updateChronicProblemList(List<ChronicDiagnosis> chronicDiagnosisNewList,
                                            List<ChronicDiagnosis> chronicDiagnosisOldList) throws DiagnosisException {
        if (CollectionUtils.isNotEmpty(chronicDiagnosisNewList)) {
            Set<String> cdlNewSet = new HashSet<>();
            for (int i = 0; i < chronicDiagnosisNewList.size(); i++) {
                if (cdlNewSet.contains(chronicDiagnosisNewList.get(i).toString())) {
                    throw new DiagnosisException(ErrorMsgEnum.UPDATE_ERROR.getMessage());
                }
                cdlNewSet.add(chronicDiagnosisNewList.get(i).toString());
            }
        }

        try {
            if (CollectionUtils.isNotEmpty(chronicDiagnosisOldList)) {
                List<ChronicDiagnosis> chronicDiagnosisList = chronicDiagnosisOraRepository.findByEncounterId(chronicDiagnosisOldList.get(0).getEncounterId());
                chronicDiagnosisOraRepository.deleteAll(chronicDiagnosisList);
                rocketmqService.deleteChronic(chronicDiagnosisList);
            }
            if (CollectionUtils.isNotEmpty(chronicDiagnosisNewList)) {
                setChronicProblem(chronicDiagnosisNewList);
            }
        } catch (Exception e) {
            throw new DiagnosisException(ErrorMsgEnum.UPDATE_ERROR.getMessage());
        }
        return true;

    }

    /**
     * update chronic problem cancel
     *
     * @param chronicDiagnosisNewList
     * @param chronicDiagnosisOldList
     * @throws DiagnosisException
     */
    public void updateChronicProblemCancel(List<ChronicDiagnosis> chronicDiagnosisNewList,
                                           List<ChronicDiagnosis> chronicDiagnosisOldList) throws DiagnosisException {
        try {

            if (chronicDiagnosisNewList != null) {
                List<ChronicDiagnosis> chronicDiagnosisList = chronicDiagnosisOraRepository.findByEncounterId(chronicDiagnosisNewList.get(0).getEncounterId());
                chronicDiagnosisOraRepository.deleteAll(chronicDiagnosisList);
                rocketmqService.deleteChronic(chronicDiagnosisList);
            }
            chronicDiagnosisOraRepository.saveAll(chronicDiagnosisOldList);
            rocketmqService.saveChronic(chronicDiagnosisOldList);
        } catch (Exception e) {
            throw new DiagnosisException(ErrorMsgEnum.UPDATE_ERROR.getMessage());
        }
    }
}
