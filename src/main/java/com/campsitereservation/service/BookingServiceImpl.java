package com.campsitereservation.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.campsitereservation.domain.Booking;
import com.campsitereservation.exception.UnAvailableException;
import com.campsitereservation.repository.BookingRepository;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	BookingRepository bookingRepository;

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	@CachePut(value = "bookings", condition = "#result != null", key = "#booking.id")
	public synchronized long reserve(Booking booking) {

		Booking bookings = new Booking();

		if (isAvailable(booking.getArrivalDate(), booking.getDepartureDate())) {
			bookings = bookingRepository.save(booking);
			return bookings.getBookingId();
		}else {
			throw new UnAvailableException();
		}
		
	}

	@Override
	@Transactional
	@CachePut(value = "bookings", condition = "#result != null", key = "#booking.id")
	public Booking modify(Booking booking) {

		if (exists(booking.getBookingId())) {
			
			if (isAvailable(booking.getArrivalDate(), booking.getDepartureDate())) {				
				Booking bookings = bookingRepository.save(booking);
				return bookings;
			}else {
				throw new UnAvailableException();
			}
			
		} else {
			return null;
		}
	}

	@Override
	@CacheEvict(value = "bookings", condition = "#result == success", key = "#id")
	public String cancel(long id) {
		if (exists(id)) {
			bookingRepository.deleteById(id);
			return "success";
		} else {
			return "fail";
		}
	}

	@Override
	@Cacheable("bookings")
	public List<Booking> getAllBookings() {

		List<Booking> list = bookingRepository.findAll();

		Comparator<Booking> sortByDate = new Comparator<Booking>() {

			@Override
			public int compare(Booking o1, Booking o2) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				LocalDate arrivalDate1 = LocalDate.parse(o1.getArrivalDate(), formatter);
				LocalDate arrivalDate2 = LocalDate.parse(o2.getArrivalDate(), formatter);
				return arrivalDate1.compareTo(arrivalDate2);
			}
		};

		list.sort(sortByDate);
		return list;
	}

	@Override
	public boolean exists(long id) {
		return bookingRepository.existsById(id);
	}

	@Override
	public Map<LocalDate, String> checkAvailability(String fromDate, String toDate) {

		// List<Map<LocalDate, String>> list = new ArrayList<Map<LocalDate,String>>();

		Map<LocalDate, String> map = new TreeMap<LocalDate, String>();

		List<Booking> bookingList = getAllBookings();

		List<LocalDate> datesList = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		for (Booking b : bookingList) {

			LocalDate arrival = LocalDate.parse(b.getArrivalDate(), formatter);
			LocalDate departure = LocalDate.parse(b.getDepartureDate(), formatter);
			while (departure.isAfter(arrival)) {
				datesList.add(arrival);
				arrival = arrival.plusDays(1);
			}
			datesList.add(departure);
		}

		// if the range is not given then provide the availability for 1 month
		if (fromDate == null && toDate == null) {

			LocalDate d = LocalDate.now();
			for (int i = 0; i < 30; i++) {

				if (datesList.contains(d)) {
					map.put(d, "Booked");
				} else {
					map.put(d, "Available");
				}
				d = d.plusDays(1);
			}
		} // if the range is provided then provide the availability of given range
		else if (fromDate != null && toDate != null) {

			LocalDate fromdate = LocalDate.parse(fromDate, formatter);
			LocalDate todate = LocalDate.parse(toDate, formatter);

			while (todate.isAfter(fromdate)) {

				if (datesList.contains(fromdate)) {
					map.put(fromdate, "Booked");
				} else {
					map.put(fromdate, "Available");
				}
				fromdate = fromdate.plusDays(1);
			}

			if (datesList.contains(todate)) {
				map.put(todate, "Booked");
			} else {
				map.put(todate, "Available");
			}

		}
		return map;
	}

	public boolean isAvailable(String fromDate, String toDate) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		List<Booking> list = getAllBookings();
		List<LocalDate> datesList = new ArrayList<>();

		for (Booking b : list) {

			LocalDate arrival = LocalDate.parse(b.getArrivalDate(), formatter);
			LocalDate departure = LocalDate.parse(b.getDepartureDate(), formatter);
			while (departure.isAfter(arrival)) {
				datesList.add(arrival);
				arrival = arrival.plusDays(1);
			}
			datesList.add(departure);
			
		}
		
		LocalDate fromdate = LocalDate.parse(fromDate, formatter);
		LocalDate todate = LocalDate.parse(toDate, formatter);
		Period p = Period.between(fromdate, todate);
		
		if(fromdate.isEqual(todate)) {
			if(datesList.contains(fromdate)) {
				return false;
			}else {
				return true;
			}
		}
		
		for(int i=0; i<p.getDays(); i++) {
			
			if(datesList.contains(fromdate)) {
				return false;
			}
			fromdate = fromdate.plusDays(1);
		}
		return true;		

	}

}
