package com.greentstudio.sailingloggerapi.boat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greentstudio.sailingloggerapi.port.Port;
import java.time.Instant;
import java.util.Optional;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class for the Boats.
 *
 * @author Luis Martin Schick
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor // For testing purposes
public class Boat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String strName;
  private String strColor;
  private Instant instantBoatConstruction;

  @JsonIgnore // Stops serialization to avoid a recursive, bi-directional relationship
  @ManyToOne
  // @JsonBackReference
  // @NotNull(message = "You must add a port.")
  private Port port;

  public Boat(String strName, String strColor, Instant instantBoatConstruction, Port port) {
    this.strName = strName;
    this.strColor = strColor;
    this.instantBoatConstruction = instantBoatConstruction;
    this.port = port;
  }

  public Boat(String strName, String strColor, Instant instantBoatConstruction) {
    this.strName = strName;
    this.strColor = strColor;
    this.instantBoatConstruction = instantBoatConstruction;
  }

  public Optional<Long> getId() {
    return Optional.ofNullable(this.id);
  }
}
