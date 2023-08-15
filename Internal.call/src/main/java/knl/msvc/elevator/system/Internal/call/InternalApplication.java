package knl.msvc.elevator.system.Internal.call;

import jakarta.annotation.PostConstruct;
import knl.msvc.elevator.system.Internal.call.cache.BuildingCached;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InternalApplication {

	@Autowired
	private BuildingCached buildingCached;

	public static void main(String[] args) {
		SpringApplication.run(InternalApplication.class, args);
	}

	@PostConstruct
	public void initBuilding(){

		if(buildingCached.getPublicElevator() == null){
			buildingCached.createPublicElevator();
		}

		if(buildingCached.getFreightElevator() == null){
			buildingCached.createFreightElevator();
		}
	}

}
