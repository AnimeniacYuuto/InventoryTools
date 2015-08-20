/**
 * @author Yuuto
 */
package yuuto.inventorytools.client

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.InputEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.client.Minecraft
import yuuto.inventorytools.util.NBTHelper
import yuuto.inventorytools.util.NBTTags
import yuuto.inventorytools.InventoryTools
import yuuto.inventorytools.client.gui.GuiScreenToolBox
import cpw.mods.fml.client.FMLClientHandler
import net.minecraft.util.ChatComponentText
import yuuto.inventorytools.proxy.ProxyCommon

object InputHandler {
  
  @SubscribeEvent
  def handleKeyPress(event:InputEvent.KeyInputEvent){
    val player:EntityPlayer = Minecraft.getMinecraft().thePlayer;
    if(KeyBindings.TOOL_BOX.isPressed()){
      //System.out.println("Checking fool");
      if(player.inventory.getCurrentItem() == null)
        return;
      if(!NBTHelper.hasTag(player.inventory.getCurrentItem(),NBTTags.TOOL_BOX_INV)){
        if(player.inventory.getCurrentItem().getItem() == ProxyCommon.itemToolBox)
          player.addChatComponentMessage(new ChatComponentText("No Items in Tool Box"));
        return;
      }
      if(FMLClientHandler.instance().isGUIOpen(classOf[GuiScreenToolBox]))
        return;
      player.openGui(InventoryTools, 1, player.worldObj, player.posX.asInstanceOf[Int], player.posY.asInstanceOf[Int], player.posZ.asInstanceOf[Int])
    }
  }
}