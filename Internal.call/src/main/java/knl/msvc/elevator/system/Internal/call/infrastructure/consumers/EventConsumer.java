package knl.msvc.elevator.system.Internal.call.infrastructure.consumers;


import com.elevator.system.common.events.FreightElevatorCallEvent;
import com.elevator.system.common.events.PublicElevatorCallEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {

     void consume(@Payload PublicElevatorCallEvent event, Acknowledgment ack) throws InterruptedException;
     void consume(@Payload FreightElevatorCallEvent event, Acknowledgment ack) throws InterruptedException;
}
