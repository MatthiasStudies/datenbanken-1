package de.hka.iwii.db1.jpa.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Flight {
    @Id @NotNull
    String nr;

    @NotNull
    LocalDateTime takeOff;

    @NotNull
    String startAirport;

    public Flight() {}
}
