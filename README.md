# Secure-Notes-Service
Small REST API where users can create, read, update, and delete “secure notes.”

## Design choices

- Used SpringBoot for backend as from specifications
- Used H2 for the database as from specifications
- Chose not to use Spring security as it interfered with H2

### Testing Approach
To ensure the application works correctly and securely, I wrote three main test classes, each with a specific focus:

1. `NotesTest`   
A simple unit test for the Notes model class.
Its purpose is to verify that getters, setters, and timestamp updates work as expected, ensuring the core data structure 
behaves correctly.

2. `TokenAuthFilterTest`  
I used @WebMvcTest here because it allows testing the filter and controller in isolation without starting the full 
application context.
These tests simulate HTTP requests with MockMvc to ensure that requests without an authorization header and requests 
with an invalid token are rejected.
This ensures the static token-based authentication works reliably and protects all endpoints.

3. `NotesServiceTest`  
Tests the business logic of creating, updating, and validating notes.
I dynamically define required Spring properties using @DynamicPropertySource so that the service can read values like 
notes.secret and NOTES_API_TOKEN without relying on environment variables.
These tests also cover input validation, ensuring empty titles, blank content, or overly long fields are rejected, and 
only valid notes are stored.

## Security measures

1. `Content Encryption`  
All note content is encrypted with AES before being saved in the database.
The encryption key (notes.secret) is configurable in the Spring properties, ensuring it is not hard-coded in the 
application.
When notes are retrieved, the content is automatically decrypted so users see their original input.

2. `Token-Based Authentication` 
I implemented TokenAuthFilter to check incoming requests and block any request without a token or with an invalid token,
returning a 401 Unauthorized. This ensures that only authorized clients can access or modify notes.
I intentionally allowed /health and the H2 console to bypass the token check because these endpoints are used for 
monitoring and testing, and I did not want to block safe, internal tools.

3. `Input Validation`  
Titles and contents are validated to prevent empty or overly long inputs.
Strings are trimmed to avoid saving notes with only whitespace.
This ensures that only meaningful, properly formatted data is persisted.

## Setup instructions

1. Clone the Repository with HTTPS or SSH  
`git clone https://github.com/Elly-01/Secure-Notes-Service.git`  
`git clone git@github.com:Elly-01/Secure-Notes-Service.git`

2. Configure Environment Variables  
Either locally on your machine or on the preferred IDE

3. Install Maven if you don't have it already

3. Build the Project    
`mvn install -f pom.xml`  
`mvn compile -f pom.xml`

4. Run the Application  
`mvn spring-boot:run`

5. Access Endpoints  
All endpoints require the header: Authorization: Bearer YOUR_API_TOKEN
   - POST  /api/notes – Create a note
   - GET  /api/notes – Get all notes
   - GET  /api/notes/{id} – Get note by ID
   - PUT  /api/notes/{id} – Update a note
   - DELETE  /api/notes/{id} – Delete a note
   - GET  /health – Health check (no token required)

6. Run Test classes separately  
`mvn test -Dtest=NotesTest`  
`mvn test -Dtest=TokenAuthFilterTest`  
`mvn test -Dtest=NotesServiceTest`  