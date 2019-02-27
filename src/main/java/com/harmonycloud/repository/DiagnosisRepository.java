package com.harmonycloud.repository;

import com.harmonycloud.entity.Diagnosis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author qidong
 * @date 2019/2/26
 */
@RepositoryRestResource
public interface DiagnosisRepository extends MongoRepository<Diagnosis,Integer> {
    public List<Diagnosis> findByDiagnosisId(Integer keywordNum);

    public List<Diagnosis> findByDiagnosisDescriptionLike(String keyword);

}
