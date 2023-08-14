package knl.msvc.elevator.system.Internal.call.infrastructure.drivers;

import knl.msvc.elevator.system.Internal.call.cache.BuildingCached;
import knl.msvc.elevator.system.Internal.call.domains.Direction;
import knl.msvc.elevator.system.Internal.call.domains.PublicElevator;
import knl.msvc.elevator.system.Internal.call.domains.Request;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UpProcessor implements Runnable{

    private final Logger logger = Logger.getLogger(UpProcessor.class.getName());
    private static final int TIME_LAPSE_BETWEEN_FLOOR = 5000;
    private final int desireFloor;
    private final Request request;
    private final PublicElevator elevator;
    private final BuildingCached buildingCached;

    public UpProcessor(PublicElevator elevator,
                       Request request,
                       int desireFloor,
                       BuildingCached buildingCached){
        this.elevator = elevator;
        this.request = request;
        this.desireFloor = desireFloor;
        this.buildingCached = buildingCached;
    }

    @Override
    public void run() {
        try {
            moveUpTo(elevator, request, desireFloor);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void moveUpTo(PublicElevator elevator, Request request, int desiredFloor) throws InterruptedException {

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
        elevator.cleanCompletedCarriage(request);
        buildingCached.updatePublicElevator(elevator);
        logger.log(Level.INFO,"The elevator has arrived to Floor: " + currentFloor);
    }
}
