# Chess Sprint Android (Android Studio Project)

A complete Android Studio project for a beginner-friendly chess app that includes:

- **Play tab:** an interactive chess practice board (piece movement, captures, turn handling, reset, quick bot move).
- **Routine tab:** your full 7-day chess improvement routine with checkable daily progress.

## Open in Android Studio
1. Open Android Studio.
2. Choose **Open** and select this folder.
3. Let Gradle sync complete.
4. Run on an emulator or Android phone.

## Tech
- Kotlin
- Jetpack Compose
- Material 3

## Key files
- `app/src/main/java/com/example/chesssprint/MainActivity.kt`
- `app/src/main/AndroidManifest.xml`
- `app/build.gradle.kts`
- `gradle/wrapper/gradle-wrapper.properties`

## Notes
- This repo keeps only text-based wrapper files due binary-file restrictions in this environment.
- If `./gradlew` complains about a missing wrapper JAR, regenerate it in Android Studio or terminal with `gradle wrapper`.
- If sync fails on a restricted network, connect to a network that allows Gradle/Maven downloads.
