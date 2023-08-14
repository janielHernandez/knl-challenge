package org.msvc.elevator.system.external.call.domain;


import com.elevator.system.common.constants.BuildingAttributes;
import com.elevator.system.common.domain.AggregateRoot;
import com.elevator.system.common.events.ElevatorCallEvent;
import com.elevator.system.common.events.PublicElevatorCallEvent;
import lombok.NoArgsConstructor;
import org.msvc.elevator.system.external.call.api.dto.ElevatorCall;

import java.util.Date;

@NoArgsConstructor
public class PublicElevatorAggregate extends AggregateRoot {

    private Boolean active;

    public PublicElevatorAggregate(ElevatorCall call){
        raiseEvent(PublicElevatorCallEvent.builder()
                .id(call.getId())
                .origin(call.getOrigin())
                .destination(call.getDestination())
                .hasKeyCard(addKeyCardIfHasAccess(call.getDestination()))
                .createdDate(new Date())
                .build()
        );
    }

    private boolean addKeyCardIfHasAccess(Integer destination) {
        return BuildingAttributes.BASEMENT_FLOOR == destination ||
                BuildingAttributes.MAX_FLOOR == destination;
    }

    public void apply(PublicElevatorCallEvent event){
        this.id = event.getId();
        this.active =  true;
    }
}
