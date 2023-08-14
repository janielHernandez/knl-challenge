package knl.msvc.elevator.system.Internal.call.exceptions;

public class InvalidWeightException extends IllegalArgumentException{
    public InvalidWeightException(String message){
        super(message);
    }
}
