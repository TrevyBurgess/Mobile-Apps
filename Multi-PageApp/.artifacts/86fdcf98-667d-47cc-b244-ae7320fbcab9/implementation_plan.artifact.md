# Implementation Plan - Address AGP 9.0 & KMP Deprecations

I will resolve the remaining deprecation warnings identified in the build configuration to ensure compatibility with AGP 9.0 and future-proof the project for AGP 10.0.

## Proposed Changes

### Module: `:composeApp` (KMP Shared Logic)

#### [MODIFY] [composeApp/build.gradle.kts](file:///D:/Dev/GitHub/YouTubeManager/Code/YouTubeManager/composeApp/build.gradle.kts)
- Rename `androidLibrary { ... }` to `android { ... }` within the `kotlin` extension.
- Update `val desktopMain by getting` to `desktopMain { ... }` to resolve the "unused property" warning and follow modern KMP DSL.
- Add `compilerOptions` to the `android` (formerly `androidLibrary`) block to set `jvmTarget`.

### Module: `:app` (Android Entry Point)

#### [MODIFY] [app/build.gradle.kts](file:///D:/Dev/GitHub/YouTubeManager/Code/YouTubeManager/app/build.gradle.kts)
- Fix the `proguardFiles` path to `src/main/keepRules/rules.keep`.
- Use the `kotlin { ... }` block provided by built-in Kotlin to configure `jvmTarget`, ensuring consistency with the shared module and avoiding potential legacy DSL path triggers.

### Version Catalog

#### [MODIFY] [libs.versions.toml](file:///D:/Dev/GitHub/YouTubeManager/Code/YouTubeManager/gradle/libs.versions.toml)
- Check and update any library versions that might be causing internal deprecation warnings if found. (Currently targeting build script deprecations).

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` and check for the absence of deprecation warnings.
- Run `./gradlew :composeApp:assemble` and check for the absence of deprecation warnings.
- Use `analyze_file` again to verify the specific lines previously flagged are resolved.

### Manual Verification
- Verify that both Android and Desktop targets still build and run correctly.
