package knl.msvc.elevator.system.Internal.call.cache;


import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import knl.msvc.elevator.system.Internal.call.domains.PublicElevator;
import knl.msvc.elevator.system.Internal.call.constants.ElevatorCachedKey;
import knl.msvc.elevator.system.Internal.call.domains.ElevatorType;
import knl.msvc.elevator.system.Internal.call.domains.FreightElevator;
import knl.msvc.elevator.system.Internal.call.domains.factory.ElevatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class BuildingCached {

    @Autowired
    private RedisTemplate redisTemplate;

    Gson gsonConverter;

    @PostConstruct
    public void initBuilding(){
        gsonConverter = new Gson();
    }

    public PublicElevator getPublicElevator(){
        return gsonConverter
                    .fromJson(
                            (String) redisTemplate
                                    .opsForValue()
                                    .get(ElevatorCachedKey.PUBLIC),
                            PublicElevator.class
                    );
    }

    public FreightElevator getFreightElevator(){
        return gsonConverter
                .fromJson(
                        (String) redisTemplate
                                .opsForValue()
                                .get(ElevatorCachedKey.FREIGHT),
                        FreightElevator.class
                );
    }


    public void updatePublicElevator(PublicElevator elevator){
            redisTemplate.opsForValue().set(ElevatorCachedKey.PUBLIC,
                    gsonConverter.toJson(elevator));
    }

    public void updateFreightElevator(FreightElevator elevator){
        redisTemplate.opsForValue().set(ElevatorCachedKey.FREIGHT,
                gsonConverter.toJson(elevator));
    }

    public void createPublicElevator(){
         redisTemplate.opsForValue().set(ElevatorCachedKey.PUBLIC,
                gsonConverter.toJson(ElevatorFactory.createElevator(ElevatorType.PUBLIC)));
    }
    public void createFreightElevator(){
        redisTemplate.opsForValue().set(ElevatorCachedKey.FREIGHT,
                gsonConverter.toJson(ElevatorFactory.createElevator(ElevatorType.FREIGHT)));
    }


}
