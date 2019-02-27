package com.harmonycloud.repository;

import com.harmonycloud.entity.ChronicDiagnosis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author qidong
 * @date 2019/2/27
 */
@RepositoryRestResource
public interface ChronicDiagnosisRepository extends MongoRepository<ChronicDiagnosis, Integer> {
    public List<ChronicDiagnosis> findByPatientId(Integer patientId);
}
