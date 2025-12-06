package com.agora.Secure_Notes_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SecureNotesServiceApplication {
/**
 * The main method in Java runs the SecureNotesServiceApplication using
 * SpringApplication.
 */
	public static void main(String[] args) {
		SpringApplication.run(SecureNotesServiceApplication.class, args);
	}

/**
 * The function responds with a "200 OK" status and the message "OK" when a
 * GET request is made to the "/health" endpoint.
 * 
 * @return A ResponseEntity object with a status code of 200 and a body
 * containing the string "OK" is being returned.
 */
	@GetMapping("/health")
	public ResponseEntity<String> ping() {
		return ResponseEntity.status(200).body("OK");
	}
}