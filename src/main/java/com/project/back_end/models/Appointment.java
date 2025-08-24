package com.project.back_end.models;

import java.beans.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;

@Entity
public class Appointment {

	// @Entity annotation:
	// - Marks the class as a JPA entity, meaning it represents a table in the
	// database.
	// - Required for persistence frameworks (e.g., Hibernate) to map the class to a
	// database table.

	// 1. 'id' field:
	// - Type: private Long
	// - Description:
	// - Represents the unique identifier for each appointment.
	// - The @Id annotation marks it as the primary key.
	// - The @GeneratedValue(strategy = GenerationType.IDENTITY) annotation
	// auto-generates the ID value when a new record is inserted into the database.

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 2. 'doctor' field:
	// - Type: private Doctor
	// - Description:
	// - Represents the doctor assigned to this appointment.
	// - The @ManyToOne annotation defines the relationship, indicating many
	// appointments can be linked to one doctor.
	// - The @NotNull annotation ensures that an appointment must be associated with
	// a doctor when created.

	@ManyToOne
	@NotNull
	private Doctor doctor;

	// 3. 'patient' field:
	// - Type: private Patient
	// - Description:
	// - Represents the patient assigned to this appointment.
	// - The @ManyToOne annotation defines the relationship, indicating many
	// appointments can be linked to one patient.
	// - The @NotNull annotation ensures that an appointment must be associated with
	// a patient when created.

	@ManyToOne
	@NotNull
	private Patient patient;

	// 4. 'appointmentTime' field:
	// - Type: private LocalDateTime
	// - Description:
	// - Represents the date and time when the appointment is scheduled to occur.
	// - The @Future annotation ensures that the appointment time is always in the
	// future when the appointment is created.
	// - It uses LocalDateTime, which includes both the date and time for the
	// appointment.

	@Future(message = "Appointment time must be in the future")
	private LocalDateTime appointmentTime;

	// 5. 'status' field:
	// - Type: private int
	// - Description:
	// - Represents the current status of the appointment. It is an integer where:
	// - 0 means the appointment is scheduled.
	// - 1 means the appointment has been completed.
	// - The @NotNull annotation ensures that the status field is not null.

	@NotNull
	private int status;

	// 6. 'getEndTime' method:
	// - Type: private LocalDateTime
	// - Description:
	// - This method is a transient field (not persisted in the database).
	// - It calculates the end time of the appointment by adding one hour to the
	// start time (appointmentTime).
	// - It is used to get an estimated appointment end time for display purposes.

	// Getter for endTime (transient field)
	public LocalDateTime getEndTime() {
		return appointmentTime.plusHours(1);
	}

	// Getter for appointmentDate (transient field)
	public LocalDate getAppointmentDate() {
		return appointmentTime.toLocalDate();
	}

	// Getter for appointmentTimeOnly (transient field)
	public LocalTime getAppointmentTimeOnly() {
		return appointmentTime.toLocalTime();
	}

	// 9. Constructor(s):
	// - A no-argument constructor is implicitly provided by JPA for entity
	// creation.
	// - A parameterized constructor can be added as needed to initialize fields.

	// 10. Getters and Setters:
	// - Standard getter and setter methods are provided for accessing and modifying
	// the fields: id, doctor, patient, appointmentTime, status, etc.

	// Getter for id
	public Long getId() {
		return id;
	}

	// Setter for id
	public void setId(Long id) {
		this.id = id;
	}

	// Getter for doctor
	public Doctor getDoctor() {
		return doctor;
	}

	// Setter for doctor
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	// Getter for patient
	public Patient getPatient() {
		return patient;
	}

	// Setter for patient
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	// Getter for appointmentTime
	public LocalDateTime getAppointmentTime() {
		return appointmentTime;
	}

	// Setter for appointmentTime
	public void setAppointmentTime(LocalDateTime appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	// Getter for status
	public int getStatus() {
		return status;
	}

	// Setter for status
	public void setStatus(int status) {
		this.status = status;
	}

	// toString method for debugging
	@Override
	public String toString() {
		return "Appointment{" +
				"id=" + id +
				", doctor=" + (doctor != null ? "Doctor" : "null") +
				", patient=" + (patient != null ? "Patient" : "null") +
				", appointmentTime=" + appointmentTime +
				", status=" + status +
				'}';
	}
}
