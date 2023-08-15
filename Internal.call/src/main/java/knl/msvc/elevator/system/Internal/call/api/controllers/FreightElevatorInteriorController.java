package knl.msvc.elevator.system.Internal.call.api.controllers;


import com.elevator.system.common.validations.raisers.RaiserErrorController;
import jakarta.validation.Valid;
import knl.msvc.elevator.system.Internal.call.api.dto.ElevatorStatusDTO;
import knl.msvc.elevator.system.Internal.call.api.dto.InteriorRequestWrapper;
import knl.msvc.elevator.system.Internal.call.infrastructure.services.PublicElevatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/elevator/freight")
@Validated
public class FreightElevatorInteriorController extends RaiserErrorController {
    private final Logger logger = Logger.getLogger(FreightElevatorInteriorController.class.getName());


    @Autowired
    private PublicElevatorService service;


    @GetMapping("/status")
    public ResponseEntity<?> getStatus(){

        try{
            ElevatorStatusDTO status= service.getElevatorStatus();
            return ResponseEntity.ok(status);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Error Getting Elevator Status");
        }
    }

    @PostMapping
    public ResponseEntity<?> transport(@Valid @RequestBody InteriorRequestWrapper request, BindingResult result){

        logger.log(Level.INFO,"Request received public elevator " + request);

        if (result.hasErrors()){
            return raiseError(result);
        }

        try {
            ElevatorStatusDTO status = service.start(request);
            return ResponseEntity.ok().body(status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
