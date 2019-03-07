package com.harmonycloud.bo;

import com.harmonycloud.entity.AttendingDiagnosis;

import java.util.List;

/**
 * @author qidong
 * @date 2019/3/6
 */
public class AttendingDiagnosisNewAndOldList {
    List<AttendingDiagnosis> attendingDiagnosisNewList;
    List<AttendingDiagnosis> attendingDiagnosisOldList;

    public AttendingDiagnosisNewAndOldList(List<AttendingDiagnosis> attendingDiagnosisNewList,
                                           List<AttendingDiagnosis> attendingDiagnosisOldList) {
        this.attendingDiagnosisNewList = attendingDiagnosisNewList;
        this.attendingDiagnosisOldList = attendingDiagnosisOldList;
    }

    public List<AttendingDiagnosis> getAttendingDiagnosisNewList() {
        return attendingDiagnosisNewList;
    }

    public void setAttendingDiagnosisNewList(List<AttendingDiagnosis> attendingDiagnosisNewList) {
        this.attendingDiagnosisNewList = attendingDiagnosisNewList;
    }

    public List<AttendingDiagnosis> getAttendingDiagnosisOldList() {
        return attendingDiagnosisOldList;
    }

    public void setAttendingDiagnosisOldList(List<AttendingDiagnosis> attendingDiagnosisOldList) {
        this.attendingDiagnosisOldList = attendingDiagnosisOldList;
    }
}
