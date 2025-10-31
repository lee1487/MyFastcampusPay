package com.fastcampuspay.money.adapter.in.kafka;

import com.fastcampuspay.common.CnEnum.SubTaskStatus;
import com.fastcampuspay.common.logging.LoggingProducer;
import com.fastcampuspay.common.task.CountDownLatchManager;
import com.fastcampuspay.common.task.RechargingMoneyTask;
import com.fastcampuspay.common.task.SubTask;
import com.fastcampuspay.common.util.ObjectMapperUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Component
public class RechargingMoneyResultConsumer {
    private final KafkaConsumer<String, String> consumer;
    private final LoggingProducer loggingProducer;

    @NotNull
    private final CountDownLatchManager countDownLatchManager;

    public RechargingMoneyResultConsumer(
            @Value("${kafka.clusters.bootstrapservers}") String bootstrapServers,
            @Value("${task.result.topic}") String topic,
            LoggingProducer loggingProducer,
            CountDownLatchManager countDownLatchManager

    ) {
        this.loggingProducer = loggingProducer;
        this.countDownLatchManager = countDownLatchManager;

        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("group.id", "my-group");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singleton(topic));
        Thread consumerThread = new Thread(() -> {
            try {
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                    for (ConsumerRecord<String, String> record : records) {
                        // print to Stdout
                        System.out.println("Received message: " + record.key() + " / " + record.value());

                        RechargingMoneyTask mainTask = ObjectMapperUtil.fromJson(record.value(), RechargingMoneyTask.class);

                        List<SubTask> subTaskList = mainTask.getSubTaskList();

                        boolean taskResult = true;
                        // validation membership
                        // validation banking
                        for (SubTask subTask : subTaskList) {
                            // 한번만 실패해도 실패한 task로 간주.
                            if (subTask.getStatus().equals(SubTaskStatus.FAIL.getValue())) {
                                taskResult = false;
                                break;
                            }
                        }

                        if (taskResult) {
                            this.loggingProducer.sendMessage(mainTask.getTaskId(), "task success");
                            this.countDownLatchManager.setDataForKey(mainTask.getTaskId(), "success");
                        } else {
                            this.loggingProducer.sendMessage(mainTask.getTaskId(), "task fail");
                            this.countDownLatchManager.setDataForKey(mainTask.getTaskId(), "fail");
                        }

                        this.countDownLatchManager.getCountDownLatch(mainTask.getTaskId()).countDown();
                    }
                }
            } finally {
                consumer.close();
            }
        });
        consumerThread.start();
    }
}
