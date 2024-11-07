package de.hka.iwii.db1.jpa.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Booking {
    @Id @GeneratedValue
    Long id;

    @Min(1)
    int count;

    @NotNull
    LocalDateTime date;

    @ManyToOne
    Customer customer;

    public Booking() {}
}
