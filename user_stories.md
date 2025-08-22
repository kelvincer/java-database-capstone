## Admin User Stories

**Title:**
_As a admin, I want to log into the portal with my username and my password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. User must be redirected to the portal upon successful login.
2. Incorrect credentials must show an error message without granting access.
3. Session must time out after 15 minutes of inactivity.

**Priority:** High
**Story Points:** 8
**Notes:**
- This user story must be developed by two developers.

**Title:**
_As a admin, I want to log out of the portal, so that I can protect system access._

**Acceptance Criteria:**
1. I must be redirected to the login page.
2. I should not see any sensitive data or dashboard content and must log in again to continue
3. Frontend must delete any accesss token after log out.

**Priority:** High
**Story Points:** 8
**Notes:**
- Developers should test all cases.

**Title:**
_As a admin, I want to add doctors to the portal, so that I can register every doctor of the clinic._

**Acceptance Criteria:**
1. Dashborad should have a feature to add doctors to the system.
2. The doctor should be added to the portal and appear in the doctors list.
3. I should see a confirmation message indicating that the doctor was registered successfully.

**Priority:** High
**Story Points:** 5
**Notes:**
- The test data is provided for the clinic.

**Title:**
_As a admin, I want to delete doctor's profile from the portal, so that I can remove his profile from database._

**Acceptance Criteria:**
1. Dashboard should have a feature to delete a doctor on the system.
2. I will not able to see the doctor information in the doctors list.
3. I should see a confirmation message indicating that the doctor was deleted successfully.

**Priority:** High
**Story Points:** 5
**Notes:**
- The test data is provided for the clinic.

**Title:**
_As a admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month and track usage statistics, so that I can monitor the system._

**Acceptance Criteria:**
1. I should be able to use stored procedures to accomplish my task.
2. I should be able to see the clinic appointments.
3. I should be able to the usage statistic of the system.

**Priority:** Medium
**Story Points:** 5
**Notes:**
- You should begin with this story when the developer have all tools to start this story.

## Patient User Stories

**Title:**
_As a patient, I want to View a list of doctors without logging in to explore options before registering, so that I can trust the clinic staff._

**Acceptance Criteria:**
1. I should see a list of all available doctors with their basic information.
2. I should only see non-confidential details (e.g., name, specialty) and not personal contact information or internal notes.
3. I should be prompted to register or log in to continue.

**Priority:** High
**Story Points:** 5
**Notes:**
- The test data is provided by the clinic (Doctors personal information).

**Title:**
_As a patient, I want to sign up using your email and password to book appointments, so that I can use clinic services._

**Acceptance Criteria:**
1. My account should be created and I should see a confirmation or be automatically logged in.
2. I should see a clear error message and the account should not be created until corrected.
3. I should see an error message indicating that the email is already in use and be prompted to log in instead.
   
**Priority:** High
**Story Points:** 5
**Notes:**
- QA Team should test this user story by checking all acceptance criterias.

**Title:**
_As a patient, I want to Log into the portal to manage your bookings, so that I can book an appointment with a doctor, so that I can use clinic service._

**Acceptance Criteria:**
1. I should be granted access to my patient dashboard where I can manage bookings.
2. I should see an error message indicating invalid credentials, and I should not be logged in.
3. I should be able to view, create, or cancel appointments with doctors.

**Priority:** High
**Story Points:** 5
**Notes:**
- Developers need to follow software standards of security.

**Title:**
_As a patient, I want to log out of the portal, so that I can secure my account._

**Acceptance Criteria:**
1. I should be signed out and redirected to the login page.
2. I should not see any sensitive information or dashboard content, and I must log in again to continue.
3. I should be able to log in again.

**Priority:** High
**Story Points:** 5
**Notes:**
- Developers need to follow software standards of security.

**Title:**
_As a patient, I want to log in and book an hour-long appointment to consult with a doctor, so that I can heal my pains._

**Acceptance Criteria:**
1. I should be able to book an appointment with a available doctor.
2. I should be able to find the right speciality for my pains.
3. I should be able to see doctor schedule.

**Priority:** High
**Story Points:** 5
**Notes:**
- Developers need to follow software standards of security.

## Doctor User Stories

**Title:**
_As a doctor, I want to log into the portal to manage your appointments, so that I can use the portal to work._

**Acceptance Criteria:**
1. I should be granted access and redirected to my  dashboard.
2. I should see an error message indicating invalid credentials, and I should not be logged in.
3. I should be able to view, update, or cancel my assigned appointments.

**Priority:** High
**Story Points:** 5
**Notes:**
- The login page should be friendly to any user.

**Title:**
_As a doctor, I want to log out of the portal to protect my data, so that I can protect my information and patient information._

**Acceptance Criteria:**
1. I should be signed out and redirected to the login page.
2. I should be redirected to the login page and not see any protected information.
3. No sensitive information (doctor data or patient records) should be visible, and I must log in again to continue.

**Priority:** High
**Story Points:** 5
**Notes:**
- Log out should follow right secutiry standards.

**Title:**
_As a doctor, I want to View my appointment calendar to stay organized, so that I can manage my patients._

**Acceptance Criteria:**
1. I should see a calendar view showing all my scheduled appointments with patients.
2. I should see key details such as patient name, appointment date and time, and reason for visit.
3. I should see key details such as patient name, appointment date and time, and reason for visit.

**Priority:** High
**Story Points:** 5
**Notes:**
- The use interface for this user story should follow the exact desing provided by design team.

**Title:**
_As a doctor, I want to mark your unavailability to inform patients only the available slots, so that I can schedule my time._

**Acceptance Criteria:**
1. The system should prevent the booking and only allow selection of open slots.
2. The blocked time should be clearly displayed.
3. The unavaible slot should no longer appear as available to patients booking appointments.

**Priority:** High
**Story Points:** 3
**Notes:**
- The use interface for this user story should follow the exact desing provided by design team.

**Title:**
_As a doctor, I want to update my profile with specialization and contact information so that patients have up-to-date information, so that I can provide updated information._

**Acceptance Criteria:**
1. The changes should be saved successfully and reflected in my profile.
2. I should see an error message and the profile should not save until corrected.
3. They should see the updated specialization and contact information immediately.

**Priority:** High
**Story Points:** 3
**Notes:**
- Update information should be public immediately for patients.