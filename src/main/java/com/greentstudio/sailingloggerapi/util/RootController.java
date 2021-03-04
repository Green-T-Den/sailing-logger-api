package com.greentstudio.sailingloggerapi.util;

import com.greentstudio.sailingloggerapi.boat.BoatController;
import com.greentstudio.sailingloggerapi.port.PortController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<RepresentationModel> root() {
        RepresentationModel model = new RepresentationModel();

        model.add(linkTo(methodOn(RootController.class).root()).withSelfRel());
        model.add(linkTo(methodOn(BoatController.class).findAll()).withRel("boats"));
        model.add(linkTo(methodOn(PortController.class).findAll()).withRel("ports"));

        return ResponseEntity.ok(model);
    }
}
