package org.msvc.elevator.system.external.call.infrastructure;


import com.elevator.system.common.domain.AggregateRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class ElevatorEventSourcingHandler {

    private final Logger logger = Logger.getLogger(ElevatorEventSourcingHandler.class.getName());

    @Autowired
    private ElevatorEventStore eventStore;

    public void save(AggregateRoot aggregate) {
        eventStore.saveEvent(aggregate.getId(), aggregate.getEvent(),
                aggregate.getVersion(), aggregate.getClass().getSimpleName());
        aggregate.markChangesAsCommitted();
    }

}
