package com.greentstudio.sailingloggerapi.database;

import com.greentstudio.sailingloggerapi.boat.Boat;
import com.greentstudio.sailingloggerapi.boat.BoatRepository;
import com.greentstudio.sailingloggerapi.port.Port;
import com.greentstudio.sailingloggerapi.port.PortRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;

@Component
public class DatabaseLoader {

    //TODO finish DB implementation

    @Bean
    CommandLineRunner initDatabase(BoatRepository boatRepository, PortRepository portRepository) {
        return args -> {

            //Load Port of Hamburg
            Port portHamburg = portRepository.save(new Port("Hamburg"));
            Boat boatBoatyMcBoat = boatRepository.save(new Boat("BoatyMcBoat", "Green", Instant.now(), portHamburg));
            Boat boatBooat = boatRepository.save(new Boat("Booat", "Red",  Instant.now(), portHamburg));

            portHamburg.setBoats(Arrays.asList(boatBoatyMcBoat, boatBooat));
            portRepository.save(portHamburg);

            //Load Port of Rotterdam
            Port portRotterdam = portRepository.save(new Port("Rotterdam"));
            Boat boatRotti = boatRepository.save(new Boat("Rotti", "Grey", Instant.now(), portRotterdam));
            Boat boatDammi = boatRepository.save(new Boat("Dammi", "Black", Instant.now(), portRotterdam));
            portRotterdam.setBoats(Arrays.asList(boatRotti, boatDammi));
            portRepository.save(portRotterdam);
        };
    }
}
