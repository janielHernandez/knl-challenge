package org.msvc.elevator.system.external.call.domain;


import com.elevator.system.common.events.BaseEvent;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "eventStore")
public class ElevatorEventModel {

    @Id
    private String id;
    private Date timeStamp;
    private int version;
    private String eventType;
    private BaseEvent baseEvent;
    private String aggregateIdentifier;
    private String aggregateType;

}
