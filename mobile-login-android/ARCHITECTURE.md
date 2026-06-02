# Architecture

The app follows MVVM + Clean Architecture principles.

## Layers

### UI Layer
Jetpack Compose screens and UI rendering.

### ViewModel Layer
Handles:
- UI state
- validation
- business logic
- navigation events

### Repository Layer
Acts as abstraction between:
- UI
- network
- persistence

### Data Layer
Contains:
- Retrofit API
- DataStore
- Network monitor

### Backend Layer
Vert.x REST API server.

---

# Data Flow

Compose UI
→ ViewModel
→ Repository
→ Retrofit
→ Backend

Response
→ Repository
→ ViewModel
→ UI State