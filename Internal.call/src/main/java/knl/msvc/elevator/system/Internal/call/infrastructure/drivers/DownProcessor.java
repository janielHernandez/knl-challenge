package knl.msvc.elevator.system.Internal.call.infrastructure.drivers;

import knl.msvc.elevator.system.Internal.call.cache.BuildingCached;
import knl.msvc.elevator.system.Internal.call.domains.Direction;
import knl.msvc.elevator.system.Internal.call.domains.PublicElevator;
import knl.msvc.elevator.system.Internal.call.domains.Request;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DownProcessor implements Runnable{

    private final Logger logger = Logger.getLogger(DownProcessor.class.getName());
    private static final int TIME_LAPSE_BETWEEN_FLOOR = 5000;
    private final int desireFloor;
    private final Request request;
    private final PublicElevator elevator;
    private final BuildingCached buildingCached;

    public DownProcessor(PublicElevator elevator,
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
            moveDownTo(elevator, request, desireFloor);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
        elevator.cleanCompletedCarriage(request);
        buildingCached.updatePublicElevator(elevator);
        logger.log(Level.INFO,"The elevator has arrived to Floor: " + currentFloor);

    }
}
