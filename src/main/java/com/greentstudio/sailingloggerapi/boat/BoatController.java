package com.greentstudio.sailingloggerapi.boat;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class BoatController {
    private final BoatRepository repository;
    private final BoatRepresentationModelAssembler assembler;

    BoatController(BoatRepository repository, BoatRepresentationModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /**
     * Gets all boats and returns them in the form of a REST collection via
     * {@link BoatRepresentationModelAssembler#toCollectionModel(Iterable)}
     * using Spring Web {@link ResponseEntity}.
     */
    @GetMapping("/boats")
    public ResponseEntity<CollectionModel<EntityModel<Boat>>> findAll() {
        return ResponseEntity.ok(assembler.toCollectionModel(repository.findAll()));
    }

    /**
     * Get a single {@link Boat} matching the given id and transforms it using
     * {@link BoatRepresentationModelAssembler#toModel(Object)}.
     * @param id The id of the boat.
     * @return Returns a {@link ResponseEntity}
     */
    @GetMapping("/boats/{id}")
    public ResponseEntity<EntityModel<Boat>> findOne(@PathVariable long id) {
        return repository.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Finds the {@link Boat}'s {@link com.greentstudio.sailingloggerapi.port.Port}. Uses the id to match them.
     * @param id The id of the boat.
     * @return Returns a context-based link .
     */

    @GetMapping("/ports/{id}/boats")
    public ResponseEntity<CollectionModel<EntityModel<Boat>>> findPorts(@PathVariable long id) {

        CollectionModel<EntityModel<Boat>> collectionModel = assembler
                .toCollectionModel(repository.findByPortId(id));

        Links newLinks = collectionModel.getLinks().merge(Links.MergeMode.REPLACE_BY_REL,
                linkTo(methodOn(BoatController.class).findPorts(id)).withSelfRel());

        return ResponseEntity.ok(CollectionModel.of(collectionModel.getContent(), newLinks));
    }

}
