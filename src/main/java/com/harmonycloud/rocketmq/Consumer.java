package com.harmonycloud.rocketmq;

import com.alibaba.fastjson.JSON;
import com.harmonycloud.entity.AttendingDiagnosis;
import com.harmonycloud.entity.ChronicDiagnosis;
import com.harmonycloud.monRepository.AttendingDiagnosisMonRepository;
import com.harmonycloud.monRepository.ChronicDiagnosisMonRepository;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * @author qidong
 * @date 2019/3/4
 */
@Service
public class Consumer implements CommandLineRunner {
    /**
     * 消费者
     */
    @Value("${apache.rocketmq.consumer.pushConsumer}")
    private String pushConsumer;

    /**
     * NameServer 地址
     */
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Autowired
    private AttendingDiagnosisMonRepository attendingDiagnosisMonRepository;

    @Autowired
    private ChronicDiagnosisMonRepository chronicDiagnosisMonRepository;

    /**
     * 初始化RocketMq的监听信息，渠道信息
     */
    public void messageListener(){

        DefaultMQPushConsumer consumer=new DefaultMQPushConsumer("AttendingGroup");

        consumer.setNamesrvAddr(namesrvAddr);
        try {

            // 订阅PushTopic下Tag为push的消息,都订阅消息
            consumer.subscribe("AttendingTopic", "attendingPush");

            // 程序第一次启动从消息队列头获取数据
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //可以修改每次消费消息的数量，默认设置是每次消费一条
            consumer.setConsumeMessageBatchMaxSize(1);

            //在此监听中消费信息，并返回消费的状态信息
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {

                // 会把不同的消息分别放置到不同的队列中
                for(Message msg:msgs){
                    AttendingDiagnosis ad = JSON.toJavaObject(JSON.parseObject(new String(msg.getBody())), AttendingDiagnosis.class);
                    attendingDiagnosisMonRepository.save(ad);
                    System.out.println("接收到了消息："+new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });

            consumer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void messageListenerDel(){

        DefaultMQPushConsumer consumer=new DefaultMQPushConsumer("AttendingGroupDel");

        consumer.setNamesrvAddr(namesrvAddr);
        try {

            // 订阅PushTopic下Tag为push的消息,都订阅消息
            consumer.subscribe("AttendingTopicDel", "attendingPushDel");

            // 程序第一次启动从消息队列头获取数据
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //可以修改每次消费消息的数量，默认设置是每次消费一条
            consumer.setConsumeMessageBatchMaxSize(1);

            //在此监听中消费信息，并返回消费的状态信息
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {

                // 会把不同的消息分别放置到不同的队列中
                for(Message msg:msgs){
                    AttendingDiagnosis ad = JSON.toJavaObject(JSON.parseObject(new String(msg.getBody())), AttendingDiagnosis.class);
                    attendingDiagnosisMonRepository.delete(ad);
                    System.out.println("接收到了消息："+new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });

            consumer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void messageListenerChronic(){

        DefaultMQPushConsumer consumerChronic=new DefaultMQPushConsumer("ChronicGroup");

        consumerChronic.setNamesrvAddr(namesrvAddr);
        try {

            // 订阅PushTopic下Tag为push的消息,都订阅消息
            consumerChronic.subscribe("ChronicTopic", "chronicPush");

            // 程序第一次启动从消息队列头获取数据
            consumerChronic.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //可以修改每次消费消息的数量，默认设置是每次消费一条
            consumerChronic.setConsumeMessageBatchMaxSize(1);

            //在此监听中消费信息，并返回消费的状态信息
            consumerChronic.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {

                // 会把不同的消息分别放置到不同的队列中
                for(Message msg:msgs){
                    ChronicDiagnosis cd = JSON.toJavaObject(JSON.parseObject(new String(msg.getBody())), ChronicDiagnosis.class);
                    chronicDiagnosisMonRepository.save(cd);
                    System.out.println("接收到了消息："+ new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });

            consumerChronic.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void messageListenerChronicDel(){

        DefaultMQPushConsumer consumerChronic=new DefaultMQPushConsumer("ChronicGroupDel");

        consumerChronic.setNamesrvAddr(namesrvAddr);
        try {

            // 订阅PushTopic下Tag为push的消息,都订阅消息
            consumerChronic.subscribe("ChronicTopicDel", "chronicPushDel");

            // 程序第一次启动从消息队列头获取数据
            consumerChronic.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //可以修改每次消费消息的数量，默认设置是每次消费一条
            consumerChronic.setConsumeMessageBatchMaxSize(1);

            //在此监听中消费信息，并返回消费的状态信息
            consumerChronic.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {

                // 会把不同的消息分别放置到不同的队列中
                for(Message msg:msgs){
                    ChronicDiagnosis cd = JSON.toJavaObject(JSON.parseObject(new String(msg.getBody())), ChronicDiagnosis.class);
                    chronicDiagnosisMonRepository.delete(cd);
                    System.out.println("接收到了消息："+ new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });

            consumerChronic.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        this.messageListener();
        this.messageListenerChronic();
        this.messageListenerDel();
        this.messageListenerChronicDel();
    }
}
