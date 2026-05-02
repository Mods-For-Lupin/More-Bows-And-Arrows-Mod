# More Bows and Arrows — Design Reference

Multiloader (Fabric + NeoForge) Minecraft mod that introduces a modular bow crafting system. Players assemble bows from 4 independent parts (limb, string, riser, rest), each loaded from data pack JSON. All stats come from JSON — no hardcoded per-material Java logic.

---

## Bow Part Slot System

Four independent slots. Each slot is its own data pack registry typed `Registry<BowPartDefinition>`.

| Slot | Index | Registry ID | Item | Default color |
|------|-------|-------------|------|---------------|
| Limb | 0 | `more_bows_and_arrows:bow_limb_type` | `bow_limb` | 0xFF0000 (red) |
| String | 1 | `more_bows_and_arrows:bow_string_type` | `bow_string` | 0x00FF00 (green) |
| Riser | 2 | `more_bows_and_arrows:bow_riser_type` | `bow_riser` | 0x8B6A13 (brown) |
| Rest | 3 | `more_bows_and_arrows:bow_rest_type` | `bow_rest` | 0xA69666 (tan) |

All 4 slots share the same Java class (`BowPartDefinition`). Different registries provide type safety and separate JSON namespacing.

---

## BowPartDefinition — the unified part type class

```java
public record BowPartDefinition(List<BowStatEffect> effects, int addedDurability, int color)
    implements BowPart
```

**DIRECT_CODEC** built via `RecordCodecBuilder`. Used as the codec for all 4 data pack registries.

**`applyTo(BowStats stats)`** — iterates `effects`, calls `stats.apply(effect)` on each, then `stats.addDurability(addedDurability)`.

### JSON format

```json
{
  "effects": [
    {"stat": "more_bows_and_arrows:velocity", "operation": "set", "value": 1.5},
    {"stat": "more_bows_and_arrows:draw_speed", "operation": "multiply", "value": 0.8},
    {"stat": "more_bows_and_arrows:crit_chance", "operation": "add", "value": 0.1}
  ],
  "added_durability": 75,
  "color": 16777215
}
```

Data files live at: `data/<namespace>/more_bows_and_arrows/<registry_path>/<entry_name>.json`

Example: `data/more_bows_and_arrows/more_bows_and_arrows/bow_limb_type/default.json`

---

## Stat System

### BowStat — identifier constants

`impl/common/component/bow/BowStat.java`

Static `Identifier` constants only — no generics, no combiners. These are the known stat IDs referenced from JSON.

| Constant | Identifier | Default |
|----------|-----------|---------|
| `VELOCITY` | `more_bows_and_arrows:velocity` | 1.0 |
| `DRAW_SPEED` | `more_bows_and_arrows:draw_speed` | 1.0 |
| `ACCURACY` | `more_bows_and_arrows:accuracy` | 1.0 |
| `DAMAGE` | `more_bows_and_arrows:damage` | 1.0 |
| `CRIT_CHANCE` | `more_bows_and_arrows:crit_chance` | 0.0 |
| `STEALTH` | `more_bows_and_arrows:stealth` | 1.0 |

`BowStat.DEFAULTS` is a `Map<Identifier, Float>` used to initialize `BowStats`.

Durability is **not** a stat — it lives as `addedDurability: int` on `BowPartDefinition` and is accumulated separately in `BowStats.addedDurability`.

### BowStatOperation — effect operation enum

`impl/common/component/bow/BowStatOperation.java`

```
ADD       current + value
MULTIPLY  current * value
SET       value  (ignores current)
```

JSON: lowercase string (`"add"`, `"multiply"`, `"set"`). Codec uses `comapFlatMap` for safe `valueOf` mapping.

### BowStatEffect — one stat modification

`impl/common/component/bow/BowStatEffect.java`

```java
record BowStatEffect(Identifier stat, BowStatOperation operation, float value)
```

`stat` is any `Identifier` string — known IDs are in `BowStat`, but JSON can reference custom stat IDs for future extensibility.

### BowStats — runtime stat container

