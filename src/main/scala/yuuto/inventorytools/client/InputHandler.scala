/**
 * @author Yuuto
 */
package yuuto.inventorytools.client

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.InputEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.client.Minecraft
import yuuto.inventorytools.network.MessageKeyPress
import yuuto.inventorytools.until.ITKeyBinds
import yuuto.inventorytools.util.NBTHelper
import yuuto.inventorytools.util.NBTTags
import yuuto.inventorytools.InventoryTools
import yuuto.inventorytools.client.gui.GuiScreenToolBox
import cpw.mods.fml.client.FMLClientHandler
import net.minecraft.util.ChatComponentText
import yuuto.inventorytools.proxy.ProxyCommon

@SideOnly(Side.CLIENT)
object InputHandler {
  
  @SubscribeEvent
  def handleKeyPress(event:InputEvent.KeyInputEvent){
    val player:EntityPlayer = Minecraft.getMinecraft().thePlayer;
    if(KeyBindings.TOOL_BOX.isPressed()) {
      openToolBoxGui(player);
    }
    if(KeyBindings.DOLLY_MODE.getIsKeyPressed){
      switchDollyMode(player);
    }
  }
  def openToolBoxGui(player:EntityPlayer){
    //System.out.println("Checking fool");
    if (player.inventory.getCurrentItem() == null)
      return;
    if (!NBTHelper.hasTag(player.inventory.getCurrentItem(), NBTTags.TOOL_BOX_INV)) {
      if (player.inventory.getCurrentItem().getItem() == ProxyCommon.itemToolBox)
        player.addChatComponentMessage(new ChatComponentText("No Items in Tool Box"));
      return;
    }
    if (FMLClientHandler.instance().isGUIOpen(classOf[GuiScreenToolBox]))
      return;
    player.openGui(InventoryTools, 1, player.worldObj, player.posX.asInstanceOf[Int], player.posY.asInstanceOf[Int], player.posZ.asInstanceOf[Int])
  }
  def switchDollyMode(player:EntityPlayer){
    if(player.inventory.getCurrentItem == null)
      return;
    if(player.inventory.getCurrentItem.getItem==ProxyCommon.itemDolly || player.inventory.getCurrentItem.getItem==ProxyCommon.itemDollyAdvanced) {
      InventoryTools.network.sendToServer(new MessageKeyPress(ITKeyBinds.DOLLY_MODE, player.worldObj.provider.dimensionId, player.posX.toInt, player.posY.toInt, player.posZ.toInt))
    }
  }
}