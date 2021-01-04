package com.campsitereservation.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campsitereservation.domain.Booking;
import com.campsitereservation.service.BookingService;
import com.campsitereservation.validation.ReservationValidator;

@RestController
public class BookingController {
	
	@Autowired
	BookingService bookingService;
	
	@PostMapping(value = "/book")
	public ResponseEntity<?> book(@Valid @RequestBody Booking booking) throws BindException{
		
		System.out.println("In controller");
		
		ReservationValidator validator = new ReservationValidator();

		WebDataBinder binder = new WebDataBinder(booking);
		binder.setValidator(validator);

		// throws BindException if there are binding/validation
		// errors, exceptions are handled using @ControllerAdvice
		// UserValidationErrorHandler
		binder.validate();
		binder.close();

		// If no validation errors save and return status
		long id = bookingService.reserve(booking);
		//if(id>0) {
			return new ResponseEntity<Long>(id,HttpStatus.CREATED);
		//}else {
		//	return new ResponseEntity<String>("Problem occured while booking, try with dates.", HttpStatus.BAD_REQUEST);
		//}
	}
	
	@DeleteMapping(value = "/cancel/{id}")
	public ResponseEntity<?> cancelBooking(@PathVariable long id){
		
		String cancelStatus = bookingService.cancel(id);
		if(cancelStatus == "success") {
			return new ResponseEntity<String>("Cancelled", HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Enter valid booking id", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value = "/modify")
	public ResponseEntity<?> modifyBooking(@RequestBody Booking booking){
		
		Booking bookings = bookingService.modify(booking);
		if(bookings != null) {
			return new ResponseEntity<String>("Updated", HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Could not update.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/availability")
	public ResponseEntity<?> checkAvailability(@RequestParam(required = false) String arrivalDate, @RequestParam(required = false) String departureDate){
		
//		List<Booking> list = bookingService.getAllBookings();
//		return new ResponseEntity<>(list, HttpStatus.OK);
		
		return new ResponseEntity<>(bookingService.checkAvailability(arrivalDate, departureDate), HttpStatus.OK);
		
	}

}