`impl/common/component/bow/BowStats.java`

Mutable container, not a Builder. Created fresh in `BowAssembler.assemble()`.

- `Map<Identifier, Float> values` — initialized from `BowStat.DEFAULTS`
- `int addedDurability` — accumulated from all parts
- `void apply(BowStatEffect)` — applies operation
- `float get(Identifier stat)` — returns value or 0.0f if unknown stat

---

## Data Components on Items

Each item stores a **`ResourceKey<BowPartDefinition>`** — just a serialized `ResourceLocation` string.

`ResourceKey.codec(registryKey)` provides the persistent codec (wraps `ResourceLocation.CODEC`). No `RegistryOps` needed at codec time. Registry lookup happens at use time via `RegistryAccess`.

| Component field | Type | Codec |
|-----------------|------|-------|
| `ModDataComponents.BOW_LIMB_TYPE` | `ResourceKey<BowPartDefinition>` | `ResourceKey.codec(BOW_LIMB_TYPE_KEY)` |
| `ModDataComponents.BOW_STRING_TYPE` | `ResourceKey<BowPartDefinition>` | `ResourceKey.codec(BOW_STRING_TYPE_KEY)` |
| `ModDataComponents.BOW_RISER_TYPE` | `ResourceKey<BowPartDefinition>` | `ResourceKey.codec(BOW_RISER_TYPE_KEY)` |
| `ModDataComponents.BOW_REST_TYPE` | `ResourceKey<BowPartDefinition>` | `ResourceKey.codec(BOW_REST_TYPE_KEY)` |

**Items and their default components:**
- `bow_limb` → `BOW_LIMB_TYPE = more_bows_and_arrows:default`
- `bow_string` → `BOW_STRING_TYPE = more_bows_and_arrows:default`
- `bow_riser` → `BOW_RISER_TYPE = more_bows_and_arrows:default`
- `bow_rest` → `BOW_REST_TYPE = more_bows_and_arrows:default`
- `strung_bow` → all 4 components set to their respective defaults

`ModRegistries.DEFAULT_LIMB / DEFAULT_STRING / DEFAULT_RISER / DEFAULT_REST` are `ResourceKey<BowPartDefinition>` constants pointing at the built-in default entries.

---

## BowAssembler

`api/common/component/bow/BowAssembler.java`

```java
public static BowStats assemble(ItemStack stack, HolderLookup.Provider registries)
```

Iterates a static `List<PartSlot>` (4 entries), resolves each `ResourceKey` → `Holder<BowPartDefinition>` via registry lookup, calls `definition.applyTo(stats)`.

**Currently has no callers** — wire up from `StrungBowItem` item use / arrow shoot logic when implementing bow mechanics.

---

## Registry Registration

### NeoForge — `DataPackRegistryEvent.NewRegistry`

Fired on the **mod event bus** during `MoreBowsAndArrowsNeoForge` constructor. Register before item/block registration. Second codec argument = network codec (synced to clients).

```java
EVENT_BUS.addListener((Consumer<DataPackRegistryEvent.NewRegistry>) event -> {
    event.dataPackRegistry(ModRegistries.BOW_LIMB_TYPE_KEY, BowPartDefinition.DIRECT_CODEC, BowPartDefinition.DIRECT_CODEC);
    // ... 3 more
});
```

Data load path: `data/<ns>/more_bows_and_arrows/<registry_path>/<name>.json`

### Fabric — `DynamicRegistries.registerSynced`

Called before all other `bind(...)` registrations in `MoreBowsAndArrowsFabric.onInitialize()`.

```java
DynamicRegistries.registerSynced(ModRegistries.BOW_LIMB_TYPE_KEY, BowPartDefinition.DIRECT_CODEC);
// ... 3 more
```

Import: `net.fabricmc.fabric.api.event.registry.DynamicRegistries`

**RULE: never use `net.fabricmc.fabric.impl.*`** — impl classes are absent from release jars. Use `net.fabricmc.fabric.api.resource.v1.DataResourceLoader` (public API, no `PackType` arg) instead of `DataResourceLoaderImpl.get(PackType.SERVER_DATA)`.

