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

    @ManyToOne
    Flight flight;

    public Booking() {}

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String format() {
        return String.format("Booking %d: %s, %d booked seats, name: %s %s, flight: %s from %s at %s", id, date, count, customer.getFirstName(), customer.getLastName(), flight.getNr(), flight.getStartAirport(), flight.getTakeOff());
    }
}
