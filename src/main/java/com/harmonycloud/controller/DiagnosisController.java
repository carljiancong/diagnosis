package com.harmonycloud.controller;

import com.alibaba.fastjson.JSON;
import com.harmonycloud.entity.AttendingDiagnosis;
import com.harmonycloud.entity.ChronicDiagnosis;
import com.harmonycloud.result.CodeMsg;
import com.harmonycloud.result.Result;
import com.harmonycloud.rocketmq.Producer;
import com.harmonycloud.service.AttendingDiagnosisService;
import com.harmonycloud.service.ChronicDiagnosisService;
import com.harmonycloud.service.DiagnosisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "Diagnosis")
public class DiagnosisController {

    @Autowired
    DiagnosisService diagnosisService;

    @Autowired
    AttendingDiagnosisService attendingDiagnosisService;

    @Autowired
    ChronicDiagnosisService chronicDiagnosisService;

    @ApiOperation(value = "search problem by keyword", httpMethod = "GET")
    @ApiImplicitParam(name = "keyword", value = "keyword", paramType = "query", dataType = "String")
    @GetMapping("/diagnosisProblem")
    public Result searchByKeyword(@RequestParam("keyword") String keyword) {
        if (keyword != null) {
            return diagnosisService.searchByKeyword(keyword);
        }
        return Result.buildError(CodeMsg.PARAM_ERROR);
    }

    @ApiOperation(value = "save patient attending problem list", httpMethod = "POST")
    @ApiImplicitParam(name = "attendingDiagnosisList", value = "attendingDiagnosisList", dataType = "AttendingDiagnosis")
    @PostMapping("/attendingDiagnosis")
    public Result setAttendingProblem(@RequestBody List<AttendingDiagnosis> attendingDiagnosisList) {
        return attendingDiagnosisService.setAttendingProblem(attendingDiagnosisList);
    }

    @ApiOperation(value = "update patient attending problem list", httpMethod = "POST")
    @ApiImplicitParam(name = "attendingDiagnosisOldList", value = "attendingDiagnosisOldList", dataType = "AttendingDiagnosis")
    @PostMapping("/attendingDiagnosisUpdate")
    public Result updateAttendingProblemList(@RequestBody List<AttendingDiagnosis> attendingDiagnosisNewList,
                                             @RequestBody List<AttendingDiagnosis> attendingDiagnosisOldList) {
        return attendingDiagnosisService.setAttendingProblem(attendingDiagnosisNewList);
    }

    @ApiOperation(value = "get patient attending problem list", httpMethod = "GET")
    @ApiImplicitParam(name = "patientId", value = "patientId", paramType = "query", dataType = "Integer")
    @GetMapping("/attendingproblemList")
    public Result getAttendingProblem(@RequestParam("patientId") Integer patientId) {
        if (patientId == null) {
            return Result.buildError(CodeMsg.PARAM_ERROR);
        }
        return attendingDiagnosisService.getPatientDiagnosisList(patientId);
    }

    @ApiOperation(value = "save chronic problem ", httpMethod = "POST")
    @ApiImplicitParam(name = "chronicDiagnosisList", value = "chronicDiagnosisList", dataType = "ChronicDiagnosis")
    @PostMapping("/chronicProblem")
    public Result setChronicProblem(@RequestBody List<ChronicDiagnosis> chronicDiagnosisList) {
        return chronicDiagnosisService.setChronicProblem(chronicDiagnosisList);
    }

    @ApiOperation(value = "update chronic problem list", httpMethod = "POST")
    @ApiImplicitParam(name = "chronicDiagnosisOldList", value = "chronicDiagnosisOldList", dataType = "ChronicDiagnosis")
    @PostMapping("/chronicProblemUpdate")
    public Result updateChronicProblemList(@RequestBody List<ChronicDiagnosis> chronicDiagnosisNewList,
                                           @RequestBody List<ChronicDiagnosis> chronicDiagnosisOldList) {
        return chronicDiagnosisService.setChronicProblem(chronicDiagnosisNewList);
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


}
