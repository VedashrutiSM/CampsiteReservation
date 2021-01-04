package com.campsitereservation.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        name="Booking", 
        uniqueConstraints=
            @UniqueConstraint(columnNames={"arrivalDate", "departureDate"})
    )
public class Booking {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long bookingId;
	
	@NotNull(message = "Name is required")
	@NotEmpty (message = "Please enter fullname")
	@NotBlank(message = "Please enter fullname")
	private String fullName;
	
	@NotNull(message = "Email is required")
	@NotEmpty(message = "Please enter email id")
	@NotBlank(message = "Please enter email id")
	private String email;
	
	@NotNull(message = "ArrivalDate is required")
	@NotEmpty(message = "Please enter arrival date")
	@NotBlank(message = "Please enter arrival date")
	//@Column(unique = true)
	private String arrivalDate;
	
	@NotNull(message = "DepartureDate is required")
	@NotEmpty(message = "Please enter departure date")
	@NotBlank(message = "Please enter departure date")
	//@Column(unique = true)
	private String departureDate;
	
	public long getBookingId() {
		return bookingId;
	}
	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public String getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	
	public Booking() {}
	
	public Booking(
			@NotNull(message = "Name is required") @NotEmpty(message = "Please enter fullname") @NotBlank(message = "Please enter fullname") String fullName,
			@NotNull(message = "Email is required") @NotEmpty(message = "Please enter email id") @NotBlank(message = "Please enter email id") String email,
			@NotNull(message = "ArrivalDate is required") @NotEmpty(message = "Please enter arrival date") @NotBlank(message = "Please enter arrival date") String arrivalDate,
			@NotNull(message = "DepartureDate is required") @NotEmpty(message = "Please enter departure date") @NotBlank(message = "Please enter departure date") String departureDate) {
		super();
		this.fullName = fullName;
		this.email = email;
		this.arrivalDate = arrivalDate;
		this.departureDate = departureDate;
	}
	public Booking(long bookingId, @NotNull(message = "Name is required") String fullName,
			@NotNull(message = "Email is required") String email,
			@NotNull(message = "ArrivalDate is required") String arrivalDate,
			@NotNull(message = "DepartureDate is required") String departureDate) {
		super();
		this.bookingId = bookingId;
		this.fullName = fullName;
		this.email = email;
		this.arrivalDate = arrivalDate;
		this.departureDate = departureDate;
	}

	
	
}
