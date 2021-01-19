In your server scripts in `kubejs/server_scripts`, you can add Big bench Theory recipes like so:
```javascript
events.listen('recipes', event => {
  event.recipes.bigbenchtheory.crafting_shaped(item.of('minecraft:stone', 3), [
      'SSSSS',
      'S   S',
      'S   S',
      'S   S',
      'SSSSS'
  ], {
      S: 'minecraft:sponge'
  })

  event.recipes.bigbenchtheory.crafting_shapeless(item.of('minecraft:cobblestone', 4),
      ['minecraft:stone',
       'minecraft:stone',
       'minecraft:stone',
       'minecraft:stone',
       'minecraft:stone',
       '#minecraft:logs',
       '#minecraft:logs',
       '#minecraft:logs',
       '#minecraft:logs',
       '#minecraft:logs'])
})
```