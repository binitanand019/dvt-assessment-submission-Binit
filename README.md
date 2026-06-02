# DVT Technical Assessment Submission

## Overview

This repository contains my implementation of the DVT technical assessment across three domains:

1. Backend Service (Java / Spring Boot)
2. Android Application (Kotlin / Jetpack Compose)
3. iOS Application (SwiftUI / MVVM)

The solutions were designed with a strong focus on clean architecture, testability, maintainability, dependency isolation, and modern development practices.

---

# Repository Structure

```text
dvt-assessment-submission
│
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── README.md
│
├── mobile-login-android/
│   ├── app/
│   ├── gradle/
│   └── README.md
│
└── ios-login-swiftui/
    ├── Sources/
    ├── Tests/
    ├── DemoApp/
    └── README.md
```

---

# Backend – Flight Rewards Service

## Objective

Implement a backend service capable of calculating flight reward quotations based on customer details, travel preferences, and business rules.

## Features

* Reward quote calculation
* Customer tier support
* Reward breakdown generation
* Currency conversion integration
* Validation and error handling
* REST API endpoints
* Component testing
* Structured response models

## Technical Stack

* Java 17
* Spring Boot
* Maven
* JUnit 5
* MockMvc

## Architecture

The backend follows a layered architecture:

```text
Controller
    ↓
Service Layer
    ↓
External Clients / Business Logic
    ↓
DTOs & Models
```

### Key Components

* RewardQuoteController
* RewardEngineService
* CurrencyConversionClient
* RewardQuoteRequest
* RewardQuoteResult
* ApiErrorResponse

## Running the Application

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

## Running Tests

```bash
mvn test
```

---

# Android – Login Application

## Objective

Build a login flow using modern Android development practices with Jetpack Compose and MVVM.

## Features Implemented

### Authentication

* Email validation
* Password validation
* Login success flow
* Login failure handling

### Security

* Lockout after 3 failed login attempts
* Token persistence
* Secure storage abstraction

### User Experience

* Remember Me functionality
* Offline detection
* Error messaging
* State-driven UI updates

### Architecture

* MVVM
* Repository Pattern
* Dependency Injection
* Unidirectional State Management

## Technical Stack

* Kotlin
* Jetpack Compose
* ViewModel
* Coroutines
* Hilt Dependency Injection
* Retrofit
* DataStore
* JUnit
* Compose UI Testing

## Project Structure

```text
data/
├── network/
├── remote/
├── repository/
└── storage/

domain/
└── validation/

navigation/

ui/
├── screens/
├── state/
└── theme/

viewmodel/
```

## Test Coverage

Implemented tests for:

1. Validation enables/disables login button
2. Successful login flow
3. Failure count increments
4. Lockout after three failures
5. Offline behavior
6. Remember Me token persistence

## Running the Application

Open the project using Android Studio:

```text
mobile-login-android
```

Run on any Android emulator or physical device.

## Running Tests

```bash
./gradlew test
```

---

# iOS – Login Application

## Objective

Implement the same login workflow on iOS using SwiftUI and MVVM architecture.

## Features Implemented

### Authentication

* Email validation
* Password validation
* Login success flow
* Login error handling

### Security

* Lockout after three failed attempts
* Token persistence abstraction
* Logout support

### User Experience

* Remember Me functionality
* Offline handling
* Reactive state updates

### Architecture

* SwiftUI
* MVVM
* Protocol-Oriented Design
* Dependency Injection

## Technical Stack

* Swift 5.9
* SwiftUI
* XCTest

## Project Structure

```text
Sources/FlightRewardsFeature/

├── FlightRewardsLoginView.swift
├── AuthViewModel.swift
├── AuthState.swift
├── AuthService.swift
├── NetworkMonitor.swift
└── PasswordValidator.swift
```

## Demo Application

A demo application is included for execution and validation.

```text
DemoApp/LoginDemoApp.xcodeproj
```

## Test Credentials

```text
Email: user@example.com
Password: password
```

## Running the Application

Open:

```text
ios-login-swiftui/DemoApp/LoginDemoApp.xcodeproj
```

Select any iPhone simulator and run:

```text
⌘ + R
```

## Running Tests

```text
⌘ + U
```

or

```bash
swift test
```

---

# Engineering Principles Applied

## Clean Architecture

* Separation of concerns
* Layered architecture
* Dependency inversion

## Testability

* Protocol-based abstractions
* Dependency injection
* Deterministic testing

## Maintainability

* Clear folder structure
* Reusable components
* Consistent naming conventions

## Scalability

* Repository abstractions
* Service-oriented backend design
* Feature-based modularization

---

# Assessment Requirements Coverage

| Requirement              | Status |
| ------------------------ | ------ |
| Input Validation         | ✅      |
| Offline Handling         | ✅      |
| Error Handling           | ✅      |
| Remember Me              | ✅      |
| Token Persistence        | ✅      |
| Lockout After 3 Failures | ✅      |
| Logout Functionality     | ✅      |
| MVVM Architecture        | ✅      |
| Dependency Injection     | ✅      |
| Unit Testing             | ✅      |
| Component Testing        | ✅      |
| Documentation            | ✅      |

---

# Author

**Binit Anand**


