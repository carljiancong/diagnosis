package com.harmonycloud.monRepository;

import com.harmonycloud.entity.ChronicDiagnosis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @date 2019/2/27
 */
@RepositoryRestResource
public interface ChronicDiagnosisMonRepository extends MongoRepository<ChronicDiagnosis, Integer> {
    List<ChronicDiagnosis> findByPatientIdOrderByEncounterId(Integer patientId);
    List<ChronicDiagnosis> findByEncounterId(Integer encounterId);
}
