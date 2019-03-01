package com.harmonycloud.oraRepository;

import com.harmonycloud.entity.AttendingDiagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author qidong
 * @date 2019/3/1
 */
@Repository
public interface AttendingDiagnosisOraRepository extends JpaRepository<AttendingDiagnosis,Integer> {

}
