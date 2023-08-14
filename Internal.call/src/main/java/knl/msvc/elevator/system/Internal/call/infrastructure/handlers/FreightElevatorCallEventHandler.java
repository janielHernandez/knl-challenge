package knl.msvc.elevator.system.Internal.call.infrastructure.handlers;


import com.elevator.system.common.events.FreightElevatorCallEvent;
import knl.msvc.elevator.system.Internal.call.cache.BuildingCached;
import knl.msvc.elevator.system.Internal.call.domains.*;
import knl.msvc.elevator.system.Internal.call.infrastructure.drivers.FreightElevatorDriver;
import knl.msvc.elevator.system.Internal.call.infrastructure.drivers.PublicElevatorDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class FreightElevatorCallEventHandler extends ElevatorCallHandler {

    private final Logger logger = Logger.getLogger(FreightElevatorCallEventHandler.class.getName());

    @Autowired
    private BuildingCached buildingCached;

    @Autowired
    private FreightElevatorDriver elevatorDriver;


    public void handle(FreightElevatorCallEvent event) throws InterruptedException {

        logger.info("External Request received " + event.toString());

        PublicElevator elevator = buildingCached.getPublicElevator();

        Request r = mapExternalEventToRequest(event);

        addRequest(elevator, r);
        buildingCached.updatePublicElevator(elevator);

        elevatorDriver.run(elevator);
    }

    private void addRequest(PublicElevator elevator, Request r) {

        if (r.getDirection() == Direction.UP){
            elevator.addRequestTo(r, elevator.getPendingUP());
            logger.info("New Request added to Pending Up List");
        } else {
            elevator.addRequestTo(r, elevator.getPendingDown());
            logger.info("New Request added to Pending Down List");
        }
    }
}
