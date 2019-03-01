package com.harmonycloud.service;

import com.harmonycloud.entity.Diagnosis;
import com.harmonycloud.monRepository.DiagnosisRepository;
import com.harmonycloud.result.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qidong
 * @date 2019/2/26
 */
@Service
public class DiagnosisService {

    @Resource
    DiagnosisRepository diagnosisRepository;


    public Result searchByKeyword(String keyword) {
        List<Diagnosis> diagnosesList = new ArrayList<>();
        List<Diagnosis> diagnoses = diagnosisRepository.findByDiagnosisDescriptionLike(keyword);

        if (diagnoses != null && diagnoses.size() != 0) {
            diagnosesList.addAll(diagnoses);
        }

        // 根据id查询
        try {
            Integer keywordNum = Integer.valueOf(keyword);
            diagnoses = diagnosisRepository.findByDiagnosisId(keywordNum);
            diagnosesList.addAll(diagnoses);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.buildSuccess(diagnosesList);
    }


}
