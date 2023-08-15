package knl.msvc.elevator.system.Internal.call.domains;



import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.Optional;
import java.util.TreeSet;

@Data
@Log4j2
public abstract class Elevator {


    private int currentFloor = 0;
    private double currentWeight = 0.0;
    private boolean isOpen = false;
    private boolean alarmRinging = false;
    private Direction direction = Direction.NONE;
    private TreeSet<RequestWrapper> pendingUP = new TreeSet<>();
    private TreeSet<RequestWrapper> pendingDown = new TreeSet<>();

    public Optional<RequestWrapper> getRequestFrom(int source, TreeSet<RequestWrapper> pending){
        return pending.stream()
                .filter(req->req.getSourceFloor()==source)
                .findAny();
    }

    public boolean anyPendingRequest(int source, TreeSet<RequestWrapper> pending){
        var existPending = pending.stream().anyMatch(req->req.getSourceFloor()==source);
        log.info("Value -> " + existPending + " for list -> " + pending +" and source " + source);
        return existPending;
    }


    //delete completed request
    public void cleanCompletedUPCarriage(Request request) {

        if (!this.pendingUP.isEmpty()) {
            var wrapper = getRequestFrom(request.getOrigin(), this.pendingUP);
            if(wrapper.isPresent()){
                removeRequestAndWeight(wrapper.get());
                removeWrapperFromPendingTree(wrapper.get(), this.pendingUP);
            }
        }
    }

    public void cleanCompletedDownCarriage(Request request) {

        if (!this.pendingDown.isEmpty()){
            var wrapper = getRequestFrom(request.getOrigin(), this.pendingDown);
            if(wrapper.isPresent()){
                removeRequestAndWeight(wrapper.get());
                removeWrapperFromPendingTree(wrapper.get(), this.pendingDown);
            }
        }
    }


    private void removeWrapperFromPendingTree(RequestWrapper wrapper, TreeSet<RequestWrapper> pending) {
        if (wrapper.getRequests().isEmpty())
            pending.remove(wrapper);
    }

    private void removeRequestAndWeight(RequestWrapper wrapper) {
        wrapper.getRequests().removeIf(r -> {
            if (r.getDestination() == this.currentFloor) {
                this.currentWeight -= r.getWeight();
                return true;
            }
            return false;
        });

    }


    public void stop(){
        log.info("Elevator has been stopped..");
        this.direction = Direction.NONE;
    }
    public void setToTakeIt(){
        setDirection(Direction.NONE);
        setOpen(true);
        setAlarmRinging(false);
    }

    public  void addRequestTo(Request r, TreeSet<RequestWrapper> pending) {
        boolean requestedBefore = requestedBefore(r.getOrigin(), pending);

        if (!requestedBefore){
            pending.add(createRequest(r));
        } else {
            this.getRequestFrom(r.getOrigin(), pending).orElseThrow().addRequest(r);
        }
    }


    private static RequestWrapper createRequest(Request r) {
        return RequestWrapper.builder()
                .sourceFloor(r.getOrigin())
                .requests(new TreeSet( Arrays.asList(r) ))
                .build();
    }

    public void setOpen(boolean isOpen){
        this.isOpen = isOpen;
    }

    private boolean requestedBefore(int sourceFloor, TreeSet<RequestWrapper> pending) {
        return pending
                .stream()
                .anyMatch(r -> r.getSourceFloor() == sourceFloor);
    }

    public abstract boolean weightLimitExceeded();

    public boolean isPreparedToTakeIt() {
        return isOpen() && getDirection() == Direction.NONE && !isAlarmRinging();
    }


    public boolean idExist(String id) {
        return this.pendingUP.stream().anyMatch(pd -> pd.validateIDExistence(id))
                || this.pendingDown.stream().anyMatch(pd -> pd.validateIDExistence(id));
    }

    public Request getRequestById(String id){
        var request = this.pendingUP.stream()
                .flatMap(pu->pu.getRequests().stream())
                .filter(r->r.getId().equals(id))
                .findFirst();

        if(request.isPresent()){
            return request.get();
        }

        request = this.pendingDown.stream()
                .flatMap(pu->pu.getRequests().stream())
                .filter(r->r.getId().equals(id))
                .findFirst();

        if(request.isPresent()){
            return request.get();
        }
        return new Request();
    }
}
