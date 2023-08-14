package knl.msvc.elevator.system.Internal.call.services;

import knl.msvc.elevator.system.Internal.call.api.dto.InternalRequest;
import knl.msvc.elevator.system.Internal.call.domains.*;
import knl.msvc.elevator.system.Internal.call.infrastructure.drivers.PublicElevatorDriver;
import org.dozer.DozerBeanMapper;
import knl.msvc.elevator.system.Internal.call.api.dto.ElevatorStatusDTO;
import knl.msvc.elevator.system.Internal.call.api.dto.InteriorRequestWrapper;
import knl.msvc.elevator.system.Internal.call.cache.BuildingCached;
import knl.msvc.elevator.system.Internal.call.exceptions.InvalidWeightException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.TreeSet;
import java.util.logging.Logger;

import static knl.msvc.elevator.system.Internal.call.validations.ValidateRequest.*;


@Service
public class PublicElevatorService {

    private final Logger logger = Logger.getLogger(PublicElevatorService.class.getName());

    @Autowired
    private BuildingCached buildingCached;

    @Autowired
    private PublicElevatorDriver elevatorDriver;

    private DozerBeanMapper mapper = new DozerBeanMapper();
    public ElevatorStatusDTO getElevatorStatus(){
        PublicElevator elevator = buildingCached.getPublicElevator();
        return mapper.map(elevator, ElevatorStatusDTO.class);
    }

    public ElevatorStatusDTO start(InteriorRequestWrapper requestWrapper) throws Exception {

        try {
            PublicElevator elevator = buildingCached.getPublicElevator();
            validate(requestWrapper, elevator);
            buildingCached.updatePublicElevator(elevator);

            for (InternalRequest ir: requestWrapper.getRequesters()){
                var request = elevator.getRequestById(ir.getId());
                request.setLocation(Location.INSIDE_ELEVATOR);
                request.setWeight(ir.getWeight());
                buildingCached.updatePublicElevator( elevator);
                elevatorDriver.run(elevator, request,request.getDestination());
            }
            return getElevatorStatus();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void validate(InteriorRequestWrapper requestWrapper, PublicElevator elevator) {
        validateHeight(requestWrapper.getOrigin(), elevator.getCurrentFloor());
        logger.info("The origin floor is valid " + requestWrapper.getOrigin());
        boolean validWeight = validateWeight(requestWrapper.getRequesters(), elevator);
        if (!validWeight){
            buildingCached.updatePublicElevator(elevator);
            throw new InvalidWeightException("Weight Limit Exceeded, try again with less weight");
        }
        logger.info("The sum of weight is valid " + elevator.getCurrentWeight());
        validateIDs(requestWrapper, elevator);
    }
}
