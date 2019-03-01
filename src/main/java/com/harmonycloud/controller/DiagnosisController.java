package com.harmonycloud.controller;

import com.harmonycloud.bo.PatientDiagnosisList;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qidong
 * @date 2019/2/26
 */

@RestController
@Api(tags = "Diagnosis")
public class DiagnosisController {

    @Resource
    DiagnosisService diagnosisService;

    @Resource
    AttendingDiagnosisService attendingDiagnosisService;

    @Resource
    ChronicDiagnosisService chronicDiagnosisService;

//    @Resource
//    DiagnosisRepository diagnosisRepository;
//
//    @ApiImplicitParam(name = "diagnosis", value = "diagnosis", dataType = "Diagnosis")
//    @PostMapping("/setDiagnosisProblem")
//    public void setSearchByKeyword(@RequestBody Diagnosis diagnosis) {
//        diagnosisRepository.save(diagnosis);
//    }

    @ApiOperation(value = "search problem by keyword", httpMethod = "GET")
    @ApiImplicitParam(name = "keyword", value = "keyword", paramType = "query", dataType = "String")
    @GetMapping("/diagnosisProblem")
    public Result searchByKeyword(@RequestParam("keyword") String keyword) {

        if (keyword != null) {
            return diagnosisService.searchByKeyword(keyword);
        }
        return Result.buildError(CodeMsg.PARAM_ERROR);
    }


    /**
     * 增加了oraRepository。save时，存到oracle里。get时，从mongo取。
     *
     * 调试save时，有bug，前端调用
     * {
     *   "diagnosisId": 0,
     *   "encounterId": 0,
     *   "patientId": 0
     * }时，save失败。
     *
     * @param patientDiagnosis
     * @return
     */
    @ApiOperation(value = "save patient diagnosis list", httpMethod = "POST")
    @ApiImplicitParam(name = "patientDiagnosis", value = "patientDiagnosis", dataType = "AttendingDiagnosis")
    @PostMapping("/PatientDiagnosis")
    public Result setPatientDiagnosis(@RequestBody AttendingDiagnosis patientDiagnosis) {
        if (patientDiagnosis == null) {
            return Result.buildError(CodeMsg.PARAM_ERROR);
        }
        return attendingDiagnosisService.setPatientDiagnosis(patientDiagnosis);
    }

    @ApiOperation(value = "get patient diagnosis list", httpMethod = "GET")
    @ApiImplicitParam(name = "patientId", value = "patientId", paramType = "query", dataType = "Integer")
    @GetMapping("/PatientDiagnosisList")
    public Result getPatientDiagnosis(@RequestParam("patientId") Integer patientId) {
        if (patientId == null) {
            return Result.buildError(CodeMsg.PARAM_ERROR);
        }
        return attendingDiagnosisService.getPatientDiagnosisList(patientId);
    }


    @ApiOperation(value = "delete patient diagnosis", httpMethod = "POST")
    @ApiImplicitParam(name = "patientDiagnosis", value = "patientDiagnosis", dataType = "AttendingDiagnosis")
    @PostMapping("/delPatientDiagnosis")
    public Result deletePatientDiagnosis(@RequestBody AttendingDiagnosis patientDiagnosis) {
        if (patientDiagnosis == null) {
            return Result.buildError(CodeMsg.PARAM_ERROR);
        }
        return attendingDiagnosisService.deletePatientDiagnosis(patientDiagnosis);
    }

    @ApiOperation(value = "save chronic problem", httpMethod = "POST")
    @ApiImplicitParam(name = "chronicDiagnosis", value = "chronicDiagnosis", dataType = "ChronicDiagnosis")
    @PostMapping("/chronicProblem")
    public Result setPatientChronicProblem(@RequestBody ChronicDiagnosis chronicDiagnosis) {
        if (chronicDiagnosis == null) {
            return Result.buildError(CodeMsg.PARAM_ERROR);
        }
        return chronicDiagnosisService.setPatientChronicProblem(chronicDiagnosis);
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

    @ApiOperation(value = "delete chronic problem", httpMethod = "POST")
    @ApiImplicitParam(name = "chronicDiagnosis", value = "chronicDiagnosis", dataType = "ChronicDiagnosis")
    @PostMapping("/delChronicProblem")
    public Result deletePatientChronicProblem(@RequestBody ChronicDiagnosis chronicDiagnosis) {
        if (chronicDiagnosis == null) {
            return Result.buildError(CodeMsg.PARAM_ERROR);
        }
        return chronicDiagnosisService.deletePatientChronicProblem(chronicDiagnosis);
    }




}
