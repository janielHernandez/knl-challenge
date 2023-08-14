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
public class ExternalRequest implements Comparable<ExternalRequest>{

    private String id;
    private boolean hasKeyCard;
    private int origin;
    private int destination;
    private Direction direction;
    private InternalRequest internalRequest;


    @Override
    public int compareTo(ExternalRequest o) {
        return this.destination - o.getDestination();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternalRequest that = (ExternalRequest) o;
        return Objects.equals(id, that.id);
    }

}
