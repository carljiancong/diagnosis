package com.harmonycloud.oraRepository;

import com.harmonycloud.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @date 2019/2/26
 */
@Repository
public interface DiagnosisOraRepository extends JpaRepository<Diagnosis, Integer> {
    Diagnosis findByDiagnosisId(Integer keywordNum);

    List<Diagnosis> findByDiagnosisDescriptionContaining(String keyword);

}
