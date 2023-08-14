package org.msvc.elevator.system.external.call.infrastructure.services;



import org.msvc.elevator.system.external.call.api.dto.ElevatorCall;
import org.msvc.elevator.system.external.call.domain.PublicElevatorAggregate;
import org.msvc.elevator.system.external.call.infrastructure.ElevatorEventSourcingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("public")
public class PublicElevatorCallService implements ElevatorCallService {


    @Autowired
    private ElevatorEventSourcingHandler elevatorEventSourcingHandler;

    @Override
    public ElevatorCall call(ElevatorCall request) {
        var aggregate = new PublicElevatorAggregate(request);
        elevatorEventSourcingHandler.save(aggregate);
        request.setMessage("Elevator called successfully");
        return request;
    }
}
