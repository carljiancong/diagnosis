package com.harmonycloud.service;

import com.harmonycloud.entity.Diagnosis;
import com.harmonycloud.monRepository.DiagnosisMonRepository;
import com.harmonycloud.oraRepository.DiagnosisOraRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @date 2019/2/26
 */
@Service
public class DiagnosisService {


    @Autowired
    private DiagnosisMonRepository diagnosisMonRepository;

    @Autowired
    private DiagnosisOraRepository diagnosisOraRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * get diagnosis list by key word
     *
     * @param keyword
     * @return
     * @throws Exception
     */
    public List<Diagnosis> searchByKeyword(String keyword) throws Exception {
        List<Diagnosis> diagnosesList = new ArrayList<>();
        Query query = new TextQuery(new TextCriteria().matchingAny(keyword)).sortByScore();
        List<Diagnosis> diagnoses = mongoTemplate.find(query, Diagnosis.class);

        if (CollectionUtils.isNotEmpty(diagnoses)) {
            diagnosesList.addAll(diagnoses);
        } else {
            diagnoses = diagnosisOraRepository.findByDiagnosisDescriptionContaining(keyword);
            diagnosesList.addAll(diagnoses);
        }

        // 根据id查询
        try {
            Integer keywordNum = Integer.valueOf(keyword);
            Diagnosis diagnose = diagnosisMonRepository.findByDiagnosisId(keywordNum);
            if (diagnose == null) {
                diagnose = diagnosisOraRepository.findByDiagnosisId(keywordNum);
            }
            diagnosesList.add(diagnose);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        Collections.sort(diagnosesList, new Comparator<Diagnosis>() {
            @Override
            public int compare(Diagnosis o1, Diagnosis o2) {
                return o1.getDiagnosisDescription().compareToIgnoreCase(o2.getDiagnosisDescription());
            }
        });
        return diagnosesList;
    }

    /**
     * get diagnosis by integer list
     *
     * @param integerList
     * @return
     * @throws Exception
     */
    public Map<Integer, Diagnosis> getDiagnosisByIntegerList(List<Integer> integerList) throws Exception {
        Iterable<Diagnosis> diagnoses = diagnosisMonRepository.findAllById(integerList);
        Map<Integer, Diagnosis> diagnosisMap = new HashMap<>();
        if (diagnoses != null) {
            diagnoses = diagnosisOraRepository.findAllById(integerList);
        }
        diagnoses.forEach(diagnosis -> {
            diagnosisMap.put(diagnosis.getDiagnosisId(), diagnosis);
        });
        return diagnosisMap;
    }

}
