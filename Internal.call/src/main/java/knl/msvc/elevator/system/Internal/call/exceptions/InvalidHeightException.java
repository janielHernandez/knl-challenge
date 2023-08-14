package knl.msvc.elevator.system.Internal.call.exceptions;

public class InvalidHeightException extends IllegalArgumentException{
    public InvalidHeightException(String message){
        super(message);
    }
}
