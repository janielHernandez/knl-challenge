package org.msvc.elevator.system.external.call.api.dto;

import com.elevator.system.common.constants.BuildingAttributes;
import com.elevator.system.common.dto.BaseResponse;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ElevatorCall extends BaseResponse {

    private String id;

    @NotNull
    @Min(BuildingAttributes.BASEMENT_FLOOR)
    @Max(BuildingAttributes.MAX_FLOOR)
    private Integer origin;

    private Integer destination;

}
