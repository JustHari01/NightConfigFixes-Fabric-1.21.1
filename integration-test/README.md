# Manual client smoke test

This harness is development-only and is not part of the release JAR or Gradle source set.

To reproduce the parser recovery smoke test:

1. Copy `NightConfigFixesTestHarness.java` into `src/main/java/fuzs/nightconfigfixes/`.
2. Temporarily add this entrypoint to `fabric.mod.json`:
   ```json
   "entrypoints": { "main": ["fuzs.nightconfigfixes.NightConfigFixesTestHarness"] },
   ```
3. Run `./gradlew runClient` (Windows: `gradlew.bat runClient`).
4. Confirm the client log reports `NightConfigFixes Fabric recovery smoke test PASS` and the fixture under `run/client/` contains `restored = true`.
5. Remove the temporary class and entrypoint, then build the release JAR.

The test deliberately writes a malformed TOML file into the isolated development run directory. It verifies that the mixin invokes Night Config's configured `FileNotFoundAction` and reads back the restored value.