package com.project.back_end.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST controller that serves JSON responses.
//    - Use `@RequestMapping("${api.path}doctor")` to prefix all endpoints with a configurable API path followed by "doctor".
//    - This class manages doctor-related functionalities such as registration, login, updates, and availability.


// 2. Autowire Dependencies:
//    - Inject `DoctorService` for handling the core logic related to doctors (e.g., CRUD operations, authentication).
//    - Inject the shared `Service` class for general-purpose features like token validation and filtering.


// 3. Define the `getDoctorAvailability` Method:
//    - Handles HTTP GET requests to check a specific doctor’s availability on a given date.
//    - Requires `user` type, `doctorId`, `date`, and `token` as path variables.
//    - First validates the token against the user type.
//    - If the token is invalid, returns an error response; otherwise, returns the availability status for the doctor.


// 4. Define the `getDoctor` Method:
//    - Handles HTTP GET requests to retrieve a list of all doctors.
//    - Returns the list within a response map under the key `"doctors"` with HTTP 200 OK status.


// 5. Define the `saveDoctor` Method:
//    - Handles HTTP POST requests to register a new doctor.
//    - Accepts a validated `Doctor` object in the request body and a token for authorization.
//    - Validates the token for the `"admin"` role before proceeding.
//    - If the doctor already exists, returns a conflict response; otherwise, adds the doctor and returns a success message.


// 6. Define the `doctorLogin` Method:
//    - Handles HTTP POST requests for doctor login.
//    - Accepts a validated `Login` DTO containing credentials.
//    - Delegates authentication to the `DoctorService` and returns login status and token information.


// 7. Define the `updateDoctor` Method:
//    - Handles HTTP PUT requests to update an existing doctor's information.
//    - Accepts a validated `Doctor` object and a token for authorization.
//    - Token must belong to an `"admin"`.
//    - If the doctor exists, updates the record and returns success; otherwise, returns not found or error messages.


// 8. Define the `deleteDoctor` Method:
//    - Handles HTTP DELETE requests to remove a doctor by ID.
//    - Requires both doctor ID and an admin token as path variables.
//    - If the doctor exists, deletes the record and returns a success message; otherwise, responds with a not found or error message.


// 9. Define the `filter` Method:
//    - Handles HTTP GET requests to filter doctors based on name, time, and specialty.
//    - Accepts `name`, `time`, and `speciality` as path variables.
//    - Calls the shared `Service` to perform filtering logic and returns matching doctors in the response.

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private Service service;

    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(@PathVariable String user, @PathVariable Long doctorId, @PathVariable String date, @PathVariable String token) {
        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, user);
        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return validationResponse;
        }
        LocalDate appointmentDate = LocalDate.parse(date);
        List<String> availabilities = doctorService.getDoctorAvailability(doctorId, appointmentDate);
        return ResponseEntity.ok(Map.of("availabilities", availabilities));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {
        List<Doctor> doctors = doctorService.getDoctors();
        return ResponseEntity.ok(Map.of("doctors", doctors));
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(@PathVariable String token,
                                                         @RequestBody Doctor doctor) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
        if (validation.getStatusCode() != HttpStatus.OK) {
            return validation;
        }
        int result = doctorService.saveDoctor(doctor);
        return switch (result) {
            case 1 -> ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Doctor added to db"));
            case -1 -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Doctor already exists"));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body(Map.of("error", "Some internal error occurred"));
        };
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(@PathVariable String token,
                                                            @RequestBody Doctor doctor) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
        if (validation.getStatusCode() != HttpStatus.OK) {
            return validation;
        }
        int result = doctorService.updateDoctor(doctor);
        return switch (result) {
            case 1 -> ResponseEntity.ok(Map.of("message", "Doctor updated"));
            case -1 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Doctor not found"));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body(Map.of("error", "Some internal error occurred"));
        };
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable Long id,
                                                            @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
        if (validation.getStatusCode() != HttpStatus.OK) {
            return validation;
        }
        int result = doctorService.deleteDoctor(id);
        return switch (result) {
            case 1 -> ResponseEntity.ok(Map.of("message", "Doctor deleted successfully"));
            case -1 -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                    .body(Map.of("error", "Doctor not found with id"));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body(Map.of("error", "Some internal error occurred"));
        };
    }

    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filterDoctors(@PathVariable String name,
                                                             @PathVariable String time,
                                                             @PathVariable String speciality) {
                                                                
        String realDoctorName = "null".equalsIgnoreCase(name) ? null : name;
        String realTime = "null".equalsIgnoreCase(time) ? null : time;
        String realSpecility = "null".equalsIgnoreCase(speciality) ? null : speciality;

        System.err.println("filter doctor: " + name + ":" + time + ":" + speciality);

        if (realDoctorName == null && realTime == null && realSpecility == null) {
            return getDoctor();
        }

        Map<String, Object> filteredDoctors = service.filterDoctor(realDoctorName, realSpecility, realTime);
        return ResponseEntity.ok(filteredDoctors);
    }
}
