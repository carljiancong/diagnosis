package com.harmonycloud.service;

import com.alibaba.fastjson.JSON;
import com.harmonycloud.entity.AttendingDiagnosis;
import com.harmonycloud.entity.ChronicDiagnosis;
import com.harmonycloud.rocketmq.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qidong
 * @date 2019/3/13
 */
@Service
public class RocketmqService {

    public static final String ATTENDING_TOPIC = "AttendingTopic";
    public static final String ATTENDING_PUSH = "attendingPush";

    public static final String ATTENDING_TOPIC_DEL = "AttendingTopicDel";
    public static final String ATTENDING_PUSH_DEL = "attendingPushDel";

    public static final String CHRONIC_TOPIC = "ChronicTopic";
    public static final String CHRONIC_PUSH = "chronicPush";

    public static final String CHRONIC_TOPIC_DEL = "ChronicTopicDel";
    public static final String CHRONIC_PUSH_DEL = "chronicPushDel";

    @Autowired
    private Producer producer;

    public boolean saveAttending(List<AttendingDiagnosis> attendingDiagnosisList) throws Exception{
        for (int i = 0; i < attendingDiagnosisList.size(); i++) {
            producer.send(ATTENDING_TOPIC, ATTENDING_PUSH, JSON.toJSONString(attendingDiagnosisList.get(i)));
        }
        return true;
    }

    public boolean deleteAttending(List<AttendingDiagnosis> attendingDiagnosisList) throws Exception{
        for (int i = 0; i < attendingDiagnosisList.size(); i++) {
            producer.send(ATTENDING_TOPIC_DEL, ATTENDING_PUSH_DEL, JSON.toJSONString(attendingDiagnosisList.get(i)));
        }
        return true;
    }

    public boolean saveChronic(List<ChronicDiagnosis> chronicDiagnosisList) throws Exception{
        for (int i = 0; i < chronicDiagnosisList.size(); i++) {
            producer.send(CHRONIC_TOPIC, CHRONIC_PUSH, JSON.toJSONString(chronicDiagnosisList.get(i)));
        }
        return true;
    }

    public boolean deleteChronic(List<ChronicDiagnosis> chronicDiagnosisList) throws Exception{
        for (int i = 0; i < chronicDiagnosisList.size(); i++) {
            producer.send(CHRONIC_TOPIC_DEL, CHRONIC_PUSH_DEL, JSON.toJSONString(chronicDiagnosisList.get(i)));
        }
        return true;
    }



}
