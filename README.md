# Night Config Fixes — unofficial Fabric 1.21.1 port

A Fabric port of the upstream unreadable-config recovery workaround for Minecraft 1.21.1. When Night Config raises a parsing exception while reading a configuration file, the mod removes the unreadable file and invokes Night Config's existing `FileNotFoundAction` to restore it. If restoration or retrying the parse fails, the original parsing error is preserved.

## Scope

The upstream Fabric implementation contains this one workaround. The other upstream options modify Forge's config system and are not part of this Fabric port. See [Fuzss/nightconfigfixes](https://github.com/Fuzss/nightconfigfixes) for the original mod and behavior description.

- Minecraft: 1.21.1
- Loader: Fabric Loader 0.16.14 or newer
- Java: 21 or newer
- Bundled Night Config: 3.8.4 (`core` and `toml`)

## License and attribution

This is an unofficial port by JustHari01, based on the MPL-2.0-licensed upstream project by Fuzs. It is not affiliated with or endorsed by Fuzs. No upstream logo or banner assets are included.

The port and project source are under MPL-2.0; see [LICENSE.md](LICENSE.md). The unmodified Night Config libraries are separately licensed under LGPL-3.0. Copies of the LGPL-3.0 and GPL-3.0 texts, third-party notices, and the matching Night Config 3.8.4 source archives are included in this repository and in the built mod JAR; see [THIRD-PARTY-NOTICES.md](THIRD-PARTY-NOTICES.md).
## Build and test

Build with `./gradlew build` (Windows: `gradlew.bat build`). The JAR is written to `build/libs/`.

The development-only manual client smoke harness and reproduction steps are in [`integration-test/`](integration-test/); they are not registered in the release mod metadata or included in the release JAR.