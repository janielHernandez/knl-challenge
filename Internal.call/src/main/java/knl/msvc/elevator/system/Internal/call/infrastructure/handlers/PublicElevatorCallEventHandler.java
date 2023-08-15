package knl.msvc.elevator.system.Internal.call.infrastructure.handlers;

import com.elevator.system.common.events.PublicElevatorCallEvent;
import knl.msvc.elevator.system.Internal.call.cache.BuildingCached;
import knl.msvc.elevator.system.Internal.call.domains.*;
import knl.msvc.elevator.system.Internal.call.infrastructure.drivers.PublicElevatorDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class PublicElevatorCallEventHandler extends ElevatorCallHandler{

    private final Logger logger = Logger.getLogger(PublicElevatorCallEventHandler.class.getName());

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BuildingCached buildingCached;

    @Autowired
    private PublicElevatorDriver elevatorDriver;


    public void handle(PublicElevatorCallEvent event) throws InterruptedException {

        logger.info("External Request received " + event.toString());

        PublicElevator elevator = buildingCached.getPublicElevator();

        Request r = mapExternalEventToRequest(event);
        r.setDirection(r.getDestination()>r.getOrigin()? Direction.UP: Direction.DOWN);
        r.setHasKeyCard(event.isHasKeyCard());


        addRequest(elevator, r);

        if (elevator.getCurrentWeight()==0.0 && elevator.getDirection()==Direction.NONE)
            elevatorDriver.run(elevator, r, r.getOrigin());
    }

    private void addRequest(PublicElevator elevator, Request r) {

        if (r.getDirection() == Direction.UP){
            elevator.addRequestTo(r, elevator.getPendingUP());
            logger.info("New Request added to Pending Up List");
        } else {
            elevator.addRequestTo(r, elevator.getPendingDown());
            logger.info("New Request added to Pending Down List");
        }
        buildingCached.updatePublicElevator(elevator);
    }


}
