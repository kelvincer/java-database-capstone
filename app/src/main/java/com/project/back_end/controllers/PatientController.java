package com.project.back_end.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;

@RestController
@RequestMapping("/patient")
public class PatientController {

    // 1. Set Up the Controller Class:
    // - Annotate the class with `@RestController` to define it as a REST API
    // controller for patient-related operations.
    // - Use `@RequestMapping("/patient")` to prefix all endpoints with `/patient`,
    // grouping all patient functionalities under a common route.

    // 2. Autowire Dependencies:
    // - Inject `PatientService` to handle patient-specific logic such as creation,
    // retrieval, and appointments.
    // - Inject the shared `Service` class for tasks like token validation and login
    // authentication.

    // 3. Define the `getPatient` Method:
    // - Handles HTTP GET requests to retrieve patient details using a token.
    // - Validates the token for the `"patient"` role using the shared service.
    // - If the token is valid, returns patient information; otherwise, returns an
    // appropriate error message.

    // 4. Define the `createPatient` Method:
    // - Handles HTTP POST requests for patient registration.
    // - Accepts a validated `Patient` object in the request body.
    // - First checks if the patient already exists using the shared service.
    // - If validation passes, attempts to create the patient and returns success or
    // error messages based on the outcome.

    // 5. Define the `login` Method:
    // - Handles HTTP POST requests for patient login.
    // - Accepts a `Login` DTO containing email/username and password.
    // - Delegates authentication to the `validatePatientLogin` method in the shared
    // service.
    // - Returns a response with a token or an error message depending on login
    // success.

    // 6. Define the `getPatientAppointment` Method:
    // - Handles HTTP GET requests to fetch appointment details for a specific
    // patient.
    // - Requires the patient ID, token, and user role as path variables.
    // - Validates the token using the shared service.
    // - If valid, retrieves the patient's appointment data from `PatientService`;
    // otherwise, returns a validation error.

    // 7. Define the `filterPatientAppointment` Method:
    // - Handles HTTP GET requests to filter a patient's appointments based on
    // specific conditions.
    // - Accepts filtering parameters: `condition`, `name`, and a token.
    // - Token must be valid for a `"patient"` role.
    // - If valid, delegates filtering logic to the shared service and returns the
    // filtered result.

    @Autowired
    private PatientService patientService;

    @Autowired
    private Service service;

    @GetMapping("/{token}")
    public ResponseEntity<?> getPatient(@PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "patient");
        if (validation.getStatusCode() != HttpStatus.OK) {
            return validation;
        }
        return patientService.getPatientDetails(token);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        boolean noExists = service.validatePatient(patient);

        if (!noExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Patient with email id or phone already exist"));
        }

        int result = patientService.createPatient(patient);

        return switch (result) {
            case 1 -> ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Signup successful"));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        };
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    @GetMapping("/{id}/{user}/{token}")
    public ResponseEntity<?> getPatientAppointment(@PathVariable long id, @PathVariable String user,
            @PathVariable String token) {
        System.out.println("filter 2: " + user + ":" + id);
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, user);
        if (validation.getStatusCode() != HttpStatus.OK) {
            return validation;
        }

        return patientService.getPatientAppointment(id, token, user);
    }

    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterPatientAppointments(@PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "patient");
        if (validation.getStatusCode() != HttpStatus.OK) {
            return validation;
        }

        String realPatientName = "null".equalsIgnoreCase(name) ? null : name;
        String realCondition = "null".equalsIgnoreCase(condition) ? null : condition;

        return service.filterPatient(realCondition, realPatientName, token);
    }
}
