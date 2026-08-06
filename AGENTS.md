# AGENTS.md - Guidance for AI Coding Agents

## Project Overview

**Chance** is a D&D dice roller app built with Kotlin/Android. It manages dice bags, performs rolls, and displays results. The architecture uses clean separation of concerns with protocol buffers for persistence and Jetpack Compose for UI.

## Architecture Essentials

### Layered Module Structure

- **app** - Entry point with `MainActivity`, initializes logging & shortcuts
- **module:common** - Shared utilities: logging, UI theme, preview helpers, epoch time, colour utilities
- **module:data-domain** - Domain models: `Dice`, `Side`, `DiceBag`, `Group`, `Roll`, `RollHistory`, `Settings`
- **module:data-repo-api** - Repository interfaces & JSON schemas for import/validation, `RepositoryImportExportInterface`
- **module:data-repo-impl** - Protocol Buffer serialization (DataStore) + test doubles for persistence
- **module:data-common** - `RepositoryFactory` (singleton-like DI), SVG serializer
- **module:ui** - Compose UI: tabs, dialogs, zoomed views, groups, ViewModels, Events, Services, TestTags, Previews

### Data Flow Pattern

```
UI (Compose) → ViewModel → Repository (Flow-based) → Serializer → Protocol Buffer Storage (DataStore)
                                ↓
                        Test Double (if FLAG enabled)
```

**Key Flow Usage**: All repositories use `Flow<T>` for reactive reads. Repositories use `.store()` for writes and `.fetch()` for reads.

### Communication Pattern (Events)

The project uses `SharedFlow` objects defined in `Event.kt` files for decoupled communication between ViewModels and UI components. Events are `object` singletons. Some extend `MutableSharedFlowEvent` (`ui/MutableSharedFlowEvent.kt`) — an empty marker base class — while `RollsEvent`, `DiceResizeEvent`, and `DisplayIndexEvent` are standalone. ViewModels collect events in `init {}` to trigger state updates.

Common events:

- `RollsEvent` (tab/rolls): Triggered after roll sequences or undo actions. It is an `object` singleton.
- `DiceResetEvent` (tab/setup/dice): Triggered when the dice bag is reset to defaults.
- `DiceResizeEvent` (tab/setup/dice): Triggered when the dice size is changed in settings.
- `GroupEvent` (tab): Triggered when groups are created, modified, or deleted.
- `SetupImportEvent` (tab): Triggered after successful JSON import.
- `DisplayIndexEvent` (tab): Triggered when items are reordered.
- `DialogDiceCloseEvent` (dialog/dice): Triggered when the dice configuration dialog is closed.
- `CardDiceSideEvent` (dialog/dice/card/dice): Triggered when a specific dice side is selected or updated.

### Side Effects Pattern

ViewModels use a `SharedFlow` of "Side Effects" to trigger one-off UI events that aren't part of the persistent state, such as haptic feedback, sound effects, or Text-to-Speech.

Example from `RollsAndroidViewModel`:
```kotlin
sealed class RollSideEffect {
    object RollHaptic : RollSideEffect()
    object RollSound : RollSideEffect()
    data class ScoreTTS(val score: Int) : RollSideEffect()
    object UndoHaptic : RollSideEffect()
    object UndoAllHaptic : RollSideEffect()
}
```

### Dependency Injection via RepositoryFactory

`RepositoryFactory` is a simple service locator (not DI framework). It conditionally creates real or test implementations based on:

1. **BuildConfig.DEBUG** - Dev mode enables features
2. **UtilityFeature flags** - Switches between protocol buffers and test doubles

Example from `MainActivity`:

```kotlin
val repositoryFactory = RepositoryFactory(application)
MainComposable(
    application,
    repositoryFactory.repositorySettings,
    repositoryFactory.repositoryBag,
    repositoryFactory.repositoryRoll,
    repositoryFactory.repositoryGroup,
    settings.resizeZoom
)
```

## Critical Workflows

### Local Development

```bash
./gradlew build                    # Full build
./gradlew testFdroidDebugUnitTest  # Unit tests
./gradlew ktlintCheck              # Format check
./gradlew detekt                   # Static analysis (parallel)
```

### Testing Strategy

