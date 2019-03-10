package com.harmonycloud.service;

import com.harmonycloud.entity.Diagnosis;
import com.harmonycloud.monRepository.DiagnosisMonRepository;
import com.harmonycloud.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qidong
 * @date 2019/2/26
 */
@Service
public class DiagnosisService {

    @Autowired
    DiagnosisMonRepository diagnosisMonRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    public Result searchByKeyword(String keyword) {
        List<Diagnosis> diagnosesList = new ArrayList<>();
        //Query query = TextQuery(new TextCriteria().matchingAny("小张", "小李")).sortByScore();

        List<Diagnosis> diagnoses = diagnosisMonRepository.findByDiagnosisDescriptionMatchesRegex(keyword);

        if (diagnoses != null && diagnoses.size() != 0) {
            diagnosesList.addAll(diagnoses);
        }

        // 根据id查询
        try {
            Integer keywordNum = Integer.valueOf(keyword);
            Diagnosis diagnose = diagnosisMonRepository.findByDiagnosisId(keywordNum);
            diagnosesList.add(diagnose);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return Result.buildSuccess(diagnosesList);
    }

//    public boolean isNumeric(String str){
//        for (int i = str.length()-1;i>=0;i--){
//            if (!Character.isDigit(str.charAt(i))){
//                return false;
//            }
//        }
//        return true;
//    }

}
