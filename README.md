# Percentage Calculator

[<img src="https://github.com/ermanergoz/percentage-calculator/blob/master/assets/google-play-badge.png" width="200">](https://play.google.com/store/apps/details?id=com.erman.percentagecalculator)

A feature-rich percentage calculator for Android, built with Jetpack Compose and Redux architecture.

<p float=”center”>
    <img src="https://github.com/ermanergoz/percentage-calculator/blob/master/assets/ss1.png" height="500">
    <img src="https://github.com/ermanergoz/percentage-calculator/blob/master/assets/ss2.png" height="500">
</p>

## Features

### Calculations

- **12 operations** — percentage of a value, what percent A is of B, percentage change,
  increase/decrease by percentage, fraction to percentage, discount, markup, tax, and GPA converter
- **Tip calculator** — calculates tip amount, total, and per-person cost with adjustable tip
  percentage and split count
- **Compound interest calculator** — computes future value and total interest with configurable
  principal, rate, time, and compounding frequency
- **Batch mode** — run any calculation across multiple values at once from a single screen

### History

- All calculation results are automatically saved to a local database
- Browse, search, and delete past calculations
- Tap a history entry to pre-fill inputs and recalculate

### Widgets

- **Main widget** — provides quick access to all 12 operations directly from the home screen
- **Single operation widget** — configurable widget for a single operation, opens a bottom sheet
  calculator overlay

### Personalization

- Light, dark, and system theme modes
- 19 languages: Arabic, Bengali, Chinese (Simplified), Dutch, English, Filipino, French, German,
  Hindi, Indonesian, Italian, Japanese, Korean, Portuguese (Brazil), Portuguese (Portugal), Russian,
  Spanish, Turkish, Urdu
- Operations on the home screen can be sorted by usage frequency
- Share or copy any result to clipboard

## Architecture

MVVM would be sufficient for an app of this scope. Redux was chosen deliberately to serve as a
reference implementation of the pattern on Android with Jetpack Compose. The architecture enforces
unidirectional data flow:

```
UI dispatches Event → Middleware (side effects) → Reducer (pure state update) → UI observes new State
```

### Redux layer

- **Store** — holds immutable state as `StateFlow`, processes events through the middleware chain
  then the reducer, thread-safe via `Mutex`
- **Reducer** — pure function that takes the current state and an event, returns a new state using
  `.copy()`
- **Middleware** — handles side effects (network, database, validation) and can transform events
  before they reach the reducer
- **ReduxViewModel** — base ViewModel that wires the Store to Compose via `StateFlow` and exposes a
  `dispatch()` function

Each feature (calculation, batch, tip calculator, compound interest, history, settings, home) has
its own sealed Event class, immutable State data class, Reducer, and Middleware.

### Data layer

- **Repository pattern** — interfaces defined in the domain layer, implementations in the data layer
- **Room** — local database for calculation history
- **SharedPreferences** — theme, language, and sort preferences

### Libraries

- **Jetpack Compose** — declarative UI
- **Koin** — dependency injection
- **Kotlin Coroutines** — asynchronous operations
- **Jetpack Glance** — home screen widgets
- **ktlint / detekt** — static analysis and code style enforcement

## Build & Run

Clone the repository and open it in Android Studio:

```sh
git clone https://github.com/ermanergoz/PercentageCalculator.git
```

## Meta

Yusuf Erman ERGÖZ – erman.ergoz@gmail.com

Distributed under the MIT license. See ``LICENSE`` for more information.

[https://github.com/ermanergoz](https://github.com/ermanergoz)
