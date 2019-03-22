package com.harmonycloud.service;

import com.harmonycloud.dto.AttendingDiagnosisDto;
import com.harmonycloud.entity.AttendingDiagnosis;
import com.harmonycloud.entity.Diagnosis;
import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.exception.DiagnosisException;
import com.harmonycloud.monRepository.AttendingDiagnosisMonRepository;
import com.harmonycloud.monRepository.DiagnosisMonRepository;
import com.harmonycloud.oraRepository.AttendingDiagnosisOraRepository;
import com.harmonycloud.oraRepository.DiagnosisOraRepository;
import com.harmonycloud.rocketmq.Producer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @date 2019/2/27
 */
@Service
public class AttendingDiagnosisService {

    private static final Logger logger = LoggerFactory.getLogger(AttendingDiagnosisService.class);

    @Autowired
    private AttendingDiagnosisMonRepository attendingDiagnosisMonRepository;

    @Autowired
    private AttendingDiagnosisOraRepository attendingDiagnosisOraRepository;

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
     * first data save oracle ,and then send mongodb by rocketmq
     *
     * @param attendingDiagnosisList
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setAttendingProblem(List<AttendingDiagnosis> attendingDiagnosisList) throws Exception {
        for (int i = 0; i < attendingDiagnosisList.size(); i++) {
            AttendingDiagnosis attendingDiagnosis = attendingDiagnosisList.get(i);
            Integer adId = attendingDiagnosisOraRepository.save(attendingDiagnosis).getAttendingDiagnosisId();
            attendingDiagnosis.setAttendingDiagnosisId(adId);
        }
        rocketmqService.saveAttending(attendingDiagnosisList);
        return true;
    }

    /**
     * set attending problem cancel
     *
     * @param attendingDiagnosisList
     * @throws DiagnosisException
     */
    public void setAttendingProblemCancel(List<AttendingDiagnosis> attendingDiagnosisList) throws DiagnosisException {
        List<AttendingDiagnosis> newAttendingDiagnosisList = null;
        try {
            newAttendingDiagnosisList = attendingDiagnosisOraRepository.findByEncounterId(attendingDiagnosisList.get(0).getEncounterId());
            attendingDiagnosisOraRepository.deleteAll(newAttendingDiagnosisList);
            rocketmqService.deleteAttending(newAttendingDiagnosisList);
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new DiagnosisException(ErrorMsgEnum.SAVE_ERROR.getMessage());
        }
    }

    /**
     * get patient diagnosis list by encounter id
     *
     * @param encounterId
     * @return
     * @throws Exception
     */
    public List<AttendingDiagnosisDto> getPatientDiagnosisList(Integer encounterId) throws Exception {
        List<AttendingDiagnosis> attendingDiagnosisList = null;

        attendingDiagnosisList = attendingDiagnosisMonRepository.findByEncounterId(encounterId);

        //oracle search
        if (CollectionUtils.isEmpty(attendingDiagnosisList)) {
            attendingDiagnosisList = attendingDiagnosisOraRepository.findByEncounterId(encounterId);
        }

        List<AttendingDiagnosisDto> addList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(attendingDiagnosisList)) {
            List<Integer> integerList = new ArrayList<>();
            for (AttendingDiagnosis ad : attendingDiagnosisList) {
                integerList.add(ad.getDiagnosisId());
            }
            Map<Integer, Diagnosis> diagnosisMap = diagnosisService.getDiagnosisByIntegerList(integerList);

            for (AttendingDiagnosis ad : attendingDiagnosisList) {
                AttendingDiagnosisDto add = new AttendingDiagnosisDto();
                Diagnosis diagnosis = diagnosisMap.get(ad.getDiagnosisId());

                add.setAttendingDiagnosis(ad);
                add.setDiagnosisDescription(diagnosis.getDiagnosisDescription());
                addList.add(add);
            }
        }
        return addList;
    }

    /**
     * first delete, then insert
     *
     * @param attendingDiagnosisNewList
     * @param attendingDiagnosisOldList
     * @return
     * @throws DiagnosisException
     */
    public boolean updateAttendingProblemList(List<AttendingDiagnosis> attendingDiagnosisNewList,
                                              List<AttendingDiagnosis> attendingDiagnosisOldList) throws DiagnosisException {
        if (CollectionUtils.isNotEmpty(attendingDiagnosisNewList)) {
            Set<String> adlNewSet = new HashSet<>();
            for (int i = 0; i < attendingDiagnosisNewList.size(); i++) {
                if (adlNewSet.contains(attendingDiagnosisNewList.get(i).toString())) {
                    throw new DiagnosisException("save fail");
                }
                adlNewSet.add(attendingDiagnosisNewList.get(i).toString());
            }
        }
        try {
            if (CollectionUtils.isNotEmpty(attendingDiagnosisOldList)) {
                List<AttendingDiagnosis> attendingDiagnosisList = attendingDiagnosisOraRepository.findByEncounterId(attendingDiagnosisOldList.get(0).getEncounterId());
                attendingDiagnosisOraRepository.deleteAll(attendingDiagnosisList);
                rocketmqService.deleteAttending(attendingDiagnosisList);
            }
            if (CollectionUtils.isNotEmpty(attendingDiagnosisNewList)) {
                setAttendingProblem(attendingDiagnosisNewList);
            }
        } catch (Exception e) {
            throw new DiagnosisException(ErrorMsgEnum.UPDATE_ERROR.getMessage());
        }
        return true;
    }


    /**
     * update attending problem cancel
     *
     * @param attendingDiagnosisNewList
     * @param attendingDiagnosisOldList
     * @throws DiagnosisException
     */
    public void updateAttendingProblemCancel(List<AttendingDiagnosis> attendingDiagnosisNewList,
                                             List<AttendingDiagnosis> attendingDiagnosisOldList) throws DiagnosisException {
        try {
            if (attendingDiagnosisNewList != null) {
                List<AttendingDiagnosis> attendingDiagnosisList = attendingDiagnosisOraRepository.findByEncounterId(attendingDiagnosisNewList.get(0).getEncounterId());
                attendingDiagnosisOraRepository.deleteAll(attendingDiagnosisList);
                rocketmqService.deleteAttending(attendingDiagnosisList);
            }
            attendingDiagnosisOraRepository.saveAll(attendingDiagnosisOldList);
            rocketmqService.saveAttending(attendingDiagnosisOldList);

        } catch (Exception e) {
            throw new DiagnosisException(ErrorMsgEnum.UPDATE_ERROR.getMessage());
        }
    }
}