- **Unit Tests**: `*Test.kt` or `*UnitTest.kt` in `src/test/kotlin`
- **Instrumented Tests**: `*Test.kt` in `src/androidTest/kotlin`. Inherit from `TestSupport` (extends `UtilityLoggingHelper`) for `androidComposeTestRule` and helpers.
- **Test Helpers** (`TestSupport`): `displayBottomSheet(testTag)`, `assertClick(testTag)`, `waitForGitHubCI(testTag)` (20s timeout), `isCI` property
- **Base classes**: `UtilityLoggingHelper` (auto-enables `REPO_PROTOCOL_BUFFER_TEST_DOUBLE`, plants Timber), `AndroidTestHelper` (UI-less instrumented tests), `UtilityAndroidUnitTestHelper`, `UtilityRuleMainDispatcher`
- **Data Reset**: `runBlocking { RepositoryFactory(context).resetStorage() }` in `@Before`
- **Async Testing**: `androidComposeTestRule.waitUntil` with `fetchSemanticsNodes().isNotEmpty()`
- **Coverage**: Jacoco 0.8.14, F-Droid flavor, protocol buffer filters
- **Test Retry**: Gradle plugin 1.6.5 (2 retries, 2 max failures)
- **CI Tests**: 4 workflows — `codecov.unitTests` (4-module matrix), `codecov.app.androidTest`, `codecov.data-common.androidTest`, `codecov.ui.androidTest` — all API 36 AVD

### Preview & Debugging

16 `*Preview.kt` files across the UI module. Pattern:

1. Set `UtilityFeature.enabled` to `REPO_PROTOCOL_BUFFER_TEST_DOUBLE`
2. Create `RepositoryFactory()`, instantiate ViewModels
3. Use `@UtilityPreview` (multi-device: phone 360x640, landscape 640x360, foldable 673x841, tablet 1280x800) or `@Preview`
4. Wrap with `ChanceTheme {}`

```kotlin
@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun TabBagPreview() {
    UtilityFeature.enabled = setOf(Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE)
    val repositoryFactory = RepositoryFactory()
    val viewModel = DiceAndroidViewModel(...)
    ChanceTheme { TabBagDice(viewModel, ...) }
}
```

## Project-Specific Conventions

### Naming Patterns

- **Repository Interfaces**: `RepositoryXxxInterface` (in `data-repo-api`)
- **Repository Implementations**: `RepositoryXxxProtocolBufferImpl` with singleton `getInstance()`
- **Test Doubles**: `RepositoryXxxProtocolBufferTestDouble` and `XxxDataTestDouble`
- **ViewModels**: `XxxAndroidViewModel`, `ZoomXxxAndroidViewModel` (extends abstract `ZoomAndroidViewModel`)
- **ViewModel Factories**: `XxxAndroidViewModelFactory` implementing `ViewModelProvider.Factory`
- **Services**: `CardDiceService`, `CardFaceService`, `CardRollService` (dialog dice cards); `RollsScoreTtsPlayer` (TTS), `RollsSoundPlayer` (sound effects), `ShakeToRollService` (shake gesture), `HapticHelper` (haptic feedback), `RollsSequenceHelper` (roll persistence)
- **Test Tags**: `class XxxTestTag { companion object { const val FIELD = "FIELD" } }` (15 files + inline `TabRowTestTag`)
- **Previews**: `XxxPreview.kt` with `@Preview` or `@UtilityPreview`

### Persistence Layer

Protocol Buffers via `androidx.datastore` (not Room). 8 `.proto` files in `module/data-repo-impl/src/proto/`:

- `bag.proto` / `dice.proto` / `side.proto` / `settings.proto`
- `roll.proto` / `roll_history.proto` / `group.proto` / `group_history.proto`

Serializers in `impl` subpackages:
- `RepositorySettingsProtocolBufferSerializer`
- `RepositoryBagProtocolBufferSerializer`
- `RepositoryGroupProtocolBufferSerializer`
- `RepositoryRollProtocolBufferSerializer`

#### Protocol Buffer Caching Strategy

The `bag.proto` and `roll_history.proto` implementations use a custom caching strategy to deduplicate large `imageBase64` strings within the Protocol Buffers:

- **Deduplication**: Unique images are stored once in a dedicated "cache" entry with `epoch = -1L` (`EPOCH_IMAGE_CACHE`).
    - In `BagProtocolBuffer`, this is a specific `DiceProtocolBuffer` in the `dice` list.
    - In `RollHistoryProtocolBuffer`, this is a specific entry in the `values` map.
