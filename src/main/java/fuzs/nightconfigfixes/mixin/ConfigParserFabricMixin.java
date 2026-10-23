package fuzs.nightconfigfixes.mixin;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.electronwill.nightconfig.core.io.ConfigParser;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.core.io.ParsingMode;
import com.electronwill.nightconfig.core.io.WritingException;
import fuzs.nightconfigfixes.NightConfigFixes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/** Port of the upstream Fabric workaround for Night Config parser failures. */
@Mixin(value = ConfigParser.class, remap = false)
interface ConfigParserFabricMixin<C extends Config> {
    @Shadow(remap = false)
    ConfigFormat<C> getFormat();

    /**
     * Recreates a missing or unreadable file through the caller's configured fallback action.
     * @author Fuzs / JustHari01
     * @reason Recover unreadable Night Config files.
     */
    @Overwrite(remap = false)
    default C parse(Path file, FileNotFoundAction notFoundAction, Charset charset) {
        try {
            if (Files.notExists(file) && !notFoundAction.run(file, this.getFormat())) {
                return this.getFormat().createConfig();
            }
            try (InputStream input = Files.newInputStream(file)) {
                return this.parse(input, charset);
            } catch (ParsingException exception) {
                try {
                    Files.delete(file);
                    if (notFoundAction.run(file, this.getFormat())) {
                        C config;
                        try (InputStream input = Files.newInputStream(file)) {
                            config = this.parse(input, charset);
                        }
                        NightConfigFixes.LOGGER.warn("Configuration file {} could not be parsed. Correcting", file.toAbsolutePath());
                        return config;
                    }
                } catch (Throwable throwable) {
                    exception.addSuppressed(throwable);
                }
                throw exception;
            }
        } catch (IOException exception) {
            throw new WritingException("An I/O error occurred", exception);
        }
    }

    @Shadow(remap = false)
    C parse(InputStream input, Charset charset);

    /**
     * Recreates an unreadable file before retrying a parse into an existing destination.
     * @author Fuzs / JustHari01
     * @reason Recover unreadable Night Config files.
     */
    @Overwrite(remap = false)
    default void parse(Path file, Config destination, ParsingMode parsingMode, FileNotFoundAction notFoundAction, Charset charset) {
        try {
            if (Files.notExists(file) && !notFoundAction.run(file, this.getFormat())) {
                return;
            }
            try (InputStream input = Files.newInputStream(file)) {
                this.parse(input, destination, parsingMode, charset);
            } catch (ParsingException exception) {
                try {
                    Files.delete(file);
                    if (notFoundAction.run(file, this.getFormat())) {
                        try (InputStream input = Files.newInputStream(file)) {
                            this.parse(input, destination, parsingMode, charset);
                        }
                        NightConfigFixes.LOGGER.warn("Configuration file {} could not be parsed. Correcting", file.toAbsolutePath());
                        return;
                    }
                } catch (Throwable throwable) {
                    exception.addSuppressed(throwable);
                }
                throw exception;
            }
        } catch (IOException exception) {
            throw new WritingException("An I/O error occurred", exception);
        }
    }

    @Shadow(remap = false)
    void parse(InputStream input, Config destination, ParsingMode parsingMode, Charset charset);
}
