# The Big Bench Theory

## Setup

For setup instructions please see the [fabric wiki page](https://fabricmc.net/wiki/tutorial:setup) that relates to the IDE that you are using.

## Current Features

### Blocks

1. Tiny Crafting Table, a 1x1 crafting table.
2. Greater Crafting Table, a 5x5 crafting table.
3. Massive Crafting Table, a 7x7 crafting table.
4. Ultimate Crafting Table, a 9x9 crafting table.

### Recipes

To use shaped recipes with a width or height greater than 3, they must be of the type "bigbenchtheory:crafting_shaped".

To use shapeless recipes with an ingredient list over 9, they must be of the type "bigbenchtheory:crafting_shapeless".

Note: Regular crafting recipes and "bigbenchtheory" recipes are interchangeable, but only in the tables this mod adds and vanilla.
Most likely though, modded crafting tables will work too.

### Compatibility

1. Vanilla Recipe Book (Recipes in groups look weird)
1. REI Integration, so recipes will show up correctly.
    * Currently, all modded recipes take up the space a 9x9 takes. This is subject to change.
2. KubeJS Integration [Examples](markdown/kubejs_examples.md)
    * What kind of crafting table and recipe mod would this be without KubeJS?
3. Full NBT Crafting Integration
    * Including support for dollars.
4. Other Mods' Crafting Tables
    * Most modded crafting tables should work fine. Make an issue on the github if there's an incompatibility please.
