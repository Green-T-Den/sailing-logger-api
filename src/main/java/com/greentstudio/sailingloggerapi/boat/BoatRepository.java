package com.greentstudio.sailingloggerapi.boat;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoatRepository extends CrudRepository<Boat, Long> {
    List<Boat> findByPortId(Long id);
}