- **References**: Individual `SideProtocolBuffer` entries store a reference (either a `REF:<SHA256_HASH>` string or just the `uuid`) instead of the full base64 data.
- **Lifecycle**:
    - `getOrUpdateCache()`: Computes hashes and updates the cache during writes.
    - `pruneCache()`: Removes unreferenced images from the cache to save space.
    - Mapping functions (e.g., `mapBagProtocolBufferIntoDiceBag`): Reconstruct the full `Side` objects by resolving references from the cache during reads.
- **Implementation**: Managed by `RepositoryBagProtocolBufferInterface`, `RepositoryRollProtocolBufferInterface`, and `RepositoryProtocolBufferImageCache`.

Test doubles use in-memory storage (no persistence).

### Feature Flags

Defined in `UtilityFeature.kt` (`module/common/.../utility/feature/UtilityFeature.kt`):

- `REPO_PROTOCOL_BUFFER_PROD` — Real protocol buffer storage (DataStore)
- `REPO_PROTOCOL_BUFFER_TEST_DOUBLE` — In-memory test doubles
- `REPO_PROTOCOL_BUFFER_EMPTY_AT_STARTUP` — Clear storage at startup
- `UI_SHOW_CRASHLYTICS_BUTTON` — Debug crash button (F-Droid)
- `UI_SHOW_UUID` — Show UUIDs in UI

**Default (production)**: `REPO_PROTOCOL_BUFFER_PROD` only.
**Test context** (extends `UtilityLoggingHelper`): Auto-switches to `REPO_PROTOCOL_BUFFER_TEST_DOUBLE`.

### Code Quality

- **ktlint**: 14.2.0 (Android mode, excludes `composable/**`)
- **detekt**: 1.23.8 (parallel, `LongMethod` threshold 100, custom `detekt.yml`)
- **Spotless**: 8.9.0
- **Jacoco**: 0.8.14 (F-Droid flavor reports)
- **Lint**: Android lint with baseline per module

### Build Flavors

Two product flavors (dimension `store`):

- **fdroid** — F-Droid build (no Firebase, `default` AVD target in CI)
- **googleplay** — Google Play build (Firebase Crashlytics 20.1.0, plugin 3.0.7; GMS AVD target)

Tasks: `testFdroidDebugUnitTest`, `jacocoFdroidTestReport`, `assembleFdroidRelease`.

## Cross-Module Patterns

### UI Layer Integration

Compose screens use `AndroidViewModelFactory`:

1. Factory created with repositories from `RepositoryFactory`
2. Factory creates `AndroidViewModel` with dependencies
3. ViewModel exposes `StateFlow<UIState>` or `Flow<UIState>` for Compose

**UI State Patterns**:
- **StateFlow<T>**: Used for main state objects (e.g., `diceBag`, `groupHistory`).
- **SettingsState**: A data class aggregating specific settings for the UI (e.g., in `RollsAndroidViewModel`).
- **collectAsStateWithLifecycle**: Preferred way to collect flows in Compose to be lifecycle-aware.

**Base ViewModel** (`zoom/ZoomAndroidViewModel.kt`): Abstract base for zoom views. Provides `ZoomState` data class (`resizeViewDp`, `diceBag`, `rollHistory`, scroll positions), move/reorder (`move()`/`moveUp()`/`moveDown()`), SVG image caching (`sideSvgImageRequestAsync`), and collects `DialogDiceCloseEvent`, `SetupImportEvent`, `DiceResetEvent`, `DisplayIndexEvent`.

```kotlin
class ZoomDiceAndroidViewModelFactory(
    private val application: Application,
    private val repositorySettings: RepositorySettingsInterface,
    private val repositoryBag: RepositoryBagInterface,
    private val repositoryRoll: RepositoryRollInterface
) : ViewModelProvider.Factory
```

### Data Domain Types

Core models are in sub-packages under `data.domain.core`:

