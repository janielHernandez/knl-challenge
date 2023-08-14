package knl.msvc.elevator.system.Internal.call.api.dto;

import com.elevator.system.common.constants.BuildingAttributes;
import com.elevator.system.common.dto.BaseResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import knl.msvc.elevator.system.Internal.call.domains.Direction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InternalRequest extends BaseResponse {

    @NotNull
    private String id;

    @Max(BuildingAttributes.MAX_FLOOR)
    @Min(BuildingAttributes.BASEMENT_FLOOR)
    private Integer destination;

    @NotNull
    private Double weight;

}
