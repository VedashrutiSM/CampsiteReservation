package com.campsitereservation.validation;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.campsitereservation.domain.Booking;


@Component
public class ReservationValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Booking.class.equals(clazz);
	}

	@Override
	public void validate(Object bookingObject, Errors errors) {

		Booking booking = (Booking) bookingObject;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate arrivalDate = LocalDate.parse(booking.getArrivalDate(), formatter);
		LocalDate depDate = LocalDate.parse(booking.getDepartureDate(), formatter);

		Period period = Period.between(arrivalDate, depDate);

		if (period.getDays() > 3) {
			errors.rejectValue("arrivalDate", "field.valid", "You can reserve maximum of 3 days");
		}

		Period advance = Period.between(LocalDate.now(), arrivalDate);

		if (advance.getMonths() > 0) {
			errors.rejectValue("arrivalDate", "field.valid", "You can reserve maximum of 1 month in advance");
		} else if (advance.getDays() < 1) {
			errors.rejectValue("arrivalDate", "field.valid", "You can reserve minimum of 1 day in advance");
		}

	
	}

	

}
