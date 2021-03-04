package com.greentstudio.sailingloggerapi.port;

import com.greentstudio.sailingloggerapi.boat.BoatController;
import com.greentstudio.sailingloggerapi.util.RootController;
import com.greentstudio.sailingloggerapi.util.SimpleIdentifiableRepresentationModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PortRepresentationModelAssembler extends SimpleIdentifiableRepresentationModelAssembler<Port> {
    PortRepresentationModelAssembler() {
        super(PortController.class);
    }

    /**
     * Retain default links provided by {@link SimpleIdentifiableRepresentationModelAssembler},
     * but add extra ones to each {@link Port}.
     *
     * @param resource The resource to which the links will be added.
     */
    @Override
    public void addLinks(EntityModel<Port> resource) {
        super.addLinks(resource); //Keep default links

        Objects.requireNonNull(resource.getContent()).getId()
                .ifPresent(id -> {
                    //Add custom link for all corresponding boats.
                    resource.add(linkTo(methodOn(BoatController.class).findPorts(id)).withRel("boats"));
                });
    }

    /**
     * Retain default links for the entire collection, but add extra custom links for the {@link Port} collection.
     *
     * @param resources The resource to which the links will be added.
     */
    @Override
    public void addLinks(CollectionModel<EntityModel<Port>> resources) {
        super.addLinks(resources);

        resources.add(linkTo(methodOn(BoatController.class).findAll()).withRel("boats"));
        resources.add(linkTo(methodOn(RootController.class).root()).withRel("root"));

    }
}
