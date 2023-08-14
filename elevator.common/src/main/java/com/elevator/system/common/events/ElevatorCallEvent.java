package com.elevator.system.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class ElevatorCallEvent extends BaseEvent {

    private Date createdDate;
    private int origin;
    private int destination;

}
