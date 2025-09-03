package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

@org.springframework.stereotype.Service
public class AppointmentService {
    // 1. **Add @Service Annotation**:
    // - To indicate that this class is a service layer class for handling business
    // logic.
    // - The `@Service` annotation should be added before the class declaration to
    // mark it as a Spring service component.
    // - Instruction: Add `@Service` above the class definition.

    // 2. **Constructor Injection for Dependencies**:
    // - The `AppointmentService` class requires several dependencies like
    // `AppointmentRepository`, `Service`, `TokenService`, `PatientRepository`, and
    // `DoctorRepository`.
    // - These dependencies should be injected through the constructor.
    // - Instruction: Ensure constructor injection is used for proper dependency
    // management in Spring.

    // 3. **Add @Transactional Annotation for Methods that Modify Database**:
    // - The methods that modify or update the database should be annotated with
    // `@Transactional` to ensure atomicity and consistency of the operations.
    // - Instruction: Add the `@Transactional` annotation above methods that
    // interact with the database, especially those modifying data.

    // 4. **Book Appointment Method**:
    // - Responsible for saving the new appointment to the database.
    // - If the save operation fails, it returns `0`; otherwise, it returns `1`.
    // - Instruction: Ensure that the method handles any exceptions and returns an
    // appropriate result code.

    // 5. **Update Appointment Method**:
    // - This method is used to update an existing appointment based on its ID.
    // - It validates whether the patient ID matches, checks if the appointment is
    // available for updating, and ensures that the doctor is available at the
    // specified time.
    // - If the update is successful, it saves the appointment; otherwise, it
    // returns an appropriate error message.
    // - Instruction: Ensure proper validation and error handling is included for
    // appointment updates.

    // 6. **Cancel Appointment Method**:
    // - This method cancels an appointment by deleting it from the database.
    // - It ensures the patient who owns the appointment is trying to cancel it and
    // handles possible errors.
    // - Instruction: Make sure that the method checks for the patient ID match
    // before deleting the appointment.

    // 7. **Get Appointments Method**:
    // - This method retrieves a list of appointments for a specific doctor on a
    // particular day, optionally filtered by the patient's name.
    // - It uses `@Transactional` to ensure that database operations are consistent
    // and handled in a single transaction.
    // - Instruction: Ensure the correct use of transaction boundaries, especially
    // when querying the database for appointments.

    // 8. **Change Status Method**:
    // - This method updates the status of an appointment by changing its value in
    // the database.
    // - It should be annotated with `@Transactional` to ensure the operation is
    // executed in a single transaction.
    // - Instruction: Add `@Transactional` before this method to ensure atomicity
    // when updating appointment status.

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private Service service;

    public int bookAppointment(Appointment appointment) {

        try {
            Appointment result = appointmentRepository.save(appointment);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {

        Map<String, String> response = new HashMap<>();
        Optional<Appointment> existing = appointmentRepository.findById(appointment.getId());

        if (!existing.isPresent()) {
            response.put("message", "Appointment not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Optional: validate using a custom validation method
        if (service.validateAppointment(appointment) != 1) {
            response.put("message", "Invalid appointment data");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        appointmentRepository.save(appointment);
        response.put("message", "Appointment updated successfully");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {

        Map<String, String> response = new HashMap<>();
        Optional<Appointment> existing = appointmentRepository.findById(id);
        appointmentRepository.delete(existing.get());

        if (!existing.isPresent()) {
            response.put("message", "Appointment not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Verify that the token belongs to the patient who booked the appointment
        if (!tokenService.validateToken(token, "patient")) {
            response.put("message", "Unauthorized to cancel this appointment");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        appointmentRepository.delete(existing.get());
        response.put("message", "Appointment canceled successfully");
        return ResponseEntity.ok(response);
    }

    @Transactional
    public Map<String, Object> getAppointments(String pname, LocalDate date, String token) {

        Map<String, Object> result = new HashMap<>();

        String email = tokenService.extractIdentifier(token);
        Doctor doctor = doctorRepository.findByEmail(email);

        if (!tokenService.validateToken(token, "doctor")) {
            result.put("error", "Invalid Token");
            return result;
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Appointment> appointments = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(
                        doctor.getId(),
                        start,
                        end);

        if (pname != null && !pname.trim().isEmpty()) {
            System.out.print("inside");
            appointments.removeIf(a -> !a.getPatient().getName().toLowerCase().contains(pname.toLowerCase()));
        }

        result.put("appointments", appointments);
        return result;
    }

    @Transactional
    public void changeStatus(Long appointmentId, int newStatus) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isEmpty()) {
            return;
        }

        Appointment appointment = optionalAppointment.get();
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
    }
}
