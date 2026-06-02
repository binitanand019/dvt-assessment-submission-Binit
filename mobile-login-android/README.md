# Flight Rewards Android App

A modern Android login application built using Kotlin, Jetpack Compose, MVVM, Hilt, Retrofit, and Clean Architecture principles.

This project demonstrates production-style Android engineering practices including state management, dependency injection, networking, offline handling, persistence, and unit testing.

---

# Features

## Authentication
- Login screen
- Email/password validation
- Remember Me functionality
- Token persistence
- Lockout after multiple failed attempts

## Networking
- Retrofit integration
- Real backend API integration
- Offline detection
- Error handling
- Loading states

## Architecture
- MVVM architecture
- Clean Architecture principles
- Repository pattern
- Dependency Injection with Hilt
- StateFlow-based UI state management

## UI
- Jetpack Compose
- Material 3
- Navigation Compose
- Snackbar error handling
- Responsive state-driven UI

## Testing
- ViewModel unit tests
- Validation tests
- Network handling tests
- Offline mode tests
- Lockout logic tests

---

# Tech Stack

## Android
- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Hilt
- Retrofit
- OkHttp
- Coroutines
- StateFlow
- Navigation Compose
- DataStore

## Backend
- Java
- Vert.x
- REST API

---

# Architecture

The application follows Clean Architecture with MVVM.

## Layers

### UI Layer
Responsible for rendering Compose UI and observing ViewModel state.

### ViewModel Layer
Handles:
- business logic
- validation
- UI state
- navigation events

### Repository Layer
Acts as abstraction between:
- UI
- network
- persistence

### Data Layer
Contains:
- Retrofit API
- Token storage
- Network monitoring

### Backend Layer
Vert.x-based REST API server providing login endpoint.

---

# Data Flow

```text
Compose UI
    ↓
ViewModel
    ↓
Repository
    ↓
Retrofit API
    ↓
Backend Server
```

Response Flow:

```text
Backend
    ↓
Repository
    ↓
ViewModel State
    ↓
Compose UI Update
```

---

# Project Structure

```text
app/
├── data/
│   ├── remote/
│   ├── repository/
│   ├── network/
│   └── storage/
│
├── di/
│
├── domain/
│   └── validation/
│
├── navigation/
│
├── ui/
│   ├── screens/
│   ├── state/
│   └── theme/
│
├── viewmodel/
│
└── tests/
```

---

# Backend API

## Base URL

```text
http://10.0.2.2:8080/
```

## Login Endpoint

```text
POST /login
```

---

# Test Credentials

## Valid Login

```text
Email: user@example.com
Password: Password1
```

---

# Implemented Functionalities

- Login validation
- Real backend integration
- Retrofit networking
- Loading spinner
- Error handling
- Snackbar messaging
- Offline mode
- Remember Me
- Token persistence
- Dynamic navigation
- Lockout protection
- State management
- Dependency injection
- Unit testing

---

# How To Run

## Android App

```bash
./gradlew build
```

Run app using Android Studio emulator.

---

## Backend

```bash
mvn clean install
```

Run backend server.

Backend starts on:

```text
http://localhost:8080
```

---

# Future Improvements

- JWT authentication
- Refresh token support
- Encrypted storage
- Biometrics login
- CI/CD pipeline
- Firebase Crashlytics
- Analytics
- Dark mode
- Multi-module architecture
- UI screenshot tests
# Screenshots
## Login Screen
- ![Login Screen](screenshots/LoginScreen.png)

## Invalid Login

![Invalid Login](screenshots/invalid_login.png)
---

# Author

Binit Anand