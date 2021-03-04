package com.greentstudio.sailingloggerapi.port;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortController {
    private final PortRepository repository;
    private final PortRepresentationModelAssembler assembler;

    public PortController(PortRepository repository, PortRepresentationModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /**
     * Gets all ports and returns them in the form of a REST collection via
     * {@link PortRepresentationModelAssembler#toCollectionModel(Iterable)}
     * using Spring Web {@link ResponseEntity}.
     */
    @GetMapping("/ports")
    public ResponseEntity<CollectionModel<EntityModel<Port>>> findAll() {

        return ResponseEntity.ok( //
                assembler.toCollectionModel(repository.findAll()));

    }

    /**
     * Get a single {@link Port} matching the given id and transforms it using
     * {@link PortRepresentationModelAssembler#toModel(Object)}.
     * @param id The id of the boat.
     * @return Returns a {@link ResponseEntity}
     */
    @GetMapping("/ports/{id}")
    public ResponseEntity<EntityModel<Port>> findOne(@PathVariable long id) {

        return repository.findById(id) //
                .map(assembler::toModel) //
                .map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Finds the {@link com.greentstudio.sailingloggerapi.boat.Boat}'s {@link com.greentstudio.sailingloggerapi.port.Port}. Uses the id to match them.
     * @param id The id of the boat.
     * @return Returns a context-based link .
     */
    @GetMapping("/boats/{id}/port")
    ResponseEntity<EntityModel<Port>> findPort(@PathVariable long id) {

        return ResponseEntity.ok( //
                assembler.toModel(repository.findByBoatsId(id)));
    }
}
