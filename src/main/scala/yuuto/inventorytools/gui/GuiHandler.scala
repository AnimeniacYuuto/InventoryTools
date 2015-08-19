/**
 * @author Yuuto
 */
package yuuto.inventorytools.gui

import cpw.mods.fml.common.network.IGuiHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import yuuto.inventorytools.tiles.TileToolBench
import yuuto.inventorytools.client.gui.GuiContainerToolBench
import net.minecraft.item.ItemStack
import yuuto.inventorytools.util.NBTHelper
import yuuto.inventorytools.client.gui.GuiScreenToolBox
import yuuto.inventorytools.InventoryTools
import net.minecraft.util.ChatComponentText
import yuuto.inventorytools.proxy.ProxyCommon

class GuiHandler extends IGuiHandler{

  override def getServerGuiElement(ID:Int, player:EntityPlayer, world:World,x:Int, y:Int, z:Int):Object={
    val tile:TileEntity = world.getTileEntity(x, y, z);
    ID match{
      case 0=>{
        if(!tile.isInstanceOf[TileToolBench])
          return null;
        return new ContainerToolBench(tile.asInstanceOf[TileToolBench], player);
      }
      case i=>return null;
    }
    return null;
  }
  
  override def getClientGuiElement(ID:Int, player:EntityPlayer, world:World,x:Int, y:Int, z:Int):Object={
    val tile:TileEntity = world.getTileEntity(x, y, z);
    val item:ItemStack=player.inventory.getCurrentItem();
    ID match{
      case 0=>{
        if(!tile.isInstanceOf[TileToolBench])
          return null;
        return new GuiContainerToolBench(tile.asInstanceOf[TileToolBench], player);
      }
      case 1=>{
        val inv:Array[ItemStack] = NBTHelper.getToolBoxInventory(item);
        if(inv == null || inv.length < 1){
          if(item.getItem() == ProxyCommon.itemToolBox)
            player.addChatComponentMessage(new ChatComponentText("No Items in Tool Box"));
          return null;
        }
        return new GuiScreenToolBox(inv);
      }
      case i=>return null;
    }
    return null;
  }
  
}