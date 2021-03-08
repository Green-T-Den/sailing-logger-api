package com.greentstudio.sailingloggerapi.boat;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.greentstudio.sailingloggerapi.port.PortRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoatController {
  private final BoatRepository boatRepository;
  private final PortRepository portRepository;
  private final BoatRepresentationModelAssembler assembler;

  BoatController(
      BoatRepository repository,
      PortRepository portRepository,
      BoatRepresentationModelAssembler assembler) {
    this.boatRepository = repository;
    this.portRepository = portRepository;
    this.assembler = assembler;
  }

  /**
   * Gets all boats and returns them in the form of a REST collection via {@link
   * BoatRepresentationModelAssembler#toCollectionModel(Iterable)} using Spring Web {@link
   * ResponseEntity}.
   */
  @GetMapping("/boats")
  public ResponseEntity<CollectionModel<EntityModel<Boat>>> findAll() {
    return ResponseEntity.ok(assembler.toCollectionModel(boatRepository.findAll()));
  }

  /**
   * Get a single {@link Boat} matching the given id and transforms it using {@link
   * BoatRepresentationModelAssembler#toModel(Object)}.
   *
   * @param id The id of the boat.
   * @return Returns a {@link ResponseEntity}
   */
  @GetMapping("/boats/{id}")
  public ResponseEntity<EntityModel<Boat>> findOne(@PathVariable long id) {
    return boatRepository
        .findById(id)
        .map(assembler::toModel)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Finds the {@link Boat}'s {@link com.greentstudio.sailingloggerapi.port.Port}. Uses the id to
   * match them.
   *
   * @param id The id of the boat.
   * @return Returns a context-based link .
   */
  @GetMapping("/ports/{id}/boats")
  public ResponseEntity<CollectionModel<EntityModel<Boat>>> findPorts(@PathVariable long id) {

    CollectionModel<EntityModel<Boat>> collectionModel =
        assembler.toCollectionModel(boatRepository.findByPortId(id));

    Links newLinks =
        collectionModel
            .getLinks()
            .merge(
                Links.MergeMode.REPLACE_BY_REL,
                linkTo(methodOn(BoatController.class).findPorts(id)).withSelfRel());

    return ResponseEntity.ok(CollectionModel.of(collectionModel.getContent(), newLinks));
  }

  /**
   * Saves the given Boat to the database.
   *
   * @param boat The boat to be saved.
   * @return Returns a context-based link.
   */
  @PostMapping("/ports/{port_id}/boats")
  public ResponseEntity<EntityModel<Boat>> newBoat(
      @RequestBody Boat boat, @PathVariable Long port_id) {
    boat.setPort((portRepository.findById(port_id).orElse(null)));
    Boat savedBoat = boatRepository.save(boat);

    return savedBoat
        .getId()
        .map(
            id ->
                ResponseEntity.created(linkTo(methodOn(BoatController.class).findOne(id)).toUri())
                    .body(assembler.toModel(savedBoat)))
        .orElse(ResponseEntity.notFound().build());
  }

  /*
  @PutMapping("/boats/{id}")
  public ResponseEntity<EntityModel<Boat>> replaceBoat(@RequestBody EntityModel<Boat> newBoat, @PathVariable Long id) {
      return newBoat.getId(id)
              .map(id -> ResponseEntity.created(
                      linkTo(methodOn(BoatController.class).findOne(id)).toUri()).body(assembler.toModel(newBoat)               ))
              .orElseGet(() -> {
                 newBoat.setId(id);
                 return ResponseEntity<repository.save(newBoat)>;
              });
  }
  */

}
