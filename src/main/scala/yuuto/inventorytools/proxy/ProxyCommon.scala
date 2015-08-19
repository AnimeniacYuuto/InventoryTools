/**
 * @author Yuuto
 */
package yuuto.inventorytools.proxy

import yuuto.yuutolib.IProxy
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry
import yuuto.inventorytools.blocks.BlockToolBench
import yuuto.inventorytools.tiles.TileToolBench
import yuuto.inventorytools.gui.GuiHandler
import yuuto.inventorytools.items.ItemToolBox
import net.minecraft.item.Item
import net.minecraft.block.Block
import yuuto.inventorytools.InventoryTools
import yuuto.inventorytools.events.EntityEventHandler
import net.minecraftforge.common.MinecraftForge
import yuuto.inventorytools.api.InvToolsApiLoader
import yuuto.inventorytools.config.ConfigurationIT
import yuuto.inventorytools.items.ItemDolly
import yuuto.yuutolib.recipe.RecipeHelper
import net.minecraft.item.ItemStack

object ProxyCommon{
  final val blockToolBench:Block = new BlockToolBench("toolbench");
  
  final val itemToolBox:Item = new ItemToolBox("toolbox");
  final val itemDolly:Item=new ItemDolly("dolly", false);
  final val itemDollyAdvanced:Item=new ItemDolly("dollyAdvanced", true);
}
class ProxyCommon extends IProxy{
  override def preInit(event:FMLPreInitializationEvent){
    ConfigurationIT.preInit(event);
    InvToolsApiLoader.initialize();
    GameRegistry.registerBlock(ProxyCommon.blockToolBench, "toolbench");
    GameRegistry.registerTileEntity(classOf[TileToolBench], "container.it.toolbench");
    GameRegistry.registerItem(ProxyCommon.itemToolBox, "toolbox");
    GameRegistry.registerItem(ProxyCommon.itemDolly, "itemDolly");
    GameRegistry.registerItem(ProxyCommon.itemDollyAdvanced, "itemDollyAdvanced");
    
    
    RecipeHelper.addShapedRecipe(true, new ItemStack(ProxyCommon.itemDolly), Array[Object](
        "g  ","gc ","ggg", 
        'g'.asInstanceOf[Object], "ingotGold", 
        'c'.asInstanceOf[Object], "chestWood"
    ));
    RecipeHelper.addShapedRecipe(true, new ItemStack(ProxyCommon.itemDollyAdvanced), Array[Object](
        "d  ","dg ","ddd", 'd'.asInstanceOf[Object], "gemDiamond", 
        'g'.asInstanceOf[Object], new ItemStack(ProxyCommon.itemDolly)
    ));
    RecipeHelper.addShapedRecipe(true, new ItemStack(ProxyCommon.itemToolBox), Array[Object](
        "ibi","iri","iii", 
        'i'.asInstanceOf[Object], "ingotIron", 
        'b'.asInstanceOf[Object], "barsIron",
        'r'.asInstanceOf[Object], "dyeRed"
    ));
    RecipeHelper.addShapedRecipe(true, new ItemStack(ProxyCommon.blockToolBench), Array[Object](
        "www","wtw","www",
        'w'.asInstanceOf[Object], "plankWood",
        't'.asInstanceOf[Object], new ItemStack(ProxyCommon.itemToolBox)
    ));
    
    
    NetworkRegistry.INSTANCE.registerGuiHandler(InventoryTools, new GuiHandler());
    MinecraftForge.EVENT_BUS.register(EntityEventHandler);
  }
  
  override def init(event:FMLInitializationEvent){}

  override def postInit(event:FMLPostInitializationEvent){
    ConfigurationIT.postInit(event);
  }
}