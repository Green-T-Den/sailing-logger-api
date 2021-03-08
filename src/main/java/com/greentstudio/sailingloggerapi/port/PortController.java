package com.greentstudio.sailingloggerapi.port;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class PortController {
  private final PortRepository repository;
  private final PortRepresentationModelAssembler assembler;

  /**
   * {@link RestController} makes the {@link PortController} return responses as JSON instead of
   * templates.
   *
   * @param repository The {@link PortRepository} which will be injected via constructor injection.
   * @param assembler The {@link PortRepresentationModelAssembler} that will be injected via
   *     constructor injection.
   */
  public PortController(PortRepository repository, PortRepresentationModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  /**
   * Gets all ports and returns them in the form of a REST collection via {@link
   * PortRepresentationModelAssembler#toCollectionModel(Iterable)} using Spring Web {@link
   * ResponseEntity}.
   */
  @GetMapping("/ports")
  public ResponseEntity<CollectionModel<EntityModel<Port>>> findAll() {

        return ResponseEntity.ok(
        assembler.toCollectionModel(repository.findAll()));
  }

  /**
   * Get a single {@link Port} matching the given id and transforms it using {@link
   * PortRepresentationModelAssembler#toModel(Object)}.
   *
   * @param id The id of the boat.
   * @return Returns a {@link ResponseEntity}
   */
  @GetMapping("/ports/{id}")
  public ResponseEntity<EntityModel<Port>> findOne(@PathVariable long id) {


    return repository
        .findById(id) //
        .map(assembler::toModel) //
        .map(ResponseEntity::ok) //
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Finds the {@link com.greentstudio.sailingloggerapi.boat.Boat}'s {@link
   * com.greentstudio.sailingloggerapi.port.Port}. Uses the id to match them.
   *
   * @param id The id of the boat.
   * @return Returns a context-based link .
   */
  @GetMapping("/boats/{id}/port")
  public ResponseEntity<EntityModel<Port>> findPort(@PathVariable long id) {

        return ResponseEntity.ok(
        assembler.toModel(repository.findByBoatsId(id)));
  }
  /**
   * Saves the given port to the database.
   *
   * @param port The port to be saved.
   * @return Returns a context-based link.
   */
  @PostMapping("/ports")
    public ResponseEntity<EntityModel<Port>> newPort(@Valid @RequestBody Port port) {
    Port savedPort = repository.save(port);

    return savedPort
        .getId()
        .map(
            id ->
                ResponseEntity.created(linkTo(methodOn(PortController.class).findOne(id)).toUri())
                    .body(assembler.toModel(savedPort)))
        .orElse(ResponseEntity.notFound().build());
  }
}
