/**
 * @author Yuuto
 */
package yuuto.inventorytools.network

import io.netty.buffer.ByteBuf
import cpw.mods.fml.common.network.simpleimpl.IMessage
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler
import cpw.mods.fml.common.network.simpleimpl.MessageContext
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import yuuto.inventorytools.util.NBTHelper
import yuuto.inventorytools.util.NBTTags

class MessageToolBoxHandler extends IMessageHandler[MessageToolBox, IMessage] {

    override def onMessage(message:MessageToolBox, ctx:MessageContext):IMessage={
      if(ctx.getServerHandler().playerEntity != null){
        val player:EntityPlayer=ctx.getServerHandler().playerEntity
        val heldItem:ItemStack=player.inventory.getCurrentItem();
        if(heldItem !=null && NBTHelper.hasTag(heldItem, NBTTags.TOOL_BOX_INV)){
          val inv=NBTHelper.getToolBoxInventory(heldItem);
          val id=message.id;
          if(inv != null && id>=0 && id < inv.length){
            val selectedItem=inv(id);
            if(selectedItem == null)
              return null;
            NBTHelper.removeTag(heldItem, NBTTags.TOOL_BOX_INV);
            inv(id)=heldItem;
            NBTHelper.setToolBoxInventory(selectedItem, inv);
            player.inventory.setInventorySlotContents(player.inventory.currentItem, selectedItem)
          }
        }
      }
      return null;
    }
}