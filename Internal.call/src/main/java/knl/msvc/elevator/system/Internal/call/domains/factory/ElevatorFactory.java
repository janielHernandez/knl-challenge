package knl.msvc.elevator.system.Internal.call.domains.factory;



import knl.msvc.elevator.system.Internal.call.domains.Elevator;
import knl.msvc.elevator.system.Internal.call.domains.ElevatorType;
import knl.msvc.elevator.system.Internal.call.domains.FreightElevator;
import knl.msvc.elevator.system.Internal.call.domains.PublicElevator;
import knl.msvc.elevator.system.Internal.call.exceptions.ElevatorFactoryException;

public class ElevatorFactory {

    public static Elevator createElevator(ElevatorType type) {
        switch (type){
            case PUBLIC: return new PublicElevator();
            case FREIGHT: return new FreightElevator();
            default: throw new ElevatorFactoryException("Could create elevator with type: " + type);
        }
    }
}
