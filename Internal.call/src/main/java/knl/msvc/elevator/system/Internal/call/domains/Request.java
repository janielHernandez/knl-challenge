package knl.msvc.elevator.system.Internal.call.domains;


import knl.msvc.elevator.system.Internal.call.api.dto.InternalRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Data
@SuperBuilder
@NoArgsConstructor
@ToString
public class Request implements Comparable<Request>{

    private String id;
    private boolean hasKeyCard;
    private int origin;
    private int destination;
    private Direction direction;
    private Double weight;
    private Location location;


    @Override
    public int compareTo(Request o) {
        return this.destination - o.getDestination();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request that = (Request) o;
        return Objects.equals(id, that.id);
    }

}
