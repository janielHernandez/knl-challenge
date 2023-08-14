package org.msvc.elevator.system.external.call.infrastructure.services;


import org.msvc.elevator.system.external.call.api.dto.ElevatorCall;

public interface ElevatorCallService {
    ElevatorCall call(ElevatorCall request);
}
