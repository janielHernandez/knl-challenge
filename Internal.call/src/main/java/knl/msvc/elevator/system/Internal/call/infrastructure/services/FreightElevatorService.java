package knl.msvc.elevator.system.Internal.call.infrastructure.services;

import knl.msvc.elevator.system.Internal.call.api.dto.ElevatorStatusDTO;
import knl.msvc.elevator.system.Internal.call.api.dto.InteriorRequestWrapper;
import knl.msvc.elevator.system.Internal.call.api.dto.InternalRequest;
import knl.msvc.elevator.system.Internal.call.cache.BuildingCached;
import knl.msvc.elevator.system.Internal.call.domains.FreightElevator;
import knl.msvc.elevator.system.Internal.call.domains.Location;
import knl.msvc.elevator.system.Internal.call.exceptions.InvalidWeightException;
import knl.msvc.elevator.system.Internal.call.infrastructure.drivers.FreightElevatorDriver;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static knl.msvc.elevator.system.Internal.call.validations.ValidateRequest.*;


@Service
public class FreightElevatorService {

    private final Logger logger = Logger.getLogger(FreightElevatorService.class.getName());

    @Autowired
    private BuildingCached buildingCached;

    @Autowired
    private FreightElevatorDriver elevatorDriver;

    private DozerBeanMapper mapper = new DozerBeanMapper();
    public ElevatorStatusDTO getElevatorStatus(){
        FreightElevator elevator = buildingCached.getFreightElevator();

        return mapper.map(elevator, ElevatorStatusDTO.class);
    }

    public ElevatorStatusDTO start(InteriorRequestWrapper requestWrapper) throws Exception {

        try {
            FreightElevator elevator = buildingCached.getFreightElevator();
            validate(requestWrapper, elevator);
            buildingCached.updateFreightElevator(elevator);

            for (InternalRequest ir: requestWrapper.getRequesters()){
                var request = elevator.getRequestById(ir.getId());
                request.setLocation(Location.INSIDE_ELEVATOR);
                request.setWeight(ir.getWeight());
                buildingCached.updateFreightElevator( elevator);
                elevatorDriver.run(elevator, request,request.getDestination());
            }
            return getElevatorStatus();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void validate(InteriorRequestWrapper requestWrapper, FreightElevator elevator) {
        validateHeight(requestWrapper.getOrigin(), elevator.getCurrentFloor());
        logger.info("The origin floor is valid " + requestWrapper.getOrigin());
        boolean validWeight = validateWeight(requestWrapper.getRequesters(), elevator);
        if (!validWeight){
            buildingCached.updateFreightElevator(elevator);
            throw new InvalidWeightException("Weight Limit Exceeded, try again with less weight");
        }
        logger.info("The sum of weight is valid " + elevator.getCurrentWeight());
        validateIDs(requestWrapper, elevator);
    }
}
