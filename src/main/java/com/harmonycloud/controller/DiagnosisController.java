package com.harmonycloud.controller;

import com.harmonycloud.dto.AttendingDiagnosisNewAndOldList;
import com.harmonycloud.dto.ChronicDiagnosisNewAndOldList;
import com.harmonycloud.dto.AttendingDiagnosisDto;
import com.harmonycloud.dto.ChronicDiagnosisDto;
import com.harmonycloud.entity.AttendingDiagnosis;
import com.harmonycloud.entity.ChronicDiagnosis;
import com.harmonycloud.entity.Diagnosis;
import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.result.CimsResponseWrapper;
import com.harmonycloud.service.AttendingDiagnosisService;
import com.harmonycloud.service.ChronicDiagnosisService;
import com.harmonycloud.service.DiagnosisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.saga.omega.transaction.annotations.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@Api(tags = "Diagnosis")
public class DiagnosisController {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosisController.class);

    @Autowired
    private DiagnosisService diagnosisService;

    @Autowired
    private AttendingDiagnosisService attendingDiagnosisService;

    @Autowired
    private ChronicDiagnosisService chronicDiagnosisService;


    /**
     * search problem by keyword
     *
     * @param keyword
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "search problem by keyword", httpMethod = "GET")
    @ApiImplicitParam(name = "keyword", value = "keyword", paramType = "query", dataType = "String")
    @GetMapping("/diagnosisProblem")
    public CimsResponseWrapper<List> searchByKeyword(@RequestParam("keyword") String keyword) throws Exception {
        if (StringUtils.isEmpty(keyword)) {
            return new CimsResponseWrapper<>(false, ErrorMsgEnum.PARAM_ERROR.getMessage(), null);
        }
        List<Diagnosis> diagnosisList = diagnosisService.searchByKeyword(keyword);
        return new CimsResponseWrapper<>(true, null, diagnosisList);
    }

    /**
     * get patient attending problem list
     *
     * @param encounterId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "get patient attending problem list", httpMethod = "GET")
    @ApiImplicitParam(name = "encounterId", value = "encounterId", paramType = "query", dataType = "Integer")
    @GetMapping("/attendingproblemList")
    public CimsResponseWrapper<List> getAttendingProblem(@RequestParam("encounterId") Integer encounterId) throws Exception {
        if (encounterId == null || encounterId < 0) {
            return new CimsResponseWrapper<>(false, ErrorMsgEnum.PARAM_ERROR.getMessage(), null);
        }
        List<AttendingDiagnosisDto> attendingDiagnosisDtoList = attendingDiagnosisService.getPatientDiagnosisList(encounterId);
        return new CimsResponseWrapper<>(true, null, attendingDiagnosisDtoList);
    }

    /**
     * get chronic problem list by patient id
     *
     * @param patientId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "get chronic problem list", httpMethod = "GET")
    @ApiImplicitParam(name = "patientId", value = "patientId", paramType = "query", dataType = "Integer")
    @GetMapping("/chronicProblemList")
    public CimsResponseWrapper<List> getPatientChronicProblem(@RequestParam("patientId") Integer patientId) throws Exception {
        if (patientId == null || patientId < 0) {
            return new CimsResponseWrapper<>(false, ErrorMsgEnum.PARAM_ERROR.getMessage(), null);
        }
        List<ChronicDiagnosisDto> chronicDiagnosisDtoList = chronicDiagnosisService.getPatientChronicProblemList(patientId);
        return new CimsResponseWrapper<>(true, null, chronicDiagnosisDtoList);
    }


    /**
     * save attending diagnosis
     *
     * @param attendingDiagnosisList
     * @return
     * @throws Exception
     */
    @PostMapping(path = "/saveAttendingDiagnosis", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Compensable(compensationMethod = "saveAttendingCancel", timeout = 10)
    @Transactional(rollbackFor = Exception.class)
    public CimsResponseWrapper<List> setAttendingProblem(@RequestBody List<AttendingDiagnosis> attendingDiagnosisList) throws Exception {
        if (CollectionUtils.isNotEmpty(attendingDiagnosisList)) {
            attendingDiagnosisService.setAttendingProblem(attendingDiagnosisList);
        }
        return new CimsResponseWrapper<>(true, null, null);
    }

    /**
     * save attending cancel
     *
     * @param attendingDiagnosisList
     */
    public void saveAttendingCancel(List<AttendingDiagnosis> attendingDiagnosisList) {
        attendingDiagnosisService.setAttendingProblemCancel(attendingDiagnosisList);
    }

    /**
     * update attending diagnosis
     *
     * @param attendingDiagnosisNewAndOldList
     * @return
     * @throws Exception
     */
    @PostMapping(path = "/updateAttendingDiagnosis", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Compensable(compensationMethod = "updateAttendingProblemCancel", timeout = 10)
    @Transactional(rollbackFor = Exception.class)
    public CimsResponseWrapper<List> updateAttendingProblemList(@RequestBody AttendingDiagnosisNewAndOldList attendingDiagnosisNewAndOldList) throws Exception {
        attendingDiagnosisService.updateAttendingProblemList(attendingDiagnosisNewAndOldList.getNewAttendingDiagnosisList(),
                attendingDiagnosisNewAndOldList.getOldAttendingDiagnosisList());

        return new CimsResponseWrapper<>(true, null, null);
    }

    /**
     * update attending problem cancel
     *
     * @param attendingDiagnosisNewAndOldList
     */
    public void updateAttendingProblemCancel(AttendingDiagnosisNewAndOldList attendingDiagnosisNewAndOldList) {
        attendingDiagnosisService.updateAttendingProblemCancel(attendingDiagnosisNewAndOldList.getNewAttendingDiagnosisList(),
                attendingDiagnosisNewAndOldList.getOldAttendingDiagnosisList());

    }

    /**
     * save chronic problem
     *
     * @param chronicDiagnosisList
     * @return
     * @throws Exception
     */
    @PostMapping(path = "/saveChronicProblem", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Compensable(compensationMethod = "saveChronicCancel", timeout = 10)
    @Transactional(rollbackFor = Exception.class)
    public CimsResponseWrapper<List> setChronicProblem(@RequestBody List<ChronicDiagnosis> chronicDiagnosisList) throws Exception {
        if (CollectionUtils.isNotEmpty(chronicDiagnosisList)) {
            chronicDiagnosisService.setChronicProblem(chronicDiagnosisList);
        }
        return new CimsResponseWrapper<>(true, null, null);
    }

    /**
     * save chronic cancel
     *
     * @param chronicDiagnosisList
     */
    public void saveChronicCancel(List<ChronicDiagnosis> chronicDiagnosisList) {
        chronicDiagnosisService.setChronicProblemCancel(chronicDiagnosisList);

    }

    /**
     * update chronic problem
     *
     * @param chronicDiagnosisNewAndOldList
     * @return
     * @throws Exception
     */
    @PostMapping(path = "/updateChronicProblem", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Compensable(compensationMethod = "updateChronicProblemCancel", timeout = 10)
    @Transactional(rollbackFor = Exception.class)
    public CimsResponseWrapper<List> updateChronicProblemList(@RequestBody ChronicDiagnosisNewAndOldList chronicDiagnosisNewAndOldList) throws Exception {

        chronicDiagnosisService.updateChronicProblemList(chronicDiagnosisNewAndOldList.getNewChronicDiagnosisList(),
                chronicDiagnosisNewAndOldList.getOldChronicDiagnosisList());

        return new CimsResponseWrapper<>(true, null, null);
    }

    /**
     * update chronic problem cancel
     *
     * @param chronicDiagnosisNewAndOldList
     */
    public void updateChronicProblemCancel(ChronicDiagnosisNewAndOldList chronicDiagnosisNewAndOldList) {
        chronicDiagnosisService.updateChronicProblemCancel(chronicDiagnosisNewAndOldList.getNewChronicDiagnosisList(),
                chronicDiagnosisNewAndOldList.getOldChronicDiagnosisList());

    }


}
