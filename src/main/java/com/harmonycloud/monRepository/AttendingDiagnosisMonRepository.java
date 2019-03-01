package com.harmonycloud.monRepository;

import com.harmonycloud.entity.AttendingDiagnosis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author qidong
 * @date 2019/2/27
 */
@RepositoryRestResource
public interface AttendingDiagnosisMonRepository extends MongoRepository<AttendingDiagnosis,Integer> {

    public List<AttendingDiagnosis> findByEncounterId(Integer encounterId);

}
