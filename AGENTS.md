# AGENTS.md - Guidance for AI Coding Agents

## Project Overview

**Chance** is a D&D dice roller app built with Kotlin/Android. It manages dice bags, performs rolls, and displays results. The architecture uses clean separation of concerns with protocol buffers for persistence and Jetpack Compose for UI.

## Architecture Essentials

### Layered Module Structure

- **app** - Entry point with `MainActivity`, initializes logging & shortcuts
- **module:common** - Shared utilities: logging, UI theme, preview helpers
- **module:data-domain** - Domain models: `Dice`, `Side`, `Bag`, `Roll`, `RollHistory`, `Settings`
- **module:data-repo-api** - Repository interfaces & JSON schemas for import/validation
- **module:data-repo-impl** - Protocol Buffer serialization + test doubles for persistence
- **module:data-common** - `RepositoryFactory` (singleton-like DI for repositories)
- **module:ui** - Compose UI: tabs, dialogs, zoomed views with ViewModels

### Data Flow Pattern

```
UI (Compose) → ViewModel → Repository (Flow-based) → Serializer → Protocol Buffer Storage
                                ↓
                        Test Double (if FLAG enabled)
```

**Key Flow Usage**: All repositories use `Flow<T>` for reactive reads. Repositories use `.store()` for writes and `.fetch()` for reads.

### Dependency Injection via RepositoryFactory

`RepositoryFactory` is a simple service locator (not DI framework). It conditionally creates real or test implementations based on:

1. **BuildConfig.DEBUG** - Dev mode enables features
2. **UtilityFeature flags** - Switches between protocol buffers and test doubles

Example from `MainActivity`:

```kotlin
val repositoryFactory = RepositoryFactory(application)
MainComposable(
    app,
    repositoryFactory.repositorySettings,
    repositoryFactory.repositoryBag,
    repositoryFactory.repositoryRoll,
    settings?.resize ?: 2
)
```

## Critical Workflows

### Local Development

```bash
# Build & test locally
./gradlew build                    # Full build
./gradlew testFdroidDebugUnitTest  # Unit tests
./gradlew ktlintCheck              # Format check
./gradlew detekt                   # Static analysis
```

### Testing Strategy

- **Unit Tests**: `*Test.kt` or `*UnitTest.kt` in `src/test/kotlin`
- **Instrumented Tests**: `*AndroidTest.kt` in `src/androidTest/kotlin`
- **Coverage**: Jacoco reports with protocol buffer filters (see root `build.gradle.kts`)
- **CI Tests**: GitHub Actions run `codecov.unitTests`, `codecov.data-common.androidTest`, `codecov.ui.androidTest`, `codecov.app.androidTest`

### Preview & Debugging

All Compose screens have `*Preview.kt` files that:

1. Set `UtilityFeature.enabled` flags (usually `REPO_PROTOCOL_BUFFER_TEST_DOUBLE`)
2. Create `RepositoryFactory()` and pass repositories to ViewModels
3. Use `@UtilityPreview` or `@Preview` annotations
4. Wrap with `ChanceTheme {}`

Example from `BagPreview.kt`:

```kotlin
@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun TabBagPreview() {
    UtilityFeature.enabled = setOf(Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE)
    val repositories = RepositoryFactory().run {
        Triple(repositorySettings, repositoryBag, repositoryRoll)
    }
    // Pass to ViewModel and display
}
```

## Project-Specific Conventions

### Naming Patterns

- **Repository Interfaces**: `RepositoryXxxInterface` (in `data-repo-api`)
- **Repository Implementations**: `RepositoryXxxProtocolBufferImpl` with singleton getter via `getInstance()`
- **Test Doubles**: `RepositoryXxxProtocolBufferTestDouble` and `XxxDataTestDouble`
- **ViewModels**: `TabXxxAndroidViewModel`, `ZoomXxxAndroidViewModel`
- **Test Tags**: `class XxxTestTag { companion object { const val FIELD = "FIELD" } }`
- **Previews**: `XxxPreview.kt` with `@Preview` or `@UtilityPreview` annotations

### Persistence Layer

Protocol Buffers stored via `androidx.datastore` (not Room). Serializers located in `impl` subpackages:

