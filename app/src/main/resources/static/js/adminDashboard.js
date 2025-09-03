/*
  This script handles the admin dashboard functionality for managing doctors:
  - Loads all doctor cards
  - Filters doctors by name, time, or specialty
  - Adds a new doctor via modal form


  Attach a click listener to the "Add Doctor" button
  When clicked, it opens a modal form using openModal('addDoctor')


  When the DOM is fully loaded:
    - Call loadDoctorCards() to fetch and display all doctors


  Function: loadDoctorCards
  Purpose: Fetch all doctors and display them as cards

    Call getDoctors() from the service layer
    Clear the current content area
    For each doctor returned:
    - Create a doctor card using createDoctorCard()
    - Append it to the content div

    Handle any fetch errors by logging them


  Attach 'input' and 'change' event listeners to the search bar and filter dropdowns
  On any input change, call filterDoctorsOnChange()


  Function: filterDoctorsOnChange
  Purpose: Filter doctors based on name, available time, and specialty

    Read values from the search bar and filters
    Normalize empty values to null
    Call filterDoctors(name, time, specialty) from the service

    If doctors are found:
    - Render them using createDoctorCard()
    If no doctors match the filter:
    - Show a message: "No doctors found with the given filters."

    Catch and display any errors with an alert


  Function: renderDoctorCards
  Purpose: A helper function to render a list of doctors passed to it

    Clear the content area
    Loop through the doctors and append each card to the content area


  Function: adminAddDoctor
  Purpose: Collect form data and add a new doctor to the system

    Collect input values from the modal form
    - Includes name, email, phone, password, specialty, and available times

    Retrieve the authentication token from localStorage
    - If no token is found, show an alert and stop execution

    Build a doctor object with the form values

    Call saveDoctor(doctor, token) from the service

    If save is successful:
    - Show a success message
    - Close the modal and reload the page

    If saving fails, show an error message
*/

import { openModal } from './components/modals.js';
import { getDoctors, filterDoctors, saveDoctor } from './services/doctorServices.js';
import { createDoctorCard } from './components/doctorCard.js';

document.addEventListener('DOMContentLoaded', () => {
    loadDoctorCards();

    const addDoctorBtn = document.getElementById('addDocBtn');
    if (addDoctorBtn) {
        addDoctorBtn.addEventListener('click', () => {
            openModal('addDoctor');
        });
    }
});

export async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
        contentDiv.innerHTML = ''; // clear current content
        doctors.forEach(doctor => {
            const card = createDoctorCard(doctor);
            contentDiv.appendChild(card);
        });
    } catch (error) {
        console.error('Error fetching doctors:', error);
    }
}

const contentDiv = document.getElementById('content');
const searchInput = document.getElementById('searchBar');
const timeFilter = document.getElementById('filterTime');
const specialtyFilter = document.getElementById('filterSpecialty');

if (searchInput) searchInput.addEventListener('input', filterDoctorsOnChange);
if (timeFilter) timeFilter.addEventListener('change', filterDoctorsOnChange);
if (specialtyFilter) specialtyFilter.addEventListener('change', filterDoctorsOnChange);

async function filterDoctorsOnChange() {
    try {
        const name = searchInput.value.trim() || null;
        const time = timeFilter.value.trim() || null;
        const specialty = specialtyFilter.value.trim() || null;

        const data = await filterDoctors(name, time, specialty);

        contentDiv.innerHTML = '';

        if (data.doctors && data.doctors.length > 0) {
            renderDoctorCards(data.doctors);
        } else {
            const message = document.createElement('p');
            message.textContent = 'No doctors found with the given filters.';
            contentDiv.appendChild(message);
        }
    } catch (error) {
        alert('An error occurred while filtering doctors.');
        console.error('Error filtering doctors:', error);
    }
}

function renderDoctorCards(doctors) {
    doctors.forEach(doctor => {
        const card = createDoctorCard(doctor);
        contentDiv.appendChild(card);
    });
}

export async function adminAddDoctor() {
    const name = document.getElementById('doctorName').value.trim();
    const email = document.getElementById('doctorEmail').value.trim();
    const phone = document.getElementById('doctorPhone').value.trim();
    const password = document.getElementById('doctorPassword').value.trim();
    const specialty = document.getElementById('specialization').value.trim();

    const checked = document.querySelectorAll('input[name="availability"]:checked');
    const times = Array.from(checked).map(cb => cb.value);

    const token = localStorage.getItem('token');
    if (!token) {
        alert('Authentication token not found. Please log in again.');
        return;
    }

    const doctor = { name, email, phone, password, specialty, availableTimes: times };

    try {
        const success = await saveDoctor(doctor, token);
        if (success) {
            alert('Doctor added successfully!');
            document.getElementById('modal').style.display = 'none';
            location.reload();
        } else {
            alert('Failed to add doctor. Please try again.');
        }
    } catch (error) {
        alert('An error occurred while adding the doctor.');
        console.error('Error saving doctor:', error);
    }
}

window.adminAddDoctor = adminAddDoctor;