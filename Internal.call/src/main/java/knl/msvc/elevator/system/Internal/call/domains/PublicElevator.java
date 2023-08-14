package knl.msvc.elevator.system.Internal.call.domains;

import com.elevator.system.common.constants.BuildingAttributes;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.TreeSet;

@NoArgsConstructor
@Data
public class PublicElevator extends Elevator{

    private final double MAX_WEIGHT = BuildingAttributes.MAX_WEIGHT_PUBLIC_ELEVATOR;
    @Override
    public boolean weightLimitExceeded() {
        return super.getCurrentWeight() > MAX_WEIGHT;
    }
}