- `RepositorySettingsProtocolBufferSerializer`
- `RepositoryBagProtocolBufferSerializer`
- `RepositoryRollProtocolBufferSerializer`

Test doubles use in-memory storage without persistence.

### Feature Flags

Defined in `UtilityFeature.kt`:

- `REPO_PROTOCOL_BUFFER` - Use real protocol buffer storage
- `REPO_PROTOCOL_BUFFER_TEST_DOUBLE` - Use in-memory test doubles
- `UI_SHOW_CRASHLYTICS_BUTTON` - Debug button (F-Droid vs Google Play)
- `UI_SHOW_EPOCH_UUID` - Show timestamps in UI

**Default (production)**: `REPO_PROTOCOL_BUFFER` enabled.
**Test context** (extends `UtilityLoggingHelper`): Auto-switches to `REPO_PROTOCOL_BUFFER_TEST_DOUBLE`.

### Code Quality

- **ktlint**: Runs on all `.kt` files except `composable/**` directories
- **detekt**: Parallel linting with `LongMethod` threshold of 100 lines
- **SonarQube**: Reports to SonarCloud; excludes `/src/main/java/**`
- **Lint**: Android lint with baseline in each module

### Build Flavors

Two product flavors:

- **fdroid** - F-Droid store build (no Firebase)
- **googleplay** - Google Play store build (includes Firebase Crashlytics)

Tasks reference fdroid flavor: `testFdroidDebugUnitTest`, `jacocoFdroidTestReport`.

## Cross-Module Patterns

### UI Layer Integration

Compose screens use a consistent pattern with `AndroidViewModelFactory`:

1. Create factory with repositories from `RepositoryFactory`
2. Factory creates `AndroidViewModel` with dependencies
3. ViewModel exposes `Flow<UIState>` for Compose to consume

Example from `ZoomBagAndroidViewModelFactory`:

```kotlin
class ZoomBagAndroidViewModelFactory(
    private val repositorySettings: RepositorySettingsInterface,
    private val repositoryBag: RepositoryBagInterface,
    private val repositoryRoll: RepositoryRollInterface
) : ViewModelProvider.Factory
```

### Data Domain Types

Core models use data classes with sensible defaults:

- **Dice**: UUID, epoch timestamp, sides list, multiplier/explode/modify rules
- **Side**: Number, color, description, SVG image
- **RollHistory**: Immutable list of `Roll` events
- **Settings**: User preferences (resize, display flags, etc.)

### Testing Utilities

Inherit from `UtilityLoggingHelper` for instrumented tests. It auto-enables test double flag and plants Timber logger:

```kotlin
class MyAndroidTest : UtilityLoggingHelper() {
    @Test
    fun example() = runTest {
        val factory = RepositoryFactory(ApplicationProvider.getApplicationContext())
        // Test with real repositories
    }
}
```

## Important Files & Directories

- `build.gradle.kts` - Root config with Jacoco, detekt, spotless, SonarQube
- `gradle/libs.versions.toml` - Centralized version management (Kotlin 2.3.20, Compose 2026.03.01)
- `detekt.yml` - Reduced rules config
- `.github/workflows/` - CI pipelines (static analysis, unit/android tests, coverage)
- `module/*/src/{main,test,androidTest}/kotlin` - Source organization
- `local.properties` - Local build config (git-ignored)
- `keystore.jks` + `google-services.json` - Encrypted credentials (*.gpg files)

## When Adding Features

1. **Domain Models**: Add to `module:data-domain`
2. **Persistence**: Implement repo in `data-repo-impl`, interface in `data-repo-api`
3. **UI Screens**: Create in `module:ui`, add `*Preview.kt`, use `@Preview` with test double flag
4. **Tests**: Match module structure (`src/androidTest` or `src/test`), inherit `UtilityLoggingHelper`
5. **Test Tags**: Define in `XxxTestTag` companion object for E2E automation
6. **Format**: Run `ktlintFormat` before commit

## Environment & External Tools

- **Java**: JVM 21 (configured in all modules)
- **Gradle**: 8.x wrapper with daemon disabled in CI (`GRADLE_OPTS`)
- **External Dependency**: GitHub CLI (`gh`) for release automation
- **CI**: GitHub Actions with gradle cache and AVD caching

