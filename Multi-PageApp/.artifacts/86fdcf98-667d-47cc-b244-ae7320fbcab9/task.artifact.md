# Tasks - Address AGP 9.0 & KMP Deprecations

- [x] Update `:composeApp` Module
    - [x] Rename `androidLibrary` to `android` in `kotlin` block
    - [x] Modernize `sourceSets` access
    - [x] Add `compilerOptions` for `jvmTarget`
- [x] Update `:app` Module
    - [x] Fix ProGuard rules path in `build.gradle.kts`
    - [x] Add `kotlin { compilerOptions }` block for `jvmTarget`
- [x] Final Verification
    - [x] Run `:app:assembleDebug`
    - [x] Run `analyze_file` on build scripts
    - [x] Run `:composeApp:assemble`
