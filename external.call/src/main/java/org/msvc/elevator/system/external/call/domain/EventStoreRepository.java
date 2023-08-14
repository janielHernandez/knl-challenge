package org.msvc.elevator.system.external.call.domain;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventStoreRepository
        extends MongoRepository<ElevatorEventModel, String> {
    List<ElevatorEventModel> findByAggregateIdentifier(String id);
}
