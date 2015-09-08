/**
 * @author Yuuto
 */
package yuuto.inventorytools.client

import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

object KeyBindings {
  final val TOOL_BOX=new KeyBinding("key.inventorytools.toolbox", Keyboard.KEY_V, "key.inventorytools.category");
  final val DOLLY_MODE=new KeyBinding("key.inventorytools.dollymode", Keyboard.KEY_N, "key.inventorytools.category");
}