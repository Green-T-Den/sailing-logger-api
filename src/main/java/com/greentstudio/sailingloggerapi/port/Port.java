package com.greentstudio.sailingloggerapi.port;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greentstudio.sailingloggerapi.boat.Boat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class for the Ports.
 *
 * @author Luis Martin Schick
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor // For testing purposes
public class Port {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String strName;

  @JsonIgnore // Stops serialization to avoid a recursive, bi-directional relationship
  @OneToMany(mappedBy = "port", cascade = CascadeType.ALL, orphanRemoval = true)
  // @JsonManagedReference
  private List<Boat> boats = new ArrayList<>();

  public Port(String strName) {
    this.strName = strName;
  }

  public Optional<Long> getId() {
    return Optional.ofNullable(this.id);
  }
}
