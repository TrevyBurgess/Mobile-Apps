# Implementation Plan - Fix Compose Preview Render Issue

The Compose Preview in `App.kt` is failing with `java.lang.ClassNotFoundException: androidx.compose.ui.tooling.ComposeViewAdapter`. This is caused by missing `ui-tooling` dependencies required for rendering previews on the Android target in a Kotlin Multiplatform project using the `com.android.kotlin.multiplatform.library` plugin.

## User Review Required

> [!IMPORTANT]
> I will be updating the `Preview` annotation import in `App.kt` to use the modern multiplatform `androidx.compose.ui.tooling.preview.Preview` instead of the deprecated `org.jetbrains.compose.ui.tooling.preview.Preview`.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///D:/Dev/GitHub/FidgetGames/Code/gradle/libs.versions.toml)
- Add `compose-ui-tooling` and `compose-ui-tooling-preview` using the `org.jetbrains.compose.ui` group.

#### [MODIFY] [build.gradle.kts](file:///D:/Dev/GitHub/FidgetGames/Code/composeApp/build.gradle.kts)
- Replace `libs.compose.components.uiToolingPreview` with `libs.compose.ui.tooling.preview` in `commonMain`.
- Add `androidRuntimeClasspath(libs.compose.ui.tooling)` to the top-level `dependencies` block.

### Source Code

#### [MODIFY] [App.kt](file:///D:/Dev/GitHub/FidgetGames/Code/composeApp/src/commonMain/kotlin/com/cyberfeedforward/youtubemanager/App.kt)
- Update `Preview` import to `androidx.compose.ui.tooling.preview.Preview`.

## Verification Plan

### Automated Tests
- Run `./gradlew :composeApp:assembleDebug` to ensure the project still builds.

### Manual Verification
- The user should check if the Compose Preview in `App.kt` now renders correctly in Android Studio.
