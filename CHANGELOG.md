# Changelog

All notable changes to TravelPack are documented here.

## [0.1.4] - 2026-06-29

### Fixed
- Mining the head half of a placed Sleeping Bag now drops the item correctly
- Wooden Bucket no longer destroys waterlogged blocks (slabs, stairs, etc.) when scooping water
- Wooden Bucket lava interaction no longer consumes the bucket in creative mode
- Wooden Water Bucket is now accessible in the creative inventory (Tools tab)

## [0.1.3] - 2026-06-25

### Added
- **Wooden Bucket** — a lightweight bucket carved from wood; holds water but not lava
- **Wooden Water Bucket** — the filled variant of the Wooden Bucket; place water to get the empty bucket back

## [0.1.2] - 2026-06-25

### Changed
- Ported to Minecraft 1.21.11 (Fabric API 0.141.4, Loader 0.19.3, Loom 1.17.12, Gradle 9.6.0)
- Added Patchouli 1.21.1-93-FABRIC as compile dependency

## [0.1.1] - 2026-06-24

### Fixed
- Horse bedroll slot was mapped to inventory index 1 (the vanilla armor slot) instead of index 2, causing the Sleeping Bag to be dropped on relog

## [0.1.0] - 2026-06-24

### Added
- **Sleeping Bag** — portable bedroll that lets you skip the night without placing a permanent bed block; stackable up to 16 and upgradeable into a White Bed
- **Horse Bedroll Slot** — dedicated slot in the horse inventory screen; equip a Sleeping Bag to see it draped across the horse's back