---

## BowPartTintSource

`impl/client/item/BowPartTintSource.java`

Client-side `ItemTintSource`. Takes a `slotIndex` (0–3) in its JSON. Looks up the corresponding component + registry for that slot index, resolves the `BowPartDefinition`, returns its `color()`.

```java
public record BowPartTintSource(int slotIndex, int defaultColor) implements ItemTintSource
```

**MAP_CODEC** (for item model JSON tint registration):
```java
RecordCodecBuilder.mapCodec(i -> i.group(
    Codec.INT.fieldOf("slot").forGetter(...),
    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(...)
).apply(i, BowPartTintSource::new))
```

Registered on client init via `ItemTintSources.ID_MAPPER.put(identifier("bow_part"), MAP_CODEC)`.

### Item model JSON tint format

```json
{
  "model": {
    "type": "minecraft:model",
    "model": "more_bows_and_arrows:item/bow_limb",
    "tints": [{"type": "more_bows_and_arrows:bow_part", "slot": 0, "default": 16711680}]
  }
}
```

Slot indices match the table at the top. `strung_bow.json` can carry 4 tint entries (slots 0–3) once its model has 4 texture layers.

---

## Creative Tab — generateBowPartTypes

`impl/common/registry/ModTabs.java`

Mirrors `CreativeModeTabs.generatePotionEffectTypes` from vanilla. For each registered entry in each of the 4 registries, creates an `ItemStack` of the appropriate part item with the entry's `ResourceKey` set as its data component.

```java
ModTabs.generateBowPartTypes(params, output);
```

Uses `params.holders().lookup(registryKey)` → `registry.listElements()` → `holder.key()` → `stack.set(component, key)`.

**Already wired into `ModTabs.register()`.**

---

## Strung Bow Assembly (NeoForge entity tick)

`MoreBowsAndArrowsNeoForge` — `EntityTickEvent.Pre` listener.

When a `bow_limb` `ItemEntity` and a `bow_string` `ItemEntity` are within 1 block of each other on a `ServerLevel`:
1. Reads `BOW_LIMB_TYPE` + `BOW_STRING_TYPE` `ResourceKey`s from the items
2. Resolves `addedDurability` from each via `level.registryAccess().lookup(...)`
3. Creates `new ItemStack(ModItems.STRUNG_BOW)` — carries default RISER + REST components from item `Properties`
4. Sets limb + string keys onto the new stack
5. Sets `DataComponents.MAX_DAMAGE` = base + limbDur + stringDur
6. Discards both source entities, drops assembled bow

**TODO:** Add riser + rest to the assembly mechanic (crafting recipe or additional entity collision).

---

## Class Responsibilities

| Class | Location | Role |
|-------|----------|------|
| `BowStatOperation` | impl/common/component/bow | ADD/MULTIPLY/SET enum + codec |
| `BowStatEffect` | impl/common/component/bow | Single stat modification record + codec |
| `BowPartDefinition` | impl/common/component/bow | Unified part definition: effects + durability + color |
| `BowStat` | impl/common/component/bow | Known stat Identifier constants + DEFAULTS map |
| `BowStats` | impl/common/component/bow | Mutable stat container assembled at runtime |
| `BowPart` | api/common/component/bow | Interface: `void applyTo(BowStats)` |
| `BowAssembler` | api/common/component/bow | Resolves all 4 slots, builds BowStats |
| `ModRegistries` | impl/common/registry | 4 registry keys + 4 default ResourceKeys |
| `ModDataComponents` | impl/common/registry | 4 DataComponentType fields |
| `ModItems` | impl/common/registry | 5 items: bow_limb, bow_string, bow_riser, bow_rest, strung_bow |
| `ModTabs` | impl/common/registry | Creative tab + generateBowPartTypes |
| `BowPartTintSource` | impl/client/item | Slot-aware item tint → color from registry |
| `BowPartItem` | impl/common/item | Generic part item with durability tooltip |
| `StrungBowItem` | impl/common/item | Assembled bow item |
| `MoreBowsAndArrowsFabric` | fabric/ | Fabric entrypoint + 4 DynamicRegistries calls |
| `MoreBowsAndArrowsNeoForge` | neoforge/ | NeoForge entrypoint + DataPackRegistryEvent + entity tick |

