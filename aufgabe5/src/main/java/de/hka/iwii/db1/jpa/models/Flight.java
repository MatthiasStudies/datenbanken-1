package de.hka.iwii.db1.jpa.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Flight {
    @Id @NotNull
    String nr;

    @NotNull @Temporal(TemporalType.TIME)
    LocalDateTime takeOff;

    @NotNull
    String startAirport;

    public Flight() {}

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public LocalDateTime getTakeOff() {
        return takeOff;
    }

    public void setTakeOff(LocalDateTime takeOff) {
        this.takeOff = takeOff;
    }

    public String getStartAirport() {
        return startAirport;
    }

    public void setStartAirport(String startAirport) {
        this.startAirport = startAirport;
    }
}
