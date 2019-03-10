package com.harmonycloud.controller;

import com.harmonycloud.bo.AttendingDiagnosisNewAndOldList;
import com.harmonycloud.bo.ChronicDiagnosisNewAndOldList;
import com.harmonycloud.entity.AttendingDiagnosis;
import com.harmonycloud.entity.ChronicDiagnosis;
import com.harmonycloud.monRepository.DiagnosisMonRepository;
import com.harmonycloud.result.CodeMsg;
import com.harmonycloud.result.Result;
import com.harmonycloud.service.AttendingDiagnosisService;
import com.harmonycloud.service.ChronicDiagnosisService;
import com.harmonycloud.service.DiagnosisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.servicecomb.saga.omega.transaction.annotations.Compensable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@Api(tags = "Diagnosis")
public class DiagnosisController {

    @Autowired
    DiagnosisService diagnosisService;

    @Autowired
    AttendingDiagnosisService attendingDiagnosisService;

    @Autowired
    ChronicDiagnosisService chronicDiagnosisService;

    @Autowired
    DiagnosisMonRepository diagnosisMonRepository;


    @ApiOperation(value = "search problem by keyword", httpMethod = "GET")
    @ApiImplicitParam(name = "keyword", value = "keyword", paramType = "query", dataType = "String")
    @GetMapping("/diagnosisProblem")
    public Result searchByKeyword(@RequestParam("keyword") String keyword) {
        if (keyword != null) {
            return diagnosisService.searchByKeyword(keyword);
        }
        return Result.buildError(CodeMsg.PARAM_ERROR);
    }

    @ApiOperation(value = "get patient attending problem list", httpMethod = "GET")
    @ApiImplicitParam(name = "encounterId", value = "encounterId", paramType = "query", dataType = "Integer")
    @GetMapping("/attendingproblemList")
    public Result getAttendingProblem(@RequestParam("encounterId") Integer encounterId) {
        if (encounterId == null) {
            return Result.buildError(CodeMsg.PARAM_ERROR);
        }
        return attendingDiagnosisService.getPatientDiagnosisList(encounterId);
    }

    @ApiOperation(value = "get chronic problem list", httpMethod = "GET")
    @ApiImplicitParam(name = "patientId", value = "patientId", paramType = "query", dataType = "Integer")
    @GetMapping("/chronicProblemList")
    public Result getPatientChronicProblem(@RequestParam("patientId") Integer patientId) {
        if (patientId == null) {
            return Result.buildError(CodeMsg.PARAM_ERROR);
        }
        return chronicDiagnosisService.getPatientChronicProblemList(patientId);
    }


    @PostMapping(path = "/saveAttendingDiagnosis", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Compensable(compensationMethod = "saveAttendingCancel", timeout = 10)
    @Transactional(rollbackFor = Exception.class)
    public Result setAttendingProblem(@RequestBody List<AttendingDiagnosis> attendingDiagnosisList) throws InterruptedException, RemotingException, MQClientException, MQBrokerException, UnsupportedEncodingException {
        return attendingDiagnosisService.setAttendingProblem(attendingDiagnosisList);
    }

    public void saveAttendingCancel(List<AttendingDiagnosis> attendingDiagnosisList) {
        try {
            attendingDiagnosisService.setAttendingProblemCancel(attendingDiagnosisList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping(path = "/updateAttendingDiagnosis", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Compensable(compensationMethod = "updateAttendingProblemCancel", timeout = 10)
    @Transactional(rollbackFor = Exception.class)
    public Result updateAttendingProblemList(@RequestBody AttendingDiagnosisNewAndOldList attendingDiagnosisNewAndOldList) {
        return attendingDiagnosisService.updateAttendingProblemList(attendingDiagnosisNewAndOldList.getNewAttendingDiagnosisList(),
                attendingDiagnosisNewAndOldList.getOldAttendingDiagnosisList());
    }

    public void updateAttendingProblemCancel(AttendingDiagnosisNewAndOldList attendingDiagnosisNewAndOldList) {
        try {
            attendingDiagnosisService.updateAttendingProblemCancel(attendingDiagnosisNewAndOldList.getNewAttendingDiagnosisList(),
                    attendingDiagnosisNewAndOldList.getOldAttendingDiagnosisList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @PostMapping(path = "/saveChronicProblem", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Compensable(compensationMethod = "saveChronicCancel", timeout = 10)
    @Transactional(rollbackFor = Exception.class)
    public Result setChronicProblem(@RequestBody List<ChronicDiagnosis> chronicDiagnosisList) throws Exception {
        return chronicDiagnosisService.setChronicProblem(chronicDiagnosisList);
    }

    public void saveChronicCancel(List<ChronicDiagnosis> chronicDiagnosisList) {
        try {
            chronicDiagnosisService.setChronicProblemCancel(chronicDiagnosisList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping(path = "/updateChronicProblem", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Compensable(compensationMethod = "updateChronicProblemCancel", timeout = 10)
    @Transactional(rollbackFor = Exception.class)
    public Result updateChronicProblemList(@RequestBody ChronicDiagnosisNewAndOldList chronicDiagnosisNewAndOldList) {
        return chronicDiagnosisService.updateChronicProblemList(chronicDiagnosisNewAndOldList.getNewChronicDiagnosisList(),
                chronicDiagnosisNewAndOldList.getOldChronicDiagnosisList());
    }

    public void updateChronicProblemCancel(ChronicDiagnosisNewAndOldList chronicDiagnosisNewAndOldList) {
        try {
            chronicDiagnosisService.updateChronicProblemCancel(chronicDiagnosisNewAndOldList.getNewChronicDiagnosisList(),
                    chronicDiagnosisNewAndOldList.getOldChronicDiagnosisList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
