package com.greentstudio.sailingloggerapi.boat;

import com.greentstudio.sailingloggerapi.port.PortController;
import com.greentstudio.sailingloggerapi.util.RootController;
import com.greentstudio.sailingloggerapi.util.SimpleIdentifiableRepresentationModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BoatRepresentationModelAssembler extends SimpleIdentifiableRepresentationModelAssembler<Boat> {

    BoatRepresentationModelAssembler() {
        super(BoatController.class);
    }

    /**
     * All {@link EntityModel} have these links.
     *
     * @param resource The resource to which the links will be added.
     */
    @Override
    public void addLinks(EntityModel<Boat> resource) {


        //Adds custom links to the default ones.

        super.addLinks(resource);
        Objects.requireNonNull(resource.getContent()).getId()
                .ifPresent(id -> { //
                    // Add additional link(s)
                    resource.add(linkTo(methodOn(PortController.class).findOne(id)).withRel("port"));
                });
    }

    /**
     * Define links to add to {@link CollectionModel} collection.
     *
     * @param resources The resource to which the links will be added.
     */
    @Override
    public void addLinks(CollectionModel<EntityModel<Boat>> resources) {

        super.addLinks(resources);

        resources.add(linkTo(methodOn(PortController.class).findAll()).withRel("ports"));
        resources.add(linkTo(methodOn(RootController.class).root()).withRel("root"));
    }
}
