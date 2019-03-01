package com.harmonycloud.bo;

import com.harmonycloud.entity.AttendingDiagnosis;

import java.util.List;

/**
 * @author qidong
 * @date 2019/3/1
 */
public class PatientDiagnosisList {
    List<AttendingDiagnosis> attendingDiagnosesList;

    public PatientDiagnosisList() {
    }

    public List<AttendingDiagnosis> getAttendingDiagnosesList() {
        return attendingDiagnosesList;
    }

    public void setAttendingDiagnosesList(List<AttendingDiagnosis> attendingDiagnosesList) {
        this.attendingDiagnosesList = attendingDiagnosesList;
    }

    public PatientDiagnosisList(List<AttendingDiagnosis> attendingDiagnosesList) {
        this.attendingDiagnosesList = attendingDiagnosesList;
    }
}
