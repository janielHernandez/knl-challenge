package org.msvc.elevator.system.external.call.infrastructure;


import com.elevator.system.common.events.BaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ElevatorEventProducer{

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void produce(String topic, BaseEvent event) {
        this.kafkaTemplate.send(topic, event);
    }
}
