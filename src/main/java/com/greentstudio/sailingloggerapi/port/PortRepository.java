package com.greentstudio.sailingloggerapi.port;

import org.springframework.data.repository.CrudRepository;

public interface PortRepository extends CrudRepository<Port, Long> {
    Port findByBoatsId(Long id);
}
