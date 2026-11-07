# Simple Energy — Vehicle HMI Dashboard (Assignment)

A landscape Android Home/Ride dashboard that simulates vehicle telemetry —
speed (km/h), motor status (ON/OFF), and kill-switch status (Active/Inactive) —
using Kotlin, Jetpack Compose, Coroutines, and `Flow`/`StateFlow`.

No real vehicle hardware, CAN bus, or backend is involved; all data comes from
a simulated provider.

## Features

- **Speedometer** — live vehicle speed in km/h, updated automatically.
- **Motor status** — ON/OFF indicator with distinct color coding.
- **Kill-switch status** — ACTIVE/INACTIVE indicator with distinct color coding.
- **Reactive UI** — Compose collects a `StateFlow`; no manual refresh needed.
- **Simulated scenarios** (one emission per second):

  | Scenario        | Speed | Motor | Kill switch |
  |-----------------|-------|-------|-------------|
  | Parked          | 0     | OFF   | Inactive    |
  | Ready           | 0     | ON    | Inactive    |
  | Riding          | 25    | ON    | Inactive    |
  | Accelerating    | 50    | ON    | Inactive    |
  | Motor disabled  | 25 → 15 → 5 → 0 (coasting) | OFF | Active |
  | Stopped         | 0     | OFF   | Active      |

  Motor OFF does **not** instantly zero the speed — the vehicle coasts down,
  matching real-world behavior.

## Architecture

```
app/src/main/java/com/aman/simpleenergyassignment/
├── domain/
│   ├── VehicleData.kt               # VehicleData model + VehicleScenario enum
│   └── VehicleSignalValidator.kt    # Signal validation (VALID/INVALID/UNAVAILABLE)
├── data/
│   ├── VehicleDataProvider.kt       # Interface: observeVehicleData(): Flow<VehicleData>
│   └── FakeVehicleDataProvider.kt   # Simulated ~1s telemetry emissions
└── presentation/
    ├── HomeScreen.kt                # Compose UI (speedometer + status cards)
    ├── VehicleUiState.kt            # UI state model
    └── viewmodel/
        ├── VehicleViewModel.kt      # StateFlow-based state management
        └── VehicleViewModelFactory.kt
```

### Data flow

1. `FakeVehicleDataProvider.observeVehicleData()` emits `VehicleData` every
   ~1 second inside a `flow { }` builder (runs forever, cancellable).
2. `VehicleViewModel` collects the flow in `viewModelScope`, validates each
   signal via `validateSpeedKmh` / `validateBooleanSignal`, and publishes a
   `VehicleUiState` through a `MutableStateFlow`.
3. `HomeScreen` collects `uiState` with `collectAsStateWithLifecycle()` and
   recomposes automatically on every emission.

### Edge-case handling

- **Invalid speed** — negative, `NaN`, or infinite values are rejected
  (`SignalStatus.INVALID`); the UI shows `--` instead of crashing.
- **Missing signals** — `null` values map to `SignalStatus.UNAVAILABLE` and are
  displayed as `UNKNOWN` / `--`.
- **Stale data** — a monitor coroutine flags `isDataStale` if no emission is
  received within 3 s; the UI shows a "Vehicle data is stale" warning.
- **Flow errors** — exceptions in the data flow are caught and surfaced as an
  error message without crashing the app.

## Build & Run

**Requirements:** Android Studio (latest stable), JDK 17+, Android SDK.

```bash
# Clone / open the project, then:
./gradlew assembleDebug
```

Or open the project in Android Studio and press **Run**. The app locks to
landscape orientation (`android:screenOrientation="landscape"` in the manifest).

## Running Unit Tests

```bash
./gradlew testDebugUnitTest
```

Test reports are generated under `app/build/reports/tests/testDebugUnitTest/`.

### Test coverage

- `FakeVehicleDataProviderTest` — verifies the simulation emits all required
  scenarios in order with correct values (state updates).
- `VehicleSignalValidatorTest` — edge/failure conditions: negative, NaN,
  infinite, and null speed signals plus null boolean signals.

## Assumptions & Limitations

- All telemetry is simulated; no CAN/hardware integration.
- Emission interval is fixed at ~1 s (configurable via the provider's
  `intervalMillis` constructor parameter for tests).
- Speedometer is a numeric readout rather than an analog gauge.
- Stale-data timeout is fixed at 3 s.
- Dependency injection is manual (ViewModel factory); no Hilt.
