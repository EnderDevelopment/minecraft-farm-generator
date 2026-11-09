# ItemGenerator Minecraft Auto Farm Plugin

ItemGenerator is a Minecraft plugin for Paper servers that creates automatic item farms using dropper blocks and item frames. Place an item in an item frame attached to a dropper, and the plugin periodically generates random quantities of that item directly into the dropper inventory.

## Install

1. Build the plugin with Java 21 and Maven:

   ```bash
   mvn package
   ```

2. Copy the generated `target/plugin.jar` file to your Paper server's `plugins` folder.

3. Start or restart the server.

4. Confirm that the plugin loads successfully in the server console.

## Use

1. Place a dropper block.
2. Attach an item frame to the dropper.
3. Put the item you want to generate into the item frame.
4. Keep enough free space in the dropper inventory.
5. The generator will add between 1 and 5 items every 3 server ticks.
6. Remove the generated items from the dropper inventory as needed.

Use `/itemgenerator` or `/ig` to display the setup instructions. Operators with the `itemgenerator.admin` permission can use `/itemgenerator reload`, which confirms that generators are running.

The plugin automatically removes the attached item frame when its generator dropper is broken. Item frames attached to generator droppers are protected from entity damage.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/ac09f905-33d5-4950-8448-f2fc45d76df9?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=ac09f905-33d5-4950-8448-f2fc45d76df9
