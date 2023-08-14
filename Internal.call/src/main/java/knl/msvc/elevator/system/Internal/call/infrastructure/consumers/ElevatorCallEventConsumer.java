package knl.msvc.elevator.system.Internal.call.infrastructure.consumers;



import com.elevator.system.common.events.FreightElevatorCallEvent;
import com.elevator.system.common.events.PublicElevatorCallEvent;
import knl.msvc.elevator.system.Internal.call.infrastructure.handlers.FreightElevatorCallEventHandler;
import knl.msvc.elevator.system.Internal.call.infrastructure.handlers.PublicElevatorCallEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class ElevatorCallEventConsumer implements EventConsumer{

    @Autowired
    private PublicElevatorCallEventHandler publicElevatorCallEventHandler;

    @Autowired
    private FreightElevatorCallEventHandler freightElevatorCallEventHandler;

    @KafkaListener(topics = "PublicElevatorCallEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(PublicElevatorCallEvent event, Acknowledgment ack) throws InterruptedException {
        publicElevatorCallEventHandler.handle(event);
        ack.acknowledge();
    }

    @KafkaListener(topics = "FreightElevatorCallEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FreightElevatorCallEvent event, Acknowledgment ack) throws InterruptedException {
        freightElevatorCallEventHandler.handle(event);
        ack.acknowledge();
    }

}
