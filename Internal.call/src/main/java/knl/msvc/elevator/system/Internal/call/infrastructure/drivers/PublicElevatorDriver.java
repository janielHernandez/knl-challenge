package knl.msvc.elevator.system.Internal.call.infrastructure.drivers;

import knl.msvc.elevator.system.Internal.call.cache.BuildingCached;
import knl.msvc.elevator.system.Internal.call.domains.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PublicElevatorDriver {

    private static final int TIME_LAPSE_BETWEEN_FLOOR = 5000;

    private final Logger logger = Logger.getLogger(PublicElevatorDriver.class.getName());

    @Autowired
    private BuildingCached buildingCached;

    public void run(PublicElevator elevator, Request r, int desiredFloor) throws InterruptedException {

        if (elevator.getCurrentFloor() == desiredFloor){
            elevator.setToTakeIt();
            logger.info("Elevator is prepared to take it..");
            buildingCached.updatePublicElevator( elevator);
        } else {
            if (elevator.getDirection() == Direction.NONE) {
                if (desiredFloor > elevator.getCurrentFloor()) {
                    var processor = new UpProcessor(elevator, r, desiredFloor, buildingCached);
                    Thread upProcessor = new Thread(processor);
                    upProcessor.start();
                    logger.info("New Thread for up request has been created..");
                } else {
                    var processor = new DownProcessor(elevator, r, desiredFloor, buildingCached);
                    Thread downProcessor = new Thread(processor);
                    downProcessor.start();
                    logger.info("New Thread for down request has been created..");
                }
            }
        }
    }

}
