## MySQL Database Desig

### Table: patients

- id: INT, Primary Key, Auto Increment
- first name: VARCHAR(50), Not Null
- last name: VARCHAR(50), Not Null
- address: VARCHAR(150), Not Null
- birthday: DATE, Not Null
- gender: INT (0 = male, 1 = female)

### Table: doctors

- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), Not Null
- address: VARCHAR(150), Not Null
- email: VARCHAR(50)
- phone: VARCHAR(20)
- specialty: VARCHAR(100), Not Null

### Table: appointments

- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

### Table: admim

- id: INT, Primary Key, Auto Increment
- role: VARCHAR(50), Not Null
- create_at: TIMESTAMP
- update_at: TIMESTAMP
- username: VARCHAR(50), Not Null
- password: VARCHAR(30), Not Null

## MongoDB Collection Design

### Collection: prescriptions

```json
{
    "_id": "ObjectId('64abc123456')",
    "patientName": "John Smith",
    "patientId": 12,
    "appointmentId": 51,
    "medication": "Paracetamol",
    "dosage": "500mg",
    "doctorNotes": "Take 1 tablet every 6 hours.",
    "refillCount": 2,
    "pharmacy": {
        "name": "Walgreens SF",
        "location": "Market Street"
    }
}
```

### Collection: feedback

```json
{
    "feedbackId": "fbk-10234",
    "doctor": {
        "id": "5678",
        "name": "Dr. Alicia Martinez",
        "specialization": "Cardiology"
    },
    "patient": {
        "id": "78",
        "name": "John Doe"
    },
    "appointment": {
        "id": "123",
        "date": "2025-08-20",
        "time": "10:30",
        "location": "HeartCare Clinic - Room 203"
    },
    "rating": 4.7,
    "comments": "The doctor was very thorough and explained everything clearly. Waiting time was minimal.",
    "recommend": true,
    "submittedAt": "2025-08-21T14:05:00Z"
}
```

### Collection: logs

```json
{
    "logId": "LOG-0001",
    "timestamp": "2025-08-23T09:15:00Z",
    "eventType": "check_in",
    "patient": {
        "patientId": "455",
        "name": "Alice Smith"
    },
    "location": "Main Reception",
    "details": {
        "reason": "Annual physical exam"
    }
},
{
    "logId": "LOG-0002",
    "timestamp": "2025-08-23T09:30:45Z",
    "eventType": "message",
    "patient": {
        "patientId": "432",
        "name": "John Doe"
    },
    "doctor": {
        "doctorId": "423",
        "name": "Dr. Clara Johnson"
    },
    "details": {
        "subject": "Medication refill",
        "message": "Could I get a refill for my blood pressure medication?"
    }
}
```

### Collection: messages

```json
{
    "messageId": "MSG-0001",
    "sender": {
        "role": "doctor",
        "id": "DOC-456",
        "name": "Dr. Alice Morgan"
    },
    "receiver": {
        "role": "patient",
        "id": "PAT-789",
        "name": "John Smith"
    },
    "timestamp": "2025-08-23T10:45:00Z",
    "type": "text",
    "content": "Please remember to fast for 8 hours before your blood test tomorrow.",
    "read": false
}
```