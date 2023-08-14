package knl.msvc.elevator.system.Internal.call.infrastructure.handlers;

import com.elevator.system.common.events.ElevatorCallEvent;
import knl.msvc.elevator.system.Internal.call.domains.Location;
import knl.msvc.elevator.system.Internal.call.domains.Request;

public abstract class ElevatorCallHandler {

    public static Request mapExternalEventToRequest(ElevatorCallEvent event){
        return Request.builder()
                .id(event.getId())
                .origin(event.getOrigin())
                .destination(event.getDestination())
                .location(Location.OUTSIDE_ELEVATOR)
                .build();
    }
}
