package de.hka.iwii.db1.jpa.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Customer {
    @Id @GeneratedValue
    Long id;

    @NotNull
    String firstName;

    @NotNull
    String lastName;

    @NotNull
    String email;

    public Customer() {

    }
}
