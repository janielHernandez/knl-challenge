package knl.msvc.elevator.system.Internal.call.infrastructure.drivers;



import knl.msvc.elevator.system.Internal.call.cache.BuildingCached;
import knl.msvc.elevator.system.Internal.call.domains.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class FreightElevatorDriver {
    private static final int TIME_LAPSE_BETWEEN_FLOOR = 3000;

    private final Logger logger = Logger.getLogger(PublicElevatorDriver.class.getName());

    @Autowired
    private BuildingCached buildingCached;

    public void run(PublicElevator elevator) throws InterruptedException {

        if (elevator.getDirection() == Direction.UP || elevator.getDirection() == Direction.NONE){
            moveUpTo(elevator);
            moveDownTo(elevator);
        } else {
            moveDownTo(elevator);
            moveUpTo(elevator);
        }
    }

    private void moveUpTo(PublicElevator elevator) throws InterruptedException {

        while(!elevator.getPendingUP().isEmpty()){
            RequestWrapper requestWrapper = elevator.getPendingUP().pollFirst();

            if (elevator.getCurrentFloor() == requestWrapper.getSourceFloor()){
                elevator.setToTakeIt();
                break;
            }

            Request request = requestWrapper.getRequests().pollFirst();
            logger.log(Level.INFO, "The elevator is on its way up ...");
            logger.log(Level.INFO,"Moving from " + elevator.getCurrentFloor());
            elevator.setDirection(Direction.UP);
            elevator.setOpen(false);

            for(int currentFloor = elevator.getCurrentFloor();
                !requestWrapper.getRequests().isEmpty();
                currentFloor++){
                Thread.sleep(TIME_LAPSE_BETWEEN_FLOOR);
                elevator.setCurrentFloor(currentFloor);
                logger.log(Level.INFO, "Moving FROM " + currentFloor);
                buildingCached.updatePublicElevator( elevator);

            }
            elevator.stop();
            elevator.setOpen(true);
            buildingCached.updatePublicElevator(elevator);
            logger.log(Level.INFO,"The elevator has arrived");
        }
    }

    private void moveDownTo(PublicElevator elevator) throws InterruptedException {
        while(!elevator.getPendingDown().isEmpty()){
            RequestWrapper requestWrapper = elevator.getPendingUP().pollFirst();

            if (elevator.getCurrentFloor() == requestWrapper.getSourceFloor()){
                elevator.setToTakeIt();
                break;
            }

            Request request = requestWrapper.getRequests().pollFirst();
            logger.log(Level.INFO, "The elevator is on its way down ...");
            logger.log(Level.INFO,"Moving from " + elevator.getCurrentFloor());
            elevator.setDirection(Direction.DOWN);
            elevator.setOpen(false);

            for(int currentFloor = elevator.getCurrentFloor();
                !requestWrapper.getRequests().isEmpty();
                currentFloor++){
                Thread.sleep(TIME_LAPSE_BETWEEN_FLOOR);
                elevator.setCurrentFloor(currentFloor);
                logger.log(Level.INFO, "Moving FROM " + currentFloor);
                buildingCached.updatePublicElevator( elevator);

            }
            elevator.stop();
            elevator.setOpen(true);
            buildingCached.updatePublicElevator(elevator);
            logger.log(Level.INFO,"The elevator has arrived");
        }
    }
}
