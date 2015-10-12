/**
 * @author Yuuto
 */
package yuuto.inventorytools

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import yuuto.inventorytools.gui.GuiHandler
import yuuto.inventorytools.items.ItemToolBox
import yuuto.inventorytools.ref.ReferenceInvTools
import yuuto.inventorytools.network._
import yuuto.inventorytools.tiles.TileToolBench
import yuuto.inventorytools.proxy.ProxyCommon
import cpw.mods.fml.common.SidedProxy

@Mod(modid = ReferenceInvTools.MOD_ID, name = ReferenceInvTools.MOD_NAME, version = ReferenceInvTools.VERSION, dependencies = "after:LogisticsPipes",modLanguage = "scala")
object InventoryTools {
  
  var network:SimpleNetworkWrapper=null;
  
  val tab:CreativeTabs = new CreativeTabs("InventoryTools"){
    @SideOnly(Side.CLIENT)
    override def getTabIconItem():Item={
      return ProxyCommon.itemToolBox;
    }
  };
  
  @SidedProxy(clientSide = ReferenceInvTools.PROXY_CLIENT, serverSide = ReferenceInvTools.PROXY_SERVER)
  var proxy:ProxyCommon=null;
  
  @EventHandler
  def preInit(event:FMLPreInitializationEvent) {
    proxy.preInit(event);
    network = NetworkRegistry.INSTANCE.newSimpleChannel(ReferenceInvTools.CHANNEL);
    network.registerMessage(classOf[MessageToolBenchHandler], classOf[MessageToolBench], 0, Side.SERVER);
    network.registerMessage(classOf[MessageToolBoxHandler], classOf[MessageToolBox], 1, Side.SERVER);
    network.registerMessage(classOf[MessageKeyPressHandler], classOf[MessageKeyPress], 2, Side.SERVER);
  }

  @EventHandler
  def init(event:FMLInitializationEvent) {
    proxy.init(event);
  }

  @EventHandler
  def postInit(event:FMLPostInitializationEvent) {
    proxy.postInit(event);
  }
  
}