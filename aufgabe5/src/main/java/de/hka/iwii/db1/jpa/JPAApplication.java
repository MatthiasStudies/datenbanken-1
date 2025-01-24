package de.hka.iwii.db1.jpa;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.hka.iwii.db1.jpa.models.Booking;
import de.hka.iwii.db1.jpa.models.Customer;
import de.hka.iwii.db1.jpa.models.Flight;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAApplication {
	private EntityManagerFactory entityManagerFactory;

	public JPAApplication() {
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		entityManagerFactory = Persistence.createEntityManagerFactory("DB1");
	}
	
	public void createData() {
		try (var entityManager = entityManagerFactory.createEntityManager()) {
			entityManager.getTransaction().begin();

			Customer customer1 = new Customer();
			customer1.setFirstName("John");
			customer1.setLastName("Doe");
			customer1.setEmail("john@doe.com");


			Customer customer2 = new Customer();
			customer2.setFirstName("Jane");
			customer2.setLastName("Does");
			customer2.setEmail("jane@doe.com");

			entityManager.persist(customer1);
			entityManager.persist(customer2);

			Flight flight1 = new Flight();
			flight1.setNr("FL123");
			flight1.setStartAirport("Berlin");
			flight1.setTakeOff(java.time.LocalDateTime.now());

			Flight flight2 = new Flight();
			flight2.setNr("FL456");
			flight2.setStartAirport("Frankfurt");
			flight2.setTakeOff(java.time.LocalDateTime.now());

			Flight flight3 = new Flight();
			flight3.setNr("FL789");
			flight3.setStartAirport("Munich");
			flight3.setTakeOff(java.time.LocalDateTime.now());

			entityManager.persist(flight1);
			entityManager.persist(flight2);
			entityManager.persist(flight3);

			Booking booking1customer1 = new Booking();
			booking1customer1.setNumberOfSeats(2);
			booking1customer1.setDate(java.time.LocalDateTime.now());
			booking1customer1.setCustomer(customer1);
			booking1customer1.setFlight(flight1);

			Booking booking2customer1= new Booking();
			booking2customer1.setNumberOfSeats(2);
			booking2customer1.setDate(java.time.LocalDateTime.now());
			booking2customer1.setCustomer(customer1);
			booking2customer1.setFlight(flight2);

			Booking booking1customer2 = new Booking();
			booking1customer2.setNumberOfSeats(2);
			booking1customer2.setDate(java.time.LocalDateTime.now());
			booking1customer2.setCustomer(customer2);
			booking1customer2.setFlight(flight2);

			Booking booking2customer2 = new Booking();
			booking2customer2.setNumberOfSeats(2);
			booking2customer2.setDate(java.time.LocalDateTime.now());
			booking2customer2.setCustomer(customer2);
			booking2customer2.setFlight(flight3);

			entityManager.persist(booking1customer1);
			entityManager.persist(booking2customer1);
			entityManager.persist(booking1customer2);
			entityManager.persist(booking2customer2);

			entityManager.getTransaction().commit();
		}
	}

	public void printBookings(String lastName) {
		try (var entityManager = entityManagerFactory.createEntityManager()) {
			var query = entityManager.createQuery("SELECT b FROM Booking b JOIN b.customer c WHERE c.lastName = :lastName", Booking.class);
			query.setParameter("lastName", lastName);
			var bookings = query.getResultList();

			System.out.println("Found " + bookings.size() + " bookings for " + lastName + ":");

			for (var booking : bookings) {
				System.out.println(booking.format());
			}
		}
	}
	
	public static void main(String[] args) {
		JPAApplication app = new JPAApplication();
		app.createData();
		app.printBookings("Does");
	}
}
