package com.harmonycloud.monRepository;

import com.harmonycloud.entity.Diagnosis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author qidong
 * @date 2019/2/26
 */
@RepositoryRestResource
public interface DiagnosisMonRepository extends MongoRepository<Diagnosis, Integer> {
    Diagnosis findByDiagnosisId(Integer keywordNum);

    List<Diagnosis> findByDiagnosisDescriptionLike(String keyword);

    List<Diagnosis> findByDiagnosisDescriptionMatchesRegex(String keyword);

}
