package com.project.back_end.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

@org.springframework.stereotype.Service
public class Service {
    // 1. **@Service Annotation**
    // The @Service annotation marks this class as a service component in Spring.
    // This allows Spring to automatically detect it through component scanning
    // and manage its lifecycle, enabling it to be injected into controllers or
    // other services using @Autowired or constructor injection.

    // 2. **Constructor Injection for Dependencies**
    // The constructor injects all required dependencies (TokenService,
    // Repositories, and other Services). This approach promotes loose coupling,
    // improves testability,
    // and ensures that all required dependencies are provided at object creation
    // time.

    // 3. **validateToken Method**
    // This method checks if the provided JWT token is valid for a specific user. It
    // uses the TokenService to perform the validation.
    // If the token is invalid or expired, it returns a 401 Unauthorized response
    // with an appropriate error message. This ensures security by preventing
    // unauthorized access to protected resources.

    // 4. **validateAdmin Method**
    // This method validates the login credentials for an admin user.
    // - It first searches the admin repository using the provided username.
    // - If an admin is found, it checks if the password matches.
    // - If the password is correct, it generates and returns a JWT token (using the
    // admin’s username) with a 200 OK status.
    // - If the password is incorrect, it returns a 401 Unauthorized status with an
    // error message.
    // - If no admin is found, it also returns a 401 Unauthorized.
    // - If any unexpected error occurs during the process, a 500 Internal Server
    // Error response is returned.
    // This method ensures that only valid admin users can access secured parts of
    // the system.

    // 5. **filterDoctor Method**
    // This method provides filtering functionality for doctors based on name,
    // specialty, and available time slots.
    // - It supports various combinations of the three filters.
    // - If none of the filters are provided, it returns all available doctors.
    // This flexible filtering mechanism allows the frontend or consumers of the API
    // to search and narrow down doctors based on user criteria.

    // 6. **validateAppointment Method**
    // This method validates if the requested appointment time for a doctor is
    // available.
    // - It first checks if the doctor exists in the repository.
    // - Then, it retrieves the list of available time slots for the doctor on the
    // specified date.
    // - It compares the requested appointment time with the start times of these
    // slots.
    // - If a match is found, it returns 1 (valid appointment time).
    // - If no matching time slot is found, it returns 0 (invalid).
    // - If the doctor doesn’t exist, it returns -1.
    // This logic prevents overlapping or invalid appointment bookings.

    // 7. **validatePatient Method**
    // This method checks whether a patient with the same email or phone number
    // already exists in the system.
    // - If a match is found, it returns false (indicating the patient is not valid
    // for new registration).
    // - If no match is found, it returns true.
    // This helps enforce uniqueness constraints on patient records and prevent
    // duplicate entries.

    // 8. **validatePatientLogin Method**
    // This method handles login validation for patient users.
    // - It looks up the patient by email.
    // - If found, it checks whether the provided password matches the stored one.
    // - On successful validation, it generates a JWT token and returns it with a
    // 200 OK status.
    // - If the password is incorrect or the patient doesn't exist, it returns a 401
    // Unauthorized with a relevant error.
    // - If an exception occurs, it returns a 500 Internal Server Error.
    // This method ensures only legitimate patients can log in and access their data
    // securely.

    // 9. **filterPatient Method**
    // This method filters a patient's appointment history based on condition and
    // doctor name.
    // - It extracts the email from the JWT token to identify the patient.
    // - Depending on which filters (condition, doctor name) are provided, it
    // delegates the filtering logic to PatientService.
    // - If no filters are provided, it retrieves all appointments for the patient.
    // This flexible method supports patient-specific querying and enhances user
    // experience on the client side.

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> response = new HashMap<>();
        if (tokenService.validateToken(token, user)) {
            response.put("message", "Token is valid");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();

        Admin dbAdmin = adminRepository.findByUsername(receivedAdmin.getUsername());
        System.out.println("db: " + dbAdmin.getPassword());
        if (dbAdmin != null && dbAdmin.getPassword().equals(receivedAdmin.getPassword())) {
            String token = tokenService.generateToken(receivedAdmin.getUsername());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
    }

    public int validateAppointment(Appointment appointment) {

        Optional<Doctor> optDoctor = doctorRepository.findById(appointment.getDoctor().getId());

        if (!optDoctor.isPresent()) {
            return -1;
        }

        List<String> doctorAvailability = doctorService.getDoctorAvailability(optDoctor.get().getId(), appointment.getAppointmentDate());

        if(doctorAvailability.isEmpty()) {
            return 0;
        } else {
            return 1;
        }
    }

    public boolean validatePatient(Patient patient) {

        Patient dbPatient = patientRepository.findByEmailOrPhone(patient.getName(), patient.getPhone());
        return dbPatient == null ? true : false;
    }

    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {

        Map<String, String> response = new HashMap<>();
        Patient dbPatient = patientRepository.findByEmail(login.getEmail());

        if (dbPatient == null) {
            response.put("error", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = tokenService.generateToken(login.getEmail());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {

        Map<String, Object> response = new HashMap<>();

        if (!tokenService.validateToken(token, "patient")) {
            response.put("error", "Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String patientEmail = tokenService.extractIdentifier(token);
        Patient patient = patientRepository.findByEmail(patientEmail);

        List<Appointment> appointments;
        if (condition != null && name != null) {
            return patientService.filterByDoctorAndCondition(condition, name, patient.getId());
        } else if (condition != null) {
            return patientService.filterByCondition(condition, patient.getId());
        } else if (name != null) {
            return patientService.filterByDoctor(name, patient.getId());
        } else {
            appointments = Collections.emptyList();
        }

        response.put("appointments", appointments);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
