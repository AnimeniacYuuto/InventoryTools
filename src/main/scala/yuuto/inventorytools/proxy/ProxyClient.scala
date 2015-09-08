/**
 * @author Yuuto
 */
package yuuto.inventorytools.proxy

import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.client.registry.ClientRegistry
import yuuto.inventorytools.client.InputHandler
import cpw.mods.fml.common.FMLCommonHandler
import yuuto.inventorytools.client.KeyBindings

class ProxyClient extends ProxyCommon{
  override def preInit(event:FMLPreInitializationEvent){
    super.preInit(event);
    ClientRegistry.registerKeyBinding(KeyBindings.TOOL_BOX);
    ClientRegistry.registerKeyBinding(KeyBindings.DOLLY_MODE);
    FMLCommonHandler.instance().bus().register(InputHandler);
  }
  
}