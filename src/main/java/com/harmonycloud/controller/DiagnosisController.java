package com.harmonycloud.controller;

//import com.harmonycloud.bo.PatientDiagnosisList;
import com.harmonycloud.entity.AttendingDiagnosis;
import com.harmonycloud.entity.ChronicDiagnosis;
import com.harmonycloud.result.CodeMsg;
import com.harmonycloud.result.Result;
import com.harmonycloud.service.AttendingDiagnosisService;
import com.harmonycloud.service.ChronicDiagnosisService;
import com.harmonycloud.service.DiagnosisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
