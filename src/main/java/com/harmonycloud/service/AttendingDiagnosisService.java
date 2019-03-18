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
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
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

    @Autowired
    RocketmqService rocketmqService;

    /**
     * 先将数据保存到oracle中，保存成功后，将数据通过rocketmq消费到mongodb中
     *
     * @param attendingDiagnosisList
     * @return
     */

    public Result setAttendingProblem(List<AttendingDiagnosis> attendingDiagnosisList) throws InterruptedException, RemotingException, UnsupportedEncodingException, MQClientException, MQBrokerException {
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
            rocketmqService.saveAttending(attendingDiagnosisList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MQClientException("mq error", new Throwable());
        }
        return Result.buildSuccess("save attending problem success");
    }

    public void setAttendingProblemCancel(List<AttendingDiagnosis> attendingDiagnosisList) {
        List<AttendingDiagnosis> newAttendingDiagnosisList = null;
        try {
            newAttendingDiagnosisList = attendingDiagnosisOraRepository.findByEncounterId(attendingDiagnosisList.get(0).getEncounterId());
            attendingDiagnosisOraRepository.deleteAll(newAttendingDiagnosisList);
            rocketmqService.deleteAttending(newAttendingDiagnosisList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过 encounter id查到到最近一次到医院的就诊记录
     *
     * @param encounterId
     * @return
     */
    public Result getPatientDiagnosisList(Integer encounterId) {
        List<AttendingDiagnosis> attendingDiagnosisList = null;
        try {
            attendingDiagnosisList = attendingDiagnosisMonRepository.findByEncounterId(encounterId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildError(CodeMsg.QUERY_DATA_ERROR);
        }
        List<AttendingDiagnosisDto> addList = new ArrayList<>();
        if ((attendingDiagnosisList != null) && (attendingDiagnosisList.size() != 0)) {
            for (AttendingDiagnosis ad : attendingDiagnosisList) {
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
     *
     * @param attendingDiagnosisNewList
     * @param attendingDiagnosisOldList
     * @return
     */
    public Result updateAttendingProblemList(List<AttendingDiagnosis> attendingDiagnosisNewList,
                                             List<AttendingDiagnosis> attendingDiagnosisOldList) throws Exception {
        if (attendingDiagnosisNewList != null && attendingDiagnosisNewList.size() != 0) {
            Set<String> adlNewSet = new HashSet<>();
            for (int i = 0; i < attendingDiagnosisNewList.size(); i++) {
                if (adlNewSet.contains(attendingDiagnosisNewList.get(i).toString())) {
                    throw new Exception("save fail");
                }
                adlNewSet.add(attendingDiagnosisNewList.get(i).toString());
            }
        }
        try {
            if (attendingDiagnosisOldList != null && attendingDiagnosisOldList.size() != 0) {
                List<AttendingDiagnosis> attendingDiagnosisList = attendingDiagnosisOraRepository.findByEncounterId(attendingDiagnosisOldList.get(0).getEncounterId());
                for (int i = 0; i < attendingDiagnosisList.size(); i++) {
                    attendingDiagnosisOraRepository.delete(attendingDiagnosisList.get(i));
                }
                rocketmqService.deleteAttending(attendingDiagnosisList);
            }
            if (attendingDiagnosisNewList != null && attendingDiagnosisNewList.size() != 0) {
                setAttendingProblem(attendingDiagnosisNewList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.buildSuccess("update attending problem success");
    }


    //    目前采用了把newList全部del，oldList全部save的操作
    public void updateAttendingProblemCancel(List<AttendingDiagnosis> attendingDiagnosisNewList,
                                             List<AttendingDiagnosis> attendingDiagnosisOldList) {
        try {
            if (attendingDiagnosisNewList != null) {
                List<AttendingDiagnosis> attendingDiagnosisList = attendingDiagnosisOraRepository.findByEncounterId(attendingDiagnosisNewList.get(0).getEncounterId());
                attendingDiagnosisOraRepository.deleteAll(attendingDiagnosisList);
                rocketmqService.deleteAttending(attendingDiagnosisList);
            }
            attendingDiagnosisOraRepository.saveAll(attendingDiagnosisOldList);
            rocketmqService.saveAttending(attendingDiagnosisOldList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
