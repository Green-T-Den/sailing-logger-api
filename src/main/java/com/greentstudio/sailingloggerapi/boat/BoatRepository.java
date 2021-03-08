package com.greentstudio.sailingloggerapi.boat;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface BoatRepository extends CrudRepository<Boat, Long> {
  List<Boat> findByPortId(Long id);
}
