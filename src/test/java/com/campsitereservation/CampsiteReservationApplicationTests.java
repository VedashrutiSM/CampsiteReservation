package com.campsitereservation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;


import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.campsitereservation.domain.Booking;
import com.campsitereservation.repository.BookingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CampsiteReservationApplicationTests {
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	BookingRepository repo;
	
    @Autowired
    private MockMvc mockMvc;

	@Test
	public void testConcurrentRequests() {
		
		Runnable r = new Runnable() {
		    @Override 
		    public void run() {
		       
				//System.out.println("In run method");
				try {
					test1();
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }};
		
		for (int i = 0; i < 5; i++) {			
			(new Thread(r)).start();	
		}
		  // (new Thread(r)).start();
	}
	
	
	@Test
	public void test1() throws Exception{
		
		System.out.println("In test1");
		Booking booking = new Booking("a3", "a3@gmail.com", "01/10/2021", "01/10/2021");
		mockMvc.perform(post("/book")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(mapper.writeValueAsString(booking)))
				.andExpect(status().isCreated());
	}



}
