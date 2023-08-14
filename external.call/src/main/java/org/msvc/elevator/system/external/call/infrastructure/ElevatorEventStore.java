package org.msvc.elevator.system.external.call.infrastructure;


import com.elevator.system.common.events.BaseEvent;
import org.msvc.elevator.system.external.call.domain.ElevatorEventModel;
import org.msvc.elevator.system.external.call.domain.EventStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * This is a service to store in mongo and send to kafka
 */
@Service
public class ElevatorEventStore{

    @Autowired
    private EventStoreRepository repository;

    @Autowired
    private ElevatorEventProducer eventProducer;

    public void saveEvent(String aggregateId, BaseEvent event, int version, String aggregateType)  {

        var eventModel = ElevatorEventModel.builder()
                    .timeStamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(aggregateType)
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .baseEvent(event)
                    .build();

        var persistedEvent = repository.save(eventModel);
        if(!persistedEvent.getId().isEmpty()){
            eventProducer.produce(event.getClass().getSimpleName(), event);
        }
    }
}
