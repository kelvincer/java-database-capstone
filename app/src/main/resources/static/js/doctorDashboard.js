/*
  Import getAllAppointments to fetch appointments from the backend
  Import createPatientRow to generate a table row for each patient appointment


  Get the table body where patient rows will be added
  Initialize selectedDate with today's date in 'YYYY-MM-DD' format
  Get the saved token from localStorage (used for authenticated API calls)
  Initialize patientName to null (used for filtering by name)


  Add an 'input' event listener to the search bar
  On each keystroke:
    - Trim and check the input value
    - If not empty, use it as the patientName for filtering
    - Else, reset patientName to "null" (as expected by backend)
    - Reload the appointments list with the updated filter


  Add a click listener to the "Today" button
  When clicked:
    - Set selectedDate to today's date
    - Update the date picker UI to match
    - Reload the appointments for today


  Add a change event listener to the date picker
  When the date changes:
    - Update selectedDate with the new value
    - Reload the appointments for that specific date


  Function: loadAppointments
  Purpose: Fetch and display appointments based on selected date and optional patient name

  Step 1: Call getAllAppointments with selectedDate, patientName, and token
  Step 2: Clear the table body content before rendering new rows

  Step 3: If no appointments are returned:
    - Display a message row: "No Appointments found for today."

  Step 4: If appointments exist:
    - Loop through each appointment and construct a 'patient' object with id, name, phone, and email
    - Call createPatientRow to generate a table row for the appointment
    - Append each row to the table body

  Step 5: Catch and handle any errors during fetch:
    - Show a message row: "Error loading appointments. Try again later."


  When the page is fully loaded (DOMContentLoaded):
    - Call renderContent() (assumes it sets up the UI layout)
    - Call loadAppointments() to display today's appointments by default
*/

import { getAllAppointments } from './services/appointmentRecordService.js';
import { createPatientRow } from './components/patientRows.js';

const tableBody = document.getElementById('patientTableBody');

const today = new Date();
const year = today.getFullYear();
const month = String(today.getMonth() + 1).padStart(2, '0');
const day = String(today.getDate()).padStart(2, '0');
let selectedDate = `${year}-${month}-${day}`;

const token = localStorage.getItem('token');
let patientName = null;

const searchBar = document.querySelector('#searchBar');

searchBar.addEventListener('input', async (e) => {
    const value = e.target.value.trim();
    patientName = value !== '' ? value : 'null';
    await loadAppointments();
});

const todayBtn = document.querySelector('#todayButton');
const datePicker = document.getElementById('datePicker');

todayBtn.addEventListener('click', async () => {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    selectedDate = `${year}-${month}-${day}`;

    datePicker.value = selectedDate;
    await loadAppointments();
});

datePicker.addEventListener('change', async (e) => {
    selectedDate = e.target.value;
    console.log("selectedDate", selectedDate);
    await loadAppointments();
});

async function loadAppointments() {
    try {
        const data = await getAllAppointments(selectedDate, patientName, token);
        tableBody.innerHTML = '';

        if (!data || !data.appointments || data.appointments.length === 0) {
            const row = document.createElement('tr');
            row.innerHTML = `<td colspan="5" class="text-center">No Appointments found for today.</td>`;
            tableBody.appendChild(row);
            return;
        }

        data.appointments.forEach(app => {
            const patient = {
                id: app.patient.id,
                name: app.patient.name,
                phone: app.patient.phone,
                email: app.patient.email
            };
            const row = createPatientRow(patient, app.id, app.doctor.id);
            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error(error);
        tableBody.innerHTML = '';
        const row = document.createElement('tr');
        row.innerHTML = `<td colspan="5" class="text-center">Error loading appointments. Try again later.</td>`;
        tableBody.appendChild(row);
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    // renderContent();
    await loadAppointments();
});