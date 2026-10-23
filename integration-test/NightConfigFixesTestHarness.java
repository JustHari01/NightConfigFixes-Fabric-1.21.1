package fuzs.nightconfigfixes;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.toml.TomlParser;
import net.fabricmc.api.ModInitializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Temporary development-only smoke test. Removed before packaging the release JAR. */
public final class NightConfigFixesTestHarness implements ModInitializer {
    @Override
    public void onInitialize() {
        Path file = Path.of("nightconfigfixes-recovery-smoke-test.toml").toAbsolutePath();
        FileNotFoundAction restoreDefaults = (path, format) -> {
            Files.writeString(path, "restored = true\n", StandardCharsets.UTF_8);
            return true;
        };
        try {
            Files.writeString(file, "broken = [", StandardCharsets.UTF_8);
            CommentedConfig parsed = new TomlParser().parse(file, restoreDefaults, StandardCharsets.UTF_8);
            if (!Boolean.TRUE.equals(parsed.get("restored"))) {
                throw new IllegalStateException("Recovery did not restore the expected default value");
            }
            NightConfigFixes.LOGGER.info("NightConfigFixes Fabric recovery smoke test PASS for {}", file);
        } catch (IOException | ParsingException exception) {
            throw new RuntimeException("NightConfigFixes Fabric recovery smoke test FAILED", exception);
        }
    }
}
