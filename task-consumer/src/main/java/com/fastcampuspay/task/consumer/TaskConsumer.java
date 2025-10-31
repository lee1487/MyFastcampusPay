package com.fastcampuspay.task.consumer;

import com.fastcampuspay.common.CnEnum.SubTaskStatus;
import com.fastcampuspay.common.task.RechargingMoneyTask;
import com.fastcampuspay.common.task.SubTask;
import com.fastcampuspay.common.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Component
public class TaskConsumer {
    private final KafkaConsumer<String, String> consumer;
    private final TaskResultProducer taskResultProducer;

    public TaskConsumer(
            @Value("${kafka.clusters.bootstrapservers}") String bootstrapServers,
            @Value("${task.topic}") String topic,
            TaskResultProducer taskResultProducer

    ) {
        this.taskResultProducer = taskResultProducer;

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
                        // record: RechargingMoneyTask (jsonString)

                        // task run
                        RechargingMoneyTask mainTask = ObjectMapperUtil.fromJson(record.value(), RechargingMoneyTask.class);
                        // task result
                        List<SubTask> subTaskList = mainTask.getSubTaskList();
                        for (SubTask subTask : subTaskList) {
                            // what subtask, membership, banking
                            // external port, adapter
                            // hexagonal architecture

                            // all subtask is done. true
                            subTask.setStatus(SubTaskStatus.SUCCESS.getValue());
                        }

                        // produce TaskResult
                        this.taskResultProducer.sendTaskResult(mainTask.getTaskId(), mainTask);
                    }
                }
            } finally {
                consumer.close();
            }
        });
        consumerThread.start();
    }
}
