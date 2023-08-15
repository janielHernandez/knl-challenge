package knl.msvc.elevator.system.Internal.call.infrastructure.drivers;

import knl.msvc.elevator.system.Internal.call.cache.BuildingCached;
import knl.msvc.elevator.system.Internal.call.domains.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PublicElevatorDriver {

    private static final int TIME_LAPSE_BETWEEN_FLOOR = 1000;

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
                    moveUpTo(elevator, r, desiredFloor);
                } else {
                    moveDownTo(elevator, r, desiredFloor);
                }
            }
        }
    }

    private void moveDownTo(PublicElevator elevator, Request request, int desiredFloor) throws InterruptedException {

        elevator.setDirection(Direction.DOWN);
        elevator.setOpen(false);
        buildingCached.updatePublicElevator(elevator);


        var currentFloor = elevator.getCurrentFloor();
        logger.log(Level.INFO, "The elevator is on its way down ...");
        while(desiredFloor < currentFloor){
            logger.log(Level.INFO, "Current Floor: " + currentFloor);
            Thread.sleep(TIME_LAPSE_BETWEEN_FLOOR);
            currentFloor--;
            elevator.setCurrentFloor(currentFloor);
            buildingCached.updatePublicElevator( elevator);

            elevator =  buildingCached.getPublicElevator(); //get elevator updated
            //if there are other request on way down interrupt
            if (!elevator.getPendingDown().isEmpty() && elevator.anyPendingRequest( currentFloor, elevator.getPendingDown() )){
                logger.warning("The elevator was interrupted in the way down by other request in floor " + currentFloor);
                break;
            }
        }
        elevator.stop();
        elevator.setOpen(true);
        elevator.cleanCompletedDownCarriage(request);
        buildingCached.updatePublicElevator(elevator);
        logger.log(Level.INFO,"The elevator has arrived to Floor: " + currentFloor);

    }

    private void moveUpTo(PublicElevator elevator, Request request, int desiredFloor) throws InterruptedException {

        elevator = buildingCached.getPublicElevator();

        var currentFloor = elevator.getCurrentFloor();
        elevator.setDirection(Direction.UP);
        elevator.setOpen(false);
        buildingCached.updatePublicElevator(elevator);

        logger.log(Level.INFO, "The elevator is on its way up ...");
        while(desiredFloor > currentFloor){
            logger.log(Level.INFO, "Current Floor: " + currentFloor);
            Thread.sleep(TIME_LAPSE_BETWEEN_FLOOR);
            currentFloor++;
            elevator.setCurrentFloor(currentFloor);
            buildingCached.updatePublicElevator( elevator);

            elevator =  buildingCached.getPublicElevator(); //get elevator updated
            //if there are other request on way up interrupt
            if (elevator.anyPendingRequest( currentFloor, elevator.getPendingUP() )){
                logger.warning("The elevator was interrupted in the way up by other request in floor " + currentFloor);
                break;
            }
        }
        elevator.stop();
        elevator.setOpen(true);
        elevator.cleanCompletedUPCarriage(request);
        buildingCached.updatePublicElevator(elevator);
        logger.log(Level.INFO,"The elevator has arrived to Floor: " + currentFloor);
    }

}
