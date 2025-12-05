package com.agora.Secure_Notes_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SecureNotesServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(SecureNotesServiceApplication.class, args);
	}

	@GetMapping("/health")
	public ResponseEntity<String> ping() {
		return ResponseEntity.status(200).body("OK");
	}
}