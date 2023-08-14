package knl.msvc.elevator.system.Internal.call.api.dto;

import com.elevator.system.common.constants.BuildingAttributes;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteriorRequestWrapper {

    @NotEmpty
    private List<@Valid InternalRequest> requesters;

    @NotNull
    @Max(BuildingAttributes.MAX_FLOOR)
    @Min(BuildingAttributes.BASEMENT_FLOOR)
    private Integer origin;
}
