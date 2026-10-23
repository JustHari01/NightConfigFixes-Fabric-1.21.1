# Runtime smoke-test evidence

On 2026-10-22, an isolated Minecraft 1.21.1 client with Fabric Loader 0.18.6 and Java 21 loaded the port and its mixin. The development harness wrote malformed TOML, parsed it through Night Config, and verified that the configured fallback restored `restored = true`. The client log contained both the recovery warning and `NightConfigFixes Fabric recovery smoke test PASS`.

This verifies the single parser-recovery path in an isolated development client. It is not a test of an installed modpack or every mod/config format. The final release build keeps the harness and its entrypoint out of the mod JAR.