---

## Resource Layout

```
common/src/main/resources/
├── data/more_bows_and_arrows/more_bows_and_arrows/
│   ├── bow_limb_type/
│   │   ├── default.json          ← built-in default limb
│   │   └── default_alt.json      ← example alternate limb
│   ├── bow_string_type/
│   │   └── default.json
│   ├── bow_riser_type/
│   │   └── default.json
│   └── bow_rest_type/
│       └── default.json
└── assets/more_bows_and_arrows/
    ├── items/
    │   ├── bow_limb.json         ← slot 0 tint
    │   ├── bow_string.json       ← slot 1 tint
    │   ├── bow_riser.json        ← slot 2 tint
    │   ├── bow_rest.json         ← slot 3 tint
    │   └── strung_bow.json       ← uses vanilla bow model currently
    └── models/item/
        ├── bow_limb.json
        ├── bow_string.json
        ├── bow_riser.json        ← stub; needs texture
        └── bow_rest.json         ← stub; needs texture
```

---

## Design Decisions & Rationale

**Why one `BowPartDefinition` class for all 4 slots?**
Inspired by EvilCraft's broom system: one `BroomModifier` class regardless of which part slot carries it. Eliminates parallel class hierarchies. Codecs are shared. Adding a 5th slot (e.g. a sight/scope) requires no new Java class — just a new registry key + component field.

**Why `ResourceKey<BowPartDefinition>` in data component instead of `Holder<BowPartDefinition>`?**
`ResourceKey.codec()` wraps `ResourceLocation.CODEC` — no `RegistryOps` context needed at codec time. Simple, stable serialization. Registry lookup happens at use time (where `Level` / `RegistryAccess` is always available). `Holder<T>` codecs (`RegistryFileCodec`) require `RegistryOps` which isn't always present during normal item NBT round-trips.

**Why separate registries per slot instead of one unified registry?**
Type safety: `DataComponentType<ResourceKey<BowPartDefinition>>` with `ResourceKey.codec(BOW_LIMB_TYPE_KEY)` encodes the registry in the codec, preventing a limb key from being used in a string slot. JSON files are organized per slot. Tags (future) can target slot-specific entries.

**Why EvilCraft's effect-list approach over per-field stat records?**
Previous iteration had hardcoded fields (`drawSpeedModifier`, `arrowVelocityModifier`, etc.) — adding a new stat required a Java class change. With `List<BowStatEffect>`, a data pack author can target any stat by ID, use any operation, and combine effects freely. The stat system is open-ended.

**`BowStat.DURABILITY` removed from stat system:**
Durability behaves differently — it's always additive and an integer. Keeping it as a dedicated `addedDurability: int` field on `BowPartDefinition` is cleaner than special-casing a float stat that needs rounding.

---

## Decompiled Sources — Absolute Paths

Four decompilation roots + one reference mod are provided for API verification:

- **Vanilla MC (this project):** `D:\gitkraken\Mods-For-Lupin\More-Bows-And-Arrows-Mod.worktrees\26.1\mc_decomp`
- **Vanilla MC (shared planning):** `D:\gitkraken\Mods-For-Lupin\Dis-Enchanting-Table-Mod.worktrees\26.1\planning\mc_decomp`
- **NeoForge-patched MC:** `D:\gitkraken\Mods-For-Lupin\Dis-Enchanting-Table-Mod.worktrees\26.1\planning\neoforge_mc_decomp`
- **NeoForge API:** `D:\gitkraken\Mods-For-Lupin\Dis-Enchanting-Table-Mod.worktrees\26.1\planning\neoforge_api`
  - Common/server: `neoforge_api/main/java`
  - Client-only: `neoforge_api/client/java`
