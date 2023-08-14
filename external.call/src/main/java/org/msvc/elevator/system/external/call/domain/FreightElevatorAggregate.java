package org.msvc.elevator.system.external.call.domain;


import com.elevator.system.common.domain.AggregateRoot;
import com.elevator.system.common.events.FreightElevatorCallEvent;
import lombok.NoArgsConstructor;
import org.msvc.elevator.system.external.call.api.dto.ElevatorCall;

import java.util.Date;

@NoArgsConstructor
public class FreightElevatorAggregate extends AggregateRoot {

    private Boolean active;

    public FreightElevatorAggregate(ElevatorCall call){
        raiseEvent(FreightElevatorCallEvent.builder()
                .id(call.getId())
                .origin(call.getOrigin())
                .destination(call.getDestination())
                .createdDate(new Date())
                .build()
        );
    }

    public void apply(FreightElevatorCallEvent event){
        this.id = event.getId();
        this.active =  true;
    }
}
