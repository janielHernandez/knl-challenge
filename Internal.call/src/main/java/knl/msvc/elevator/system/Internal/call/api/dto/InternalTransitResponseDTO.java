package knl.msvc.elevator.system.Internal.call.api.dto;

import com.elevator.system.common.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InternalTransitResponseDTO extends BaseResponse {
    private String id;
}
