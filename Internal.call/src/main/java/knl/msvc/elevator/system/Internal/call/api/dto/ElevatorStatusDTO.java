package knl.msvc.elevator.system.Internal.call.api.dto;


import knl.msvc.elevator.system.Internal.call.domains.Direction;
import knl.msvc.elevator.system.Internal.call.domains.RequestWrapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.TreeSet;

@Data
@NoArgsConstructor
public class ElevatorStatusDTO {

    private int currentFloor;
    private boolean isOpen;
    private boolean alarmRinging;
    private double currentWeight;
    private Direction direction;
    private double MAX_WEIGHT;
    private TreeSet<RequestWrapper> pendingUP = new TreeSet<>();
    private TreeSet<RequestWrapper> pendingDown = new TreeSet<>();
}
