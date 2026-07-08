# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build, Test, and Lint Commands

```bash
./gradlew build                          # Full build
./gradlew testFdroidDebugUnitTest        # Unit tests (fdroid flavor)
./gradlew connectedFdroidDebugAndroidTest # Instrumented tests (requires emulator/device)
./gradlew jacocoFdroidTestReport         # Jacoco coverage report (unit tests)
./gradlew jacocoFdroidAndroidTestReport  # Jacoco coverage report (unit + instrumented)
./gradlew jacocoCombinedReport           # Aggregated coverage across all modules
./gradlew ktlintCheck                    # Kotlin lint check
./gradlew ktlintFormat                   # Auto-format Kotlin files
./gradlew detekt                         # Static analysis
./gradlew spotlessApply                  # Auto-format with Spotless
```

**Single test class/method**: Append `--tests "fully.qualified.ClassName"` or `--tests "fully.qualified.ClassName.methodName"` to the test task.

## Architecture

**Chance** is a D&D dice roller app — Kotlin/Android, Jetpack Compose UI, Protocol Buffer persistence via `androidx.datastore`.

### Module Graph

```
app  (MainActivity, entry point)
├── module:common        — Shared: logging (Timber), Compose theme, preview helpers
├── module:data-domain   — Domain models: Dice, Side, Bag, Group, Roll, RollHistory, Settings
├── module:data-repo-api — Repository interfaces + JSON schemas for import/validation
├── module:data-repo-impl — Protocol Buffer serializers + in-memory test doubles
├── module:data-common   — RepositoryFactory (service locator that wires repos)
└── module:ui            — Compose screens: tabs, dialogs, zoomed views + ViewModels
```

### Data Flow

```
Compose UI → ViewModel → Repository (Flow<T>) → Serializer → Protocol Buffer (DataStore)
                            ↓
                    Test Double (in-memory, if feature flag enabled)
```

### Key Patterns

- **DI**: `RepositoryFactory` is a simple service locator — no DI framework. It conditionally creates real or test implementations based on `UtilityFeature` flags.
- **Reactive reads**: All repositories expose `Flow<T>`. Methods use `.store()` for writes, `.fetch()` for reads.
- **Events**: Decoupled ViewModel↔UI communication via `SharedFlow` objects in `*Event.kt` files (e.g., `RollsEvent`, `DiceResetEvent`, `GroupEvent`).
- **ViewModels**: Created via `AndroidViewModelFactory` classes that accept repository interfaces. Named `Tab*AndroidViewModel` or `Zoom*AndroidViewModel`.
- **Preview files**: Every Compose screen has a `*Preview.kt` that sets `UtilityFeature.enabled` to `REPO_PROTOCOL_BUFFER_TEST_DOUBLE`, creates repos, and wraps UI in `ChanceTheme`.

### Build Flavors

- **fdroid** — F-Droid store (no Firebase). All CI tasks use this flavor.
- **googleplay** — Google Play store (Firebase Crashlytics included).

### Testing

- **Unit tests**: `src/test/kotlin`, files named `*Test.kt` or `*UnitTest.kt`
- **Instrumented tests**: `src/androidTest/kotlin`, extend `TestSupport` (provides `androidComposeTestRule`, `displayBottomSheet()`, `assertClick()`, `waitForGitHubCI()`)
- **Test doubles**: `UtilityLoggingHelper` (parent of `TestSupport`) auto-enables `REPO_PROTOCOL_BUFFER_TEST_DOUBLE`
- **Data reset in @Before**: `runBlocking { RepositoryFactory(context).resetStorage() }`

### Code Quality

- **ktlint**: All `.kt` files except `composable/**` directories
- **detekt**: Parallel, `LongMethod` threshold 100 lines, config at `detekt.yml`
- **Spotless**: Target `**/*.kt`, exclude `**/composable/**`
- **SonarQube**: Reports to SonarCloud, key `jameshnsears_Chance`

## Toolchain

- Java: JVM 21
- Kotlin: 2.4.0
- Gradle: 9.6.1 (wrapper included)
- AGP: 9.2.1
- Compose BOM: 2026.06.01
- Protocol Buffers: 4.35.1

# Claude Code Project Rules

## Context Management & Token Savings
- NEVER read, grep, or index files inside the `**/build/` or `**/.gradle/` directories.
- Generated Android artifacts (such as Room schemas or Hilt/Dagger generated code) must be ignored unless explicitly asked by the user.
- If a build fails, do not ingest the entire Gradle stack trace. Only read the specific compiler error message provided in the terminal.

## Code Fix & Quality Standards

*   **Deprecation Resolution**: For any code fix or modification, you MUST proactively identify and resolve any deprecated API usages within the touched files to ensure the codebase remains modern.
*   **Verification**: Every code fix MUST be accompanied by a unit test (or updates to existing tests) that explicitly proves the fix worked and prevents regressions.

## More Detail

See `AGENTS.md` for in-depth documentation on: domain model definitions, event system details, persistence layer design, feature flags, cross-module integration patterns, preview authoring, and step-by-step guidance for adding new features.
