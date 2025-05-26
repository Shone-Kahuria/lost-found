# How to Run the Lost and Found Project

This project consists of two main parts: a backend Spring Boot application and a frontend React application. Below are the instructions to run both parts.

---

## Backend (Spring Boot)

The backend is a Java Spring Boot application. You can run it using either Maven or Gradle.

### Prerequisites

- Java 17 installed
- Maven or Gradle installed (or use the wrapper scripts)

### Running with Maven

1. Open a terminal.
2. Navigate to the `backend` directory:
   ```bash
   cd backend
   ```
3. Run the Spring Boot application using Maven:
   ```bash
   mvn spring-boot:run
   ```
4. The backend server will start, typically on port 8080.

### Running with Gradle

1. Open a terminal.
2. Navigate to the `backend` directory:
   ```bash
   cd backend
   ```
3. Run the Spring Boot application using Gradle wrapper (Windows):
   ```bash
   gradlew.bat bootRun
   ```
   Or on Unix/macOS:
   ```bash
   ./gradlew bootRun
   ```
4. The backend server will start, typically on port 8080.

---

## Frontend (React)

The frontend is a React application created with Create React App.

### Prerequisites

- Node.js and npm installed

### Running the Frontend

1. Open a terminal.
2. Navigate to the `frontend` directory:
   ```bash
   cd frontend
   ```
3. Install dependencies (if not done already):
   ```bash
   npm install
   ```
4. Start the development server:
   ```bash
   npm start
   ```
5. The frontend will start and open in your default browser, typically at `http://localhost:3000`.

---

## Summary

- Run backend server in `backend` directory using Maven or Gradle.
- Run frontend server in `frontend` directory using npm.
- Make sure backend is running before using frontend if they communicate.

If you need further help or configuration details, please refer to the project documentation or ask.
