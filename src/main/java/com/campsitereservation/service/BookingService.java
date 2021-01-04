package com.campsitereservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.campsitereservation.domain.Booking;


public interface BookingService {
	
	public long reserve(Booking booking);
	
	public Booking modify(Booking booking);
	
	public String cancel(long id);
	
	public List<Booking> getAllBookings();
	
	public boolean exists(long id);
	
	public Map<LocalDate, String> checkAvailability(String fromDate, String toDate);

}
