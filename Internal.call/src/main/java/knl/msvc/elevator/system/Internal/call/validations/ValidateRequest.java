package knl.msvc.elevator.system.Internal.call.validations;



import knl.msvc.elevator.system.Internal.call.api.dto.InternalRequest;
import knl.msvc.elevator.system.Internal.call.api.dto.InteriorRequestWrapper;
import knl.msvc.elevator.system.Internal.call.domains.Elevator;
import knl.msvc.elevator.system.Internal.call.exceptions.InvalidHeightException;
import knl.msvc.elevator.system.Internal.call.exceptions.InvalidIdException;

import java.util.List;

public class ValidateRequest {

    public static void validateHeight(int origin, int currentFloor) {
        if (origin != currentFloor){
            throw new InvalidHeightException("Cannot take Elevator, right now is in "+ currentFloor +" floor");
        }
    }

    public static boolean validateWeight(List<InternalRequest> requesters,
                                            Elevator elevator) {
        elevator.setCurrentWeight(getSumOfWeight(requesters));
        if (elevator.weightLimitExceeded()){
            elevator.stop();
            elevator.setAlarmRinging(true);
            return false;
        } else {
            elevator.setAlarmRinging(false);
            return true;
        }
    }

    private static double getSumOfWeight(List<InternalRequest> requesters) {
        return requesters.stream()
                .mapToDouble(req -> req.getWeight())
                .sum();
    }

    public static void validateIDs(InteriorRequestWrapper requestWrapper, Elevator elevator) {

        for (InternalRequest internal: requestWrapper.getRequesters()){
            if(!elevator.idExist(internal.getId())){
              throw new InvalidIdException("This id don't exist, invalid request. ID: " + internal.getId());
            }
        }
    }

}
