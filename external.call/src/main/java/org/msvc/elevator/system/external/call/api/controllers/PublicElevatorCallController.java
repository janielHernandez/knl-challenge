package org.msvc.elevator.system.external.call.api.controllers;

import com.elevator.system.common.constants.BuildingAttributes;
import com.elevator.system.common.dto.BaseResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.msvc.elevator.system.external.call.api.dto.ElevatorCall;
import org.msvc.elevator.system.external.call.infrastructure.services.ElevatorCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = PublicElevatorCallController.URL)
@Validated
public class PublicElevatorCallController{

    public static final String URL = "/api/v1/call/public";

    private final Logger logger = Logger.getLogger(PublicElevatorCallController.class.getName());

    @Autowired
    @Qualifier("public")
    private ElevatorCallService service;

    @PostMapping(path = "/{destination}")
    public ResponseEntity<?> callPublicElevator(@Valid @RequestBody ElevatorCall request,
                                                BindingResult result,
                                                @Max(BuildingAttributes.MAX_FLOOR)
                                                @Min(BuildingAttributes.BASEMENT_FLOOR)
                                                @NotNull
                                                @PathVariable Integer destination){

        logger.log(Level.INFO,"Request received from public elevator " + request);

        if (result.hasErrors()){
            return raiseError(result);
        }

        var id = UUID.randomUUID().toString();
        try{
            request.setId(id);
            request.setDestination(destination);
            ElevatorCall response = service.call(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (IllegalStateException e){
            logger.log(Level.WARNING, MessageFormat.format("Could not call elevator - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            var safeErrorMessage = MessageFormat.format("Error processing request - {0}", id);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            request.setMessage(safeErrorMessage);
            return new ResponseEntity<>(request, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private  ResponseEntity<Map<String, String>> raiseError(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(
                err -> errors.put(err.getField(), err.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

}
