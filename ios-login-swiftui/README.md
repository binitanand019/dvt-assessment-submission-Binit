# Flight Rewards Login Feature (SwiftUI + MVVM)

A lightweight login feature built using SwiftUI and MVVM architecture, designed with testability, dependency injection, and clean state management in mind.

## Overview

This project demonstrates a production-oriented login flow with:

* SwiftUI-based user interface
* MVVM architecture
* Protocol-driven dependency injection
* Input validation
* Offline handling
* Login lockout protection
* Remember Me functionality
* Token persistence
* XCTest unit testing

## Project Structure

### Core Components

```text
Sources/FlightRewardsFeature/
├── FlightRewardsLoginView.swift
├── AuthViewModel.swift
├── AuthState.swift
├── AuthService.swift
├── NetworkMonitor.swift
└── Resources/
```

### Test Suite

```text
Tests/FlightRewardsFeatureTests/
```

## Architecture

The feature follows the MVVM pattern:

### View

* `FlightRewardsLoginView`
* Renders UI state
* Captures user input
* Dispatches user actions to the ViewModel

### ViewModel

* `AuthViewModel`
* Manages screen state
* Performs validation
* Coordinates authentication flow
* Handles lockout and error scenarios

### Services

* `AuthServicing`
* `NetworkMonitoring`
* `TokenStoring`

All external dependencies are abstracted behind protocols to support deterministic testing and easy substitution of implementations.

## Features

### Input Validation

* Email validation
* Password validation
* Login button enabled only when inputs are valid

### Authentication

* Successful login flow
* Error handling for invalid credentials
* User-friendly validation feedback

### Offline Handling

* Detects network availability
* Prevents authentication requests when offline
* Displays appropriate error messages

### Security Controls

* Lockout after three consecutive failed login attempts
* Failure counter reset after successful authentication

### Remember Me

* Persists authentication token
* Restores session when applicable
* Clears token on logout

### Logout

* Removes stored authentication data
* Resets application state
* Returns user to login screen

## Testing Strategy

The project emphasizes isolated and deterministic testing.

Covered scenarios include:

1. Login button enable/disable validation
2. Successful authentication flow
3. Failed authentication handling
4. Failure count tracking
5. Lockout after three failures
6. Offline login prevention
7. Token persistence with Remember Me
8. Logout behavior

## Running the Application

### Xcode

Open:

```text
DemoApp/LoginDemoApp.xcodeproj
```

Run:

```text
LoginDemoApp
```

on any iOS Simulator.

## Test Credentials

```text
Email: user@example.com
Password: password
```

## Technical Highlights

* SwiftUI
* MVVM
* Async/Await
* XCTest
* Protocol-Oriented Design
* Dependency Injection
* ObservableObject State Management
* Token Persistence
* Network Awareness