- **Fabric API:** `D:\gitkraken\Mods-For-Lupin\Dis-Enchanting-Table-Mod.worktrees\26.1\planning\fabric_api`
- **EvilCraft reference mod:** `C:\Users\jason\Downloads\EvilCraft-master-26\EvilCraft-master-26`

**RULE: Consult decompiled sources before every new feature or API change.** Do not infer API behavior from first principles — the decompilation is authoritative.

---

## Decompiled Sources Consulted

| File | Used For |
|------|----------|
| `mc_decomp/…/world/item/enchantment/Enchantment.java` | Data-driven registry pattern: `DIRECT_CODEC`, `EnchantmentDefinition`, `DataComponentMap` effects |
| `mc_decomp/…/world/item/enchantment/LevelBasedValue.java` | Reference for dispatched codec pattern (6 subtypes); not used directly |
| `mc_decomp/…/world/item/enchantment/EnchantmentEffectComponents.java` | Reference for effect component registration pattern |
| `mc_decomp/…/resources/ResourceKey.java` | Confirmed `ResourceKey.codec(registryKey)` exists and returns `Codec<ResourceKey<T>>` |
| `mc_decomp/…/world/item/Item.java` (lines 681–750) | `TooltipContext.registries()` returns `HolderLookup.Provider` (nullable) — safe to use for registry lookup in tooltip |
| `neoforge_api/main/…/registries/DataPackRegistryEvent.java` | `event.dataPackRegistry(key, codec, networkCodec)` — second codec arg is for client sync; null = unsynced |
| `fabric_api/…/event/registry/DynamicRegistries.java` | `DynamicRegistries.registerSynced(key, codec)` — single codec used for both server load and network sync |
| `fabric_api/…/resource/v1/DataResourceLoader.java` | `DataResourceLoader.get().registerReloadListener(id, listener)` — public API (no PackType arg); replaces banned `DataResourceLoaderImpl` |
| `EvilCraft/…/core/broom/BroomPartDefinition*` | Inspiration: one class for all part slots, `List<BroomModifier>` effects, per-modifier additive/multiplicative operations |
| `EvilCraft/…/core/broom/BroomModifierRegistry.java` | Inspiration: separate registry per concern; showed CyclopsCore uses in-memory Map (we use MC-native data pack registry instead) |
| `mc_decomp/…/world/item/enchantment/effects/EnchantmentAttributeEffect.java` | Reference for attribute-based stat modification pattern |

---

## Future Work

| Task | Notes |
|------|-------|
| Riser + rest assembly | Currently strung bow is assembled from limb + string only (entity collision). Add crafting recipes or additional collision logic to incorporate riser + rest. |
| Bow use mechanics | `BowAssembler.assemble(stack, registries)` has no callers yet. Wire into `StrungBowItem` — `use()`, `releaseUsing()`, arrow velocity/damage calculation. |
| Textures | `bow_riser` and `bow_rest` model stubs need actual textures at `assets/more_bows_and_arrows/textures/item/bow_riser.png` and `bow_rest.png`. |
| Strung bow tinting | `strung_bow.json` uses the vanilla bow model (no tinting). To tint by parts, create a multi-layer model and add 4 tint entries (slots 0–3). |
| Tags | Create `#more_bows_and_arrows:bow_limb_types/wood` etc. for recipe filtering. Tag files go in `data/<ns>/tags/more_bows_and_arrows/bow_limb_type/`. |
| Arrow types | Separate registry `more_bows_and_arrows:arrow_type` for data-driven arrows (damage, effect, velocity multiplier). Same pattern as bow parts. |
| Crafting recipes | Shaped/shapeless recipes for assembling each part from materials. Use NeoForge component ingredients or plain item ingredients. |
| Data generation | Extend `MBADataGen` with a `FabricCodecDataProvider` subclass to generate the default JSON files programmatically instead of hand-editing. |
| RestData / RiserData behavior | Currently both apply accuracy + stealth/draw_speed. Verify these feel distinct during gameplay and adjust default values accordingly. |