- **`Dice`** (`core/Dice.kt`): UUID, epoch timestamp, sides (`List<Side>`), title, colour, selected, multiplier/explode/modify rules (`DiceRollValues` companion provides constrained value lists for `multiplierValues`, `explodeWhenValues`, `modifyScoreValues`), `displayIndex`
- **`Side`** (`core/Side.kt`): UUID, number, colours, `imageDrawableId`, `imageBase64`, `imageRequest` (transient, not in proto), description. Overrides `copy()`.
- **`DiceBag`** (`core/bag/Bag.kt`): `typealias DiceBag = MutableList<Dice>`. Backed by `BagDataInterface` (`suspend fun allDice(): MutableList<Dice>`) with impl/test double.
- **`Group`** (`core/group/Group.kt`): UUID, name, linked dice UUIDs, notes, selected, displayIndex. `typealias GroupHistory = List<Group>`. Backed by `GroupDataInterface` (`val groupHistory: GroupHistory` — read-only).
- **`Roll`** (`core/roll/Roll.kt`): dice UUID, rolled `Side`, multiplier/explode indices, score adjustment, score, group UUID. `typealias RollHistory = LinkedHashMap<Long, List<Roll>>`. Backed by `RollHistoryDataInterface` (`var rollHistory: RollHistory` — mutable).
- **`SettingsDataInterface`** (`core/settings/SettingsDataInterface.kt`): `var` properties: `resizeZoom`, `rollIndexTime`, `rollScore`, `rollScoreTTS`, `diceTitle`, `sideNumber`, `rollBehaviour`, `sideDescription`, `sideSVG`, `haptics`, `shakeToRoll`, `rollSound`, `shuffle`, `groupTitle`.

### UI Dialog Structure

Dialog subsystem under `ui/dialog/`:

- **DialogDice** — Dice configuration with 3 card tabs: `CardDice`, `CardFace`, `CardRoll` (each with a service class)
- **DialogGroup** — Group creation/editing
- **DialogSettings** — User preferences
- **DialogConfirm** — Confirmation dialogs
- **DialogColourPicker** — Colour selection

Separate `ui/group/` package provides composables: `Group`, `GroupAddSub`, `GroupName`, `GroupNotes`, `GroupUuidDice`.

### Testing Utilities

Inherit from `TestSupport` for instrumented tests. Extends `UtilityLoggingHelper`:

```kotlin
class MyTest : TestSupport() {
    @Test
    fun example() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Test with androidComposeTestRule
    }
}
```

## Important Files & Directories

- `build.gradle.kts` — Root config: Jacoco 0.8.14, detekt 1.23.8, spotless 8.9.0, test-retry 1.6.5
- `gradle/libs.versions.toml` — Version catalog: Kotlin 2.4.10, Compose BOM 2026.06.01, AGP 9.2.1, compileSdk/targetSdk 37, minSdk 31, versionName 2.5.2, versionCode 243137
- `detekt.yml` — Reduced rules config
- `.github/workflows/` — 7 CI pipelines: `static.analysis`, `static.gitleaks`, `codecov.unitTests`, `codecov.app.androidTest`, `codecov.data-common.androidTest`, `codecov.ui.androidTest`, `release.yml`
- `.github/actions/cache-gradle/` and `.github/actions/cache-avd/` — CI caching actions
- `module/*/src/{main,test,androidTest}/kotlin` — Source organization
- `module/data-repo-impl/src/proto/` — 8 Proto schema files
- `local.properties` — Local build config (git-ignored)
- `keystore.jks` + `google-services.json` — Encrypted credentials (*.gpg files)

## When Adding Features

1. **Domain Models**: Add to `module:data-domain`
2. **Persistence**: Implement repo in `data-repo-impl`, interface in `data-repo-api`, proto in `src/proto/`
3. **UI Screens**: Create in `module:ui`, add `*Preview.kt`, use `@Preview`/`@UtilityPreview` with test double flag
4. **Tests**: Match module structure, inherit `UtilityLoggingHelper` or `TestSupport`
5. **Test Tags**: Define `XxxTestTag` companion object for E2E automation
6. **Format**: Run `ktlintFormat` before commit

## Environment & External Tools

- **Java**: JVM 21 (all modules)
- **Gradle**: 9.6.1 wrapper; daemon disabled in CI (`GRADLE_OPTS`)
- **Android CLI**: Used for managing agent skills via `android skills [add|list|remove|find]`. Skills are stored in `.agents/skills/`.
- **GitHub CLI** (`gh`): Release automation
- **CI**: GitHub Actions, `actions/checkout@v7`, `actions/upload-artifact@v7`, `codecov/codecov-action@v7`, `reactivecircus/android-emulator-runner@v2` (API 36, x86_64, 2GB RAM)
- **Release**: Tag push `*.*.*-fdroid` or `*.*.*-rc` triggers build, sign, and GitHub Release

---

## Code Fix & Quality Standards

* **Deprecation Resolution**: For any code fix or modification, you MUST proactively identify and resolve any deprecated API usages within the touched files to ensure the codebase remains modern.
* **Verification**: Every code fix MUST be accompanied by a unit test (or updates to existing tests) that explicitly proves the fix worked and prevents regressions.
