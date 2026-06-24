# MyMod

A Minecraft Fabric mod targeting version **1.21.4**.

---

## Prerequisites

Before you start, make sure you have the following installed:

- **JDK 21** — [Adoptium](https://adoptium.net/) or any OpenJDK 21 distribution
- **Git**
- An IDE — [IntelliJ IDEA](https://www.jetbrains.com/idea/) (recommended) or VS Code with the Java extension pack

You do **not** need to install Gradle — the project includes a Gradle wrapper (`./gradlew`).

---

## 1. Clone the repository

```bash
git clone https://github.com/YOUR_USER/mymod.git
cd mymod
```

---

## 2. Open in your IDE

### IntelliJ IDEA (recommended)

1. Open IntelliJ IDEA
2. Choose **File → Open** and select the `mymod` folder
3. IntelliJ will detect the Gradle project — click **Trust Project** when prompted
4. Wait for Gradle to sync and download dependencies (this can take a few minutes on first run as it downloads Minecraft assets)

### VS Code

1. Install the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
2. Open the `mymod` folder
3. VS Code will prompt you to import the Gradle project — accept it

---

## 3. Build the mod

```bash
./gradlew build
```

The compiled mod jar will be at:

```
build/libs/mymod-0.0.1.jar
```

To also produce a sources jar:

```bash
./gradlew build
# both mymod-0.0.1.jar and mymod-0.0.1-sources.jar will be in build/libs/
```

---

## 4. Run a development client

Launch Minecraft with your mod loaded directly from source:

```bash
./gradlew runClient
```

This opens a Minecraft game window with your mod active. You don't need a real Minecraft installation — Fabric Loom downloads everything it needs.

To run a dedicated server instead:

```bash
./gradlew runServer
```

---

## 5. Project structure

```
mymod/
├── .github/
│   └── workflows/
│       └── build.yml              # GitHub Actions CI — builds on every push
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/mymod/
│       │       ├── MyMod.java     # Main mod entrypoint
│       │       └── mixin/         # Put your mixin classes here
│       └── resources/
│           ├── fabric.mod.json    # Mod metadata (id, name, entrypoints)
│           └── mymod.mixins.json  # Mixin configuration
├── build.gradle                   # Build config and dependencies
├── gradle.properties              # All version numbers live here
├── settings.gradle                # Gradle plugin repos
└── gradlew / gradlew.bat          # Gradle wrapper — use this instead of gradle
```

---

## 6. Changing the mod ID, name, or package

All the key identifiers are in a few files. If you want to rename `mymod` to something real:

| What to change | Where |
|---|---|
| Mod ID (`mymod`) | `fabric.mod.json` → `"id"` and `mymod.mixins.json` → `"package"` |
| Display name | `fabric.mod.json` → `"name"` |
| Maven group / Java package | `gradle.properties` → `maven_group`, then rename the source folders |
| Archive name | `gradle.properties` → `archives_base_name` |
| Version | `gradle.properties` → `mod_version` |

---

## 7. Adding your first feature

### Add a simple item

1. Create a new class in `src/main/java/com/example/mymod/`:

```java
package com.example.travelpack;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

  public static final Item MY_ITEM = Registry.register(
      Registries.ITEM,
      Identifier.of(MyMod.MOD_ID, "my_item"),
      new Item(new Item.Settings())
  );

  public static void initialize() {
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
      entries.add(MY_ITEM);
    });
  }
}
```

2. Call it from `MyMod.java`:

```java
@Override
public void onInitialize() {
    ModItems.initialize();
    LOGGER.info("Hello from MyMod!");
}
```

### Add a mixin

Mixins let you modify vanilla Minecraft code without overwriting it. Create a class in the `mixin/` package:

```java
package com.example.travelpack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.screen.TitleScreen;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

  @Inject(at = @At("HEAD"), method = "init()V")
  private void init(CallbackInfo info) {
    MyMod.LOGGER.info("Title screen opened!");
  }
}
```

Then register it in `mymod.mixins.json`:

```json
{
    "client": [
        "TitleScreenMixin"
    ]
}
```

---

## 8. Updating dependency versions

All versions are in `gradle.properties`. Check the official sources for the latest:

| Dependency | Where to look |
|---|---|
| Fabric Loader + Yarn mappings | [fabricmc.net/develop](https://fabricmc.net/develop/) |
| Fabric API | [modrinth.com/mod/fabric-api](https://modrinth.com/mod/fabric-api) |
| Minecraft version | [minecraft.net](https://www.minecraft.net) |

After updating, re-run `./gradlew build` to verify everything still compiles.

---

## 9. GitHub Actions CI

The workflow at `.github/workflows/build.yml` automatically:

1. Builds the mod on every push and pull request
2. Uploads the compiled `.jar` as a build artifact (downloadable from the Actions tab)

No setup required — it works as soon as you push to GitHub.

To see it in action:
1. Go to your repo on GitHub
2. Click the **Actions** tab
3. Select the latest **Build** run to download the artifact

---

## 10. Publishing to Modrinth or CurseForge

When you're ready to release, consider using the [Modrinth Gradle plugin](https://github.com/modrinth/minotaur) or [CurseGradle](https://github.com/matthewprenger/CurseGradle) to automate uploads from GitHub Actions.

A basic Modrinth release step looks like this in your workflow:

```yaml
- name: Publish to Modrinth
  if: startsWith(github.ref, 'refs/tags/')
  run: ./gradlew modrinth
  env:
    MODRINTH_TOKEN: ${{ secrets.MODRINTH_TOKEN }}
```

This only runs when you push a git tag (e.g. `git tag v0.1.0 && git push --tags`).

---

## Useful commands

| Command | What it does |
|---|---|
| `./gradlew build` | Compile and package the mod |
| `./gradlew runClient` | Launch a dev Minecraft client |
| `./gradlew runServer` | Launch a dev Minecraft server |
| `./gradlew clean` | Delete the `build/` output folder |
| `./gradlew dependencies` | Show the full dependency tree |

---

## Resources

- [Fabric Wiki](https://fabricmc.net/wiki/start) — official getting-started guide
- [Fabric Discord](https://discord.gg/v6v4pMv) — community support
- [Fabric API Javadoc](https://maven.fabricmc.net/docs/fabric-api-0.114.0+1.21.4/)
- [Minecraft Mappings Viewer](https://linkie.shedaniel.dev/mappings) — look up obfuscated class/method names
