package com.harmonycloud.bo;

import com.harmonycloud.entity.AttendingDiagnosis;

import java.util.List;

/**
 * @author qidong
 * @date 2019/3/6
 */
public class AttendingDiagnosisNewAndOldList {
    List<AttendingDiagnosis> newAttendingDiagnosisList;
    List<AttendingDiagnosis> oldAttendingDiagnosisList;

    public AttendingDiagnosisNewAndOldList() {
    }

    public AttendingDiagnosisNewAndOldList(List<AttendingDiagnosis> newAttendingDiagnosisList,
                                           List<AttendingDiagnosis> oldAttendingDiagnosisList) {
        this.newAttendingDiagnosisList = newAttendingDiagnosisList;
        this.oldAttendingDiagnosisList = oldAttendingDiagnosisList;
    }

    public List<AttendingDiagnosis> getNewAttendingDiagnosisList() {
        return newAttendingDiagnosisList;
    }

    public void setNewAttendingDiagnosisList(List<AttendingDiagnosis> newAttendingDiagnosisList) {
        this.newAttendingDiagnosisList = newAttendingDiagnosisList;
    }

    public List<AttendingDiagnosis> getOldAttendingDiagnosisList() {
        return oldAttendingDiagnosisList;
    }

    public void setOldAttendingDiagnosisList(List<AttendingDiagnosis> oldAttendingDiagnosisList) {
        this.oldAttendingDiagnosisList = oldAttendingDiagnosisList;
    }
}
