package com.harmonycloud.oraRepository;

import com.harmonycloud.entity.ChronicDiagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChronicDiagnosisOraRepository extends JpaRepository<ChronicDiagnosis,Integer> {
    List<ChronicDiagnosis> findByEncounterId(Integer encounterId);
}
